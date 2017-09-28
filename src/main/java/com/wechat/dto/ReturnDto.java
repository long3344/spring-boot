package com.wechat.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 描述     :数据返回，格式化返回参数。
 */
public class ReturnDto {

    private String code;
    private String message;
    private Object value;
    private String status;
    private String errorMessage;

    public ReturnDto() {

    }

    public ReturnDto(String code, String message, Object value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    private ReturnDto(String code, String message, Object value, String status) {
        this.code = code;
        this.message = message;
        this.value = value;
        this.status = status;
    }


    private ReturnDto(String status) {
        this.status = status;
    }

    private ReturnDto(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static ReturnDto buildStatusOK() {
        return new ReturnDto("OK");
    }

    public static ReturnDto buildStatusERRO(String errorMessage) {
        return new ReturnDto("ERROR", errorMessage);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static ReturnDto buildSuccessReturnDto() {
        return new ReturnDto("000", "Success", null, "OK");
    }

    public static ReturnDto buildSuccessReturnDto(Object value) {

        return new ReturnDto("000", "Success", value, "OK");
    }

    public static ReturnDto buildFailedReturnDto(String failMessage) {
        return new ReturnDto("101", failMessage, null, "ERROR");
    }

    public static ReturnDto buildSystemErrorReturnDto() {
        return new ReturnDto("599", "System Error", null);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ReturnDto buildListOnEmptyFail(List s) {
        return buildListOnEmptyFail(s, "暂无数据");
    }

    /**
     * 返回一个列表 如果是空返回失败的响应
     * @param s
     * @return
     */
    public static ReturnDto buildListOnEmptyFail(List s, String message) {
        if (CollectionUtils.isEmpty(s)) {
            return buildFailedReturnDto(message);
        }

        return buildSuccessReturnDto(s);
    }

    @Override
    public String toString() {
        return "ReturnDto{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", value=" + value +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
