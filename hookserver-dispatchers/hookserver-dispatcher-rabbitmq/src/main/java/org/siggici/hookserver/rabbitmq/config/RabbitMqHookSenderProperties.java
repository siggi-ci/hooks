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
package org.siggici.hookserver.rabbitmq.config;

import org.siggici.hookserver.rabbitmq.RabbitMqHookSender;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for {@link RabbitMqHookSender}.
 * 
 * @author jbellmann
 *
 */
@ConfigurationProperties("siggi.hooks.messaging.rabbitmq")
public class RabbitMqHookSenderProperties {

    private final String DEFAULT_HOOKS_QUEUE_NAME = "hooks";

    private String queueName = DEFAULT_HOOKS_QUEUE_NAME;

    private boolean enabled = false;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
