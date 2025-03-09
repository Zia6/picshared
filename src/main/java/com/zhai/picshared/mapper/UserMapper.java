package com.zhai.picshared.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhai.picshared.model.entity.User;
import org.mapstruct.Mapper;

/**
* @author Z2023
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-02-24 17:46:26
* @Entity com.yupi.yupicturebackend.model.entity.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




