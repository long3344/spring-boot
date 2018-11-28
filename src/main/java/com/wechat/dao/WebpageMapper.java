package com.wechat.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WebpageMapper {
    List<Map<String, Object>> getDate();

    List<Map> selectWebPageList();
}
