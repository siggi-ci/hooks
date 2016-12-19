/**
 * Copyright (C) 2016 Joerg Bellmann (joerg.bellmann@googlemail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.siggici.hookserver.rest.client;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.post;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

public class HookserverAccountsClient {

    private final Logger logger = LoggerFactory.getLogger(HookserverAccountsClient.class);

    private final RestOperations restOperations;
    private final URI uri;

    public HookserverAccountsClient(RestOperations restOperations, URI uri) {
        this.restOperations = restOperations;
        this.uri = uri;
    }

    public void createAccount(String username, String password) {
        Assert.hasText(username, "'username' should never be null or empty");
        Assert.hasText(password, "'password' should never be null or empty");
        RequestEntity<Account> requestEntity = post(uri).contentType(APPLICATION_JSON).body(new Account(username, password));
        ResponseEntity<String> response = restOperations.exchange(requestEntity, String.class);
        logger.debug("Hookserver-Account created : {}", response.getHeaders().getLocation());
    }
}
