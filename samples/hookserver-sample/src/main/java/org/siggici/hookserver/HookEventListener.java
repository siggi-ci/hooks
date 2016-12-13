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
package org.siggici.hookserver;

import javax.annotation.PostConstruct;

import org.siggici.hookserver.event.HookPayloadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

/**
 * 
 * @author jbellmann
 *
 */
public class HookEventListener {

    private final Logger logger = LoggerFactory.getLogger(HookEventListener.class);

    @EventListener
    public void onEvent(HookPayloadEvent event) {
        logger.info("Got event : {}\n", event.getPayload());

    }

    @PostConstruct
    public void init() {
        logger.info("initialized");
    }
}
