/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.post.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zblog.sharedb.entity.MtoChannel;
import com.zblog.sharedb.entity.MtoPost;
import com.zblog.sharedb.entity.MtoPostAttribute;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.Consts;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author langhsu
 * 
 */
public class PostVO extends MtoPost implements Serializable {
	private static final long serialVersionUID = -1144627551517707139L;

	private String editor;
	private String content;

	private UserVO author;
	private MtoChannel channel;
	
	@JSONField(serialize = false)
	private MtoPostAttribute attribute;
	
	public String[] getTagsArray() {
		if (StringUtils.isNotBlank(super.getTags())) {
			return super.getTags().split(Consts.SEPARATOR);
		}
		return null;
	}

	public UserVO getAuthor() {
		return author;
	}

	public void setAuthor(UserVO author) {
		this.author = author;
	}

	public MtoPostAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(MtoPostAttribute attribute) {
		this.attribute = attribute;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MtoChannel getChannel() {
		return channel;
	}

	public void setChannel(MtoChannel channel) {
		this.channel = channel;
	}
}
