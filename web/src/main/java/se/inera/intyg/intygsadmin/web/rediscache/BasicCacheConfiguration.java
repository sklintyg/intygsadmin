/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsadmin.web.rediscache;

import com.google.common.base.Strings;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import se.inera.intyg.intygsadmin.web.rediscache.util.ConnectionStringUtil;

/**
 * Initialization and activation of Redis cache for a single Redis host.
 */
@Configuration
@EnableCaching
@Profile("!embedded-redis")
public class BasicCacheConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BasicCacheConfiguration.class);

    @Value("${intyg.redis.host}")
    protected String redisHost;
    @Value("${intyg.redis.port}")
    protected String redisPort;
    @Value("${intyg.redis.password}")
    protected String redisPassword;
    @Value("${intyg.redis.cache.default_entry_expiry_time_in_seconds}")
    protected int defaultEntryExpiry;
    @Value("${intyg.redis.sentinel.master.name}")
    protected String redisSentinelMasterName;

    @Value("${redis.cluster.nodes:}")
    private String redisClusterNodes;
    @Value("${redis.cluster.password:}")
    private String redisClusterPassword;
    @Value("${redis.cluster.max.redirects:3}")
    private Integer redisClusterMaxRedirects;
    @Value("${redis.cluster.read.timeout:PT1M}")
    private String redisClusterReadTimeout;

    @Resource
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Due to complexity reasons, we do a programmatic profile check here to pick the appropriate JedisConnectionFactory.
     */
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        final var activeProfiles = List.of(environment.getActiveProfiles());
        if (activeProfiles.contains("redis-cluster")) {
            return clusterConnectionFactory();
        }
        if (activeProfiles.contains("redis-sentinel")) {
            return sentinelConnectionFactory();
        }

        return plainConnectionFactory();
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    RedisCacheManager cacheManager() {
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(jedisConnectionFactory());
        return new RedisCacheManager(cacheWriter, RedisCacheConfiguration.defaultCacheConfig());
    }

    private JedisConnectionFactory plainConnectionFactory() {
        LOG.info("Using redis standalone configuration.");
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(redisHost);
        standaloneConfiguration.setPort(Integer.parseInt(redisPort));
        if (StringUtils.hasLength(redisPassword)) {
            standaloneConfiguration.setPassword(redisPassword);
        }

        return new JedisConnectionFactory(standaloneConfiguration);
    }

    private JedisConnectionFactory sentinelConnectionFactory() {
        LOG.info("Detected spring profile 'redis-sentinel', using redis sentinel configuration.");
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
            .master(redisSentinelMasterName);

        if (Strings.isNullOrEmpty(redisHost) || Strings.isNullOrEmpty(redisPort)) {
            throw new IllegalStateException("Cannot bootstrap RedisSentinelConfiguration, redis.host or redis.port is null or empty");
        }
        List<String> hosts = ConnectionStringUtil.parsePropertyString(redisHost);
        List<String> ports = ConnectionStringUtil.parsePropertyString(redisPort);

        if (hosts.size() == 0 || ports.size() == 0 || hosts.size() != ports.size()) {
            throw new IllegalStateException(
                "Cannot bootstrap RedisSentinelConfiguration, number of redis.host and/or redis.port was zero or not equal.");
        }

        for (int a = 0; a < hosts.size(); a++) {
            sentinelConfig = sentinelConfig.sentinel(hosts.get(a), Integer.parseInt(ports.get(a)));
        }

        return new JedisConnectionFactory(sentinelConfig);
    }

    private JedisConnectionFactory clusterConnectionFactory() {
        LOG.info("Detected spring profile 'redis-cluster', using redis cluster configuration.");
        final var clusterConfig = new RedisClusterConfiguration(ConnectionStringUtil.parsePropertyString(redisClusterNodes));
        clusterConfig.setMaxRedirects(redisClusterMaxRedirects);
        clusterConfig.setPassword(redisClusterPassword);

        final var clientConfig = JedisClientConfiguration.builder()
            .readTimeout(Duration.parse(redisClusterReadTimeout)).build();

        return new JedisConnectionFactory(clusterConfig, clientConfig);
    }
}
