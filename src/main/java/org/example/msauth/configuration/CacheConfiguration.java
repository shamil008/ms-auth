package org.example.msauth.configuration;

import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.redisson.Redisson.create;
@Configuration
public class CacheConfiguration {
    @Value("${redis.server.urls}")
    private String redisServer;

    @Bean
    public RedissonClient redissonSingleClient() {
        var config = new Config();
        config
                .setCodec(new SerializationCodec())
                .useSingleServer()
                .setAddress(redisServer);
        return create(config);
    }
}
