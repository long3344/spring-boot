package com.wechat.service.wechat;

import com.wechat.dto.ExportParams;

import java.io.File;
import java.util.Map;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/11/29
 */
public interface ExportService {


    public File getReportFile(ExportParams exportParams) throws Exception;

    public Map<String,Object> getCouponTableList(Map<String, Object> params);

    public File exportCouponDetailList(Map<String, Object> params)throws Exception;

    public File getReport(ExportParams params) throws Exception;
}
