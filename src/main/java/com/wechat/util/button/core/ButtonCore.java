package com.wechat.util.button.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 描述：创建微信菜单
 * 作者: TWL
 * 创建日期: 2017/5/14
 */
@Component
public class ButtonCore {

    private Logger logger= LoggerFactory.getLogger(ButtonCore.class);

    private  static String CUSTOM_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";


    /**
     * 创建自定义菜单
     * @param json
     * @return
     */
    public String customMenu(String json){

        return null;
    }

}
