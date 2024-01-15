package com.rubber.at.tennis.chat.dao.dal.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rubber.at.tennis.chat.dao.entity.GlobalFieldDictEntity;
import com.rubber.at.tennis.chat.dao.mapper.ChatGlobalFieldDictMapper;
import com.rubber.at.tennis.chat.dao.dal.IChatGlobalFieldDictDal;
import com.rubber.base.components.mysql.plugins.admin.BaseAdminService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 字段数据表 服务实现类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
@Service
public class ChatGlobalFieldDictDalImpl extends BaseAdminService<ChatGlobalFieldDictMapper, GlobalFieldDictEntity> implements IChatGlobalFieldDictDal {

    /**
     * @param key
     * @return
     */
    @Override
    public GlobalFieldDictEntity queryByKey(String key) {
        LambdaQueryWrapper<GlobalFieldDictEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(GlobalFieldDictEntity::getFieldKey,key);
        return getOne(lqw);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Map<String, String> batchQuery(Set<String> keys) {
        LambdaQueryWrapper<GlobalFieldDictEntity> lqw = new LambdaQueryWrapper<>();
        lqw.in(GlobalFieldDictEntity::getFieldKey,keys);
        List<GlobalFieldDictEntity> list = list(lqw);
        if (CollUtil.isEmpty(list)){
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(GlobalFieldDictEntity::getFieldKey,GlobalFieldDictEntity::getFieldValue));


    }
}
