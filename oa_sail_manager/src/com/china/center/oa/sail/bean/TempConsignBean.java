package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;

import java.io.Serializable;

/**
 * 用完删除
 * @author smart
 *
 */
@SuppressWarnings("serial")
@Entity(name = "生成发货单做准备")
@Table(name = "T_CENTER_TEMPCONSIGN")
public class TempConsignBean implements Serializable
{
	@Id(autoIncrement = true)
	private String id = "";

	private String outId = "";

	private String insId = "";

	public TempConsignBean()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getOutId()
	{
		return outId;
	}

	public void setOutId(String outId)
	{
		this.outId = outId;
	}

	public String getInsId() {
		return insId;
	}

	public void setInsId(String insId) {
		this.insId = insId;
	}
}
