package com.zhai.picshared.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhai.picshared.model.dto.user.UserLoginRequest;
import com.zhai.picshared.model.dto.user.UserQueryRequest;
import com.zhai.picshared.model.entity.User;
import com.zhai.picshared.model.vo.user.LoginUserVO;
import com.zhai.picshared.model.vo.user.UserVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Z2023
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-24 17:46:26
*/

@Service
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    LoginUserVO getLoginUserVo(User user);

    /**
     * 获取当前登录用户脱敏
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    String getEncryptPassword(String userPassword);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 用户信息列表脱敏
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 条件查询
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

}
