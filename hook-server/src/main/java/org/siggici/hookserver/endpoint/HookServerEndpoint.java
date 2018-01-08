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
package org.siggici.hookserver.endpoint;

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;

import org.siggici.hookserver.event.HookPayloadEvent;
import org.siggici.hookserver.payload.ScmPayloadExtractor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Endpoint for Scm-Provider Webhooks.
 * 
 * @author jbellmann
 *
 */
@RestController
@RequestMapping("${siggi.hookserver.endpoint.path:/hooks}")
public class HookServerEndpoint implements ApplicationEventPublisherAware {

    private static final String CREATED = "created";

    private static final String OK = "OK";

    private ApplicationEventPublisher eventPublisher;

    private ScmPayloadExtractor extractor;

    public HookServerEndpoint(ScmPayloadExtractor extractor) {
        this.extractor = extractor;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> hooks(@RequestHeader HttpHeaders headers, @RequestBody JsonNode request) {
        extractor.extractPayload(headers, request).ifPresent(payload -> enrichAndPublish(payload));
        return ResponseEntity.ok(OK);
    }

    protected void enrichAndPublish(JsonNode payload) {
        JsonNode result = addMetadata(payload);
        eventPublisher.publishEvent(new HookPayloadEvent(this, result));
    }

    protected JsonNode addMetadata(JsonNode payload) {
        ((ObjectNode)payload).put(CREATED, valueOf(currentTimeMillis()));
        return payload;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

}
