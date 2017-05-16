package com.wechat.util.button.core;

import com.wechat.util.http.HttpClient;
import com.wechat.util.json.JacksonUtils;
import com.wechat.util.response.ResponseParam;
import com.wechat.util.token.core.TokenCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 描述：创建微信菜单
 * 作者: TWL
 * 创建日期: 2017/5/14
 */
@Component
public class ButtonCore {

    private Logger logger= LoggerFactory.getLogger(ButtonCore.class);

    private  static String CUSTOM_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    @Autowired
    private TokenCommon tokenCommon;

    /**
     * 创建自定义菜单
     * @param json
     * @return
     */
    public String customMenu(String json){
        //获取token

        String errorCode="";
        try {
            String token=tokenCommon.getFromRedisAccessToken();
            if (StringUtils.isEmpty(token)){
                token=tokenCommon.getAccessToken();
            }
            //发送请求
            String jsonResult= HttpClient.doHttpPost(String.format(CUSTOM_MENU_URL,token),json);
            ResponseParam responseParam= JacksonUtils.json2pojo(jsonResult,ResponseParam.class);
            errorCode=responseParam.getErrCode();
        }
        catch (IOException e){
            logger.error("请求创建自定义菜单错误：",e);
        } catch (Exception e) {
            logger.error("创建自定义菜单json转换错误：",e);
        }
        //返回结果
        return  errorCode;
    }

}
