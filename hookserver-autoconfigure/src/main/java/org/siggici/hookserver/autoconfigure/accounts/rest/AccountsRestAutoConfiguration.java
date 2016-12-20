package org.siggici.hookserver.autoconfigure.accounts.rest;

import org.siggici.hookserver.accounts.HookserverUserDetailsManager;
import org.siggici.hookserver.rest.AccountResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ AccountResource.class })
@ConditionalOnBean({ HookserverUserDetailsManager.class })
public class AccountsRestAutoConfiguration {

    @Bean
    public AccountResource accountResource(HookserverUserDetailsManager userDetailsManager) {
        return new AccountResource(userDetailsManager);
    }

}
