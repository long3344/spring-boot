package com.wechat.dao;

import com.wechat.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    Admin findAdminByAdmin(@Param("username")String username, @Param("password")String password);

    List<Map<String,Object>> selectAllAdmin(Map param);

    int insertAdmin(Map<String, Object> param);
}