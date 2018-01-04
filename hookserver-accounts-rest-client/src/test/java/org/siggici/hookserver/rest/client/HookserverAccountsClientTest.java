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
package org.siggici.hookserver.rest.client;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class HookserverAccountsClientTest {

    protected RestTemplate restTemplate;

    protected MockRestServiceServer mockServer;

    protected HttpHeaders responseHeaders;

    private HookserverAccountsClient client;

    @Test
    public void create() {
        mockServer.expect(requestTo("http://server.org/accounts"))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withCreatedEntity(URI.create("http://server.org/accounts/08da1346-a8ca-44d5-a078-f4586da2cb5e"))
                    .contentType(MediaType.APPLICATION_JSON));

        client.createAccount("klaus", "meier");
    }

    @Before
    public void setUp() {
        this.restTemplate = new RestTemplate();

        this.client = new HookserverAccountsClient(restTemplate, URI.create("http://server.org/accounts"));

        this.responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    protected Resource jsonResource(final String filename) {
        return new ClassPathResource(filename + ".json", getClass());
    }

}
