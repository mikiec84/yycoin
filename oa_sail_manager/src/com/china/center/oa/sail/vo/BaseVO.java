package com.china.center.oa.sail.vo;

import com.china.center.jdbc.annotation.Entity;
import com.china.center.jdbc.annotation.Ignore;
import com.china.center.jdbc.annotation.Relationship;
import com.china.center.oa.sail.bean.BaseBean;

@Entity(inherit = true)
public class BaseVO extends BaseBean
{
	@Relationship(relationField = "locationId")
	private String depotName = "";

	/**
	 * #190 batch update product name
	 */
	@Ignore
	private String destProductName = "";

	@Ignore
	private String destProductId = "";

	public String getDepotName()
	{
		return depotName;
	}

	public void setDepotName(String depotName)
	{
		this.depotName = depotName;
	}

	public String getDestProductName() {
		return destProductName;
	}

	public void setDestProductName(String destProductName) {
		this.destProductName = destProductName;
	}

	public String getDestProductId() {
		return destProductId;
	}

	public void setDestProductId(String destProductId) {
		this.destProductId = destProductId;
	}
}
