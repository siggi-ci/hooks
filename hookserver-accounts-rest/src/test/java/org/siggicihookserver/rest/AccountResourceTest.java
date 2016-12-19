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
package org.siggicihookserver.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.siggici.hookserver.accounts.HookserverUserDetailsManager;
import org.siggici.hookserver.rest.AccountResource;
import org.siggici.hookserver.rest.dto.Account;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.siggici.hookserver.rest.AccountResource.ACCOUNTS_ENDPOINT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AccountResourceTest {

    private MockMvc mvc;

    @Mock
    private HookserverUserDetailsManager userDetailManager;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new AccountResource(userDetailManager)).build();
    }

    @Test
    public void shouldAcceptAccountsForPost() throws Exception {
        final Account account = Account.builder()
                .username("username")
                .password("password")
                .build();

        this.mvc.perform(post(ACCOUNTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(account))
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    final String location = result.getResponse().getHeader(LOCATION);
                    assertThat(location).isNotEmpty();
                    assertThat(location).contains(ACCOUNTS_ENDPOINT + '/' + account.getUsername());
                });
    }

    @Test
    public void shouldReturn400IfUsernameIsNull() throws Exception {
        final Account account = Account.builder()
                .password("password")
                .build();

        this.mvc.perform(post(ACCOUNTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(toJson(account))
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest());
    }

    private static String toJson(final Object object) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
