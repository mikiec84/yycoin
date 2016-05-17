package com.china.center.oa.customer.bean;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Id;
import com.china.center.jdbc.annotation.Table;
import com.china.center.jdbc.annotation.Unique;

import java.io.Serializable;

/** 2016/5/17
 * #236 为避免sail和customer包相互依赖，只能复制一份代码
 */
@Entity
@Table(name = "T_CENTER_EXPRESS")
public class ExpressBean implements Serializable
{
    public static final int EXPRESS_TYPE = 0;
    public static final int FREIGHT_TYPE = 1;

	@Id
	private String id = "";
	
	@Unique
	private String name = "";
	
	private int type = 0;

	public ExpressBean()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	
	
	
}
