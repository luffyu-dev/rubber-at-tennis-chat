package com.rubber.at.tennis.chat.manager.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2023/12/27
 */
@Data
public class OpenAiConfigDto {



    /**
     * assToken
     */
    private  String openaiBeta = "assistants=v1";

    /**
     * openKey
     */
    private  String openaiKey = "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";

    /**
     * 助手id
     */
    private String assistantId = "asst_WhFAtHUc3gahhsHBPHRnXBxR";



    public Map<String,String> initHeader(){
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("Authorization","Bearer "+this.openaiKey);
        header.put("OpenAI-Beta",this.openaiBeta);
        return header;
    }
}
