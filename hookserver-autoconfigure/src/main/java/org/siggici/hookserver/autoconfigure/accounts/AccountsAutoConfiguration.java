package org.siggici.hookserver.autoconfigure.accounts;

import javax.sql.DataSource;

import org.siggici.hookserver.accounts.DelegatingHookserverUserDetailsManager;
import org.siggici.hookserver.accounts.jdbc.JdbcStatements;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@ConditionalOnClass({ DelegatingHookserverUserDetailsManager.class })
@ConditionalOnBean({ DataSource.class })
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
