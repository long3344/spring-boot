package com.wechat.service.wechat.impl;

import com.wechat.dao.AdminMapper;
import com.wechat.model.Admin;
import com.wechat.service.wechat.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/12
 */

@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public int addAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }

    @Override
    public Admin findAdminByAdmin(String username, String password) {
        return adminMapper.findAdminByAdmin(username,password);
    }
}
