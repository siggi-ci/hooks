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
package org.siggici.hookserver.event;

import org.springframework.context.ApplicationEvent;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author jbellmann
 *
 */
public class HookPayloadEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private JsonNode payload;

    public HookPayloadEvent(Object source, JsonNode payload) {
        super(source);
        this.payload = payload;
    }

    public JsonNode getPayload() {
        return payload;
    }

}
