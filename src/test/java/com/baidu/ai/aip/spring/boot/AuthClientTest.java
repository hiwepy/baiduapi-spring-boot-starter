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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AuthClient}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("AuthClient Tests")
class AuthClientTest {

    private static String originalHost;
    private HttpServer server;

    @BeforeAll
    static void saveOriginalHost() {
        originalHost = AuthClient.AUTH_HOST;
    }

    @AfterAll
    static void restoreOriginalHost() {
        AuthClient.AUTH_HOST = originalHost;
    }

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/oauth/2.0/token", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                boolean valid = query != null && query.contains("client_id=test-ak")
                        && query.contains("client_secret=test-sk");
                byte[] response;
                if (valid) {
                    response = "{\"access_token\":\"fake-token-123\",\"expires_in\":2592000}"
                            .getBytes(StandardCharsets.UTF_8);
                } else {
                    response = "{\"error\":\"invalid_client\"}".getBytes(StandardCharsets.UTF_8);
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(valid ? 200 : 400, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        });
        server.start();
        AuthClient.AUTH_HOST = "http://127.0.0.1:" + server.getAddress().getPort() + "/oauth/2.0/token";
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("Instance can be created")
    void instance() {
        assertThat(new AuthClient()).isNotNull();
    }

    @Test
    @DisplayName("AUTH_HOST constant should be the baidu OAuth endpoint")
    void authHostConstant() {
        assertThat(originalHost).isEqualTo("https://aip.baidubce.com/oauth/2.0/token");
    }

    @Test
    @DisplayName("getAuth with valid credentials should return an access token")
    void getAuthWithValidCredentials() {
        String token = AuthClient.getAuth("test-ak", "test-sk");
        assertThat(token).isEqualTo("fake-token-123");
    }

    @Test
    @DisplayName("getAuth with invalid credentials should return null")
    void getAuthWithInvalidCredentials() {
        String token = AuthClient.getAuth("wrong-ak", "wrong-sk");
        assertThat(token).isNull();
    }

    @Test
    @DisplayName("getAuth with empty credentials should never throw and return null")
    void getAuthWithEmptyCredentialsShouldBeSafe() {
        // Empty credentials -> server returns400 -> exception caught -> null
        AuthClient.AUTH_HOST = originalHost;
        String token = AuthClient.getAuth("", "");
        assertThat(token).isNull();
    }

}
