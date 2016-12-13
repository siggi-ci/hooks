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

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.siggici.hookserver.autoconfigure.aws.sqs.SqsDispatcherAutoConfiguration;
import org.siggici.hookserver.aws.sqs.SqsDispatcher;
import org.siggici.hookserver.aws.sqs.SqsHookEventListener;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Ignore
public class SqsDispatcherAutoConfigurationTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AnnotationConfigApplicationContext context;

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testDefaultRabbitConfigurationDispatcher() {
        this.thrown.expect(NoSuchBeanDefinitionException.class);
        load(TestConfiguration.class);
        context.getBean(SqsDispatcher.class);
    }

    @Test
    public void testDefaultRabbitConfigurationEventListener() {
        this.thrown.expect(NoSuchBeanDefinitionException.class);
        load(TestConfiguration.class);
        context.getBean(SqsHookEventListener.class);
    }

    @Test
    public void testDefaultRabbitConfigurationDispatcherWhenEnabled() {
        load(TestConfiguration.class, "siggi.hookserver.dispatcher.sqs.enabled:true");
        SqsDispatcher dispatcher = context.getBean(SqsDispatcher.class);
        Assertions.assertThat(dispatcher).isNotNull();
    }

    @Test
    public void testDefaultRabbitConfigurationEventListenerWhenEnabled() {
        load(TestConfiguration.class, "siggi.hookserver.dispatcher.sqs.enabled:true");
        SqsHookEventListener listener = context.getBean(SqsHookEventListener.class);
        Assertions.assertThat(listener).isNotNull();
    }

    private void load(Class<?> config, String... environment) {
        this.context = doLoad(new Class<?>[] { config }, environment);
    }

    private AnnotationConfigApplicationContext doLoad(Class<?>[] configs, String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(configs);
        applicationContext.register(SqsDispatcherAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.refresh();
        return applicationContext;
    }

    @Configuration
    protected static class TestConfiguration {
    }

}
