package com.baidu.ai.aip.spring.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Baidu AI Face Recognition V2 API.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = FaceRecognitionV2Properties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ FaceRecognitionV2Properties.class })
public class FaceRecognitionV2AutoConfiguration {
	
	/**
	 * Creates the Face Recognition V2 template.
	 * @param properties the face recognition properties
	 * @return the template
	 */
	@Bean
	public FaceRecognitionV2Template faceRecognitionV2Template(FaceRecognitionV2Properties properties) {
		return new FaceRecognitionV2Template(properties);
	}
	
	
}
