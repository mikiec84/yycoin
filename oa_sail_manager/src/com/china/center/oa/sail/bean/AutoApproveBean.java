package com.china.center.oa.sail.bean;

import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.annotation.*;
import com.china.center.jdbc.annotation.enums.JoinType;

import java.io.Serializable;

/**
 * 
 * #150
 * 待商务审批状态的SO单导入后自动库管通过
 *
 * @author  2015-12-26
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "t_center_auto_approve")
public class AutoApproveBean implements Serializable
{
    @Id(autoIncrement = true)
	private int id = 0;

	private String fullId = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
}
