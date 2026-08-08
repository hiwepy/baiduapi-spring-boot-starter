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
 * Unit tests for {{ @link FaceRecognitionV2Properties }}.
 *
 * <p>Verifies default values, getters/setters and POJO contract.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("FaceRecognitionV2Properties Tests")
class FaceRecognitionV2PropertiesTest {

    @Test
    @DisplayName("Should expose the expected PREFIX constant")
    void prefixConstant() {
        assertThat(FaceRecognitionV2Properties.PREFIX).isEqualTo("baidu.face.v2");
    }

    @Test
    @DisplayName("Default values should match the documented defaults")
    void defaults() {
        FaceRecognitionV2Properties props = new FaceRecognitionV2Properties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.getClientId()).isNull();
        assertThat(props.getClientSecret()).isNull();
        assertThat(props.getMaxFaceNum()).isEqualTo(1);
        assertThat(props.getFaceFields()).isNotEmpty();
        assertThat(props.getUserTopNum()).isEqualTo(1);
        assertThat(props.getFaceliveness()).isEqualTo(0.834963);
    }

    @Test
    @DisplayName("Getters and setters should round-trip every field")
    void gettersAndSetters() {
        FaceRecognitionV2Properties props = new FaceRecognitionV2Properties();

        props.setEnabled(true);
        props.setClientId("ak");
        props.setClientSecret("sk");
        props.setMaxFaceNum(5);
        props.setFaceFields("age,gender");
        props.setUserTopNum(3);
        props.setFaceliveness(0.5);

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getClientId()).isEqualTo("ak");
        assertThat(props.getClientSecret()).isEqualTo("sk");
        assertThat(props.getMaxFaceNum()).isEqualTo(5);
        assertThat(props.getFaceFields()).isEqualTo("age,gender");
        assertThat(props.getUserTopNum()).isEqualTo(3);
        assertThat(props.getFaceliveness()).isEqualTo(0.5);
    }

    @Test
    @DisplayName("ToString should work for POJO usage")
    void pojoContract() {
        FaceRecognitionV2Properties props = new FaceRecognitionV2Properties();
        assertThat(props.toString()).isNotNull();
        assertThat(props).isEqualTo(props);
    }

}
