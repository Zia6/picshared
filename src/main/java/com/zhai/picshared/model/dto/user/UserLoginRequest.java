package com.zhai.picshared.model.dto.user;


import lombok.Data;

@Data
public class UserLoginRequest {
    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
