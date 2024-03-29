package com.rubber.at.tennis.chat.service.controller;

import com.rubber.at.tennis.chat.api.AiTennisChatGptApi;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.message.MsgChatReq;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.base.components.util.annotation.NeedLogin;
import com.rubber.base.components.util.result.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luffyu
 * Created on 2024/1/1
 */
@RestController
@RequestMapping("/chat-gpt")
public class AiTennisChatGptController {


    @Autowired
    private AiTennisChatGptApi aiTennisChatGptApi;


    /**
     * 查询消息列表
     * @param req
     * @return
     */
    @PostMapping("/thread/list")
    @NeedLogin(request = false)
    public ResultMsg queryChatThreadList(@RequestBody BaseChatReq req){
        if (req.getUid() == null){
            return ResultMsg.success();
        }
        return ResultMsg.success(aiTennisChatGptApi.queryAssistantsThreads(req));
    }


    /**
     * 查询消息列表
     * @param req
     * @return
     */
    @PostMapping("/thread/remove")
    @NeedLogin
    public ResultMsg removeChatThreadList(@RequestBody ThreadChatReq req){
        if (req.getUid() == null){
            return ResultMsg.success();
        }
        aiTennisChatGptApi.removeChatThreads(req);
        return ResultMsg.success();
    }


    /**
     * 查询消息列表
     * @param req
     * @return
     */
    @PostMapping("/message/list")
    @NeedLogin(request = false)
    public ResultMsg queryChatMessageList(@RequestBody ThreadChatReq req){
        return ResultMsg.success(aiTennisChatGptApi.queryChatMessageList(req));
    }


    /**
     * 查询消息列表
     * @param req
     * @return
     */
    @PostMapping("/message/one")
    @NeedLogin
    public ResultMsg oneChatMessageList(@RequestBody MsgChatReq req){
        return ResultMsg.success(aiTennisChatGptApi.querySingleMsg(req));
    }



    /**
     * 发送消息列表
     * @param req
     * @return
     */
    @PostMapping("/message/send")
    @NeedLogin
    public ResultMsg sendMessage(@RequestBody SendMessageReq req){
        return ResultMsg.success(aiTennisChatGptApi.sendMessage(req));
    }


    /**
     * 查询状态
     * @param req
     * @return
     */
    @PostMapping("/message/status")
    @NeedLogin
    public ResultMsg queryRunsStatus(@RequestBody ChatRunsReq req){
        return ResultMsg.success(aiTennisChatGptApi.queryRunsStatus(req));
    }


}
