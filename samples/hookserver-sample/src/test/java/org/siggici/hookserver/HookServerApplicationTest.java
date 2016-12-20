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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RBlockingQueue;
import org.siggici.hookserver.redisson.config.RedissonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.zalando.stups.junit.postgres.PostgreSqlRule;
import org.zalando.stups.junit.redis.RedisServerRule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HookServerApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class HookServerApplicationTest {

    @ClassRule
    public static RedisServerRule redisServer = new RedisServerRule.Builder().build();

    @ClassRule
    public static final PostgreSqlRule postgres = new PostgreSqlRule.Builder().withPort(5432)
            .addScriptLocation(getScriptDirectory()).build();

    @LocalServerPort
    private int port;

    @Autowired
    private RedissonProperties redissonProperties;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void runHookServer() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
        runGithubRequest();

        runBitbucketRequest();

        fetchMessagesFromQueue();
    }

    private void fetchMessagesFromQueue() {
        RedissonClient redisson;
        try {
            redisson = Redisson.create(Config
                    .fromYAML(new ClassPathResource(redissonProperties.getRedissonConfigPath()).getInputStream()));
            RBlockingQueue<String> queue = redisson.getBlockingQueue(redissonProperties.getQueueName());
            for (int i = 0; i < 3; i++) {
                String message = queue.poll(10, TimeUnit.SECONDS);
                if (message == null) {
                    System.out.println("NO MESSAGE IN QUEUE");
                } else {
                    System.out.println("GOT MESSAGE : " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void runBitbucketRequest()
            throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
        Map<String, Object> value = getRequestBody("/hooks/bitbucket/example_1.json");

        RequestEntity<?> request = RequestEntity.post(new URI("http://localhost:" + port + "/simplehook/hooks"))
                .header("X-Event-Key", "push:Repo").header("X-Hook-UUID", UUID.randomUUID().toString()).body(value);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        Assertions.assertThat(response.getBody()).isEqualTo("OK");
    }

    protected void runGithubRequest() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
        Map<String, Object> value = getRequestBody("/hooks/github/push.json");
        RequestEntity<?> request = RequestEntity.post(new URI("http://localhost:" + port + "/simplehook/hooks"))
                .header("X-Github-Event", "push").body(value);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        Assertions.assertThat(response.getBody()).isEqualTo("OK");
    }

    public Map<String, Object> getRequestBody(String path)
            throws JsonParseException, JsonMappingException, IOException {
        Map<String, Object> value = new ObjectMapper().readValue(new ClassPathResource(path).getInputStream(),
                new TypeReference<Map<String, Object>>() {
                });
        return value;
    }

    private static String getScriptDirectory() {
        return new File(HookServerApplicationTest.class.getResource("/scripts/00_create_schema.sql").getFile())
                .getParent();
    }
}
