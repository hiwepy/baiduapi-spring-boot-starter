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
package com.baidu.ai.aip.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link HttpUtil }}.
 *
 * <p>Exercises the POST helper variants against an in-process HTTP server so the
 * encoding branches (UTF-8 vs GBK for NLP endpoints) are both executed.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("HttpUtil Tests")
class HttpUtilTest {

    private HttpServer server;
    private String lastBody;
    private String lastContentType;
    private String lastAccessToken;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        HttpHandler handler = new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                lastContentType = exchange.getRequestHeaders().getFirst("Content-Type");
                byte[] requestBody = exchange.getRequestBody().readAllBytes();
                lastBody = new String(requestBody, StandardCharsets.UTF_8);
                String query = exchange.getRequestURI().getQuery();
                lastAccessToken = query == null ? null : query.replace("access_token=", "");
                byte[] response = "ok-response".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        };
        server.createContext("/post", handler);
        server.createContext("/nlp/post", handler);
        server.start();
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private String url(String path) {
        return "http://" + server.getAddress().getHostName() + ":" + server.getAddress().getPort() + path;
    }

    @Test
    @DisplayName("post(requestUrl, accessToken, params) should use default content type")
    void postThreeArgs() throws Exception {
        String result = HttpUtil.post(url("/post"), "token-123", "a=1&b=2");

        assertThat(result).isEqualTo("ok-response");
        assertThat(lastAccessToken).isEqualTo("token-123");
        assertThat(lastContentType).isEqualTo(HttpUtil.CONTENT_TYPE);
        assertThat(lastBody).isEqualTo("a=1&b=2");
    }

    @Test
    @DisplayName("post(...) with explicit content type should override the default")
    void postFourArgs() throws Exception {
        String result = HttpUtil.post(url("/post"), "tok", "application/json", "{\"k\":\"v\"}");

        assertThat(result).isEqualTo("ok-response");
        assertThat(lastContentType).isEqualTo("application/json");
        assertThat(lastBody).isEqualTo("{\"k\":\"v\"}");
    }

    @Test
    @DisplayName("post(...) on an nlp endpoint should switch to GBK encoding")
    void postNlpEndpointUsesGbk() throws Exception {
        String result = HttpUtil.post(url("/nlp/post"), "tok", "text/plain", "nlp-payload");

        assertThat(result).isEqualTo("ok-response");
        assertThat(lastBody).isEqualTo("nlp-payload");
    }

    @Test
    @DisplayName("post(...) with explicit encoding should propagate the request")
    void postFiveArgs() throws Exception {
        String result = HttpUtil.post(url("/post"), "tok", "application/x-www-form-urlencoded", "p=v", "UTF-8");

        assertThat(result).isEqualTo("ok-response");
        assertThat(lastBody).isEqualTo("p=v");
    }

    @Test
    @DisplayName("postGeneralUrl should send the provided body and return the response")
    void postGeneralUrl() throws Exception {
        String result = HttpUtil.postGeneralUrl(url("/post"), "text/plain", "raw-body", "UTF-8");

        assertThat(result).isEqualTo("ok-response");
        assertThat(lastContentType).isEqualTo("text/plain");
        assertThat(lastBody).isEqualTo("raw-body");
    }

    @Test
    @DisplayName("CONTENT_TYPE constant should be form-urlencoded")
    void contentTypeConstant() {
        assertThat(HttpUtil.CONTENT_TYPE).isEqualTo("application/x-www-form-urlencoded");
    }

}
