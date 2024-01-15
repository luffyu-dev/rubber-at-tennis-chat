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
    private  String openaiBeta;

    /**
     * openKey
     */
    private  String openaiKey ;

    /**
     * 助手id
     */
    private String assistantId;



    public Map<String,String> initHeader(){
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        header.put("Authorization","Bearer "+this.openaiKey);
        header.put("OpenAI-Beta",this.openaiBeta);
        return header;
    }
}
