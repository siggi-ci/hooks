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
package org.siggici.hookserver.cloud.stream.config;

import org.siggici.hookserver.cloud.stream.CloudStreamHookEventListener;
import org.siggici.hookserver.cloud.stream.HookEventSender;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(HookEventSender.class)
public class CloudStreamHookEventSenderConfig {

    @Bean
    public CloudStreamHookEventListener cloudStreamEventListener(HookEventSender hookEventSender) {
        return new CloudStreamHookEventListener(hookEventSender);
    }
}
