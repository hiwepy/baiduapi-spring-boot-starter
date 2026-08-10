package com.baidu.ai.aip.spring.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Baidu AI Face Recognition V3 API.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = FaceRecognitionV3Properties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ FaceRecognitionV3Properties.class })
public class FaceRecognitionV3AutoConfiguration {
	
	/**
	 * Creates the Face Recognition V3 template.
	 * @param properties the face recognition properties
	 * @return the template
	 */
	@Bean
	public FaceRecognitionV3Template faceRecognitionV3Template(FaceRecognitionV3Properties properties) {
		return new FaceRecognitionV3Template(properties);
	}
	
}
