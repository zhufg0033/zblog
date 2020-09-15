package com.zblog.sharedb.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MtoPost implements Serializable {
    private Long id = 0l;

    private Long authorId = 0l;

    private Integer channelId = 0;

    private Integer comments = 0;

    private Date created;

    private String createdStr;

    private Integer favors = 0;

    private Integer featured = 0;

    private Integer status;

    private String summary;

    private String tags;

    private String thumbnail;

    private String title;

    private Integer views= 0;

    private Integer weight = 0;

    private static final long serialVersionUID = 1L;

    private static SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Date getCreated() {
        if(created == null && createdStr!=null){
            try {
                return sim.parse(createdStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getFavors() {
        return favors;
    }

    public void setFavors(Integer favors) {
        this.favors = favors;
    }

    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCreatedStr() {
        if(createdStr == null && created!=null){
            return sim.format(created);
        }
        return createdStr;
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", authorId=").append(authorId);
        sb.append(", channelId=").append(channelId);
        sb.append(", comments=").append(comments);
        sb.append(", created=").append(created);
        sb.append(", favors=").append(favors);
        sb.append(", featured=").append(featured);
        sb.append(", status=").append(status);
        sb.append(", summary=").append(summary);
        sb.append(", tags=").append(tags);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append(", title=").append(title);
        sb.append(", views=").append(views);
        sb.append(", weight=").append(weight);
        sb.append("]");
        return sb.toString();
    }
}