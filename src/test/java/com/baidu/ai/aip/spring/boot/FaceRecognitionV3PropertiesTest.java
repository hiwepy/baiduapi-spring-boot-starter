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
 * Unit tests for {{ @link FaceRecognitionV3Properties }}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FaceRecognitionV3Properties Tests")
class FaceRecognitionV3PropertiesTest {

    @Test
    @DisplayName("Should expose the expected PREFIX constant")
    void prefixConstant() {
        assertThat(FaceRecognitionV3Properties.PREFIX).isEqualTo("baidu.face.v3");
    }

    @Test
    @DisplayName("Default values should match the documented defaults")
    void defaults() {
        FaceRecognitionV3Properties props = new FaceRecognitionV3Properties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.getClientId()).isNull();
        assertThat(props.getClientSecret()).isNull();
        assertThat(props.getMaxFaceNum()).isEqualTo(1);
        assertThat(props.getFaceFields()).isNotEmpty();
        assertThat(props.getMaxUserNum()).isEqualTo(1);
    }

    @Test
    @DisplayName("Getters and setters should round-trip every field")
    void gettersAndSetters() {
        FaceRecognitionV3Properties props = new FaceRecognitionV3Properties();

        props.setEnabled(true);
        props.setClientId("ak");
        props.setClientSecret("sk");
        props.setMaxFaceNum(8);
        props.setFaceFields("age,gender");
        props.setMaxUserNum(20);

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getClientId()).isEqualTo("ak");
        assertThat(props.getClientSecret()).isEqualTo("sk");
        assertThat(props.getMaxFaceNum()).isEqualTo(8);
        assertThat(props.getFaceFields()).isEqualTo("age,gender");
        assertThat(props.getMaxUserNum()).isEqualTo(20);
    }

}
