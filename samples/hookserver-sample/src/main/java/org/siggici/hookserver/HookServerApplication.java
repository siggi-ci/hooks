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

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HookServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HookServerApplication.class, args);
    }

    @Bean
    public HookEventListener hookEventListener() {
        return new HookEventListener();
    }

    @Bean
    public DataSource buildDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public FilterRegistrationBean headerFilter() {
        FilterRegistrationBean fb = new FilterRegistrationBean();
        fb.setFilter(new HeaderFilter());
        fb.setName("headerFilter");
        fb.setEnabled(true);
        List<String> patternList = new ArrayList<String>(1);
        patternList.add("/simplehook/*");
        fb.setUrlPatterns(patternList);
        return fb;
    }
}
