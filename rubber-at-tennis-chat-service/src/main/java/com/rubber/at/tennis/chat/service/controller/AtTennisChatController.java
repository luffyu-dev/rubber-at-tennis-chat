package com.rubber.at.tennis.chat.service.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.rubber.at.tennis.chat.api.dto.NewChatGptDto;
import com.rubber.at.tennis.chat.api.dto.SendChatGptResult;
import com.rubber.base.components.util.annotation.NeedLogin;
import com.rubber.base.components.util.result.ResultMsg;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2023/12/26
 */
@RestController
@RequestMapping("/chat")
public class AtTennisChatController {



    @PostMapping("/query/message")
    @NeedLogin(request = false)
    public ResultMsg queryResult(@RequestBody SendChatGptResult dto){
        String url = "http://38.55.107.245:38033/openai-api/chat/query/message";
        String value = HttpRequest
                .post(url)
                .body(JSON.toJSONString(dto))
                .timeout(20000)
                .execute().body();
        return JSON.parseObject(value,ResultMsg.class);
    }


    @PostMapping("/send/message")
    @NeedLogin(request = false)
    public ResultMsg sendMessgae(@RequestBody NewChatGptDto dto){
        String url = "http://38.55.107.245:38033/openai-api/chat/send/message";
        String value = HttpRequest
                .post(url)
                .body(JSON.toJSONString(dto))
                .timeout(20000)
                .execute().body();
        return JSON.parseObject(value,ResultMsg.class);
    }


}
