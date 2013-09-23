/*
 * 文件名： BallBean.java	 <br>
 * 创建日期: 2013年9月9日<br>
 * 包名: com.loong.core.bean <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 * 
 */
package com.loong.core.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 功能描述: Ball Bean <br>
 * 作者: <a href="mailto:wdps.cn@gmail.com">loong</a> <br>
 * 版本: 1.0.0<br>
 */

public class BallBean {
	private int			entity_id;
	private int			red1;
	private int			red2;
	private int			red3;
	private int			red4;
	private int			red5;
	private int			red6;
	private int			blue;
	private Date		created_at;
	private Timestamp	updated_at;
	private String		value;

	public int getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}

	public int getRed1() {
		return red1;
	}

	public void setRed1(int red1) {
		this.red1 = red1;
	}

	public int getRed2() {
		return red2;
	}

	public void setRed2(int red2) {
		this.red2 = red2;
	}

	public int getRed3() {
		return red3;
	}

	public void setRed3(int red3) {
		this.red3 = red3;
	}

	public int getRed4() {
		return red4;
	}

	public void setRed4(int red4) {
		this.red4 = red4;
	}

	public int getRed5() {
		return red5;
	}

	public void setRed5(int red5) {
		this.red5 = red5;
	}

	public int getRed6() {
		return red6;
	}

	public void setRed6(int red6) {
		this.red6 = red6;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Timestamp updated_at) {
		this.updated_at = updated_at;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
