package com.rubber.at.tennis.chat.dao.dal;

import com.rubber.at.tennis.chat.dao.entity.GlobalFieldDictEntity;
import com.rubber.base.components.mysql.plugins.admin.IBaseAdminService;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 字段数据表 服务类
 * </p>
 *
 * @author rockyu
 * @since 2024-01-07
 */
public interface IChatGlobalFieldDictDal extends IBaseAdminService<GlobalFieldDictEntity> {


    GlobalFieldDictEntity queryByKey(String key);

    Map<String,String> batchQuery(Set<String> keys);
}
