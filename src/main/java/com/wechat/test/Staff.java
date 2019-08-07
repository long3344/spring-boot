package com.wechat.test;

public class Staff extends Person{

    /**
     * 企业名称
     */
    private String companyName = "周口华软科技有限公司";

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门
     */
    private String department = "技术部";

    /**
     * 职务
     */
    private JobEnum job;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public JobEnum getJob() {
        return job;
    }

    public void setJob(JobEnum job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return super.toString()+",{" +
                "企业名称='" + companyName + '\'' +
                ", 手机号='" + phoneNumber + '\'' +
                ", 邮箱='" + email + '\'' +
                ", 部门='" + department + '\'' +
                ", 职务=" + job +
                '}';
    }
}
