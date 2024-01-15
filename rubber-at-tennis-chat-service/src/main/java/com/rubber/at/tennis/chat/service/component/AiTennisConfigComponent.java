package com.rubber.at.tennis.chat.service.component;

import com.rubber.at.tennis.chat.dao.dal.IChatGlobalFieldDictDal;
import com.rubber.at.tennis.chat.dao.entity.GlobalFieldDictEntity;
import com.rubber.at.tennis.chat.manager.dto.OpenAiConfigDto;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2024/1/1
 */
@Slf4j
@Component
public class AiTennisConfigComponent  implements ApplicationListener<ContextRefreshedEvent> {


    private static final String OPENAI_BETA_KEY = "openaiBeta";
    private static final String OPENAI_KEY = "openaiKey";
    private static final String ASSISTANTID_KET = "assistantId";

    static Set<String> OPENAI_CONFIG_FIELD = new HashSet<>(Arrays.asList(OPENAI_BETA_KEY,OPENAI_KEY,ASSISTANTID_KET));


    private final OpenAiConfigDto openAiConfig = new OpenAiConfigDto();

    @Autowired
    private IChatGlobalFieldDictDal iChatGlobalFieldDictDal;


    /**
     * 查询配置信息
     * @return
     */
    public OpenAiConfigDto initAndCreateConfig(){
        return openAiConfig;
    }


    /**
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Map<String,String> config = iChatGlobalFieldDictDal.batchQuery(OPENAI_CONFIG_FIELD);
        this.openAiConfig.setAssistantId(config.get(ASSISTANTID_KET));
        this.openAiConfig.setOpenaiKey(config.get(OPENAI_KEY));
        this.openAiConfig.setOpenaiBeta(config.get(OPENAI_BETA_KEY));
        log.info("openaiConfig{}",this.openAiConfig);
    }
}
