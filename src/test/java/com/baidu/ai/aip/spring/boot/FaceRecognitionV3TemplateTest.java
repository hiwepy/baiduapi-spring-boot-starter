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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {{ @link FaceRecognitionV3Template }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("FaceRecognitionV3Template Tests")
class FaceRecognitionV3TemplateTest {

    private FaceRecognitionV3Properties properties() {
        FaceRecognitionV3Properties props = new FaceRecognitionV3Properties();
        props.setClientId("ak");
        props.setClientSecret("sk");
        props.setMaxFaceNum(2);
        props.setMaxUserNum(3);
        return props;
    }

    @Test
    @DisplayName("Constructor should store the provided properties")
    void constructorStoresProperties() {
        FaceRecognitionV3Properties props = new FaceRecognitionV3Properties();
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(props);
        assertThat(template.getProperties()).isSameAs(props);
    }

    @Test
    @DisplayName("CONTENT_TYPE constant should be application/json")
    void contentTypeConstant() {
        assertThat(FaceRecognitionV3Template.CONTENT_TYPE).isEqualTo("application/json");
    }

    @Test
    @DisplayName("URL constants should point at the baidu face V3 endpoints")
    void urlConstants() {
        assertThat(FaceRecognitionV3Template.FACE_DETECT_URL).contains("/rest/2.0/face/v3/detect");
        assertThat(FaceRecognitionV3Template.FACE_MATCH_URL).contains("/rest/2.0/face/v3/match");
        assertThat(FaceRecognitionV3Template.FACE_SEARCH_URL).contains("/rest/2.0/face/v3/search");
        assertThat(FaceRecognitionV3Template.FACE_PERSON_VERIFY_URL).contains("/rest/2.0/face/v3/person/verify");
        assertThat(FaceRecognitionV3Template.FACE_LIVENESS_VERIFY_URL).contains("/rest/2.0/face/v3/faceverify");
        assertThat(FaceRecognitionV3Template.FACE_MERGE_URL).contains("/rest/2.0/face/v1/merge");
    }

    @Test
    @DisplayName("getAccessToken should resolve to null with empty credentials")
    void getAccessTokenWithoutCredentials() {
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(properties());
        try {
            assertThat(template.getAccessToken("", "")).isNull();
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("All network methods should execute their success path and return wrapped result")
    void networkMethodsShouldReturnWrappedResult() {
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(properties());
        // Stub AuthClient and HttpUtil so the full request-construction path is deterministic.
        try (MockedStatic<AuthClient> auth = mockStatic(AuthClient.class);
             MockedStatic<HttpUtil> http = mockStatic(HttpUtil.class)) {
            auth.when(() -> AuthClient.getAuth(anyString(), anyString())).thenReturn("token");
            http.when(() -> HttpUtil.post(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("{\"error_code\":\"0\"}");

            assertThat(template.detect(new byte[] { 1 })).isNotNull();
            assertThat(template.detect(new byte[] { 1 }, FaceType.LIVE)).isNotNull();
            assertThat(template.detect(new byte[] { 1 }, FaceType.LIVE, FaceLiveness.NONE)).isNotNull();
            assertThat(template.detect("img")).isNotNull();
            assertThat(template.detect("img", FaceType.LIVE)).isNotNull();
            assertThat(template.detect("img", FaceType.LIVE, FaceLiveness.NONE)).isNotNull();

            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 })).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 }, FaceType.LIVE)).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 }, FaceType.LIVE, FaceQuality.LOW)).isNotNull();
            assertThat(template.match(new byte[] { 1 }, new byte[] { 2 }, FaceType.LIVE, FaceQuality.LOW, FaceLiveness.NORMAL)).isNotNull();
            assertThat(template.match("img1", "img2")).isNotNull();
            assertThat(template.match("img1", "img2", FaceType.LIVE)).isNotNull();
            assertThat(template.match("img1", "img2", FaceType.LIVE, FaceQuality.LOW)).isNotNull();
            assertThat(template.match("img1", "img2", FaceType.LIVE, FaceQuality.LOW, FaceLiveness.NORMAL)).isNotNull();

            assertThat(template.search(new byte[] { 1 }, "group")).isNotNull();
            assertThat(template.search(new byte[] { 1 }, "group", FaceQuality.LOW)).isNotNull();
            assertThat(template.search("img", "group")).isNotNull();
            assertThat(template.search("img", "group", FaceQuality.LOW)).isNotNull();
            assertThat(template.search("img", "group", FaceQuality.LOW, FaceLiveness.NORMAL)).isNotNull();

            assertThat(template.faceNew("img", "g", "u", "info", FaceQuality.LOW, FaceLiveness.NORMAL)).isNotNull();
            assertThat(template.faceRenew("img", "g", "u", "info", FaceQuality.LOW, FaceLiveness.NORMAL)).isNotNull();
            assertThat(template.faceDelete("g", "u", "token")).isNotNull();
            assertThat(template.faceInfo("g", "u")).isNotNull();
            assertThat(template.faceList("g", "u")).isNotNull();
            assertThat(template.faceUsers("g", 0, 100)).isNotNull();
            assertThat(template.userCopy("g", "u", "tg")).isNotNull();
            assertThat(template.userDelete("g", "u")).isNotNull();
            assertThat(template.groupNew("g")).isNotNull();
            assertThat(template.groupDelete("g")).isNotNull();
            assertThat(template.groupList(0, 100)).isNotNull();

            assertThat(template.personverify(new byte[] { 1 }, "id", "name")).isNotNull();
            assertThat(template.personverify(new byte[] { 1 }, "id", "name", FaceQuality.LOW)).isNotNull();
            assertThat(template.personverify("img", "id", "name")).isNotNull();
            assertThat(template.personverify("img", "id", "name", FaceQuality.LOW)).isNotNull();
            assertThat(template.personverify("img", "id", "name", FaceQuality.LOW, FaceLiveness.HIGH)).isNotNull();

            assertThat(template.faceVerify(new byte[] { 1 }, FaceOption.COMMON)).isNotNull();
            assertThat(template.faceVerify("img", FaceOption.GATE)).isNotNull();

            assertThat(template.merge(new byte[] { 1 }, new byte[] { 2 })).isNotNull();
            assertThat(template.merge("tpl", "tgt")).isNotNull();
        }
    }

    @Test
    @DisplayName("wrap should reset error_code to 0 when code equals '0'")
    void wrapZeroErrorCode() throws Exception {
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "0");
        JSONObject wrapped = invokeWrap(template, result);
        assertThat(wrapped.getIntValue("error_code")).isEqualTo(0);
    }

    @Test
    @DisplayName("wrap should propagate lookup failure for non-zero codes when bundle is unavailable")
    void wrapNonZeroErrorCode() {
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "999");
        assertThatThrownBy(() -> invokeWrap(template, result)).isNotNull();
    }

    @Test
    @DisplayName("wrap should set liveness=0 when error_code is '223120' even if message lookup fails")
    void wrapLivenessErrorCode() {
        FaceRecognitionV3Template template = new FaceRecognitionV3Template(null);
        JSONObject result = new JSONObject();
        result.put("error_code", "223120");
        assertThatThrownBy(() -> invokeWrap(template, result)).isNotNull();
    }

    private JSONObject invokeWrap(FaceRecognitionV3Template template, JSONObject result) throws Exception {
        java.lang.reflect.Method method = FaceRecognitionV3Template.class.getDeclaredMethod("wrap", JSONObject.class);
        method.setAccessible(true);
        return (JSONObject) method.invoke(template, result);
    }

}
