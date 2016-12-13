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

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siggici.hookserver.accounts.jdbc.JdbcStatements;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JdbcUserDetailsManagerTest {

    private static final String CHANGED_PASSWORD = "geheim2";
    private static final String ORIG_PASSWORD = "geheim";
    private static final String HOOK_ROLE = "HOOK";
    private static final String USERNAME = "pmueller";
    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules
            .preparedDatabase(FlywayPreparer.forClasspathLocation("db/migration"));

    @Test
    @WithMockUser(USERNAME)
    public void testJdbcUserDetailsManager() {
        JdbcUserDetailsManager delegate = new JdbcUserDetailsManager();
        delegate.setDataSource(db.getTestDatabase());
        delegate.setCreateUserSql(JdbcStatements.DEF_CREATE_USER_SQL);
        delegate.setDeleteUserSql(JdbcStatements.DEF_DELETE_USER_SQL);
        delegate.setChangePasswordSql(JdbcStatements.DEF_CHANGE_PASSWORD_SQL);
        delegate.setUpdateUserSql(JdbcStatements.DEF_UPDATE_USER_SQL);
        delegate.setUserExistsSql(JdbcStatements.DEF_USER_EXISTS_SQL);
        delegate.setUsersByUsernameQuery(JdbcStatements.DEF_USERS_BY_USERNAME_QUERY);
        delegate.setAuthoritiesByUsernameQuery(JdbcStatements.DEF_AUTHORITIES_BY_USERNAME_QUERY);

        delegate.setCreateAuthoritySql(JdbcStatements.DEF_INSERT_AUTHORITY_SQL);
        delegate.setDeleteUserAuthoritiesSql(JdbcStatements.DEF_DELETE_USER_AUTHORITIES_SQL);

        HookserverUserDetailsManager hookserverUserDetailsManager = new DelegatingHookserverUserDetailsManager(
                delegate);

        UserDetails user = new User(USERNAME, ORIG_PASSWORD, AuthorityUtils.createAuthorityList(HOOK_ROLE));

        hookserverUserDetailsManager.createUser(user);

        boolean exists = hookserverUserDetailsManager.userExists(USERNAME);
        Assertions.assertThat(exists).isTrue();

        UserDetails loadedUser = hookserverUserDetailsManager.loadUserByUsername(USERNAME);
        Assertions.assertThat(loadedUser).isEqualTo(user);

        hookserverUserDetailsManager.changePassword(ORIG_PASSWORD, CHANGED_PASSWORD);
        UserDetails loadedAfterPasswordChange = hookserverUserDetailsManager.loadUserByUsername(USERNAME);
        Assertions.assertThat(loadedAfterPasswordChange.getPassword()).isEqualTo(CHANGED_PASSWORD);

        UserDetails updated = new User(USERNAME, CHANGED_PASSWORD, false, false, false, false,
                AuthorityUtils.createAuthorityList(HOOK_ROLE));
        hookserverUserDetailsManager.updateUser(updated);

        UserDetails afterUpdate = hookserverUserDetailsManager.loadUserByUsername(USERNAME);
        Assertions.assertThat(afterUpdate.isEnabled()).isFalse();

        hookserverUserDetailsManager.deleteUser(USERNAME);

        boolean existsAfterDelete = hookserverUserDetailsManager.userExists(USERNAME);
        Assertions.assertThat(existsAfterDelete).isFalse();
    }

}
