/**
 * File Name: FlowAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.oa.product.bean.*;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.publics.bean.EnumBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.EnumDAO;
import com.china.center.tools.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.constant.ProductApplyConstant;
import com.china.center.oa.product.dao.ProductApplyDAO;
import com.china.center.oa.product.dao.ProductSubApplyDAO;
import com.china.center.oa.product.dao.ProductVSStafferDAO;
import com.china.center.oa.product.facade.ProductApplyFacade;
import com.china.center.oa.product.manager.ProductApplyManager;
import com.china.center.oa.product.vo.ProductApplyVO;
import com.china.center.oa.product.vo.ProductSubApplyVO;
import com.china.center.oa.product.vo.ProductVSStafferVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.vo.FlowLogVO;

/**
 * 
 * 新产品申请
 * 
 * @author fangliwen 2012-5-23
 */
public class ProductApplyAction extends DispatchAction {

    private final Log           _logger             = LogFactory.getLog(getClass());

    private ProductApplyFacade  productApplyFacade  = null;

    private ProductApplyManager productApplyManager = null;

    private ProductApplyDAO     productApplyDAO     = null;   

    private ProductVSStafferDAO productVSStafferDAO = null;

    private ProductSubApplyDAO  productSubApplyDAO  = null;

    private FlowLogDAO          flowLogDAO          = null;

    private PrincipalshipDAO    principalshipDAO    = null;
    
    private InvoiceDAO invoiceDAO = null;

    private ProductDAO productDAO = null;

    private EnumDAO enumDAO = null;

    private static String       QUERYPRODUCTAPPLY   = "queryProductApply";

    private static final String SAVE                = "0";

    /**
     * default constructor
     */
    public ProductApplyAction() {
    }

    /**
     * queryProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String forward = request.getParameter("forward");

        final ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPRODUCTAPPLY, request, condition);

        // 1 部门审批
        if (forward.equals("1")) {

            condition.addCondition("ProductApplyBean.status", "=", ProductApplyConstant.STATUS_PRODUCTAPPLY);
            
        } else if (forward.equals("2")) {

            condition.addCondition("ProductApplyBean.status", "=", ProductApplyConstant.STATUS_PRODUCTAPPLY);
        }

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCTAPPLY, request,
                condition, this.productApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    public ActionForward preForAddProductApply(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    throws ServletException {
    	
    	prepare(request);
    	
    	return mapping.findForward("addProductApply");
    }

	private void prepare(HttpServletRequest request)
	{
		List<InvoiceBean> invoiceList1 = invoiceDAO.listForwardIn();

        request.setAttribute("invoiceList1", invoiceList1);
        
        List<InvoiceBean> invoiceList2 = invoiceDAO.listForwardOut();

        request.setAttribute("invoiceList2", invoiceList2);
	}
    
    /**
     * add ProductApply
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addProductApply(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    throws ServletException {

        ProductApplyBean productApplyBean = new ProductApplyBean();

        BeanUtil.getBean(productApplyBean, request);

        String save = request.getParameter("save");

        if (save.equals(SAVE)) 
        {
            productApplyBean.setStatus(ProductApplyConstant.STATUS_SAVE);

        } else 
        {
            
            productApplyBean.setStatus(ProductApplyConstant.STATUS_SUBMIT);
        }

        productApplyBean.setLogTime(TimeTools.now());

        setSubProduct(request, productApplyBean);
        
        setProductVSStaffer(request, productApplyBean);

        try {

            User user = Helper.getUser(request);

            productApplyFacade.addProductApply(user.getId(), productApplyBean);

            request.setAttribute(KeyConstant.MESSAGE, "操作成功");

        } catch (MYException e) {

            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败：" + e.getMessage());
        }

        return mapping.findForward("queryProductApply");
    }

    /**
     * 产品申请时合成产品之子产品
     * @param request
     * @param productApplyBean
     */
    private void setSubProduct(HttpServletRequest request, ProductApplyBean productApplyBean) 
    {
        List<ProductSubApplyBean> productSubApplyList = new ArrayList<ProductSubApplyBean>();

        productApplyBean.setProductSubApplyList(productSubApplyList);

        String[] subProductIds = request.getParameterValues("subProductId");

        String[] subProductAmounts = request.getParameterValues("subProductAmount");

        if (null != subProductIds)
        {
            for (int i = 0; i < subProductIds.length; i++) 
            {
                ProductSubApplyBean productSubApplyBean = new ProductSubApplyBean();

                productSubApplyBean.setSubProductId(subProductIds[i]);
                productSubApplyBean.setSubProductAmount(MathTools.parseInt(subProductAmounts[i]));

                productSubApplyList.add(productSubApplyBean);
            }
        }
        
    }

    /**
     * setProductVSStaffer
     * 
     * @param request
     * @param productApplyBean
     */
    private void setProductVSStaffer(final HttpServletRequest request, ProductApplyBean productApplyBean) 
    {
        List<ProductVSStafferBean> vsList = new ArrayList<ProductVSStafferBean>();

        productApplyBean.setVsList(vsList);

        String[] stafferRoles = request.getParameterValues("stafferRole");

        String[] commissionRatios = request.getParameterValues("commissionRatio");
        
        String[] stafferIds = request.getParameterValues("stafferId");

        if (null != stafferRoles)
        {
            for (int i = 0; i < stafferRoles.length; i++) 
            {
                ProductVSStafferBean vsBean = new ProductVSStafferBean();

                vsBean.setStafferRole(MathTools.parseInt(stafferRoles[i]));
                vsBean.setCommissionRatio(MathTools.parseInt(commissionRatios[i]));
                vsBean.setStafferId(stafferIds[i]);

                vsList.add(vsBean);
            }
        }
        
    }    
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try {
            User user = Helper.getUser(request);

            productApplyFacade.deleteProductApply(user.getId(), id);

            ajax.setSuccess("成功操作");

        } catch (MYException e) {

            _logger.error(e, e);

            ajax.setError("删除新申请产品失败：" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id = request.getParameter("id");

        String update = request.getParameter("update");

        if (null == update)
            update = "0";

        ProductApplyVO bean = productApplyDAO.findVO(id);

        if (null == bean) {
            return ActionTools.toError("数据异常，请重新操作", "queryProductApply", mapping, request);
        }

        int status = bean.getStatus();

        if ("1".equals(update)) {

            if (status != ProductApplyConstant.STATUS_SAVE
                    && status != ProductApplyConstant.STATUS_REJECT) {
                return ActionTools.toError("非保存或驳回状态不允许修改", "queryProductApply", mapping, request);
            }
        }

        request.setAttribute("bean", bean);

        prepare(request);
        
        processIndustryId(bean);

        List<ProductSubApplyVO> psab = productSubApplyDAO.queryEntityVOsByFK(id);

        request.setAttribute("itemList", psab);
        
        List<ProductVSStafferVO> vsList = productVSStafferDAO.queryEntityVOsByFK(id);
        
        request.setAttribute("itemList1", vsList);
        
        if (update.equals("1")) {

            return mapping.findForward("updateProductApply");

        } else {

            List<FlowLogBean> logList = flowLogDAO.queryEntityBeansByFK(id);

            List<FlowLogVO> logsVO = new ArrayList<FlowLogVO>();

            for (FlowLogBean flowLogBean : logList)
            {
                logsVO.add(getFlowLogVO(flowLogBean));
            }
            
            request.setAttribute("logList", logsVO);

            return mapping.findForward("detailProductApply");
        }

    }
    
    private FlowLogVO getFlowLogVO(FlowLogBean bean)
    {
        FlowLogVO vo = new FlowLogVO();

        if (bean == null)
        {
            return vo;
        }

        BeanUtil.copyProperties(vo, bean);

        vo.setOprModeName(DefinedCommon.getValue("productOPRMode", vo.getOprMode()));

        vo.setPreStatusName(DefinedCommon.getValue("productApplyStatus", vo.getPreStatus()));

        vo.setAfterStatusName(DefinedCommon.getValue("productApplyStatus", vo.getAfterStatus()));

        return vo;
    }

    private void processIndustryId(ProductApplyVO bean) 
    {
        String industryId = bean.getIndustryId();

        String industryNames = "";

        String[] industryIds = industryId.split(",");

        for (String each : industryIds) {
            PrincipalshipBean prin = principalshipDAO.find(each);

            industryNames += prin.getName() + ",";
        }

        bean.setIndustryIdsName(industryNames);
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException 
    {
        String id = request.getParameter("id");

        String save = request.getParameter("save");
        
        ProductApplyBean bean = productApplyDAO.find(id);

        if (null == bean) 
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误，未找到id=" + id + " 的单据");

            mapping.findForward("queryProductApply");
        }

        BeanUtil.getBean(bean, request);

        bean.setLogTime(TimeTools.now());

        if (save.equals("1"))
        {
            bean.setStatus(ProductApplyConstant.STATUS_SUBMIT);
        }
        else
        {
            bean.setStatus(ProductApplyConstant.STATUS_SAVE);
        }
        
        setSubProduct(request, bean);

        setProductVSStaffer(request, bean);
        
        try {
            User user = Helper.getUser(request);

            productApplyFacade.updateProductApply(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改申请的产品");

        } catch (MYException e) {

            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改产品失败：" + e.getMessage());
        }
        
        return mapping.findForward("queryProductApply");
    }

    public ActionForward passProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id = request.getParameter("id");

        ProductApplyBean bean = productApplyDAO.find(id);

        if (null == bean) {

            return ActionTools.toError("数据异常，请重新操作", "queryProductApply", mapping, request);
        }

        AjaxResult ajax = new AjaxResult();

        try {
            User user = Helper.getUser(request);

            productApplyFacade.passProductApply(user.getId(), bean);

            ajax.setSuccess("成功操作");

        } catch (MYException e) {

            _logger.error(e, e);

            ajax.setError("审批出错:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);

    }

    public ActionForward pass1ProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id = request.getParameter("id");

        ProductApplyBean bean = productApplyDAO.find(id);

        if (null == bean) {

            return ActionTools.toError("数据异常，请重新操作", "queryProductApply", mapping, request);
        }

        AjaxResult ajax = new AjaxResult();

        try {
            User user = Helper.getUser(request);

            productApplyFacade.pass1ProductApply(user.getId(), bean);

            ajax.setSuccess("成功操作");

        } catch (MYException e) {

            _logger.error(e, e);

            ajax.setError("审批出错:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);

    }

    public ActionForward rejectProductApply(ActionMapping mapping, ActionForm form,
            final HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String id = request.getParameter("id");

        ProductApplyBean bean = productApplyDAO.find(id);

        if (null == bean) {

            return ActionTools.toError("数据异常，请重新操作", "queryProductApply", mapping, request);
        }

        AjaxResult ajax = new AjaxResult();

        try {
            User user = Helper.getUser(request);

            productApplyFacade.rejectProductApply(user.getId(), bean);

            ajax.setSuccess("成功操作");

        } catch (MYException e) {

            _logger.error(e, e);

            ajax.setError("审批出错:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);

    }

    /**
     * 2015/11/5 批量导入新产品申请
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward importProductApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {
        User user = Helper.getUser(request);

        RequestDataStream rds = new RequestDataStream(request);

        boolean importError = false;

        List<ProductApplyBean> importItemList = new ArrayList<ProductApplyBean>();

        StringBuilder builder = new StringBuilder();

        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("importProductApply");
        }

        if ( !rds.haveStream())
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("importProductApply");
        }

        // 获取上次最后一次导入的时间
        ReaderFile reader = ReadeFileFactory.getXLSReader();

        try
        {
            reader.readFile(rds.getUniqueInputStream());

            while (reader.hasNext())
            {
                String[] obj = fillObj((String[])reader.next());

                // 第一行忽略
                if (reader.getCurrentLineNumber() == 1)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }

                int currentNumber = reader.getCurrentLineNumber();

                if (obj.length >= 2 )
                {
                    ProductApplyBean bean = new ProductApplyBean();

                    // 产品名
                    if ( !StringTools.isNullOrNone(obj[0]))
                    {
                        bean.setName(obj[0]);

                        //重复品名检查
                        ConditionParse conditionParse = new ConditionParse();
                        conditionParse.addWhereStr();
                        conditionParse.addCondition("name", "=", bean.getName());
                        List<ProductApplyBean> applyBeans = this.productApplyDAO.queryEntityBeansByCondition(conditionParse);
                        if (!ListTools.isEmptyOrNull(applyBeans)){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("产品名已存在")
                                    .append("<br>");
                        }
                    }else{
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("产品名为空")
                                .append("<br>");
                    }

                    //产品类型
                    if ( !StringTools.isNullOrNone(obj[1]))
                    {
                        int type = this.getType(obj[1]);
                        if (type == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("产品类型不支持")
                                    .append("<br>");
                        } else{
                            bean.setType(type);
                        }
                    }else{
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("产品类型为空")
                                .append("<br>");
                    }

                    // 材质类型
                    if ( !StringTools.isNullOrNone(obj[2]))
                    {
                        String materialType = obj[2];
                        ConditionParse conditionParse = new ConditionParse();
                        conditionParse.addWhereStr();
                        conditionParse.addCondition("type", "=", "201");

                        List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                        if (!ListTools.isEmptyOrNull(enumBeans)){
                            for (EnumBean enumBean : enumBeans){
                                if (materialType.equals(enumBean.getValue())){
                                    bean.setMateriaType(Integer.valueOf(enumBean.getKey()));
                                    break;
                                }
                            }
                        }

                        if (bean.getMateriaType() == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("材质类型不支持")
                                    .append("<br>");
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("材质类型为空")
                                .append("<br>");
                    }

                    // 渠道类型
                    if ( !StringTools.isNullOrNone(obj[3]))
                    {
                        int channelType = this.getChannelType(obj[3]);
                        if (channelType == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("渠道类型不支持")
                                    .append("<br>");
                        } else{
                            bean.setChannelType(channelType);
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("渠道类型为空")
                                .append("<br>");
                    }

                    // 管理类型
                    if ( !StringTools.isNullOrNone(obj[4]))
                    {
                        int managerType = this.getManagerType(obj[4]);
                        if (managerType == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("管理类型不支持")
                                    .append("<br>");
                        } else{
                            bean.setManagerType(managerType);
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("管理类型为空")
                                .append("<br>");
                    }

                    // 分类品名
                    if ( !StringTools.isNullOrNone(obj[5]))
                    {
                        bean.setClassName(obj[5]);
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("分类品名为空")
                                .append("<br>");
                    }

                    // 旧货
                    if ( !StringTools.isNullOrNone(obj[6]))
                    {
                        String secondhandGoods = obj[6];
                        ConditionParse conditionParse = new ConditionParse();
                        conditionParse.addWhereStr();
                        conditionParse.addCondition("type", "=", "210");

                        List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                        if (!ListTools.isEmptyOrNull(enumBeans)){
                            for (EnumBean enumBean : enumBeans){
                                if (secondhandGoods.equals(enumBean.getValue())){
                                    bean.setSecondhandGoods(Integer.valueOf(enumBean.getKey()));
                                    break;
                                }
                            }
                        }

                        if (bean.getSecondhandGoods() == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("旧货类型不支持")
                                    .append("<br>");
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("旧货为空")
                                .append("<br>");
                    }

                    // 金
                    if ( !StringTools.isNullOrNone(obj[7]))
                    {
                        bean.setGold(Double.valueOf(obj[7]));
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("金为空")
                                .append("<br>");
                    }

                    // 银
                    if ( !StringTools.isNullOrNone(obj[8]))
                    {
                        bean.setSilver(Double.valueOf(obj[8]));
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("银为空")
                                .append("<br>");
                    }

                    // 产品性质
                    if ( !StringTools.isNullOrNone(obj[9]))
                    {
                        int nature = this.getNature(obj[9]);
                        if (nature == -1){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("产品性质不支持")
                                    .append("<br>");
                        }else{
                            bean.setNature(nature);
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("产品性质为空")
                                .append("<br>");
                    }

                    // 关联成品
                    if (bean.getNature() == ProductApplyConstant.NATURE_SINGLE) {
                        // 根据成品，获取配件的编码
                        String refProductId = obj[10];

                        if ( !StringTools.isNullOrNone(refProductId))
                        {
                            bean.setRefProductId(refProductId);
                            ProductBean product = productDAO.find(refProductId);

                            if (null == product)
                            {
                                importError = true;

                                builder
                                        .append("第[" + currentNumber + "]错误:")
                                        .append("关联的成品不存在")
                                        .append("<br>");
                            }
                        }else
                        {
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("配件产品时要有成品产品关联")
                                    .append("<br>");
                        }
                    }

                    // 进项发票
                    if ( !StringTools.isNullOrNone(obj[11]))
                    {
                        List<InvoiceBean> invoiceList1 = invoiceDAO.listForwardIn();
                        if (!ListTools.isEmptyOrNull(invoiceList1)){
                            for (InvoiceBean invoiceBean : invoiceList1){
                                if (obj[11].equals(invoiceBean.getName())){
                                    bean.setInputInvoice(invoiceBean.getId());
                                    break;
                                }
                            }
                        }

                        if (StringTools.isNullOrNone(bean.getInputInvoice())){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("进项发票不存在")
                                    .append("<br>");
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("进项发票为空")
                                .append("<br>");
                    }

                    // 销项发票
                    if ( !StringTools.isNullOrNone(obj[12]))
                    {
                        List<InvoiceBean> invoiceList2 = invoiceDAO.listForwardOut();
                        if (!ListTools.isEmptyOrNull(invoiceList2)){
                            for (InvoiceBean invoiceBean : invoiceList2){
                                if (obj[12].equals(invoiceBean.getName())){
                                    bean.setSailInvoice(invoiceBean.getId());
                                    break;
                                }
                            }
                        }

                        if (StringTools.isNullOrNone(bean.getSailInvoice())){
                            importError = true;

                            builder
                                    .append("第[" + currentNumber + "]错误:")
                                    .append("销项发票不存在")
                                    .append("<br>");
                        }
                    }else
                    {
                        importError = true;

                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("销项发票为空")
                                .append("<br>");
                    }

                    importItemList.add(bean);
                }
                else
                {
                    builder
                            .append("第[" + currentNumber + "]错误:")
                            .append("数据长度不足5格错误")
                            .append("<br>");

                    importError = true;
                }
            }

        }catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.toString());

            return mapping.findForward("importProductApply");
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                _logger.error(e, e);
            }
        }

        rds.close();

        if (importError){

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导入出错:"+ builder.toString());

            return mapping.findForward("importProductApply");
        }

        try {
            this.productApplyManager.importProductApply(user, importItemList);
        }
        catch(MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导入出错:" + e.getErrorContent());

            return mapping.findForward("importProductApply");
        }

        request.setAttribute(KeyConstant.MESSAGE, "导入成功");

        return mapping.findForward("importProductApply");
    }

    /**
     * 产品类型
     * @param type
     * @return
     */
    private int getType(String type){
        if ("金银章".equals(type)){
            return ProductConstant.PRODUCT_TYPE_OTHER;
        } else if ("金银币".equals(type)){
            return ProductConstant.PRODUCT_TYPE_PAPER;
        } else if("流通币".equals(type)){
            return ProductConstant.PRODUCT_TYPE_METAL;
        } else if ("旧币".equals(type)){
            return ProductConstant.PRODUCT_TYPE_NUMISMATICS;
        } else if ("邮票".equals(type)){
            return ProductConstant.PRODUCT_TYPE_MONCE;
        } else {
            return -1;
        }
    }

    /**
     * 渠道类型
     * @param channelType
     * @return
     */
    private int getChannelType(String channelType){
        if ("自有".equals(channelType)){
            return ProductConstant.SAILTYPE_SELF;
        } else if ("经销".equals(channelType)){
            return ProductConstant.SAILTYPE_REPLACE;
        } else if("定制".equals(channelType)){
            return ProductConstant.SAILTYPE_CUSTOMER;
        } else if ("私采".equals(channelType)){
            return ProductConstant.SAILTYPE_OTHER;
        } else {
            return -1;
        }
    }

    /**
     * 管理类型
     * @param managerType
     * @return
     */
    private int getManagerType(String managerType){
        if ("管理".equals(managerType)){
            return PublicConstant.MANAGER_TYPE_MANAGER;
        } else if ("普通".equals(managerType)){
            return PublicConstant.MANAGER_TYPE_COMMON;
        } else {
            return -1;
        }
    }

    private int getNature(String nature){
        if ("配件产品".equals(nature)){
            return ProductApplyConstant.NATURE_SINGLE;
        } else if ("成品".equals(nature)){
            return ProductApplyConstant.NATURE_COMPOSE;
        } else {
            return -1;
        }
    }

    private String[] fillObj(String[] obj)
    {
        String[] result = new String[13];

        for (int i = 0; i < result.length; i++ )
        {
            if (i < obj.length)
            {
                result[i] = obj[i];
            }
            else
            {
                result[i] = "";
            }
        }

        return result;
    }

    public ProductApplyFacade getProductApplyFacade() {
        return productApplyFacade;
    }

    public void setProductApplyFacade(ProductApplyFacade productApplyFacade) {
        this.productApplyFacade = productApplyFacade;
    }

    public ProductApplyManager getProductApplyManager() {
        return productApplyManager;
    }

    public void setProductApplyManager(ProductApplyManager productApplyManager) {
        this.productApplyManager = productApplyManager;
    }

    public ProductApplyDAO getProductApplyDAO() {
        return productApplyDAO;
    }

    public void setProductApplyDAO(ProductApplyDAO productApplyDAO) {
        this.productApplyDAO = productApplyDAO;
    }

    public ProductVSStafferDAO getProductVSStafferDAO() {
        return productVSStafferDAO;
    }

    public void setProductVSStafferDAO(ProductVSStafferDAO productVSStafferDAO) {
        this.productVSStafferDAO = productVSStafferDAO;
    }

    public ProductSubApplyDAO getProductSubApplyDAO() {
        return productSubApplyDAO;
    }

    public void setProductSubApplyDAO(ProductSubApplyDAO productSubApplyDAO) {
        this.productSubApplyDAO = productSubApplyDAO;
    }

    public FlowLogDAO getFlowLogDAO() {
        return flowLogDAO;
    }

    public void setFlowLogDAO(FlowLogDAO flowLogDAO) {
        this.flowLogDAO = flowLogDAO;
    }

    public PrincipalshipDAO getPrincipalshipDAO() {
        return principalshipDAO;
    }

    public void setPrincipalshipDAO(PrincipalshipDAO principalshipDAO) {
        this.principalshipDAO = principalshipDAO;
    }

	/**
	 * @return the invoiceDAO
	 */
	public InvoiceDAO getInvoiceDAO()
	{
		return invoiceDAO;
	}

	/**
	 * @param invoiceDAO the invoiceDAO to set
	 */
	public void setInvoiceDAO(InvoiceDAO invoiceDAO)
	{
		this.invoiceDAO = invoiceDAO;
	}

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public EnumDAO getEnumDAO() {
        return enumDAO;
    }

    public void setEnumDAO(EnumDAO enumDAO) {
        this.enumDAO = enumDAO;
    }
}
