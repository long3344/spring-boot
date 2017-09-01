package com.wechat.service.wechat.impl;

import com.wechat.dao.AdminMapper;
import com.wechat.model.Admin;
import com.wechat.service.wechat.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/12
 */

@Service
public class WechatServiceImpl implements WechatService {

    private Logger logger= LoggerFactory.getLogger(WechatServiceImpl.class);

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

    @Cacheable(value = "admin",key = "'admin_'.concat(#admin.id.toString())")
    public Admin findAdmin(Admin admin) {
        logger.info("开始缓存数据！");
        return adminMapper.selectByPrimaryKey(admin.getId());
    }

    @Override
    public List<Admin> findAllAdmin(Admin admin) {
        return adminMapper.selectAllAdmin();
    }
}
