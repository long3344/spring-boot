package com.wechat.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wechat.dto.ReturnDto;
import com.wechat.model.Admin;
import com.wechat.service.wechat.WechatService;
import com.wechat.util.ParameterUtil;
import com.wechat.util.SessionUtil;
import org.apache.catalina.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


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


    @RequestMapping("/toRegister")
    public String toRegister(){
        return "login/register";
    }


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

    /**
     * 登录
     * @param response
     * @param username
     * @param password
     * @param remember
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Map<String,Object> login(HttpServletResponse response, @RequestParam("username")String username, @RequestParam("password") String password,
                                     @RequestParam(value = "remember",required = false)String remember) {
        logger.info("用户开始登录！");
        try {
            //用户登录，不管是否点击记住我，都要存到session中去
            Map<String,Object> resultMap = new HashMap<>();
            Admin admin  = wechatService.findAdminByAdmin(username,password);
            SessionUtil.getSession().setAttribute("admin",admin);

            //用户点击了记住我，将值存在Cookie中返回
            if(null!=remember&&remember.equals("记住我")){
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
                resultMap.put("errorMsg","登录失败，用户名或密码不正确！");
                return resultMap;
            }
        } catch (Exception e) {
            logger.error("登录失败，错误信息：",e);
        }
        return null;
    }

    /**
     * 注册登录
     * @param request
     * @return
     */
    @RequestMapping("/regist")
    @ResponseBody
    public ReturnDto regist(HttpServletRequest request){
        Map<String,Object> param= ParameterUtil.getParameterMap(request);
        logger.info("开始注册,注册信息===>{}",param);
        String code = (String) request.getSession().getAttribute("code");
        if (StringUtils.isEmpty(param.get("username"))){
            return ReturnDto.buildFailedReturnDto("用户名不能为空！");
        }
        if (StringUtils.isEmpty(param.get("password"))){
            return ReturnDto.buildFailedReturnDto("密码不能为空！");
        }
        if (code==null){
            return ReturnDto.buildFailedReturnDto("验证码超时！");
        }
        if (!code.equalsIgnoreCase(param.get("vcode")+"")){
            return ReturnDto.buildFailedReturnDto("验证码输入有误！");
        }

        param.put("createTime",new Date());
        param.put("updateTime",new Date());
        try {
            int result =wechatService.registMember(param);
            if (result==1){
                request.getSession().setAttribute("username",param.get("username"));
                request.getSession().setAttribute("password",param.get("password"));
            }
            return ReturnDto.buildSuccessReturnDto(result);
        }catch (Exception e){
            logger.error("注册失败，错误信息===>",e);
            return ReturnDto.buildFailedReturnDto("注册失败！");
        }
    }

    //登录成功跳转页面（服务内部调用）
    @RequestMapping("/index")
    public ModelAndView index1() {
        Admin admin = (Admin) SessionUtil.getSession().getAttribute("admin");
        ModelAndView mv = new ModelAndView();
        mv.addObject("admin",admin);
        mv.setViewName("login/index");
        return mv;
    }

    @RequestMapping("/toDemo")
    public String toDemo(){
        return "demo/thymeleaf_demo";
    }

    @RequestMapping("/findAdmin")
    @ResponseBody
    public Admin findAdmin(){
        Admin admin =new Admin();
        admin.setId(1);
//        PageHelper.startPage(1,10);
        PageHelper.startPage(admin.getPageNum(),admin.getPageSize());
         Admin admi=wechatService.findAdmin(admin);

//        PageInfo<?> pageInfo = new PageInfo<>(new ArrayList<Admin>());
        return admi;
//        return new ModelAndView("login/login");
    }
}
