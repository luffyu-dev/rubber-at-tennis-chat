package com.rubber.at.tennis.chat.service.common.exception;

import com.rubber.base.components.util.result.IResultHandle;
import com.rubber.base.components.util.result.code.ICodeHandle;
import com.rubber.base.components.util.result.exception.BaseResultRunTimeException;

/**
 * @author luffyu
 * Created on 2022/8/14
 */
public class RubberServiceException  extends BaseResultRunTimeException {

    public RubberServiceException(ICodeHandle handle) {
        super(handle);
    }

    public RubberServiceException(String code, String msg, Object data) {
        super(code, msg, data);
    }
}
