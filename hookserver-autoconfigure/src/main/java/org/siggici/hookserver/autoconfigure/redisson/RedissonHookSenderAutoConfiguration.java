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
package org.siggici.hookserver.autoconfigure.redisson;

import java.io.IOException;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.siggici.hookserver.redisson.RedissonHookEventListener;
import org.siggici.hookserver.redisson.config.RedissonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableConfigurationProperties({ RedissonProperties.class })
public class RedissonHookSenderAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "siggi.hookserver.dispatcher.redisson", name = "enabled")
    protected static class RedissonDispatcherConfiguration {

        @Bean
        public RedissonHookEventListener redissonHookEventListener(RedissonProperties redissonProperties) {
            try {
                RedissonClient redisson = Redisson.create(Config
                        .fromYAML(new ClassPathResource(redissonProperties.getRedissonConfigPath()).getInputStream()));
                return new RedissonHookEventListener(redisson, redissonProperties);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

    }
}
