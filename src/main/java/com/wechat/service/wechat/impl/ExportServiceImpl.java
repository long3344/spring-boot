package com.wechat.service.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wechat.dto.ExportParams;
import com.wechat.service.wechat.ExportService;
import com.wechat.util.excel.ExcelOperator;
import com.wechat.util.http.HttpClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/11/29
 */

@Service
public class ExportServiceImpl implements ExportService,ApplicationContextAware {

    private ApplicationContext  context;

    //@Value("${report.temp_dir}")
    private String temp_dir="/usr/local";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }

    private static Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    @Override
    public File getReportFile(ExportParams exportParams) throws Exception {
        if (StringUtils.isEmpty(exportParams.getBeanName())) {
            logger.error("导出文件缺少参数beanName");
            throw new Exception("导出文件缺少参数beanName");
        }
        if (StringUtils.isEmpty(exportParams.getHeader())){
            logger.error("导出文件缺少参数Header");
            throw new Exception("导出文件缺少参数Header");
        }
        if (StringUtils.isEmpty(exportParams.getMethodName())){
            logger.error("导出文件缺少参数methodName");
            throw new Exception("导出文件缺少参数methodName");
        }
        String json= JSONObject.toJSONString(exportParams);
        String fileName=temp_dir+"/report_"+System.currentTimeMillis()+".xls";
        HttpClient.download("http://127.0.0.1:8080/export/exportReport",json,fileName);
        return new File(fileName);
    }

    @Override
    public Map<String, Object> getCouponTableList(Map<String, Object> params) {
        return null;
    }

    @Override
    public File exportCouponDetailList(Map<String, Object> params) throws Exception {
        return null;
    }

    /**
     * 下载文件
     * @param params
     * @return
     * @throws IOException
     */
    @Override
    public File getReport(ExportParams params) throws Exception {
        String beanName=params.getBeanName();
        String methodName=params.getMethodName();
        List<String> header=params.getHeader();
/*        if (CollectionUtils.isEmpty(params.getCols())){
            params.setCols(header);
        }*/

        //查询条件的时间加上时、分、秒
       /* String orderTimeStart = (String) params.getParams().get("orderTimeStart");
        String orderTimeEnd = (String) params.getParams().get("orderTimeEnd");
        if(!StringUtil.isEmpty(orderTimeStart)){
            orderTimeStart =  orderTimeStart + " 00:00:00";
            params.getParams().put("orderTimeStart",orderTimeStart);
        }
        if(!StringUtil.isEmpty(orderTimeStart)){
            orderTimeEnd =  orderTimeEnd + " 23:59:59";
            params.getParams().put("orderTimeEnd",orderTimeEnd);
        }

        //参数中将tradesStaus转换为list,并添加到参数中
        String tradesStatus = (String) params.getParams().get("tradesStatus");
        if ( tradesStatus != null) {
            List list = Arrays.asList(tradesStatus.split(","));
            params.getParams().put("list",list);
        }*/

        Object target=context.getBean(beanName);

        if (target==null){
            logger.error("根据Bean名称"+beanName+"未找到相关的Bean实例，任务调度失败");
            throw new Exception("根据Bean名称"+beanName+"未找到相关的Bean实例，任务调度失败");
        }
        Method method=getTargetMethod(target,methodName);
        if (method==null){
            logger.error("未找到匹配的方法,class["+target.getClass().getName()
                    +"],method[{"+methodName+"}],是否含有参数[java.util.Map]，请检查配置.");
            throw new Exception("未找到匹配的方法,class["+target.getClass().getName()
                    +"],method[{"+methodName+"}],是否含有参数[java.util.Map]，请检查配置.");
        }
        File file=new File(temp_dir+"/report_"+System.currentTimeMillis()+".xls");
        file.getParentFile().mkdir();

        int pageNum=0,pageSize=2000,pages=0;
        Page page=null;
        HSSFWorkbook hssfWorkbook= ExcelOperator.createWorkbook("data",header);
        do {
            boolean isCount=false;
            if (page==null){
                isCount=true;
                page= PageHelper.startPage(++pageNum,pageSize,isCount);

            }else {
                //避免每次都发起count查询
                PageHelper.startPage(++pageNum,pageSize,isCount);
            }
            List<Map<String,Object>> resultList=null;
            try {
                resultList= (List<Map<String, Object>>) ReflectionUtils.invokeMethod(method,target,params.getParams());
                logger.info("导出查询结果===>{}",resultList);
                if (isCount){
                    pages=page.getPages();
                }
            } catch (Exception e) {
                logger.error("匹配的方法返回参数错误,class["+target.getClass().getName()
                        +"],method[{"+methodName+"}],参数是否是[List<Map<String, Object>>]，请检查配置.",e);
                if (e instanceof ClassCastException){
                    throw new Exception("匹配的方法返回参数错误,class["+target.getClass().getName()
                            +"],method[{"+methodName+"}],参数是否是[List<Map<String, Object>>]，请检查配置.",e);
                }
            }
            if (resultList==null)
                resultList=new ArrayList<>();
            ExcelOperator.writeData(hssfWorkbook,params.getCols(),resultList,ExcelOperator.createContentAreaStyle(hssfWorkbook));
            FileOutputStream fos=new FileOutputStream(file);
            hssfWorkbook.write(fos);
            fos.close();

        }while (pages>pageNum);

        return file;
    }

    /**
     * 获取目标方法
     * @param target
     * @param methodName
     * @return
     */
    private Method getTargetMethod(Object target, String methodName) throws Exception {

        try {
            Method[] methods=target.getClass().getDeclaredMethods();
            for (Method method:methods) {
                //拿到入参是map类型的方法
                if (method.getName().equals(methodName)&&method.getParameterCount()==1
                        &&method.getParameterTypes()[0].getName().equals(Map.class.getName())){
                    return method;
                }

            }
            return null;
        } catch (SecurityException e) {
            logger.error("查找目标方法异常,class["+target.getClass().getName()
                    +"],method[{"+methodName+"}],是否含有参数java.util.Map，请检查配置.",e);
            throw new Exception("查找目标方法异常,class["+target.getClass().getName()
                    +"],method[{"+methodName+"}],是否含有参数java.util.Map，请检查配置.",e);
        }
    }

}


