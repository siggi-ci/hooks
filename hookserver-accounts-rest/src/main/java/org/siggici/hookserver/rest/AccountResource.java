/**
 * Copyright (C) 2016 Joerg Bellmann (joerg.bellmann@googlemail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.siggici.hookserver.rest;


import org.siggici.hookserver.accounts.HookserverUserDetailsManager;
import org.siggici.hookserver.rest.dto.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static org.siggici.hookserver.rest.AccountResource.ACCOUNTS_ENDPOINT;

@RestController
@RequestMapping(ACCOUNTS_ENDPOINT)
public class AccountResource {

    public static final String ACCOUNTS_ENDPOINT = "/accounts";

    private final HookserverUserDetailsManager userDetailManager;

    public AccountResource(HookserverUserDetailsManager userDetailManager) {
        this.userDetailManager = userDetailManager;
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@Valid @RequestBody final Account account,
                                              final UriComponentsBuilder uriComponentsBuilder) {


        User newUser = new User(account.getUsername(), account.getPassword(), AuthorityUtils.createAuthorityList("HOOK"));
        userDetailManager.createUser(newUser);

        final UriComponents uriComponents = uriComponentsBuilder
                .path(ACCOUNTS_ENDPOINT).pathSegment(account.getUsername()).build();

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
