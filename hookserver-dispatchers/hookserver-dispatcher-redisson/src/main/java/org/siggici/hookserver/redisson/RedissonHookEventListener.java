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
package org.siggici.hookserver.redisson;

import org.redisson.RedissonClient;
import org.redisson.core.RBlockingQueue;
import org.siggici.hookserver.event.HookPayloadEvent;
import org.siggici.hookserver.redisson.config.RedissonProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RedissonHookEventListener {

    private final Logger log = LoggerFactory.getLogger(RedissonHookEventListener.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RBlockingQueue<String> queue;

    public RedissonHookEventListener(RedissonClient redisson, RedissonProperties redissonProperties) {
        Assert.hasText(redissonProperties.getQueueName(), "'queueName' should never be null or empty.");
        queue = redisson.getBlockingQueue(redissonProperties.getQueueName());
    }

    @EventListener
    public void onEvent(HookPayloadEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event.getPayload());
            this.queue.add(message);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

}
