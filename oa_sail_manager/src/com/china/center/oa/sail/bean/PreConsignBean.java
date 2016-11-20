package com.china.center.oa.sail.bean;

import java.io.Serializable;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;
import com.china.center.tools.TimeTools;

/**
 * 用完删除
 * @author smart
 *
 */
@SuppressWarnings("serial")
@Entity(name = "生成发货单做准备")
@Table(name = "T_CENTER_PRECONSIGN")
public class PreConsignBean implements Serializable
{
	@Id(autoIncrement = true)
	private String id = "";
	
	private String outId = "";

	private String logTime = TimeTools.now();

	public PreConsignBean()
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

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
}
