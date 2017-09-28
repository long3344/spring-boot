package com.wechat.controller.member;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wechat.model.Admin;
import com.wechat.service.redis.impl.RedisServiceImpl;
import com.wechat.service.wechat.WechatService;
import com.wechat.util.ParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/9/12
 */

@Controller
@RequestMapping("/member")
public class MemberController {

    public static final Logger logger= LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private WechatService wechatService;


    @RequestMapping("/memberList")
    public String toMemberList(){
        return "member/memberList";
    }

    @RequestMapping(value = "/getMemberList",method = RequestMethod.POST)
    @ResponseBody
    public PageInfo<?> findMemberList(HttpServletRequest request){
        Map<String,Object> param= ParameterUtil.getParameterMap(request);
        logger.info("查询会员信息，参数===>{}",param);
        PageHelper.startPage(Integer.parseInt(param.get("pageNum")+""),Integer.parseInt(param.get("pageSize")+""));
        List<Map<String,Object>> userList=wechatService.findAllAdmin(param);
        return new PageInfo<>(userList);
    }
}
