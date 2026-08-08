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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {{ @link FileUtil }}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("FileUtil Tests")
class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("readFileAsString should read file content back as string")
    void readFileAsStringShouldReadContent() throws IOException {
        Path file = tempDir.resolve("sample.txt");
        Files.write(file, "hello baidu".getBytes(StandardCharsets.UTF_8));

        String content = FileUtil.readFileAsString(file.toString());
        assertThat(content).isEqualTo("hello baidu");
    }

    @Test
    @DisplayName("readFileAsString should handle multi-byte UTF-8 content")
    void readFileAsStringShouldHandleLargeContent() throws IOException {
        Path file = tempDir.resolve("big.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            sb.append("line-").append(i).append("\n");
        }
        Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));

        String content = FileUtil.readFileAsString(file.toString());
        assertThat(content).startsWith("line-0").endsWith("line-4999\n");
    }

    @Test
    @DisplayName("readFileAsString should throw FileNotFoundException for missing file")
    void readFileAsStringMissingFile() {
        String missing = tempDir.resolve("nope.txt").toString();
        assertThatThrownBy(() -> FileUtil.readFileAsString(missing))
            .isInstanceOf(FileNotFoundException.class);
    }

    @Test
    @DisplayName("readFileByBytes should return file bytes")
    void readFileByBytesShouldReturnBytes() throws IOException {
        Path file = tempDir.resolve("bytes.bin");
        byte[] data = "abc".getBytes(StandardCharsets.UTF_8);
        Files.write(file, data);

        byte[] result = FileUtil.readFileByBytes(file.toString());
        assertThat(result).isEqualTo(data);
    }

    @Test
    @DisplayName("readFileByBytes should throw FileNotFoundException for missing file")
    void readFileByBytesMissingFile() {
        String missing = tempDir.resolve("nope.bin").toString();
        assertThatThrownBy(() -> FileUtil.readFileByBytes(missing))
            .isInstanceOf(FileNotFoundException.class);
    }

}
