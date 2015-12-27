package com.szhua.pojo;

import java.util.Date;

public class TextContextPOJO {
	private long recId;
	private String fromUrl;// 从这里抓到的
	private String title; // 简介等
	private String titleUrl;// title如果有url
	private String context; // 内容信息等文本
	private String imgLocUrl;	// 内容相关等图片中ftp中的url
	private String imgUrl;// 图片的网页路径
	private String tags;
	private int visitTimes;	//阅览次数
	private int status;
	private int categories;	//见contextType表
	private Date gettedDt;
	private Date publishDt;



	public int getVisitTimes() {
		return visitTimes;
	}

	public void setVisitTimes(int visitTimes) {
		this.visitTimes = visitTimes;
	}

	public Date getPublishDt() {
		return publishDt;
	}

	public void setPublishDt(Date publishDt) {
		this.publishDt = publishDt;
	}

	public long getRecId() {
		return recId;
	}

	public void setRecId(long recId) {
		this.recId = recId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return categories;
	}

	public void setType(int type) {
		this.categories = type;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getImgLocUrl() {
		return imgLocUrl;
	}

	public void setImgLocUrl(String imgLocUrl) {
		this.imgLocUrl = imgLocUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Date getGettedDt() {
		return gettedDt;
	}

	public void setGettedDt(Date gettedDt) {
		this.gettedDt = gettedDt;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
