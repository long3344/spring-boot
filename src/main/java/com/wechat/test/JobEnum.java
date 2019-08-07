package com.wechat.test;

public enum JobEnum {

    A("intern","实习生"),

    B("dev_lead","开发主管"),

    C("manager","项目经理");

    private String code;

    private String name;

    JobEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
