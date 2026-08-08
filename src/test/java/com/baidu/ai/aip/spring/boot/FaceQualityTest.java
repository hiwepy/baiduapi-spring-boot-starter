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
 * Unit tests for {{ @link FaceQuality }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("FaceQuality Tests")
class FaceQualityTest {

    @Test
    @DisplayName("Should expose all expected image quality control constants")
    void shouldExposeAllConstants() {
        assertThat(FaceQuality.values())
            .containsExactly(FaceQuality.NONE, FaceQuality.LOW, FaceQuality.NORMAL, FaceQuality.HIGH);
    }

    @Test
    @DisplayName("Should resolve constant by name")
    void shouldResolveByName() {
        assertThat(FaceQuality.valueOf("LOW")).isEqualTo(FaceQuality.LOW);
    }

    @Test
    @DisplayName("Should have four distinct constants")
    void shouldHaveFourConstants() {
        assertThat(FaceQuality.values()).hasSize(4);
    }

    @Test
    @DisplayName("Enum instances should be singletons")
    void shouldBeSingletons() {
        for (FaceQuality value : FaceQuality.values()) {
            assertThat(value).isSameAs(FaceQuality.valueOf(value.name()));
        }
    }

}
