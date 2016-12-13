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
package org.siggici.hookserver.rabbitmq;

import javax.annotation.PostConstruct;

import org.siggici.hookserver.rabbitmq.config.RabbitMqHookSenderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

public class RabbitMqHookSender {

    private final Logger log = LoggerFactory.getLogger(RabbitMqHookSender.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqHookSenderProperties hookSenderProperties;

    public RabbitMqHookSender(RabbitTemplate rabbitTemplate, RabbitMqHookSenderProperties hookSenderProperties) {
        Assert.notNull(rabbitTemplate, "'rabbitTemplate' should never be null");
        Assert.notNull(hookSenderProperties, "'hookSenderProperties' should never be null");
        this.rabbitTemplate = rabbitTemplate;
        this.hookSenderProperties = hookSenderProperties;
        log.info("Initialize 'RabbitHookSender' with 'queueName' : {}", this.hookSenderProperties.getQueueName());
    }

    @PostConstruct
    public void init() {
        log.info("Initialize RabbitMQDispatcher, queue-name : {}", hookSenderProperties.getQueueName());
    }

    /**
     * Sends the 'hook'-object to RabbitMq.
     * 
     * @param hook
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(hookSenderProperties.getQueueName(), message);
    }
}
