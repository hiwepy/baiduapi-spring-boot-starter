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
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link FaceRecognitionMessageSource }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FaceRecognitionMessageSource Tests")
class FaceRecognitionMessageSourceTest {

    @Test
    @DisplayName("Default constructor should create a non-null instance")
    void constructorShouldCreateInstance() {
        FaceRecognitionMessageSource source = new FaceRecognitionMessageSource();
        assertThat(source).isNotNull();
    }

    @Test
    @DisplayName("getAccessor should return a non-null MessageSourceAccessor")
    void getAccessorShouldReturnAccessor() {
        MessageSourceAccessor accessor = FaceRecognitionMessageSource.getAccessor();
        assertThat(accessor).isNotNull();
        // The accessor should resolve to the configured default message for unknown codes
        // (the bundle ships inside the main source tree and may not be resolvable at runtime).
        assertThat(accessor.getMessage("unknown-code", "fallback", null)).isEqualTo("fallback");
    }

    @Test
    @DisplayName("MessageSource should be a Spring MessageSource")
    void shouldBeMessageSource() {
        MessageSource source = new FaceRecognitionMessageSource();
        assertThat(source).isInstanceOf(MessageSource.class);
    }

}
