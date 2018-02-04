package com.wechat.java8text;

import org.junit.Test;

/**
 * 描述：
 * 作者: TWL
 * 创建日期: 2018/1/20
 */
public enum TestEnum {

    AA("100166",100,"123"),
    BB("100030",200,"1254"),
    CC("100043",300,"741");

    private  String key;
    private  Integer value;
    private  String desc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    TestEnum(String key, Integer value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public static Integer value(String key){

        for (TestEnum t:TestEnum.values()){
            if (t.getKey().equals(key)){
                return t.getValue();
            }
        }
        return null;
    }

}
