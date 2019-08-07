package com.wechat.test;

public class Person {

    private String name ;

    private String brithday;

    private String houseAddress;

    private String id;

    private Integer age;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public Integer getAge() {
        return age ;
    }

    public void setAge(Integer age) {
        this.age  = age;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "姓名='" + name + '\'' +
                ", 生日='" + brithday + '\'' +
                ", 年龄=" + age +
                ", 家庭地址='" + houseAddress + '\'' +
                ", 身份证号='" + id + '\'' +
                '}';
    }
}
