/**
 * File Name: SailConfigAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-12-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.action;


import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.sail.bean.ProductExchangeConfigBean;
import com.china.center.oa.sail.dao.ProductExchangeConfigDAO;
import com.china.center.oa.sail.manager.ProductExchangeConfigManager;
import com.china.center.oa.sail.vo.ProductExchangeConfigVO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ProductExchangeConfigAction
 * 
 * @author simon
 * @version 2015-10-31商品转换功能
 * @see com.china.center.oa.sail.action.ProductExchangeConfigAction
 * @since 3.0
 */
public class ProductExchangeConfigAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ProductExchangeConfigDAO productExchangeConfigDAO = null;

    private ProductExchangeConfigManager productExchangeConfigManager = null;

    private static final String QUERYPRODUCTEXCHANGE = "queryProductExchange";

    /**
     * default constructor
     */
    public ProductExchangeConfigAction()
    {
    }

    /**
     * list
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward list(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPRODUCTEXCHANGE, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCTEXCHANGE, request, condtion,
                this.productExchangeConfigDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForAdd
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward preForAdd(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        return mapping.findForward("add");
    }

    /**
     * add
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward add(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ProductExchangeConfigBean bean = new ProductExchangeConfigBean();

        try
        {
            BeanUtil.getBean(bean, request);
            _logger.info("create ProductExchangeConfigBean:"+bean);

            User user = Helper.getUser(request);

            this.productExchangeConfigManager.addBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("list");
    }

    /**
     * update
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward update(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ProductExchangeConfigBean bean = new ProductExchangeConfigBean();

        try
        {
            BeanUtil.getBean(bean, request);
            _logger.info("update ProductExchangeConfigBean:"+bean);

            User user = Helper.getUser(request);

            productExchangeConfigManager.updateBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "更新失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("list");
    }

    /**
     * find
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward find(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        ProductExchangeConfigVO vo = null;
        try{
            vo = this.productExchangeConfigManager.findVO(id);

            if (vo == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "不存在");

                return mapping.findForward("list");
            }
        }catch(Exception e){}

        request.setAttribute("bean", vo);

        if ("1".equals(update))
        {
           return mapping.findForward("update");
        } else{
            return mapping.findForward("detail");
        }
    }

    /**
     * delete
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws javax.servlet.ServletException
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            productExchangeConfigManager.deleteBean(user, id);

            ajax.setSuccess("成功删除");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    public ProductExchangeConfigDAO getProductExchangeConfigDAO() {
        return productExchangeConfigDAO;
    }

    public void setProductExchangeConfigDAO(ProductExchangeConfigDAO productExchangeConfigDAO) {
        this.productExchangeConfigDAO = productExchangeConfigDAO;
    }

    public ProductExchangeConfigManager getProductExchangeConfigManager() {
        return productExchangeConfigManager;
    }

    public void setProductExchangeConfigManager(ProductExchangeConfigManager productExchangeConfigManager) {
        this.productExchangeConfigManager = productExchangeConfigManager;
    }
}
