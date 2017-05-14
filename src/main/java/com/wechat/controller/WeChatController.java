package com.wechat.controller;

import com.wechat.model.Admin;
import com.wechat.service.wechat.WechatService;
import com.wechat.util.SessionUtil;
import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/10
 */

@Controller
@RequestMapping("wechat")
public class WeChatController {

    private Logger logger= LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    private WechatService wechatService;


    //登录页面访问
    @RequestMapping("/toLogin")
    @ResponseBody
    public ModelAndView index() {
        logger.info("到达登录页面！");
        ModelAndView mv = new ModelAndView();
        try {
            //返回视图之前,先去Cookie中查询是否有值，有则是用户之前点击过记住我
            HttpServletRequest request = SessionUtil.getRequest();
            Cookie[] cookies =request.getCookies();
            if(cookies!=null) {
                Admin admin=new Admin();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        String userName = cookie.getValue();
                        admin.setUsername(userName);
                    }
                    if (cookie.getName().equals("password")) {
                        String passWord = cookie.getValue();
                        admin.setPassword(passWord);
                    }
                }
                //这个判断可以优化一下，不影响主逻辑 主要是判断在Cookie中是够找到了用户信息
                if(admin.getPassword()!=null){
                    mv.addObject("admin",admin);
                }
            }
        }catch (Exception e){
            logger.error("登录页面错误，错误信息：",e);
        }
        //如果Cookie中没有值，直接返回页面
        mv.setViewName("login/login");
        return mv;
    }

    /*测试登录1*/
    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(HttpServletResponse response, @RequestParam("username")String username, @RequestParam("password") String password,
                                    @RequestParam("userId")Integer id, @RequestParam("remember")String remember) {
        logger.info("用户开始登录！");
        try {
            //用户登录，不管是否点击记住我，都要存到session中去
            Map<String,Object> resultMap = new HashMap<>();
            Admin admin  = wechatService.findAdminByAdmin(username,password);
            SessionUtil.getSession().setAttribute("admin",admin);

            //用户点击了记住我，将值存在Cookie中返回
            if(remember.equals("记住我")){
                Cookie cookie = new Cookie("username",admin.getUsername());
                Cookie cookie1 = new Cookie("password",admin.getPassword());
                response.addCookie(cookie);
                response.addCookie(cookie1);
            }else {//用户没有点击记住我，将Cookie中的值清空返回
                Cookie cookie = new Cookie("username",null);
                Cookie cookie1 = new Cookie("password",null);
                response.addCookie(cookie);
                response.addCookie(cookie1);
            }
            //判断用户是否存在，如果不存在就返回未找到该用户
            if(admin!=null){
                resultMap.put("status","ok");
                resultMap.put("admin",admin);
                return resultMap;
            }else {
                resultMap.put("status","error");
                return resultMap;
            }
        } catch (Exception e) {
            logger.error("登录失败，错误信息：",e);
        }
        return null;
    }

    //登录成功跳转页面（服务内部调用）
    @RequestMapping("/loginSuccess")
    public ModelAndView index1() {
        Admin admin = (Admin) SessionUtil.getSession().getAttribute("admin");
        ModelAndView mv = new ModelAndView();
        mv.addObject("admin",admin);
        mv.setViewName("login/loginSuccess");
        return mv;
    }

    @RequestMapping("/toDemo")
    public String toDemo(){
        return "demo/thymeleaf_demo";
    }

    @RequestMapping("/addAdmin")
    public ModelAndView addAdmin(){

        Admin admin=new Admin();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setPhone("12345678910");
        admin.setQq("10723362");
        wechatService.addAdmin(admin);
        return new ModelAndView("login/login");

    }
}
