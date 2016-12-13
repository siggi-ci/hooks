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
package org.siggici.hookserver.aws.sqs;

import org.siggici.hookserver.event.HookPayloadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SqsHookEventListener {
    private final Logger log = LoggerFactory.getLogger(SqsHookEventListener.class);

    private final SqsDispatcher sqsDispatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SqsHookEventListener(SqsDispatcher sqsDispatcher) {
        this.sqsDispatcher = sqsDispatcher;
    }

    @EventListener
    @Async
    public void onEvent(HookPayloadEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event.getPayload());
            this.sqsDispatcher.sendMessage(message);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
