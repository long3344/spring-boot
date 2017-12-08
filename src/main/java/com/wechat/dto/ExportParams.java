package com.wechat.dto;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2017/11/29
 */
public class ExportParams {

    private String beanName;

    private String methodName;

    private List<String> header=new ArrayList<String>();

    private List<String> cols=new ArrayList<String>();

    private Map<String,Object> params= new HashMap<String,Object>();

    public ExportParams(){

    }

    public ExportParams(String beanName, String methodName, String[] header, Map<String, Object> params) {
        this.beanName = beanName;
        this.methodName = methodName;
        if (null!=header){
            this.header = Arrays.asList(header);
        }
        this.params = params;
    }
    public ExportParams(String beanName, String methodName, List<String> header, Map<String, Object> params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.header = header;
        this.params = params;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(String ... _header) {
        if (null==header){
            return;
        }
        header.clear();
        for (String h:_header){
            header.add(h);
        }
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(String ... _cols) {
        if(_cols == null || _cols.length == 0){
            return;
        }
        cols.clear();
        for(String c : _cols){
            cols.add(c);
        }
    }
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
