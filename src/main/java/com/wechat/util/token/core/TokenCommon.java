package com.wechat.util.token.core;

import com.wechat.util.http.HttpClient;
import com.wechat.util.json.JacksonUtils;
import com.wechat.util.token.request.AccessTokenRequestParam;
import com.wechat.util.token.response.AccessTokenResponseParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 描述：token的核心类，包括获取token的请求，存放token到缓存，从缓存获取token
 * 作者: TWL
 * 创建日期: 2017/5/14
 */

@Component
@ConfigurationProperties(prefix = "sdk")
public class TokenCommon {

    private Logger logger= LoggerFactory.getLogger(TokenCommon.class);

    private static String TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    private static Integer TIMEOUT=60;

    //从缓存中根据key获取token
    private static String TENCENT_ACCESS_TOKEN="tencent_access_token";

    public static String GRANT_TYPE="client_credential";

    //todo
    //这里的id和appsecret写死，测试号的信息，实际是要从数据库获取，
    private static String appID="wx475d2ef1d504a985";

    private static String appsecret="54b88ebe1e920f1d4bb105700098533e";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取token
     * @return
     */
    public String getAccessToken(){
        String accessToken="";

        AccessTokenRequestParam requestParam=new AccessTokenRequestParam();
        requestParam.setAppId(appID);
        requestParam.setSecret(appsecret);
        requestParam.setGrantType(GRANT_TYPE);


        try {
            String jsonResult= HttpClient.doHttpGet(String.format(TOKEN_URL,requestParam.getGrantType(),requestParam.getAppId(),requestParam.getSecret()));
            logger.debug("获取的token信息是：",jsonResult);

            AccessTokenResponseParam responseParam= JacksonUtils.json2pojo(jsonResult,AccessTokenResponseParam.class);
            accessToken=responseParam.getAccessToken();
            setToRedisAccessToken(TENCENT_ACCESS_TOKEN);

        } catch (IOException e) {
            logger.error("获取token失败,错误信息：",e);
        } catch (Exception e) {
            logger.error("获取token时json转换pojo失败，错误信息：",e);
        }

        return accessToken;
    }

    /**
     * 把数据放进缓存
     * @param tokenStr
     */
    public void setToRedisAccessToken(String tokenStr){
        stringRedisTemplate.opsForValue().set(TENCENT_ACCESS_TOKEN,tokenStr,TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 根据key从缓存中取出数据
     *
     */
    public String getFromRedisAccessToken(){

        return stringRedisTemplate.opsForValue().get(TENCENT_ACCESS_TOKEN);
    }

}
