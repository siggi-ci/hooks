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

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

public class DelegatingHookserverUserDetailsManager implements HookserverUserDetailsManager {

    private final UserDetailsManager delegate;

    public DelegatingHookserverUserDetailsManager(UserDetailsManager delegate) {
        Assert.notNull(delegate, "'userDetailsManager' should never be null");
        this.delegate = delegate;
    }

    @Override
    public void createUser(UserDetails user) {
        delegate.createUser(user);
    }

    @Override
    public void updateUser(UserDetails user) {
        delegate.updateUser(user);
    }

    @Override
    public void deleteUser(String username) {
        delegate.deleteUser(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        delegate.changePassword(oldPassword, newPassword);
    }

    @Override
    public boolean userExists(String username) {
        return delegate.userExists(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return delegate.loadUserByUsername(username);
    }

}
