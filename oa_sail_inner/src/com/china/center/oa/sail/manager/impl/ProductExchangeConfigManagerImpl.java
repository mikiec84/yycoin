/**
 * File Name: SailConfigManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-12-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;
import com.china.center.oa.sail.dao.ProductExchangeConfigDAO;
import com.china.center.oa.sail.manager.ProductExchangeConfigManager;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;
import com.china.center.tools.JudgeTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.iaop.annotation.IntegrationAOP;
import org.springframework.transaction.annotation.Transactional;


/**
 * ProductExchangeConfigManagerImpl
 * 
 * @author simon
 * @version 2015-10-31
 * @see com.china.center.oa.sail.manager.impl.ProductExchangeConfigManagerImpl
 * @since 3.0
 */
@IntegrationAOP
public class ProductExchangeConfigManagerImpl implements ProductExchangeConfigManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ProductExchangeConfigDAO productExchangeConfigDAO = null;

    private CommonDAO commonDAO = null;


    /**
     * default constructor
     */
    public ProductExchangeConfigManagerImpl()
    {
    }

    @Transactional(rollbackFor = MYException.class)
    @Override
    public boolean addBean(User user, ProductExchangeConfigBean bean) throws MYException {

        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20());

        _logger.info("***save ProductVSGiftBean bean:"+bean);
        return this.productExchangeConfigDAO.saveEntityBean(bean);
    }

    @Override
    @Transactional(rollbackFor = MYException.class)
    public boolean updateBean(User user, ProductExchangeConfigBean bean) throws MYException {
        JudgeTools.judgeParameterIsNull(user, bean);
        return this.productExchangeConfigDAO.updateEntityBean(bean);
    }

    @Override
    @Transactional(rollbackFor = MYException.class)
    public boolean deleteBean(User user, String id) throws MYException {
        JudgeTools.judgeParameterIsNull(user, id);

        ProductExchangeConfigBean conf = this.productExchangeConfigDAO.find(id);

        if (conf == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        return productExchangeConfigDAO.deleteEntityBean(id);
    }

    public ProductExchangeConfigVO findVO(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        ProductExchangeConfigVO obj = this.productExchangeConfigDAO.findVO(id);

        if (obj == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        return obj;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    public ProductExchangeConfigDAO getProductExchangeConfigDAO() {
        return productExchangeConfigDAO;
    }

    public void setProductExchangeConfigDAO(ProductExchangeConfigDAO productExchangeConfigDAO) {
        this.productExchangeConfigDAO = productExchangeConfigDAO;
    }
}
