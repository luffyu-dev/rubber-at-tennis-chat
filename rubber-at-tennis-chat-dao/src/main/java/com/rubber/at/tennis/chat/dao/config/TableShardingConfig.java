package com.rubber.at.tennis.chat.dao.config;

import com.rubber.base.components.mysql.plugins.sharding.config.RubberShardingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2022/10/16
 */
@Configuration
public class TableShardingConfig {

    @Bean
    public RubberShardingConfig shardingConfig(){
        RubberShardingConfig rubberShardingConfig = new RubberShardingConfig();
        rubberShardingConfig.table("t_global_field_dict")
                .realDb("user_db")
                .realTable("t_global_field_dict");
        return rubberShardingConfig;
    }
}
