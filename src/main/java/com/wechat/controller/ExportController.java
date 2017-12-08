package com.wechat.controller;

import com.wechat.dto.ExportParams;
import com.wechat.service.wechat.impl.ExportServiceImpl;
import com.wechat.util.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/11/29
 */
@Controller
@RequestMapping("/export")
public class ExportController {

    private  static final Logger logger= LoggerFactory.getLogger(ExportController.class);

    @Autowired
    private ExportServiceImpl exportService;

    @RequestMapping("/exportReport")
    @ResponseBody
    public void exportReport(@RequestBody ExportParams params, HttpServletResponse response) throws IOException {
        File file = null;
        RandomAccessFile raf = null;
        OutputStream responseOS = response.getOutputStream();
        try {
            responseOS = response.getOutputStream();
            file = exportService.getReport(params);
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/msexcel");
            response.setHeader("content-disposition",
                    "attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
            raf = new RandomAccessFile(file, "rw");
            byte[] buffer = new byte[1024 * 1024];
            int avariable = -1;
            while ((avariable = raf.read(buffer)) > 0) {
                responseOS.write(buffer, 0, avariable);
            }
            buffer = null;
            responseOS.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }  finally {
            if(raf != null)
                raf.close();
            if(file != null)
                file.delete();
            if(responseOS != null)
                responseOS.close();
        }
    }

    /**
     * 导出全部会员
     * @param param
     * @param response
     */
    @RequestMapping("/exportMember")
    public void exportMember(@RequestParam Map<String,Object> param,HttpServletResponse response){
        ExportParams params=new ExportParams();

        String[] header=new String[]{"姓名","手机号","QQ"};
        String[] cols=new String[]{"username","phone","qq"};

        try {
            params.setBeanName("adminMapper");
            params.setMethodName("selectAllAdmin");
            params.setHeader(header);
            params.setCols(cols);
            params.getParams().putAll(param);

            File file=exportService.getReportFile(params);
            HttpClient.exportReportFile(file,response,"全部会员.xls");

        } catch (Exception e) {
            logger.error("导出文件错误：",e);
        }

    }
}
