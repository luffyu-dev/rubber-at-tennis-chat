package com.rubber.at.tennis.chat.api.enums;

import lombok.Getter;

/**
 * @author luffyu
 * Created on 2024/1/21
 */
@Getter
public enum MsgRoleEnums {
    USER("user",""),

    ASSISTANTS("assistants","")

    ;


    MsgRoleEnums(String role, String msg) {
        this.role = role;
        this.msg = msg;
    }

    private final String role;

    private final String msg;

}
