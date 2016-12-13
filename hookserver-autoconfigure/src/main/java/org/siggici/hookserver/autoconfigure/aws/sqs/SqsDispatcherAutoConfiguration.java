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
package org.siggici.hookserver.autoconfigure.aws.sqs;

import org.siggici.hookserver.aws.sqs.SqsDispatcher;
import org.siggici.hookserver.aws.sqs.SqsHookEventListener;
import org.siggici.hookserver.aws.sqs.config.SqsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

@Configuration
@EnableConfigurationProperties({ SqsProperties.class })
@ConditionalOnClass({ AmazonSQSClient.class })
public class SqsDispatcherAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "siggi.hookserver.dispatcher.sqs", name = "enabled")
    protected static class SqsDispatcherConfiguration {

        @Bean
        public SqsDispatcher sqsDispatcher(SqsProperties sqsProperties) {
            AWSCredentials credentials = new DefaultAWSCredentialsProviderChain().getCredentials();
            AmazonSQS sqs = new AmazonSQSClient(credentials);
            Region region = Region.getRegion(Regions.fromName(sqsProperties.getRegion()));
            sqs.setRegion(region);
            return new SqsDispatcher(sqs, sqsProperties);
        }

        @Bean
        public SqsHookEventListener sqsHookEventListener(SqsDispatcher sqsDispatcher) {
            return new SqsHookEventListener(sqsDispatcher);
        }

    }
}
