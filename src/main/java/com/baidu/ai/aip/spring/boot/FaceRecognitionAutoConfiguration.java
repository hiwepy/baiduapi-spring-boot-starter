package com.baidu.ai.aip.spring.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = FaceRecognitionProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ FaceRecognitionProperties.class })
/**
 * @see https://blog.csdn.net/u012150792/article/details/53446205
 * Berkeley DB 是一个嵌入式数据库，它适合于管理海量的(256T)、简单的数据。
 * BDB是以键值对(value/key)来存储和管理数据库的。键可以重复，数据值可以是任意类型的。BDB的底层是用B+树或者其他算法实现的。
 */
public class FaceRecognitionAutoConfiguration {
	
	@Autowired
	private FaceRecognitionProperties properties;
	
	 
	
}
