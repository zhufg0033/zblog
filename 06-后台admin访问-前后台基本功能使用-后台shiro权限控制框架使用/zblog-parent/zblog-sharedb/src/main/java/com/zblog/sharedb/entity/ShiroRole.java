package com.zblog.sharedb.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

public class ShiroRole implements Serializable {


    public static int STATUS_NORMAL = 0;
    public static int STATUS_CLOSED = 1;
    public static String ROLE_ADMIN = "admin";

    public static long ADMIN_ID = 1;

    private List<ShiroPermission> permissions;

    private Long id;

    private String description;

    private String name;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ShiroPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<ShiroPermission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", description=").append(description);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}