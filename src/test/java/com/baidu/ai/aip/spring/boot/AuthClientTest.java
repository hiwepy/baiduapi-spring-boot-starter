/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baidu.ai.aip.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link AuthClient }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("AuthClient Tests")
class AuthClientTest {

    @Test
    @DisplayName("Instance can be created")
    void instance() {
        assertThat(new AuthClient()).isNotNull();
    }

    @Test
    @DisplayName("AUTH_HOST constant should be the baidu OAuth endpoint")
    void authHostConstant() {
        assertThat(AuthClient.AUTH_HOST).isEqualTo("https://aip.baidubce.com/oauth/2.0/token");
    }

    @Test
    @DisplayName("getAuth with empty credentials should never throw and return null-or-token")
    void getAuthWithEmptyCredentialsShouldBeSafe() {
        // The method swallows all exceptions and returns null on failure; it must never throw.
        // With empty credentials the OAuth endpoint rejects the request, so we expect null.
        String token = AuthClient.getAuth("", "");
        assertThat(token).isNull();
    }

}
