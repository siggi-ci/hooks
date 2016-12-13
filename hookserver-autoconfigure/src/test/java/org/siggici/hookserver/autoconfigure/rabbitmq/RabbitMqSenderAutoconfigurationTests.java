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
package org.siggici.hookserver.autoconfigure.rabbitmq;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.siggici.hookserver.autoconfigure.rabbitmq.RabbitMqHookSenderAutoConfiguration;
import org.siggici.hookserver.rabbitmq.RabbitMqHookSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class RabbitMqSenderAutoconfigurationTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AnnotationConfigApplicationContext context;

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testDefaultRabbitConfiguration() {
        this.thrown.expect(NoSuchBeanDefinitionException.class);
        load(TestConfiguration.class);
        context.getBean(RabbitMqHookSender.class);

    }

    @Test
    public void testDefaultRabbitConfigurationWhenEnabled() {
        load(TestConfiguration.class, "siggi.hooks.messaging.rabbitmq.enabled:true");
        RabbitMqHookSender sender = context.getBean(RabbitMqHookSender.class);
        Assertions.assertThat(sender).isNotNull();

    }

    private void load(Class<?> config, String... environment) {
        this.context = doLoad(new Class<?>[] { config }, environment);
    }

    private AnnotationConfigApplicationContext doLoad(Class<?>[] configs, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(configs);
        applicationContext.register(RabbitMqHookSenderAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.refresh();
        return applicationContext;
    }

    @Configuration
    protected static class TestConfiguration {

        @Bean
        public RabbitTemplate rabbitTemplate() {
            return Mockito.mock(RabbitTemplate.class);
        }
    }

}
