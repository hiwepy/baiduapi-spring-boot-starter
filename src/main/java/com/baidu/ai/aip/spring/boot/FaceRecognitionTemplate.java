/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ai.aip.utils.FileUtil;
import com.baidu.ai.aip.utils.GsonUtils;
import com.baidu.ai.aip.utils.HttpUtil;
import com.baidu.aip.util.Base64Util;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * TODO
 * 
 * @author ： <a href="https://github.com/vindell">wandl</a>
 */

public class FaceRecognitionTemplate {

	public static final String FACE_DETECT_URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
	public static final String FACE_MATCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/match";
	public static final String FACE_SEARCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/search";
	public static final String FACE_VERIFY_URL = "https://aip.baidubce.com/rest/2.0/face/v3/person/verify";
	public static final String FACE_MERGE_URL = "https://aip.baidubce.com/rest/2.0/face/v1/merge";

	private FaceRecognitionProperties properties;
	
	public FaceRecognitionTemplate(FaceRecognitionProperties properties) {
		this.properties = properties;
	}
	
	
	/**
	 * 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
	 */
	private final LoadingCache<String, Optional<String>> ACCESS_TOKEN_CACHES = CacheBuilder.newBuilder()
			// 设置并发级别为8，并发级别是指可以同时写缓存的线程数
			.concurrencyLevel(8)
			// 设置写缓存后600秒钟过期
			.expireAfterWrite(29, TimeUnit.DAYS)
			// 设置缓存容器的初始容量为10
			.initialCapacity(2)
			// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
			.maximumSize(10)
			// 设置要统计缓存的命中率
			.recordStats()
			// 设置缓存的移除通知
			.removalListener(new RemovalListener<String, Optional<String>>() {
				@Override
				public void onRemoval(RemovalNotification<String, Optional<String>> notification) {
					System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
				}
			})
			// build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
			.build(new CacheLoader<String, Optional<String>>() {

				@Override
				public Optional<String> load(String keySecret) throws Exception {
					JSONObject key = JSONObject.parseObject(keySecret);
					String token = AuthClient.getAuth(key.getString("clientId"), key.getString("clientSecret"));
					return Optional.fromNullable(token);
				}
			});

	/**
	 * 
	 * 企业内部开发获取access_token 先从缓存查，再到百度查
	 * https://ai.baidu.com/docs#/Face-Detect-V3/top
	 * 
	 * @param clientId     官网获取的 API Key（百度云应用的AK）
	 * @param clientSecret 官网获取的 Secret Key（百度云应用的SK）
	 * @return
	 * @throws ExecutionException
	 */
	public String getAccessToken(String clientId, String clientSecret) throws ExecutionException {

		JSONObject key = new JSONObject();
		key.put("clientId", clientId);
		key.put("clientSecret", clientSecret);

		Optional<String> opt = ACCESS_TOKEN_CACHES.get(key.toJSONString());
		return opt.isPresent() ? opt.get() : null;

	}

	/**
	 * 人脸检测与属性分析 https://ai.baidu.com/docs#/Face-Detect-V3/top
	 * 
	 * @author ： <a href="https://github.com/vindell">wandl</a>
	 * @return
	 */
	public String detect() {

		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("image", "027d8308a2ec665acb1bdf63e513bcb9");
			map.put("face_field", "faceshape,facetype");
			map.put("image_type", "FACE_TOKEN");

			String param = GsonUtils.toJson(map);

			// 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
			String accessToken = getAccessToken(properties.getClientId(), properties.getClientSecret());

			String result = HttpUtil.post(FACE_DETECT_URL, accessToken, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 人脸对比 https://ai.baidu.com/docs#/Face-Match-V3/top
	 * 
	 * @author ： <a href="https://github.com/vindell">wandl</a>
	 * @return
	 */
	public String match() {
		try {

			byte[] bytes1 = FileUtil.readFileByBytes("【本地文件1地址】");
			byte[] bytes2 = FileUtil.readFileByBytes("【本地文件2地址】");
			String image1 = Base64Util.encode(bytes1);
			String image2 = Base64Util.encode(bytes2);

			List<Map<String, Object>> images = new ArrayList<>();

			Map<String, Object> map1 = new HashMap<>();
			map1.put("image", image1);
			map1.put("image_type", "BASE64");
			map1.put("face_type", "LIVE");
			map1.put("quality_control", "LOW");
			map1.put("liveness_control", "NORMAL");

			Map<String, Object> map2 = new HashMap<>();
			map2.put("image", image2);
			map2.put("image_type", "BASE64");
			map2.put("face_type", "LIVE");
			map2.put("quality_control", "LOW");
			map2.put("liveness_control", "NORMAL");

			images.add(map1);
			images.add(map2);

			String param = GsonUtils.toJson(images);

			// 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
			String accessToken = getAccessToken(properties.getClientId(), properties.getClientSecret());

			String result = HttpUtil.post(FACE_MATCH_URL, accessToken, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 人脸搜索 https://ai.baidu.com/docs#/Face-Search-V3/top
	 */
	public String search() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("image", "027d8308a2ec665acb1bdf63e513bcb9");
			map.put("liveness_control", "NORMAL");
			map.put("group_id_list", "group_repeat,group_233");
			map.put("image_type", "FACE_TOKEN");
			map.put("quality_control", "LOW");

			String param = GsonUtils.toJson(map);

			// 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
			String accessToken = getAccessToken(properties.getClientId(), properties.getClientSecret());

			String result = HttpUtil.post(FACE_SEARCH_URL, accessToken, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**##########################人脸库管理########################### */
	
	
	/**
	 * 身份验证
	 * https://ai.baidu.com/docs#/Face-PersonVerify-V3/top
	 */
	public String personverify() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", "sfasq35sadvsvqwr5q...");
            map.put("image_type", "BASE64");
            map.put("id_card_number", "1234");
            map.put("liveness_control", "HIGH");
            map.put("name", "张三");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
            String accessToken = getAccessToken(properties.getClientId(), properties.getClientSecret());

            String result = HttpUtil.post(FACE_VERIFY_URL, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	* 在线活体检测
	* https://ai.baidu.com/docs#/Face-Liveness-V3/top
	*/
	public static String faceVerify() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceverify";
        try {

            String param = GsonUtils.toJson(null);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "[调用鉴权接口获取的token]";

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 人脸融合 
	 * https://ai.baidu.com/docs#/Face-Merge/top
	 */
	public String merge() {
		try {
			Map<String, Object> map = new HashMap<>();
			Map<String, Object> image_templateMap = new HashMap<>();
			image_templateMap.put("image", "sfasq35sadvsvqwr5q...");
			image_templateMap.put("image_type", "BASE64");
			image_templateMap.put("quality_control", "NONE");
			map.put("image_template", image_templateMap);
			Map<String, Object> image_targetMap = new HashMap<>();
			image_targetMap.put("image", "sfasq35sadvsvqwr5q...");
			image_targetMap.put("image_type", "BASE64");
			image_targetMap.put("quality_control", "NONE");
			map.put("image_target", image_targetMap);

			String param = GsonUtils.toJson(map);

			// 注意：access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token；
			String accessToken = getAccessToken(properties.getClientId(), properties.getClientSecret());

			String result = HttpUtil.post(FACE_MERGE_URL, accessToken, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
