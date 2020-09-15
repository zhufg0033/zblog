package com.zblog.web.lang;


import java.util.List;

/**
 * @author : landy
 */

public class Theme {
    /**
     * 所在路径
     */
    private String path;

    /**
     * 名称 (同目录名)
     */
    private String name;

    /**
     * 介绍
     */
    private String slogan;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者网站
     */
    private String website;

    /**
     * 预览图
     */
    private List<String> previews;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getPreviews() {
        return previews;
    }

    public void setPreviews(List<String> previews) {
        this.previews = previews;
    }
}
