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

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiValueMapTest {

    private final TypeReference<LinkedMultiValueMap<String, String>> ref = new TypeReference<LinkedMultiValueMap<String, String>>() {
    };

    @Test
    public void serializeMultiValueMap() throws IOException {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("one", "valueOne");
        mvm.add("two", "valueTwo");
        ObjectMapper objecMapper = new ObjectMapper();
        String s = objecMapper.writeValueAsString(mvm);
        System.out.println(s);
        // http://stackoverflow.com/questions/29604319/jackson-json-deserialize-commons-multimap
        // objecMapper.readValue(s, MultiValueMap.class);
        // Object o = objecMapper.readValue(s, ref);
        MultiValueMap<String, String> result = (MultiValueMap<String, String>) objecMapper.readValue(s, ref);
        Assertions.assertThat(result).isNotNull();
    }
}
