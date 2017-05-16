package com.wechat.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 描述: 如果配置了sentinel则优先使用sentinel连接方式，否则使用host配置方式
 * 作者: twl
 * 创建日期: 2017-05-15
 * 修改记录:
 */

@Configuration
@EnableCaching
@EnableRedisHttpSession
public class CacheConfiguration extends CachingConfigurerSupport {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    /**
     * redis sentinel master 名称，通常配置在sentinel.conf中
     */
    @Value("${spring.redis.sentinel.master}")
    private String master;

    /**
     * sentinel节点，逗号分隔，格式如：172.20.10.192:26379,172.20.10.193:26379
     */
    @Value("${spring.redis.sentinel.nodes}")
    private String nodes;

    /**
     * 连接超时时间，单位：毫秒
     */
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * 最小空闲连接数
     */
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    /**
     * 最大空闲连接数
     */
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    /**
     * 最大激活连接数
     */
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    /**
     * 最大等待毫秒数
     */
    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    /**
     * redis密码
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * redis密码
     */
    @Value("${spring.redis.port}")
    private int port;

    /**
     * redis密码
     */
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 通过@Cacheable代理的缓存默认失效时间(单位：秒)
     */
    @Value("${spring.redis.cacheableDefaultExpSec}")
    private int cacheableDefaultExpSec;
    /**
     * 设置过期时间
     */
    @Value("${spring.redis.expires}")
    private String expires;

    @Bean
    public KeyGenerator defaultKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                //target.getClass().getName() 不适用此种方式
                sb.append(method.getDeclaringClass().getName()).append(".").append(method.getName()).append("[");
                for (Object obj : params) {
                    if(obj == null){
                        sb.append("Null");
                    }else{
                        sb.append(obj.toString());
                    }
                    sb.append("^");
                }
                if(sb.length() > 1){
                    //remove the last split
                    return sb.substring(0,sb.length()-1)+"]";
                }
                sb.append("]");
                return sb.toString();
            }
        };
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = null;
        if(!StringUtils.isEmpty(nodes)){
            factory = new JedisConnectionFactory(buildSentinelConfiguration());
        }else{
            factory = new JedisConnectionFactory();
            factory.setHostName(host);
            factory.setPort(port);
        }
        factory.setPoolConfig(buildJedisPoolConfig());
        factory.setTimeout(timeout); //设置连接超时时间
        factory.setPassword(password);
        return factory;
    }


    //redis键过期事件订阅使用单个redis
    public JedisConnectionFactory getSingleRedisConnectionFactory() {

        if(StringUtils.isEmpty(port)||StringUtils.isEmpty(port)){
            logger.error("redis host 或 port 为空");
            throw new RuntimeException("redis host 或 port 为空");
        }
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPoolConfig(buildJedisPoolConfig());
        factory.setTimeout(timeout); //设置连接超时时间
        factory.setPassword(password);
        return factory;
    }

    /**
     * 构建Redis连接池配置
     * @return
     */
    private JedisPoolConfig buildJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        return config;
    }

    /**
     * 构建Redis Sentinel配置
     * @return
     */
    private RedisSentinelConfiguration buildSentinelConfiguration(){
        RedisSentinelConfiguration sentinels = new RedisSentinelConfiguration();
        sentinels.setMaster(master);
        Objects.requireNonNull(nodes,"redis sentinels can't null.");
        String[] hostAndPosts = nodes.split(",");
        for(String hp : hostAndPosts){
            String[] parts = hp.split(":");
            if(parts.length != 2){
                logger.error("redis sentinels nodes config error,please check again.");
            }
            sentinels.addSentinel(new RedisNode(parts[0],Integer.parseInt(parts[1])));
        }
        return sentinels;
    }



    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(cacheableDefaultExpSec);
        return cacheManager;
    }

    @Bean
    public StringRedisTemplate StringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    //redis键过期事件订阅使用单个redis
    public RedisTemplate getSingleRedisTemplate() {
        RedisConnectionFactory factory = getSingleRedisConnectionFactory();
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }


    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

    private void setSerializer(RedisTemplate template) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
        template.setKeySerializer(jdkSerializer);
        template.setValueSerializer(jdkSerializer);
        template.setHashKeySerializer(jdkSerializer);
        template.setHashValueSerializer(jdkSerializer);
        template.afterPropertiesSet();
    }

    private void setExpires(RedisCacheManager cacheManager) {
        Map<String, Long> map = new HashMap<String, Long>();
        if(!StringUtils.isEmpty(expires)) {
            String[] es = expires.split(",");
            for (int i = 0; i < es.length; ++i) {
                String[] temp = es[i].split(":");
                if(temp.length == 2) {
                    map.put(temp[0], Long.parseLong(temp[1]));
                }
            }
        }
        cacheManager.setExpires(map);
    }

    /**
     * 构建接收订阅消息的监听器
     * */
    /*@Bean
    public KeyExpiresMessageListener keyExpiresMessageListener(){
        KeyExpiresMessageListener keyExpiresMessageListener = new KeyExpiresMessageListener();
        keyExpiresMessageListener.setRedisTemplate(getSingleRedisTemplate());
        return keyExpiresMessageListener;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        RedisMessageListenerContainer topinContainer = new RedisMessageListenerContainer();
        topinContainer.setConnectionFactory(redisConnectionFactory());
        ExecutorService executor = Executors.newFixedThreadPool(10);
        topinContainer.setTaskExecutor(executor);
        Set<Topic> topicSet = new HashSet<Topic>();
        topicSet.add(new ChannelTopic("__keyevent@0__:expired"));
        topinContainer.addMessageListener(keyExpiresMessageListener(),topicSet);
        return topinContainer;
    }*/
}
