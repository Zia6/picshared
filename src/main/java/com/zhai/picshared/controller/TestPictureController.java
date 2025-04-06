package com.zhai.picshared.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zhai.picshared.common.BaseResponse;
import com.zhai.picshared.common.ResultUtils;
import com.zhai.picshared.exception.ErrorCode;
import com.zhai.picshared.exception.ThrowUtils;
import com.zhai.picshared.model.dto.picture.PictureQueryRequest;
import com.zhai.picshared.model.entity.Picture;
import com.zhai.picshared.model.vo.picture.PictureVO;
import com.zhai.picshared.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/test/picture")
public class TestPictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Cache<String, String> LOCAL_CACHE =
            Caffeine.newBuilder()
                    .initialCapacity(1024)
                    .maximumSize(50000)
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build();

    /**
     * 1️⃣ 无缓存单条查询
     */
    @GetMapping("/get/no-cache")
    public BaseResponse<PictureVO> getPictureNoCache(@RequestParam Long id) {
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null,ErrorCode.PARAMS_ERROR);
        PictureVO pictureVO = pictureService.getPictureVO(picture, null);
        return ResultUtils.success(pictureVO);
    }

    /**
     * 2️⃣ 单层 Caffeine 缓存单条查询
     */
    @GetMapping("/get/local-cache")
    public BaseResponse<PictureVO> getPictureLocalCache(@RequestParam Long id) {
        String cacheKey = "test:get:local:" + id;
        String cachedValue = LOCAL_CACHE.getIfPresent(cacheKey);
        if (cachedValue != null) {
            PictureVO cachedVO = JSONUtil.toBean(cachedValue, PictureVO.class);
            return ResultUtils.success(cachedVO);
        }

        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null,ErrorCode.PARAMS_ERROR);
        PictureVO pictureVO = pictureService.getPictureVO(picture, null);

        LOCAL_CACHE.put(cacheKey, JSONUtil.toJsonStr(pictureVO));
        return ResultUtils.success(pictureVO);
    }

    /**
     * 3️⃣ 双层缓存单条查询
     */
    @GetMapping("/get/both-cache")
    public BaseResponse<PictureVO> getPictureBothCache(@RequestParam Long id) {
        String cacheKey = "test:get:both:" + id;

        // 本地缓存
        String cachedValue = LOCAL_CACHE.getIfPresent(cacheKey);
        if (cachedValue != null) {
            PictureVO cachedVO = JSONUtil.toBean(cachedValue, PictureVO.class);
            return ResultUtils.success(cachedVO);
        }

        // Redis 缓存
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        cachedValue = valueOps.get(cacheKey);
        if (cachedValue != null) {
            LOCAL_CACHE.put(cacheKey, cachedValue);
            PictureVO cachedVO = JSONUtil.toBean(cachedValue, PictureVO.class);
            return ResultUtils.success(cachedVO);
        }

        // DB 查询
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null,ErrorCode.PARAMS_ERROR);
        PictureVO pictureVO = pictureService.getPictureVO(picture, null);

        // 更新缓存
        String cacheValue = JSONUtil.toJsonStr(pictureVO);
        LOCAL_CACHE.put(cacheKey, cacheValue);
        int expire = RandomUtil.randomInt(300, 600);
        valueOps.set(cacheKey, cacheValue, expire, TimeUnit.SECONDS);

        return ResultUtils.success(pictureVO);
    }
}

