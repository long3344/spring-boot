package com.wechat.controller;

import com.wechat.util.ValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：获取公共信息，如：验证码，二维码等
 * 作者: TWL
 * 创建日期: 2017/9/22
 */
@RequestMapping("/common")
@Controller
public class CommonController {

    public  static final Logger logger= LoggerFactory.getLogger(CommonController.class);

    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response){
        try {
            ValidateCode.getCode(request,response);
        } catch (IOException e) {
            logger.error("获取验证码错误，错误信息===>{}",e);
        }
    }
}
