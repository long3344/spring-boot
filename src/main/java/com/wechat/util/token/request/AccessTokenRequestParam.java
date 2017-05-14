package com.wechat.util.token.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 描述：请求toke的参数类
 * 作者: TWL
 * 创建日期: 2017/5/14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessTokenRequestParam {

    /**获取access_token填写client_credential*/
    @JsonProperty("grant_type")
    private String grantType;

    /**第三方用户唯一凭证*/
    @JsonProperty("appid")
    private String appId;

    /**第三方用户唯一凭证密钥，即appsecret*/
    @JsonProperty("secret")
    private String secret;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
