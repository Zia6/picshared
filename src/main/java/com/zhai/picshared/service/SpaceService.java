package com.zhai.picshared.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhai.picshared.model.dto.space.SpaceAddRequest;
import com.zhai.picshared.model.dto.space.SpaceQueryRequest;
import com.zhai.picshared.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhai.picshared.model.entity.User;
import com.zhai.picshared.model.vo.space.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Z2023
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-03-01 10:28:55
*/
public interface SpaceService extends IService<Space> {


    /**
     * 校验空间
     *
     * @param space
     * @param add
     */
    void validSpace(Space space, boolean add);

    /**
     * 根据空间级别填充空间
     *
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 添加空间
     *
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    void checkSpaceAuth(User loginUser, Space space);
}
