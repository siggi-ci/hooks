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

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.siggici.hookserver.autoconfigure.redisson.RedissonHookSenderAutoConfiguration;
import org.siggici.hookserver.redisson.RedissonHookEventListener;
import org.siggici.hookserver.redisson.config.RedissonProperties;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.embedded.RedisServer;

public class RedissonHookSenderAutoConfigurationTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AnnotationConfigApplicationContext context;

    private static RedisServer redisServer;

    @BeforeClass
    public static void setUp() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();

    }

    @AfterClass
    public static void shutdown() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testDefaultRedissonConfiguration() {
        this.thrown.expect(NoSuchBeanDefinitionException.class);
        load(TestConfiguration.class);
        context.getBean(RedissonHookEventListener.class);

    }

    @Test
    public void testDefaultRedissonConfigurationWhenEnabled() {
        load(TestConfiguration.class, "siggi.hooks.messaging.redisson.enabled:true",
                "siggi.hookserver.dispatcher.redisson.redissonConfigPath:/redisson-config.yml");
        RedissonHookEventListener eventListener = context.getBean(RedissonHookEventListener.class);
        Assertions.assertThat(eventListener).isNotNull();

    }

    private void load(Class<?> config, String... environment) {
        this.context = doLoad(new Class<?>[] { config }, environment);
    }

    private AnnotationConfigApplicationContext doLoad(Class<?>[] configs, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(configs);
        applicationContext.register(RedissonHookSenderAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.refresh();
        return applicationContext;
    }

    @Configuration
    protected static class TestConfiguration {
        
        @Bean
        public RedissonProperties redissonProperties(){
            RedissonProperties props = new RedissonProperties();
            props.setQueueName("TEST_QUEUE");
            return props;
        }
    }

}
