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
package com.unknown.pgk;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siggici.hookserver.aws.sqs.SqsDispatcher;
import org.siggici.hookserver.aws.sqs.config.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("sqs-disabled")
@Ignore
public class DisabledSqsApplicationTest {

    @Autowired(required = false)
    private SqsDispatcher sqsDisptacher;

    @Autowired
    private SqsProperties sqsProperties;

    @Test
    public void startUp() throws InterruptedException {
        Assertions.assertThat(sqsDisptacher).isNull();
        Assertions.assertThat(sqsProperties).isNotNull();
    }

}
