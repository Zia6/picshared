package com.zhai.picshared.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhai.picshared.model.dto.spaceuser.SpaceUserAddRequest;
import com.zhai.picshared.model.dto.spaceuser.SpaceUserQueryRequest;
import com.zhai.picshared.model.entity.SpaceUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhai.picshared.model.vo.spaceuser.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Z2023
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2025-03-03 09:03:53
*/
public interface SpaceUserService extends IService<SpaceUser> {


    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    void validSpaceUser(SpaceUser spaceUser, boolean add);

    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);
}
