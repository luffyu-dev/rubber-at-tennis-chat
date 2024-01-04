package com.rubber.at.tennis.chat.manager.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.openai.api.dto.OpenAiModelConfig;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
public class HttpProxyUtils {


    private static final String PROXY_POST_URL = "http://38.55.107.245:38000/openai-api/proxy/post";

    private static final String PROXY_GET_URL = "http://38.55.107.245:38000/openai-api/proxy/get";


    public static String post(String url, Map<String,String> header,JSONObject body,Integer timeOut){
        OpenAiModelConfig openAiModelConfig = new OpenAiModelConfig();
        openAiModelConfig.setUrl(url);
        openAiModelConfig.setHeader(header);
        openAiModelConfig.setReq(body);
        openAiModelConfig.setTimeOut(timeOut);
        String value = HttpRequest
                .post(PROXY_POST_URL)
                .body(JSON.toJSONString(openAiModelConfig))
                .timeout(30000)
                .execute().body();
        return value;
    }



    public static String get(String url, Map<String,String> header,JSONObject body,Integer timeOut){
        OpenAiModelConfig openAiModelConfig = new OpenAiModelConfig();
        openAiModelConfig.setUrl(url);
        openAiModelConfig.setHeader(header);
        openAiModelConfig.setReq(body);
        openAiModelConfig.setTimeOut(timeOut);
        String value = HttpRequest
                .post(PROXY_GET_URL)
                .body(JSON.toJSONString(openAiModelConfig))
                .timeout(30000)
                .execute().body();
        return value;
    }

}
