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
package org.siggici.hookserver.aws.sqs;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.siggici.hookserver.aws.sqs.config.SqsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SqsDispatcher {

    private final Logger log = LoggerFactory.getLogger(SqsDispatcher.class);

    private final AmazonSQS sqs;
    private final SqsProperties properties;

    private String queueUrl;

    public SqsDispatcher(AmazonSQS sqs, SqsProperties properties) {
        this.sqs = sqs;
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        log.info("intialize SqsDistpatcher ...");
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(properties.getQueueName());
        queueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        Assert.hasText(queueUrl, "'queueUrl' should never be null or empty");
        log.info("SqsDispatcher initialized");
    }

    @PreDestroy
    public void shutdown() {
        log.info("shutdown SqsDispatcher ...");
        if (properties.isDeleteQueueOnShutdown()) {
            log.info("delete Sqs-Queue {}", properties.getQueueName());
            sqs.deleteQueue(new DeleteQueueRequest(queueUrl));
        }
        log.info("SqsDispatcher down");
    }

    public void sendMessage(String message) {
        sqs.sendMessage(new SendMessageRequest(this.queueUrl, message));
    }

}
