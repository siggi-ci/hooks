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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HookPayloadEventTests {

    private static final String SECOND_KEY = "second";
    private static final String FIRST_KEY = "first";
    private ObjectNode payload;

    @Before
    public void setUp() {
        payload = JsonNodeFactory.instance.objectNode();
        payload.put(FIRST_KEY, "FIRST");
        payload.put(SECOND_KEY, "SECOND");
    }

    @Test
    public void updateEventsPayloadShouldNotBePossible() {
        HookPayloadEvent event = new HookPayloadEvent(this, payload);
        assertThat(event.getPayload().get(FIRST_KEY).asText()).isEqualTo("FIRST");
    }

}
