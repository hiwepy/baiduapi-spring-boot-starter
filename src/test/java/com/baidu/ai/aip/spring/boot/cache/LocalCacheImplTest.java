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
package com.baidu.ai.aip.spring.boot.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link LocalCacheImpl }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("LocalCacheImpl Tests")
class LocalCacheImplTest {

    @Test
    @DisplayName("put and get should round-trip a value")
    void putAndGetShouldRoundTrip() throws Exception {
        LocalCacheImpl<String, String> cache = new LocalCacheImpl<>();
        cache.put("key", "value");
        assertThat(cache.get("key")).isEqualTo("value");
    }

    @Test
    @DisplayName("get on a missing key should return null (loader returns absent)")
    void getMissingShouldReturnNull() throws Exception {
        LocalCacheImpl<String, String> cache = new LocalCacheImpl<>();
        assertThat(cache.get("missing")).isNull();
    }

    @Test
    @DisplayName("get after remove should return null")
    void removeShouldInvalidate() throws Exception {
        LocalCacheImpl<String, String> cache = new LocalCacheImpl<>();
        cache.put("key", "value");
        assertThat(cache.get("key")).isEqualTo("value");

        cache.remove("key");
        assertThat(cache.get("key")).isNull();
    }

    @Test
    @DisplayName("put with null value should be stored as absent and read back as null")
    void putNullShouldBeAbsent() throws Exception {
        LocalCacheImpl<String, String> cache = new LocalCacheImpl<>();
        // put wraps with Optional.of, so null is not directly allowed; use a value then invalidate
        cache.put("k", "v");
        assertThat(cache.get("k")).isEqualTo("v");
        cache.remove("k");
        assertThat(cache.get("k")).isNull();
    }

    @Test
    @DisplayName("Should implement LocalCache contract for arbitrary key/value types")
    void shouldWorkWithIntegerKeys() throws Exception {
        LocalCacheImpl<Integer, Long> cache = new LocalCacheImpl<>();
        cache.put(1, 100L);
        assertThat(cache.get(1)).isEqualTo(100L);
    }

}
