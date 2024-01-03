package com.rubber.at.tennis.chat.service.component;

import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import org.springframework.stereotype.Component;

/**
 * @author luffyu
 * Created on 2024/1/1
 */
@Component
public class AiTennisConfigComponent {


    /**
     * 查询配置信息
     * @return
     */
    public OpenAiConfigDto initAndCreateConfig(){
        return new OpenAiConfigDto();
    }
}
