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
package org.siggici.hookserver.accounts;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class InMemoryHookserverUserDetailsManagerTest {

    @Test
    public void testInMemoryUserDetails() {
        InMemoryUserDetailsManager delegate = new InMemoryUserDetailsManager(new ArrayList<UserDetails>());
        HookserverUserDetailsManager mgr = new DelegatingHookserverUserDetailsManager(delegate);

        UserDetails user = new User("jbellmann", "geheim", AuthorityUtils.createAuthorityList("HOOK"));

        mgr.createUser(user);

        UserDetails loadedUser = mgr.loadUserByUsername(user.getUsername());
        loadedUser.getAuthorities();
        Assertions.assertThat(user).isEqualTo(loadedUser);
    }
}
