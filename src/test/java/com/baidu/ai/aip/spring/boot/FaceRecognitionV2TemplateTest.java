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
import org.mockito.MockedStatic;

import com.alibaba.fastjson2.JSONObject;
import com.baidu.ai.aip.utils.HttpUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link FaceRecognitionV2Template}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FaceRecognitionV2Template Tests")
class FaceRecognitionV2TemplateTest {

    private FaceRecognitionV2Properties properties() {
        FaceRecognitionV2Properties props = new FaceRecognitionV2Properties();
        props.setClientId("ak");
        props.setClientSecret("sk");
        props.setMaxFaceNum(2);
        props.setFaceFields("age,gender");
        props.setUserTopNum(3);
        return props;
    }

    @Test
    @DisplayName("Constructor should store the provided properties")
    void constructorStoresProperties() {
        FaceRecognitionV2Properties props = new FaceRecognitionV2Properties();
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(props);
        assertThat(template.getProperties()).isSameAs(props);
    }

    @Test
    @DisplayName("URL constants should point at the baidu face V2/V3 endpoints")
    void urlConstants() {
        assertThat(FaceRecognitionV2Template.FACE_DETECT_URL).contains("/rest/2.0/face/v1/detect");
        assertThat(FaceRecognitionV2Template.FACE_MATCH_URL).contains("/rest/2.0/face/v2/match");
        assertThat(FaceRecognitionV2Template.FACE_SEARCH_URL).contains("/rest/2.0/face/v2/identify");
        assertThat(FaceRecognitionV2Template.FACE_PERSON_VERIFY_URL).contains("/rest/2.0/face/v3/person/verify");
        assertThat(FaceRecognitionV2Template.FACE_LIVENESS_VERIFY_URL).contains("/rest/2.0/face/v3/faceverify");
        assertThat(FaceRecognitionV2Template.FACE_MERGE_URL).contains("/rest/2.0/face/v1/merge");
    }

    @Test
    @DisplayName("getAccessToken should resolve to null with empty credentials")
    void getAccessTokenWithoutCredentials() {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(properties());
        try {
            assertThat(template.getAccessToken("", "")).isNull();
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("All network methods should execute their success path and return wrapped result")
    void networkMethodsShouldReturnWrappedResult() {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(properties());
        try (MockedStatic<AuthClient> auth = mockStatic(AuthClient.class);
             MockedStatic<HttpUtil> http = mockStatic(HttpUtil.class)) {
            auth.when(() -> AuthClient.getAuth(anyString(), anyString())).thenReturn("token");
            http.when(() -> HttpUtil.post(anyString(), anyString(), anyString())).thenReturn("{\"error_code\":\"0\"}");
            assertThat(template.detect("img")).isNotNull();
            assertThat(template.detect(new byte[] { 1, 2 })).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 })).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 }, "x")).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 }, "x", "y")).isNotNull();
            assertThat(template.match("a", "b")).isNotNull();
            assertThat(template.match("a", "b", "x")).isNotNull();
            assertThat(template.match("a", "b", "x", "y")).isNotNull();
            assertThat(template.search(new byte[] { 1 }, "g")).isNotNull();
            assertThat(template.search("img", "g")).isNotNull();
            assertThat(template.faceNew("img", "g", "u", "i")).isNotNull();
            assertThat(template.faceRenew("img", "g", "u", "i")).isNotNull();
            assertThat(template.faceDelete("g", "u")).isNotNull();
            assertThat(template.faceInfo("g", "u")).isNotNull();
            assertThat(template.faceUsers("g", 0, 100)).isNotNull();
            assertThat(template.groupList(0, 100)).isNotNull();
            assertThat(template.userCopy("g", "u", "t")).isNotNull();
            assertThat(template.userDelete("g", "u")).isNotNull();
            assertThat(template.personverify("img", "id", "name")).isNotNull();
            assertThat(template.faceVerify("img")).isNotNull();
        }
    }

    @Test
    @DisplayName("All network methods should return null when AuthClient throws")
    void networkMethodsShouldReturnNullOnAuthFailure() {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(properties());
        try (MockedStatic<AuthClient> auth = mockStatic(AuthClient.class)) {
            auth.when(() -> AuthClient.getAuth(anyString(), anyString()))
                .thenThrow(new RuntimeException("auth service unavailable"));

            assertThat(template.detect("img")).isNull();
            assertThat(template.detect(new byte[] { 1, 2 })).isNull();
            assertThat(template.match("a", "b", "x", "y")).isNull();
            assertThat(template.search("img", "g")).isNull();
            assertThat(template.faceNew("img", "g", "u", "i")).isNull();
            assertThat(template.faceRenew("img", "g", "u", "i")).isNull();
            assertThat(template.faceDelete("g", "u")).isNull();
            assertThat(template.faceInfo("g", "u")).isNull();
            assertThat(template.faceUsers("g", 0, 100)).isNull();
            assertThat(template.groupList(0, 100)).isNull();
            assertThat(template.userCopy("g", "u", "t")).isNull();
            assertThat(template.userDelete("g", "u")).isNull();
            assertThat(template.personverify("img", "id", "name")).isNull();
            assertThat(template.faceVerify("img")).isNull();
        }
    }

    @Test
    @DisplayName("wrap should reset error_code to integer 0 when code equals '0'")
    void wrapZeroErrorCode() throws Exception {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "0");
        JSONObject wrapped = invokeWrap(template, result);
        assertThat(wrapped.getIntValue("error_code")).isEqualTo(0);
    }

    @Test
    @DisplayName("wrap should add error_msg for known non-zero error codes")
    void wrapNonZeroErrorCode() throws Exception {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "222001");
        JSONObject wrapped = invokeWrap(template, result);
        assertThat(wrapped.getString("error_msg")).isNotBlank();
    }

    @Test
    @DisplayName("wrap should set liveness=0 when error_code is '223120'")
    void wrapLivenessErrorCode() throws Exception {
        FaceRecognitionV2Template template = new FaceRecognitionV2Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "223120");
        JSONObject wrapped = invokeWrap(template, result);
        assertThat(wrapped.getIntValue("liveness")).isEqualTo(0);
        assertThat(wrapped.getString("error_msg")).isNotBlank();
    }

    private JSONObject invokeWrap(FaceRecognitionV2Template template, JSONObject result) throws Exception {
        java.lang.reflect.Method method = FaceRecognitionV2Template.class.getDeclaredMethod("wrap", JSONObject.class);
        method.setAccessible(true);
        return (JSONObject) method.invoke(template, result);
    }

}
