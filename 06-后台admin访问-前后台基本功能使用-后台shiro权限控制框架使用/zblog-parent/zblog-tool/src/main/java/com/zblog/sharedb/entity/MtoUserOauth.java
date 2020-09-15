package com.zblog.sharedb.entity;

import java.io.Serializable;

public class MtoUserOauth implements Serializable {
    private Long id;

    private Long userId;

    private String accessToken;

    private String expireIn;

    private String oauthCode;

    private Integer oauthType;

    private String oauthUserId;

    private String refreshToken;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn == null ? null : expireIn.trim();
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode == null ? null : oauthCode.trim();
    }

    public Integer getOauthType() {
        return oauthType;
    }

    public void setOauthType(Integer oauthType) {
        this.oauthType = oauthType;
    }

    public String getOauthUserId() {
        return oauthUserId;
    }

    public void setOauthUserId(String oauthUserId) {
        this.oauthUserId = oauthUserId == null ? null : oauthUserId.trim();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", accessToken=").append(accessToken);
        sb.append(", expireIn=").append(expireIn);
        sb.append(", oauthCode=").append(oauthCode);
        sb.append(", oauthType=").append(oauthType);
        sb.append(", oauthUserId=").append(oauthUserId);
        sb.append(", refreshToken=").append(refreshToken);
        sb.append("]");
        return sb.toString();
    }
}