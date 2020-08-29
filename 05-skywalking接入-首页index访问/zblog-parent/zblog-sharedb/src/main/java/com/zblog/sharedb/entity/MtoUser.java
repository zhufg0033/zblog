package com.zblog.sharedb.entity;

import java.io.Serializable;
import java.util.Date;

public class MtoUser implements Serializable {
    private Long id;

    private String username;

    private String name;

    private String avatar;

    private String email;

    private String password;

    private Integer status;

    private Date created;

    private Date updated;

    private Date lastLogin;

    private Integer gender;

    private Integer roleId;

    private Integer comments;

    private Integer posts;

    private String signature;

    private String signDb;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getSignDb() {
        return signDb;
    }

    public void setSignDb(String signDb) {
        this.signDb = signDb == null ? null : signDb.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", name=").append(name);
        sb.append(", avatar=").append(avatar);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", status=").append(status);
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append(", lastLogin=").append(lastLogin);
        sb.append(", gender=").append(gender);
        sb.append(", roleId=").append(roleId);
        sb.append(", comments=").append(comments);
        sb.append(", posts=").append(posts);
        sb.append(", signature=").append(signature);
        sb.append(", signDb=").append(signDb);
        sb.append("]");
        return sb.toString();
    }
}