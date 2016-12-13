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

import org.siggici.hookserver.rabbitmq.RabbitMqHookSender;
import org.siggici.hookserver.rabbitmq.RabbitMqHookSenderEventListener;
import org.siggici.hookserver.rabbitmq.config.RabbitMqHookSenderProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ RabbitTemplate.class, RabbitMqHookSenderProperties.class })
@AutoConfigureAfter({ RabbitAutoConfiguration.class })
@EnableConfigurationProperties({ RabbitMqHookSenderProperties.class })
@EnableRabbit
public class RabbitMqHookSenderAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "siggi.hookserver.dispatcher.rabbitmq", name = "enabled")
    protected static class RabbitDispatcherConfiguration {

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Autowired
        private RabbitMqHookSenderProperties hookSenderProperties;

        @Bean
        public RabbitMqHookSender rabbitHookSender() {
            return new RabbitMqHookSender(rabbitTemplate, hookSenderProperties);
        }

        @Bean
        public RabbitMqHookSenderEventListener rabbitMqHookSenderEventListener() {
            return new RabbitMqHookSenderEventListener(rabbitHookSender());
        }

        /**
         * Creates the {@link Queue} if not available.
         * 
         * @return
         */
        @Bean
        public Queue hookQueue() {
            return new Queue(hookSenderProperties.getQueueName());
        }
    }
}
