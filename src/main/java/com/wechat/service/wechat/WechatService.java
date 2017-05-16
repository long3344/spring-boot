package com.wechat.service.wechat;

import com.wechat.model.Admin;

import java.util.List;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/5/12
 */
public interface WechatService {
    public int addAdmin(Admin admin);

    public Admin findAdminByAdmin(String username,String password);

    public Admin findAdmin(Admin admin);
}
