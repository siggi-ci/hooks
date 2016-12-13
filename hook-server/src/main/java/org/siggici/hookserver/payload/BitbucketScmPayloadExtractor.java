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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author jbellmann
 *
 */
@Order(Ordered.LOWEST_PRECEDENCE - 200)
class BitbucketScmPayloadExtractor implements ScmPayloadExtractor {

    private static final String BITBUCKET = "bitbucket";

    private final Logger log = LoggerFactory.getLogger(BitbucketScmPayloadExtractor.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Map<String, String>> extractPayload(MultiValueMap<String, String> headers,
            Map<String, Object> request) {
        String eventType = headers.getFirst("X-Event-Key");
        if (StringUtils.hasText(eventType) && StringUtils.hasText(headers.getFirst("X-Hook-UUID"))) {
            try {
                Map<String, String> result = new HashMap<String, String>();
                result.put("eventType", eventType);
                result.put("payload", objectMapper.writeValueAsString(request));
                result.put("providerType", BITBUCKET);

                return Optional.of(result);
            } catch (Exception e) {
                log.warn("Could not handle webhook-request", e);
            }
        }
        return Optional.empty();
    }

}
