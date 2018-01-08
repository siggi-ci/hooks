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
package org.siggici.hookserver.payload;

import static org.springframework.util.StringUtils.hasText;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
class GitlabScmPayloadExtractor implements ScmPayloadExtractor {

    private static final String GITLAB = "gitlab";

    private final Logger log = LoggerFactory.getLogger(GitlabScmPayloadExtractor.class);

    @Override
    public Optional<JsonNode> extractPayload(MultiValueMap<String, String> headers,
            JsonNode request) {
        String eventType = headers.getFirst("X-Gitlab-Event");
        if (hasText(eventType)) {
            try {
                ObjectNode node = JsonNodeFactory.instance.objectNode();
                node.put("eventType", eventType);
                node.put("providerType", GITLAB);
                node.set("payload", request);
                return Optional.of(node);
            } catch (Exception e) {
                log.warn("Could not handle webhook-request", e);
            }
        }
        return Optional.empty();
    }

}
