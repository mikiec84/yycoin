package com.china.center.oa.sail.dao;

import com.china.center.jdbc.inter.DAO;
import com.china.center.oa.sail.bean.OutImportBean;
import com.china.center.oa.sail.vo.OutImportVO;

public interface OutImportDAO extends DAO<OutImportBean, OutImportVO>
{
	boolean updatePreUse(String id, int preUse);

    /**
     * 2015/9/23 根据fullId更新预占状态
     * @param fullId
     * @param preUse
     * @return
     */
    boolean updatePreUseByFullId(String fullId, int preUse);
}
