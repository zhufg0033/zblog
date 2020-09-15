package com.zblog.sharedb.entity;

import java.io.Serializable;
import java.util.Date;

public class MtoTag implements Serializable {
    private Long id;

    private Date created;

    private String description;

    private Long latestPostId;

    private String name;

    private Integer posts;

    private String thumbnail;

    private Date updated;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getLatestPostId() {
        return latestPostId;
    }

    public void setLatestPostId(Long latestPostId) {
        this.latestPostId = latestPostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", created=").append(created);
        sb.append(", description=").append(description);
        sb.append(", latestPostId=").append(latestPostId);
        sb.append(", name=").append(name);
        sb.append(", posts=").append(posts);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append(", updated=").append(updated);
        sb.append("]");
        return sb.toString();
    }
}