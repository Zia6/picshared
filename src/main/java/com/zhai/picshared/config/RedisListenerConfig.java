package com.zhai.picshared.config;

import com.zhai.picshared.manager.LocalPictureCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class RedisListenerConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LocalPictureCache localPictureCache;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(stringRedisTemplate.getConnectionFactory());

        container.addMessageListener(
                new MessageListenerAdapter((MessageListener) (message, pattern) -> {
                    String key = new String(message.getBody(), StandardCharsets.UTF_8);
                    localPictureCache.getCache().invalidate(key);
                    log.info("[本地缓存] 收到订阅清除请求，清除 key: {}", key);
                    System.out.println("[本地缓存] 收到订阅清除请求，清除 key: {key}");
                }),
                new PatternTopic("clearLocalCache")
        );

        return container; // ✅ Spring 会自动 start
    }
}
