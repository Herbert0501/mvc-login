package top.kangyaocoding.training.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述: Redis配置类
 *
 * @author K·Herbert
 * @since 2024-06-22 15:38
 */

@Configuration
public class RedisConfig {

    /**
     * @return RedissonClient - 用于与Redis进行交互的客户端实例
     */
    @Bean
    public RedissonClient redissonClient() {
        // 初始化Redisson配置对象
        Config config = new Config();
        // 配置连接到单个Redis服务器
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 根据配置创建并返回Redisson客户端实例
        return Redisson.create(config);
    }
}

