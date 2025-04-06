package com.zhai.picshared.manager.redis;

import cn.hutool.json.JSONUtil;
import com.zhai.picshared.manager.LocalPictureCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class CacheManagerHelper {

    private static final String REDIS_CACHE_PREFIX = "yupicture:listPictureVOByPage:";
    private static final String REDIS_KEY_SET_PREFIX = "cache:keys:space:";
    private static final String PUBSUB_CHANNEL = "clearLocalCache";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LocalPictureCache localPictureCache;
    /**
     * 获取缓存
     */
    public String get(String key) {
//        String local = localPictureCache.getCache().getIfPresent(key);
//        if (local != null) {
//            return local;
//        }
        String redisValue = stringRedisTemplate.opsForValue().get(key);
        if (redisValue != null) {
            localPictureCache.getCache().put(key, redisValue);
        }
        return redisValue;
    }

    /**
     * 写入缓存，并记录 key 到空间 set
     */
    public void put(String key, String value, Long spaceId) {
//        localPictureCache.getCache().put(key, value);
        int ttl = 300 + (int) (Math.random() * 300);
        stringRedisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
        stringRedisTemplate.opsForSet().add(REDIS_KEY_SET_PREFIX + spaceId, key);
        System.out.println("放入了" + REDIS_KEY_SET_PREFIX + spaceId);
    }

    /**
     * 清除某个空间的所有缓存
     */
    public void clearBySpace(Long spaceId) {
        String keySetKey = REDIS_KEY_SET_PREFIX + (spaceId == null ? "public" : spaceId);
        Set<String> keys = stringRedisTemplate.opsForSet().members(keySetKey);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
            stringRedisTemplate.delete(keySetKey);
//            keys.forEach(k -> stringRedisTemplate.convertAndSend(PUBSUB_CHANNEL, k));
            System.out.println("缓存清理");
//            log.info("[缓存清理] 清除空间 {} 的缓存共 {} 条", spaceId, keys.size());
        }
    }

} 
