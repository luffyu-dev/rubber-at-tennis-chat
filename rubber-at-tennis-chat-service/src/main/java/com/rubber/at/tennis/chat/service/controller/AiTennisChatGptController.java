package com.rubber.at.tennis.chat.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.rubber.at.tennis.chat.api.AiTennisChatGptApi;
import com.rubber.at.tennis.chat.api.dto.BaseChatReq;
import com.rubber.at.tennis.chat.api.dto.message.MsgChatReq;
import com.rubber.at.tennis.chat.api.dto.message.SendMessageReq;
import com.rubber.at.tennis.chat.api.dto.runs.ChatRunsReq;
import com.rubber.at.tennis.chat.api.dto.thread.ThreadChatReq;
import com.rubber.at.tennis.chat.dao.dal.IChatGlobalFieldDictDal;
import com.rubber.at.tennis.chat.dao.entity.GlobalFieldDictEntity;
import com.rubber.base.components.util.annotation.NeedLogin;
import com.rubber.base.components.util.result.ResultMsg;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

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
    @PostMapping("/chat/limit")
    @NeedLogin(request = false)
    public ResultMsg getChatLimit(@RequestBody BaseChatReq req){
        if (req.getUid() == null){
            return ResultMsg.success();
        }
        return ResultMsg.success(aiTennisChatGptApi.queryUserLimit(req));
    }


    /**
     * 查询消息列表
     * @param req
     * @return
     */
    @PostMapping("/chat/limit-add")
    @NeedLogin(request = false)
    public ResultMsg addChatLimit(@RequestBody BaseChatReq req){
        if (req.getUid() == null){
            return ResultMsg.success();
        }
        aiTennisChatGptApi.addUserLimit(req);
        return ResultMsg.success();
    }



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



    @Autowired
    private IChatGlobalFieldDictDal iChatGlobalFieldDictDal;

    private static AtomicInteger ADD_NUM = new AtomicInteger(0);



    /**
     * 查询状态
     * @param req
     * @return
     */
    @PostMapping("/add-ao-num")
    public ResultMsg addAoNum(@RequestBody ChatRunsReq req){
        ADD_NUM.getAndIncrement();
        return ResultMsg.success();

    }


    @PostMapping("/get-ao-num")
    public ResultMsg getAoNum(@RequestBody ChatRunsReq req){
        Integer num = ADD_NUM.get();
        if (num == 0){
            GlobalFieldDictEntity addAoZqwNum = iChatGlobalFieldDictDal.queryByKey("add_ao_zqw_num");
            Integer value = Integer.valueOf(addAoZqwNum.getFieldValue());
            ADD_NUM.set(value);
            num = ADD_NUM.get();
        }
        JSONObject js = new JSONObject();
        js.put("value",num);
        return  ResultMsg.success(js);


    }


}
