package com.wechat.service.redis.impl;

import com.wechat.model.Admin;
import com.wechat.service.redis.RedisService;
import org.springframework.stereotype.Service;


/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/16
 */
@Service
public class RedisServiceImpl extends RedisService<Admin> {

    private static final String REDIS_KEY="redis_key";

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }
}
