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
package org.siggici.hookserver.autoconfigure.accounts;

import javax.sql.DataSource;

import org.siggici.hookserver.accounts.DelegatingHookserverUserDetailsManager;
import org.siggici.hookserver.accounts.jdbc.JdbcStatements;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@ConditionalOnClass({ DelegatingHookserverUserDetailsManager.class })
@ConditionalOnBean({ DataSource.class })
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class AccountsAutoConfiguration {

    @Bean
    public DelegatingHookserverUserDetailsManager hooksUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager delegate = new JdbcUserDetailsManager();
        delegate.setDataSource(dataSource);
        delegate.setCreateUserSql(JdbcStatements.DEF_CREATE_USER_SQL);
        delegate.setDeleteUserSql(JdbcStatements.DEF_DELETE_USER_SQL);
        delegate.setChangePasswordSql(JdbcStatements.DEF_CHANGE_PASSWORD_SQL);
        delegate.setUpdateUserSql(JdbcStatements.DEF_UPDATE_USER_SQL);
        delegate.setUserExistsSql(JdbcStatements.DEF_USER_EXISTS_SQL);
        delegate.setUsersByUsernameQuery(JdbcStatements.DEF_USERS_BY_USERNAME_QUERY);
        delegate.setAuthoritiesByUsernameQuery(JdbcStatements.DEF_AUTHORITIES_BY_USERNAME_QUERY);

        delegate.setCreateAuthoritySql(JdbcStatements.DEF_INSERT_AUTHORITY_SQL);
        delegate.setDeleteUserAuthoritiesSql(JdbcStatements.DEF_DELETE_USER_AUTHORITIES_SQL);

        DelegatingHookserverUserDetailsManager hookserverUserDetailsManager = new DelegatingHookserverUserDetailsManager(
                delegate);

        return hookserverUserDetailsManager;
    }

}
