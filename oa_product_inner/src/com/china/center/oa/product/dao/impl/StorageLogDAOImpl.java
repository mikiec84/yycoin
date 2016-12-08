/**
 * File Name: StorageLogDAOImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-8-25<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.StorageLogBean;
import com.china.center.oa.product.dao.StorageLogDAO;
import com.china.center.oa.product.vo.StorageLogVO;

/**
 * StorageLogDAOImpl
 * 
 * @author ZHUZHU
 * @version 2010-8-25
 * @see StorageLogDAOImpl
 * @since 1.0
 */
public class StorageLogDAOImpl extends BaseDAO<StorageLogBean, StorageLogVO> implements
        StorageLogDAO {
    private final Log _logger = LogFactory.getLog(getClass());
    /**
     * queryStorageLogByCondition
     * 
     * @param condition
     * @return
     */
    public List<StorageLogBean> queryStorageLogByCondition(ConditionParse condition) {
        condition.addWhereStr();

        return jdbcOperation
                .queryForListBySql(
                        "select t1.*,t3.industryId, t3.industryId2 from T_CENTER_STORAGELOG t1, t_center_product t2, t_center_depot t3, t_center_storage t4 "
                                + condition.toString(), StorageLogBean.class);
    }

    public List<String> queryDistinctProductByDepotIdAndLogTime(String depotId, String logTime) {
        final List<String> result = new LinkedList<String>();

        String sql = "select distinct(productid) as productid from t_center_storagelog where locationId = ? and logTime >= ?";

        this.jdbcOperation.query(sql, new Object[] { depotId, logTime }, new RowCallbackHandler() {
            public void processRow(ResultSet rs) throws SQLException {
                result.add(rs.getString("productid"));
            }
        });

        return result;
    }

    @Override
    public void statExceptionalStorageLog() {
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("logTime",">","2016-11-30 10:00:00");
        conditionParse.addCondition(" order by logTime");
        List<StorageLogBean> storageLogBeanList = this.queryEntityBeansByCondition(conditionParse);

        class StorageKey{
            private String productId;
            private String depotId;

            public StorageKey(String productId, String depotId) {
                this.productId = productId;
                this.depotId = depotId;
            }

            @Override
            public String toString() {
                return "StorageKey{" +
                        "productId='" + productId + '\'' +
                        ", depotId='" + depotId + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                StorageKey that = (StorageKey) o;

                if (!productId.equals(that.productId)) return false;
                return depotId.equals(that.depotId);

            }

            @Override
            public int hashCode() {
                int result = productId.hashCode();
                result = 31 * result + depotId.hashCode();
                return result;
            }
        }
        //step1 group by productId and depotId
        Map<StorageKey,List<StorageLogBean>> product2Log = new HashMap<StorageKey,List<StorageLogBean>>();
        for (StorageLogBean log: storageLogBeanList){
            StorageKey key = new StorageKey(log.getProductId(),log.getLocationId());
            List<StorageLogBean> logs = product2Log.get(key);
            if (logs == null){
                logs = new ArrayList<StorageLogBean>();
                product2Log.put(key, logs);
            }
            logs.add(log);
        }

        Map<StorageKey,String> product2ExceptionalLog = new HashMap<StorageKey,String>();
        //step2 check continue
        for (StorageKey key: product2Log.keySet()){
            List<StorageLogBean> logs = product2Log.get(key);
            for (int i=0;i<logs.size()-1;i++){
                StorageLogBean log1 = logs.get(i);
                StorageLogBean log2 = logs.get(i+1);
                if (log2.getPreAmount2() != log1.getAfterAmount2()){
                    product2ExceptionalLog.put(key, log2.getSerializeId());
                    break;
                }
            }
        }

        _logger.info("***statExceptionalStorageLog***"+product2ExceptionalLog);
    }
}
