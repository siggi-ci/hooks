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

import java.util.Collections;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.siggici.hookserver.endpoint.HookServerEndpoint;
import org.siggici.hookserver.payload.CompositeScmPayloadExtractor;
import org.siggici.hookserver.payload.ScmPayloadExtractor;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class HooksEndpointTest {

    HookServerEndpoint endpoint = new HookServerEndpoint(
            new CompositeScmPayloadExtractor(Collections.<ScmPayloadExtractor>emptyList()));

    @Before
    public void init() {
        StaticApplicationContext publisher = new StaticApplicationContext();
        this.endpoint.setApplicationEventPublisher(publisher);
        publisher.refresh();
    }

    @Test
    public void testEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Github-Event", "push");
        ResponseEntity<String> response = endpoint.hooks(headers, new HashMap<String, Object>());
        Assertions.assertThat(response.getBody()).isEqualTo("OK");
    }

}
