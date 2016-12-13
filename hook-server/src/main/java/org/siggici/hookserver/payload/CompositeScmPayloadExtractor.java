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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.MultiValueMap;

/**
 * 
 * @author jbellmann
 *
 */
public class CompositeScmPayloadExtractor implements ScmPayloadExtractor {

    private final Logger logger = LoggerFactory.getLogger(CompositeScmPayloadExtractor.class);

    private final List<ScmPayloadExtractor> extractors;

    public CompositeScmPayloadExtractor(List<ScmPayloadExtractor> extractors) {
        this.extractors = new ArrayList<ScmPayloadExtractor>();
        if (extractors != null) {
            this.extractors.addAll(extractors);
        }
        AnnotationAwareOrderComparator.sort(this.extractors);
    }

    @Override
    public Optional<Map<String, String>> extractPayload(MultiValueMap<String, String> headers,
            Map<String, Object> request) {
        for (ScmPayloadExtractor extractor : extractors) {
            Optional<Map<String, String>> result = extractor.extractPayload(headers, request);
            if (result.isPresent()) {
                return result;
            }
        }
        logger.warn("Could not extract playload from : {}", request.toString());
        return Optional.empty();
    }

}
