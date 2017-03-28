package com.china.center.oa.sail.action;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.oa.client.bean.CustomerBean;
import com.china.center.oa.client.dao.CustomerMainDAO;
import com.china.center.oa.finance.bean.InsVSInvoiceNumBean;
import com.china.center.oa.finance.bean.InvoiceinsItemBean;
import com.china.center.oa.finance.dao.InsVSInvoiceNumDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.product.bean.*;
import com.china.center.oa.product.dao.*;
import com.china.center.oa.publics.bean.*;
import com.china.center.oa.publics.dao.*;
import com.china.center.oa.sail.bean.*;
import com.china.center.oa.sail.dao.*;
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
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.vo.ComposeItemVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.ShipConstant;
import com.china.center.oa.sail.manager.ShipManager;
import com.china.center.oa.sail.vo.PackageVO;
import com.china.center.oa.sail.wrap.PackageWrap;
import com.china.center.oa.sail.wrap.PickupWrap;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class ShipAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private PackageDAO packageDAO = null;

    private PackageItemDAO packageItemDAO = null;

    private OutDAO outDAO = null;

    private BaseDAO baseDAO = null;

    private DistributionDAO distributionDAO = null;

    private ExpressDAO expressDAO = null;

    private ShipManager shipManager = null;

    private ProductDAO productDAO = null;

    private ComposeProductDAO composeProductDAO = null;

    private ComposeItemDAO composeItemDAO = null;

    private OutImportDAO outImportDAO = null;

    private PackageVSCustomerDAO packageVSCustomerDAO = null;

    private UserManager userManager = null;

    private DepotDAO depotDAO = null;

    private BranchRelationDAO branchRelationDAO = null;

    private StafferDAO stafferDAO = null;

    private InvoiceinsDAO invoiceinsDAO = null;

    private InvoiceinsItemDAO invoiceinsItemDAO = null;

    private InvoiceDAO invoiceDAO = null;

    private CustomerMainDAO customerMainDAO = null;

    private ProvinceDAO provinceDAO = null;

    private CityDAO cityDAO = null;

    private ProductVSBankDAO productVSBankDAO = null;

    private CiticVSOAProductDAO citicVSOAProductDAO = null;

    private EnumDAO enumDAO = null;

    private ProductImportDAO productImportDAO = null;

    private BankConfigForShipDAO bankConfigForShipDAO = null;

    private final static String QUERYPACKAGE = "queryPackage";

    private final static String QUERYPICKUP = "queryPickup";

    /**
     * default construct
     */
    public ShipAction()
    {
    }

    /**
     * queryPackage
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPackage(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException
    {
        String customerName = request.getParameter("customerName");
        //#370 "不包含客戶"
        String notName = request.getParameter("notName");
        String productName = request.getParameter("productName");
        String notProductName = request.getParameter("notProductName");
        String outId = request.getParameter("outId");
        String stafferName = request.getParameter("stafferName");
        String insFollowOut = request.getParameter("insFollowOut");
        String status = request.getParameter("status");
        _logger.info("***queryPackage with parameter customer:"+customerName+"***stafferName***"+stafferName+"***productName***"+productName+"***insFollowOut**"+insFollowOut+"***status***"+status);
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();
        condtion.addWhereStr();

        // 要根据仓库的权限,计算出地点,根据地点查询
        setDepotCondotionInOut(user, condtion);

        // TEMPLATE 在action里面默认查询条件
        Map<String, String> initMap = initLogTime(request, condtion, true);

        ActionTools.processJSONDataQueryCondition(QUERYPACKAGE, request, condtion, initMap);

        String rawSql = condtion.toString();
        _logger.info("***rawSQL**"+rawSql);
        if ((!StringTools.isNullOrNone(productName) ||!StringTools.isNullOrNone(notProductName) || !StringTools.isNullOrNone(outId))
                && rawSql.indexOf("PackageItemBean") !=-1){
//            int index2 = rawSql.lastIndexOf("AND");
//            String prefix = rawSql.substring(0, index2);
//            String sql = prefix+"and exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.packageId=PackageBean.id and PackageItemBean.productName like '%"+productName+"%')";
            StringBuilder sb = new StringBuilder();
//            sb.append("and exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.packageId=PackageBean.id ");

            //销售单号/产品名/不包含产品这三个条件只能选其一
            if (!StringTools.isNullOrNone(outId)){
                sb.append("and exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.packageId=PackageBean.id ");

                String template = "AND PackageItemBean.outId like '%"+outId+"%'";
                rawSql = rawSql.replace(template,"");
                sb.append("and PackageItemBean.outId like '%"+outId+"%'");
            } else if (!StringTools.isNullOrNone(productName)){
                sb.append("and exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.packageId=PackageBean.id ");

                String template = "AND PackageItemBean.productName like '%"+productName+"%'";
                rawSql = rawSql.replace(template,"");
                sb.append("and PackageItemBean.productName like '%"+productName+"%'");
            } else if (!StringTools.isNullOrNone(notProductName)){
                sb.append("and not exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.packageId=PackageBean.id ");

                String template = "AND PackageItemBean.notProductName like '%"+notProductName+"%'";
                rawSql = rawSql.replace(template,"");
                sb.append("and PackageItemBean.productName like '%"+notProductName+"%'");
            }

            sb.append(")");

            String newSql = rawSql+sb.toString();
            _logger.info("***new sql***"+newSql);
            condtion.setCondition(newSql);
        }

        String rawSql2 = condtion.toString();
        if ((!StringTools.isNullOrNone(notName))
                && rawSql2.indexOf("CustomerBean") !=-1){
            String template = "AND CustomerBean.notName like '%"+notName+"%'";
            String newSql = rawSql2.replace(template,"AND CustomerBean.name not like '%"+notName+"%'");
            _logger.info("***new sql2***"+newSql);
            condtion.setCondition(newSql);
        }

        //#390
        String rawSql3 = condtion.toString();
        String pickupStatus = request.getParameter("pickupStatus");
        if ((!StringTools.isNullOrNone(pickupStatus))){
            String template = "AND PackageBean.pickupStatus ="+pickupStatus;
            if("0".equals(pickupStatus)) {
                String newSql = rawSql3.replace(template, "AND PackageBean.pickupId !=''");
                _logger.info("***new sql3***" + newSql);
                condtion.setCondition(newSql);
            }else if ("1".equals(pickupStatus)){
                String newSql = rawSql3.replace(template, "AND PackageBean.pickupId =''");
                _logger.info("***new sql3***" + newSql);
                condtion.setCondition(newSql);
            }
        }


        //默認頁面没有时间参数时,pickupId为空
        String alogTime = request.getParameter("alogTime");
        if (StringTools.isNullOrNone(alogTime)){
            condtion.addCondition(" and PackageBean.pickupId = ''");
        }

        //状态为空或者为已发货时，不能加这个判断
//        if (!StringTools.isNullOrNone(status) && !"2".equals(status)){
//            condtion.addCondition(" and PackageBean.pickupId = ''");
//        }

        //2015/3/22 按照单据时间排序，时间最老的最先显示
        condtion.addCondition(" order by PackageBean.billsTime asc");
        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPACKAGE, request, condtion, this.packageDAO,
                new HandleResult<PackageVO>()
                {
                    public void handle(PackageVO vo)
                    {
                        vo.setPay(DefinedCommon.getValue("deliverPay", vo.getExpressPay()) + "/" + DefinedCommon.getValue("deliverPay", vo.getTransportPay()));
                    }

                });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 设置仓库的过滤条件
     *
     * @param user
     * @param condtion
     */
    private void setDepotCondotionInOut(User user, ConditionParse condtion)
    {
        // 只能看到自己的仓库
        List<AuthBean> depotAuthList = userManager.queryExpandAuthById(
                user.getId(), AuthConstant.EXPAND_AUTH_DEPOT);

        if (ListTools.isEmptyOrNull(depotAuthList))
        {
            // 永远也没有结果
            condtion.addFlaseCondition();
        }
        else
        {
            StringBuffer sb = new StringBuffer();

            sb.append("and (");
            for (Iterator<AuthBean> iterator = depotAuthList.iterator(); iterator
                    .hasNext();)
            {
                AuthBean authBean = (AuthBean) iterator.next();

                // 接受仓库是自己管辖的
                if (iterator.hasNext())
                {
                    // 根据仓库找出地点
                    DepotBean dept = depotDAO.find(authBean.getId());

                    if (null != dept)
                    {
                        sb.append("PackageBean.locationId = '" + dept.getIndustryId2()
                                + "' or ");
                    }
                }
                else
                {
                    // 根据仓库找出地点
                    DepotBean dept = depotDAO.find(authBean.getId());

                    if (null != dept)
                    {
                        sb.append("PackageBean.locationId = '" + dept.getIndustryId2() + "'");
                    }
                    else
                    {
                        sb.append("1=1");
                    }
                }
            }

            sb.append(") ");

            condtion.addCondition(sb.toString());
        }
    }

    /**
     * initLogTime
     *
     * @param request
     * @param condtion
     * @return
     */
    private Map<String, String> initLogTime(HttpServletRequest request,
                                            ConditionParse condtion, boolean initStatus) {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(alogTime)
                && StringTools.isNullOrNone(blogTime)) {
            changeMap.put("alogTime", TimeTools.now_short(-7));

            changeMap.put("blogTime", TimeTools.now_short());

//            if (initStatus) {
//                changeMap.put("status",	String.valueOf(ShipConstant.SHIP_STATUS_INIT));
//
//                condtion.addIntCondition("PackageBean.status", "=", ShipConstant.SHIP_STATUS_INIT);
//            }

            condtion.addCondition("PackageBean.logTime", ">=",
                    TimeTools.now_short(-7) + " 00:00:00");

            condtion.addCondition("PackageBean.logTime", "<=",
                    TimeTools.now_short() + " 23:59:59");

            if (initStatus) {
                changeMap.put("status",	String.valueOf(ShipConstant.SHIP_STATUS_INIT));

//                condtion.addIntCondition("PackageBean.status", "=", ShipConstant.SHIP_STATUS_INIT);
                condtion.addCondition(" and PackageBean.status in (0,5) ");
            }
        }

        return changeMap;
    }

    /**
     * preForQueryPickup
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForQueryPickup(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException
    {
        prepare(request);

        return mapping.findForward("queryPickup");
    }

    private void prepare(HttpServletRequest request)
    {
        List<ExpressBean> expressList = expressDAO.listEntityBeansByOrder("order by id");

        request.setAttribute("expressList", expressList);
    }

    /**
     * queryPickup
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPickup(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException
    {
        User user = (User) request.getSession().getAttribute("user");

        List<PackageVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryPickupCondition(request,user, true);
                _logger.info("*******************condtion**********"+condtion);

                int tatol = packageDAO.countByCon(condtion);

                PageSeparate page = new PageSeparate(tatol,	30);

                ConditionParse condtion2 = getQueryPickupCondition(request,user, false);
                _logger.info("*******************condtion22222**********"+condtion);
                OldPageSeparateTools.initPageSeparate(condtion2, page, request,QUERYPICKUP);

                list = packageDAO.queryVOsByCon(condtion2, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request,
                        QUERYPICKUP);

                list = packageDAO.queryVOsByCon(OldPageSeparateTools.getCondition(request, QUERYPICKUP), OldPageSeparateTools
                        .getPageSeparate(request, QUERYPICKUP));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        // 对结果集包装
        List<PickupWrap> wrapList = new ArrayList<PickupWrap>();

        Set<String> set = new HashSet<String>();

        _logger.info(list.size()+"***Query list***"+list);
        for (PackageVO each : list)
        {
            if (!StringTools.isNullOrNone(each.getPickupId()) && !set.contains(each.getPickupId()))
            {
                set.add(each.getPickupId());
            }
        }

        for (String pickupId : set)
        {
            if (StringTools.isNullOrNone(pickupId)){
                _logger.warn("pickupId is empty!");
                continue;
            }
            List<PackageVO> voList = packageDAO.queryEntityVOsByFK(pickupId);

            // 从小到大
            Collections.sort(voList, new Comparator<PackageVO>() {
                public int compare(PackageVO o1, PackageVO o2) {
                    return o1.getIndex_pos() - o2.getIndex_pos();
                }
            });

            PickupWrap wrap = new PickupWrap();

            wrap.setPickupId(pickupId);
            wrap.setPackageList(voList);

            wrapList.add(wrap);
        }

        _logger.info("***itemList***"+wrapList);
        request.setAttribute("itemList", wrapList);

        List<ExpressBean> expressList = expressDAO.listEntityBeansByOrder("order by id");

        request.setAttribute("expressList", expressList);

        return mapping.findForward("queryPickup");
    }

    //#404 航天信息打印发票
    public ActionForward printInvoiceins(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException
    {
        User user = (User) request.getSession().getAttribute("user");
        String packageId = request.getParameter("packageId");
        _logger.info("***packageId***"+packageId);
        List<InvoiceinsVO> invoiceinsList = this.findInvoiceinsWithXN(packageId);

        _logger.info("***invoiceinsList***"+invoiceinsList);

        request.setAttribute("invoiceList", invoiceinsList);
        request.setAttribute("packageId", packageId);

        return mapping.findForward("printInvoiceins");
    }

    private List<InvoiceinsVO> findInvoiceinsWithXN(String packageId){
        List<InvoiceinsVO> invoiceinsList = new ArrayList<InvoiceinsVO>();
        List<PackageItemBean> itemList = this.packageItemDAO.queryEntityBeansByFK(packageId);
        for (PackageItemBean item: itemList){
            String productName = item.getProductName();
            if (productName!= null && productName.startsWith("发票号：XN")){
                String insId = item.getOutId();
                InvoiceinsVO invoiceinsBean = this.invoiceinsDAO.findVO(insId);
                if (invoiceinsBean!= null){
                    int amount = this.getProductAmount(invoiceinsBean);
                    double price = invoiceinsBean.getMoneys()/amount;
                    int fpsl = this.getFpsl(invoiceinsBean);
                    invoiceinsBean.setItemAmount(amount);
                    invoiceinsBean.setPrice(price);
                    invoiceinsBean.setSl(fpsl);
                    invoiceinsList.add(invoiceinsBean);
                }
            }
        }
        return invoiceinsList;
    }

    public ActionForward generateInvoiceinsXml(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response)
            throws ServletException
    {
        User user = (User) request.getSession().getAttribute("user");
        String packageId = request.getParameter("packageId");
        _logger.info("***packageId***"+packageId);
        JsonMapper mapper = new JsonMapper();
        AppResult result = new AppResult();

        Map<String,String> invoiceToXml = new HashMap<String, String>();
        List<InvoiceinsVO> invoiceinsList = this.findInvoiceinsWithXN(packageId);

        //generate XML payload
        for (InvoiceinsBean  bean:invoiceinsList){
            String payload = this.createXML(user, invoiceinsList.get(0));
            invoiceToXml.put(bean.getId(),payload);
        }

        result.setSuccessAndObj("OK", invoiceToXml);
        String jsonstr = mapper.toJson(result);
        _logger.info(jsonstr);
        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 404 更新发票号码 refer to InvoiceinsAction.generateInvoiceins
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
//    public ActionForward generateInvoiceins(ActionMapping mapping, ActionForm form,
//                                         HttpServletRequest request,
//                                         HttpServletResponse response)
//            throws ServletException
//    {
//        User user = (User) request.getSession().getAttribute("user");
//        String insId = request.getParameter("insId");
//        String fphm = request.getParameter("fphm");
//        String fpdm = request.getParameter("fpdm");
//        String packageId = request.getParameter("packageId");
//        _logger.info(packageId+"***insId***"+insId+"***fphm***"+fphm+"***fpdm***"+fpdm);
//
//        JsonMapper mapper = new JsonMapper();
//        AppResult result = new AppResult();
//
//        InsVSInvoiceNumBean ins = new InsVSInvoiceNumBean();
//        ins.setInvoiceNum(fphm);
//        ins.setInsId(fpdm);
//        result.setSuccessAndObj("OK", ins);
//        String jsonstr = mapper.toJson(result);
//        return JSONTools.writeResponse(response, jsonstr);
//    }

    private String createXML(User user, InvoiceinsBean bean){
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =
                    dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // root element
            Element rootElement = doc.createElement("invinterface");
            doc.appendChild(rootElement);

            //  invhead
            Element invhead = doc.createElement("invhead");
            rootElement.appendChild(invhead);

            // carname element
            Element fpzl = doc.createElement("fpzl");
            fpzl.appendChild(
                    doc.createTextNode("2"));
            invhead.appendChild(fpzl);

            //Invoiceins 表的ID字段值
            Element djhm = doc.createElement("djhm");
            djhm.appendChild(
                    doc.createTextNode(bean.getId()));
            invhead.appendChild(djhm);

            //heakcontent字段值
            Element gfmc = doc.createElement("gfmc");
            gfmc.appendChild(
                    doc.createTextNode(bean.getHeadContent()));
            invhead.appendChild(gfmc);

            Element gfsh = doc.createElement("gfsh");
            gfsh.appendChild(
                    doc.createTextNode(""));
            invhead.appendChild(gfsh);

            Element gfyh = doc.createElement("gfyh");
            gfyh.appendChild(
                    doc.createTextNode(""));
            invhead.appendChild(gfyh);

            Element gfdz = doc.createElement("gfdz");
            gfdz.appendChild(
                    doc.createTextNode(""));
            invhead.appendChild(gfdz);

            // 取invoiceid到 invoice表中取对应的VAL值，如果值是2，则值设为3
            Element fpsl = doc.createElement("fpsl");
            int val = this.getFpsl(bean);
            fpsl.appendChild(doc.createTextNode(String.valueOf(val)));

            invhead.appendChild(fpsl);

            Element fpbz = doc.createElement("fpbz");
            fpbz.appendChild(
                    doc.createTextNode(""));
            invhead.appendChild(fpbz);

            //开票人
            Element kprm = doc.createElement("kprm");
            kprm.appendChild(
                    doc.createTextNode("陈倩"));
            invhead.appendChild(kprm);

            //复核人
            Element fhrm = doc.createElement("fhrm");
            fhrm.appendChild(
                    doc.createTextNode("邢君君"));
            invhead.appendChild(fhrm);

            //收款人
            Element skrm = doc.createElement("skrm");
            skrm.appendChild(doc.createTextNode("黄文娟"));
            invhead.appendChild(skrm);

            //含税标识
            Element hsbz = doc.createElement("hsbz");
            hsbz.appendChild(
                    doc.createTextNode("1"));
            invhead.appendChild(hsbz);

            Element xfdz = doc.createElement("xfdz");
            xfdz.appendChild(
                    doc.createTextNode("南京市秦淮区正学路1号025-51885901"));
            invhead.appendChild(xfdz);

            Element xfyh = doc.createElement("xfyh");
            xfyh.appendChild(
                    doc.createTextNode("招商银行南京城北支行125902780610701"));
            invhead.appendChild(xfyh);

            Element hysy = doc.createElement("hysy");
            hysy.appendChild(
                    doc.createTextNode("0"));
            invhead.appendChild(hysy);


            //  invdetails
            Element invdetails = doc.createElement("invdetails");
            rootElement.appendChild(invdetails);

            // carname element
            Element details = doc.createElement("details");
            invdetails.appendChild(details);

            //Invoiceins 表的开票品名字段
            Element spmc = doc.createElement("spmc");
            spmc.appendChild(
                    doc.createTextNode(bean.getSpmc()));
            details.appendChild(spmc);

            Element ggxh = doc.createElement("ggxh");
            ggxh.appendChild(
                    doc.createTextNode(""));
            details.appendChild(ggxh);

            Element jldw = doc.createElement("jldw");
            jldw.appendChild(
                    doc.createTextNode("套"));
            details.appendChild(jldw);

            // invoiceins_item表中amount 字段合计
            int amount = this.getProductAmount(bean);
            Element spsl = doc.createElement("spsl");
            spsl.appendChild(
                    doc.createTextNode(String.valueOf(amount)));
            details.appendChild(spsl);

            // invoiceins_item表中price 字段值
            Element spdj = doc.createElement("spdj");
            spdj.appendChild(
                    doc.createTextNode(String.valueOf(this.roundDouble(bean.getMoneys()/amount))));
            details.appendChild(spdj);

            Element spje = doc.createElement("spje");
            spje.appendChild(
                    doc.createTextNode(String.valueOf(this.roundDouble(bean.getMoneys()))));
            details.appendChild(spje);

            // 含税税额 总金额*税率/(1+税率)
            double sl = this.roundDouble(val/100);
            Element spse = doc.createElement("spse");
            double se = this.roundDouble(bean.getMoneys()*sl/(1+sl));
            spse.appendChild(
                    doc.createTextNode(String.valueOf(se)));
            details.appendChild(spse);

            Element zkje = doc.createElement("zkje");
            zkje.appendChild(
                    doc.createTextNode(""));
            details.appendChild(zkje);

            Element flbm = doc.createElement("flbm");
            flbm.appendChild(
                    doc.createTextNode("106050299"));
            details.appendChild(flbm);

            Element kcje = doc.createElement("kcje");
            kcje.appendChild(
                    doc.createTextNode(""));
            details.appendChild(kcje);

            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
//            StreamResult result =
//                    new StreamResult(new File("C:\\cars.xml"));
//            transformer.transform(source, result);
            // Output to console for testing
            StreamResult consoleResult =
                    new StreamResult(System.out);
            transformer.transform(source, consoleResult);

            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            String output = writer.getBuffer().toString();
            _logger.info("***output***"+output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保留两位数
     * @param value
     * @return
     */
    private double roundDouble(double value){
        BigDecimal bd = new BigDecimal(value);
        double v1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return v1;
    }

    private int getProductAmount(InvoiceinsBean bean){
        List<InvoiceinsItemBean> items = this.invoiceinsItemDAO.queryEntityBeansByFK(bean.getId());
        int amount = 0;
        for (InvoiceinsItemBean item: items){
            amount += item.getAmount();
        }
        return amount;
    }

    private int getFpsl(InvoiceinsBean bean){
        InvoiceBean invoiceBean = this.invoiceDAO.find(bean.getInvoiceId());
        int val = 0;
        if (invoiceBean!= null){
            if (this.equals(invoiceBean.getVal(), 2, 0.001)){
                val = 3;
            }else{
                val = (int)invoiceBean.getVal();
            }
        }
        return val;
    }

    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
//            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ));
            Document doc = builder.parse( new File("G:\\invoice.xml"));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            _logger.error(e);
        }
        return null;
    }

    public boolean equals(double x, double y) {
        return (Double.isNaN(x) && Double.isNaN(y)) || x == y;
    }

    public boolean equals(double x, double y, double eps) {
        return equals(x, y) || (Math.abs(y - x) <= eps);
    }

    /**
     * getQuerySelfBalanceCondition
     *
     *
     * @param request
     * @param user
     * @param count
     * @return
     */
    private ConditionParse getQueryPickupCondition(HttpServletRequest request, User user, boolean count)
    {
        Map<String, String> queryOutCondtionMap = CommonTools.saveParamersToMap(request);

        ConditionParse condtion = new ConditionParse();

        //condtion.addWhereStr();

        String batchId = request.getParameter("batchId");

        String shipment = request.getParameter("shipment");

        if (!StringTools.isNullOrNone(batchId))
        {
            condtion.addCondition("PackageBean.pickupId", "like", batchId);

            queryOutCondtionMap.put("batchId", batchId);
        }

        int shipping = -1;

        if (!StringTools.isNullOrNone(shipment))
        {
            shipping = MathTools.parseInt(shipment);

            condtion.addIntCondition("PackageBean.shipping", "=", shipping);

            queryOutCondtionMap.put("shipment", shipment);
        }

        String transport1 = request.getParameter("transport1");

        String transport2 = request.getParameter("transport2");

        if (!StringTools.isNullOrNone(transport1))
        {
            condtion.addIntCondition("PackageBean.transport1", "=", MathTools.parseInt(transport1));

            queryOutCondtionMap.put("transport1", transport1);
        }

        if (!StringTools.isNullOrNone(transport2))
        {
            condtion.addIntCondition("PackageBean.transport2", "=", MathTools.parseInt(transport2));

            queryOutCondtionMap.put("transport2", transport2);
        }

        String packageId = request.getParameter("packageId");

        if (!StringTools.isNullOrNone(packageId))
        {
            condtion.addCondition("PackageBean.id", "like", packageId.trim());

            queryOutCondtionMap.put("packageId", packageId.trim());
        }

        String receiver = request.getParameter("receiver");

        if (!StringTools.isNullOrNone(receiver))
        {
            condtion.addCondition("PackageBean.receiver", "like", receiver.trim());

            queryOutCondtionMap.put("receiver", receiver.trim());
        }

        String mobile = request.getParameter("mobile");

        if (!StringTools.isNullOrNone(mobile))
        {
            condtion.addCondition("PackageBean.mobile", "like", mobile.trim());

            queryOutCondtionMap.put("mobile", mobile.trim());
        }

        String location = request.getParameter("location");

        if (!StringTools.isNullOrNone(location))
        {
            condtion.addCondition("PackageBean.locationId", "like", location.trim());

            queryOutCondtionMap.put("location", location.trim());
        }

        String status = request.getParameter("currentStatus");

        if (!StringTools.isNullOrNone(status))
        {
            if (status.equals("4"))
                condtion.addCondition(" and PackageBean.status in (1,3,5)");
            else
                condtion.addIntCondition("PackageBean.status", "=", MathTools.parseInt(status));

            queryOutCondtionMap.put("currentStatus",status);
        }else
        {
            condtion.addCondition(" and PackageBean.status in (1,3,5)");

            queryOutCondtionMap.put("currentStatus","4");
        }

        // 事业部
        String industryName = request.getParameter("industryName");
        if (!StringTools.isNullOrNone(industryName))
        {
            condtion.addCondition("PackageBean.industryName", "=", industryName);

            queryOutCondtionMap.put("industryName",industryName);
        }

        // 销售单
        String outId = request.getParameter("outId");
        if (!StringTools.isNullOrNone(outId))
        {
            condtion.addCondition("PackageItemBean.outid", "like", outId);

            queryOutCondtionMap.put("outId",outId);
        }

        // 销售单 紧急
        String emergency = request.getParameter("emergency");
        if (!StringTools.isNullOrNone(emergency))
        {
            condtion.addIntCondition("PackageItemBean.emergency", "=", emergency);

            queryOutCondtionMap.put("emergency", emergency);
        }

        //客户名称
        String customerName = request.getParameter("customerName");
        _logger.info("****customerName condition****"+customerName);
        if (!StringTools.isNullOrNone(customerName))
        {
            if (count){
                condtion.addCondition("and exists (select CustomerBean.id from T_CENTER_CUSTOMER_MAIN CustomerBean where PackageBean.customerId = CustomerBean.id and CustomerBean.name like '%"+customerName+ "%')");

            } else{
                condtion.addCondition("CustomerBean.name", "like", "%"+customerName+"%");
            }

            queryOutCondtionMap.put("customerName", customerName);
        }

        //产品名称
        String productName = request.getParameter("productName");
        _logger.info("****productName condition****"+productName);
        if (!StringTools.isNullOrNone(productName))
        {
            condtion.addCondition("PackageItemBean.productName", "like", "%" + productName + "%");

            queryOutCondtionMap.put("productName", productName);
        }

        setDepotCondotionInOut(user, condtion);

        condtion.addCondition(" and PackageBean.pickupId !=''");
        //按揀配时间排序，最新的排最上面
        condtion.addCondition("order by PackageBean.pickupTime desc");

        request.getSession().setAttribute("ppmap", queryOutCondtionMap);

        return condtion;
    }

    /**
     * 2015/7/25 捡配前检查是否有同一收货人或电话
     * prePickup
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward prePickup(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException
    {
        User user = Helper.getUser(request);

        // separate by ~
        String packageIds = request.getParameter("packageIds");
        String map = request.getParameter("map");
        request.setAttribute("packageIds", packageIds);
        request.setAttribute("map", map);

        return mapping.findForward("prePickup");

    }


    /**
     * addPickup
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addPickup(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException
    {
        User user = Helper.getUser(request);

        // separate by ~
        String packageIds = request.getParameter("packageIds");
        String confirm = request.getParameter("confirm");

        String template = "add pickup with package IDs:%s confirm:%s";
        _logger.info(String.format(template, packageIds, confirm));

        AjaxResult ajax = new AjaxResult();

        try{
            if ("1".equals(confirm)){
                this.shipManager.addPickup(user, packageIds);
            } else{
                Map<String, Set<String>> result = this.shipManager.prePickup(user, packageIds);
                if (result == null) {
                    shipManager.addPickup(user, packageIds);
                    ajax.setSuccess("拣配成功");
                } else {
//                    request.setAttribute("packageIds", packageIds);
//                    request.setAttribute("map", result);
//                    request.setAttribute("content", this.toHtml(result));
//                    ajax.setError("同一收货人或电话的CK单尚未合并:"+ result);
                    String html = this.toHtml(result);
                    _logger.info("*****************同一收货人或电话的CK单尚未合并:"+html);
                    ajax.setError(html);
                }
            }
        }catch(MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("拣配出错:"+ e.getErrorContent());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    private String toHtml(Map<String, Set<String>> map) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<table width='100%' border='0' cellspacing='1'>");
        buffer.append("<tr align='center' class='content0'>");
        buffer.append("<td width='40%' align='center'>收货人或电话号码</td>");
        buffer.append("<td width='60%' align='center'>CK单</td></tr>");

        int index = 0;
        String cls = null;
        for (Iterator<Map.Entry<String, Set<String>>> it = map.entrySet().iterator();
             it.hasNext();)
        {
            Map.Entry<String, Set<String>> entry = it.next();
            Set<String> ckList = entry.getValue();
            for (String ck : ckList){
                buffer.append("<tr class='" + cls + "'>");
                buffer.append("<td width='40%' align='center'>" + entry.getKey() + "</td>");
                buffer.append("<td width='60%' align='center'>" + ck + "</td></tr>");
                index++ ;
            }
        }

        buffer.append("</table>");

        return StringTools.getLineString(buffer.toString());
    }

    /**
     * deletePackage
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deletePackage(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException
    {
        User user = Helper.getUser(request);

        AjaxResult ajax = new AjaxResult();

        // separate by ~
        String packageIds = request.getParameter("packageIds");

        try{
            shipManager.deletePackage(user, packageIds);

            ajax.setSuccess("撤销成功");
        }catch(MYException e) {
            _logger.warn(e, e);

            ajax.setError("撤销出错:" + e.getErrorContent());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 2015/2/26 撤销捡配CK单
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward cancelPickup(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException
    {
        User user = Helper.getUser(request);

        // separate by ~
        String packageIds = request.getParameter("packageIds");
        String pickupId = request.getParameter("pickupId");
        if (StringTools.isNullOrNone(pickupId)){
            _logger.info("****cancelPickup with packageIds****"+packageIds);
        } else {
            _logger.info("****cancelPickup with pickupId****"+pickupId);
            List<PackageBean> packages = this.packageDAO.queryEntityBeansByFK(pickupId);
            StringBuilder sb = new StringBuilder();
            for (PackageBean pack : packages){
                sb.append(pack.getId()+"~");
            }
            packageIds = sb.toString();
            _logger.info("cancelPickup packages***"+packageIds);
        }

        try{
            shipManager.cancelPackage(user, packageIds);
            request.setAttribute(KeyConstant.MESSAGE, "撤销成功");
        }catch(MYException e)
        {
            _logger.error(e, e);
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "撤销出错:" + e.getErrorContent());
        }

//        return mapping.findForward("queryPickup");
        return this.queryPickup(mapping, form, request, response);
    }

    /**
     * findPackage
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findPackage(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException
    {
        String packageId = request.getParameter("packageId");

        PackageVO vo = packageDAO.findVO(packageId);

        if (null == vo)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "出库单不存在");

            return mapping.findForward("error");
        }

        List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(packageId);

        vo.setItemList(itemList);

        request.setAttribute("bean", vo);

        return mapping.findForward("detailPackage");
    }

    private boolean checkBankPackages(List<PackageVO> packages){
        for (PackageVO vo : packages){
            if (vo.getIndustryName().indexOf("银行业务部") ==-1){
                return false;
            }
        }
        return true;
    }

    /**
     * findPickup
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findPickup(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException
    {
        CommonTools.saveParamers(request);

        String pickupId = request.getParameter("pickupId");

        String compose = request.getParameter("compose");

        if (StringTools.isNullOrNone(compose))
            compose = "1";

        // 根据拣配单(批次单) 生成一张批次出库单
        List<PackageVO> packageList = packageDAO.queryEntityVOsByFK(pickupId);

        StringBuilder sb = new StringBuilder();


        int pickupCount = packageList.size();

        //2015/1/25 检查是否银行业务部订单
//        boolean isBankOrder = this.checkBankPackages(packageList);
        //2015/12/8 see #136 不再根据银行分组
        boolean isBankOrder = false;
        if (isBankOrder){
            //<key,value> as <银行名称，List<PackageItemBean>>
            Map<String, List<PackageItemBean>> map = new HashMap<String, List<PackageItemBean>>();
            //<key,value> as <银行名称-productId,PackageItemBean>
            Map<String, PackageItemBean> map2 = new HashMap<String, PackageItemBean>();
            for (PackageVO each : packageList)
            {
                sb.append(each.getId()).append("<br>");

                List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(each.getId());

                // 根据产品分组: 1.判断是否为合成,如是,则要找出子产品;2.按照银行合并
                for (PackageItemBean eachItem : itemList)
                {
                    String customerId = eachItem.getCustomerId();
                    CustomerBean customer = customerMainDAO.find(customerId);
                    String name = customer.getName();
                    String bank = name.split("银行")[0]+"银行";

                    PackageItemBean itemBean = new PackageItemBean();

                    itemBean.setProductId(eachItem.getProductId());
                    itemBean.setProductName(eachItem.getProductName());
                    itemBean.setAmount(eachItem.getAmount());
                    //itemBean.setShowSubProductName(showSubProductName);

                    checkCompose(eachItem, itemBean, compose);

                    String key = bank+"-"+eachItem.getProductName();

                    if (map.containsKey(bank))
                    {
                        List<PackageItemBean> items = map.get(bank);

                        //2015/1/27 同一银行同一产品数量合并
                        if (map2.containsKey(key))
                        {
                            PackageItemBean item = map2.get(key);
                            item.setAmount(itemBean.getAmount() + item.getAmount());
                        } else{
                            items.add(itemBean);
                            map2.put(key,itemBean);
                        }
                    }else{
                        List<PackageItemBean> items = new ArrayList<PackageItemBean>();
                        items.add(itemBean);
                        map.put(bank,items);
                        map2.put(key,itemBean);
                    }
                }
            }

            //2015/1/27 按照名称排序
            for (List<PackageItemBean> items: map.values()){
                Collections.sort(items, new Comparator(){
                    @Override
                    public int compare(Object o1, Object o2) {
                        PackageItemBean i1 = (PackageItemBean)o1;
                        PackageItemBean i2 = (PackageItemBean)o2;
                        return i1.getProductName().compareTo(i2.getProductName());
                    }
                });
            }


            PackageVO batchVO = new PackageVO();

            batchVO.setId(sb.toString());
            batchVO.setPickupId(pickupId);
            batchVO.setRepTime(TimeTools.now_short());

//            List<PackageItemBean> lastList = new ArrayList<PackageItemBean>();

/*            for (Entry<String, PackageItemBean> entry : map.entrySet())
            {
                lastList.add(entry.getValue());
            }*/

//            batchVO.setItemList(lastList);

            // key:以批次号做为key ?
            request.setAttribute("bean", batchVO);
            request.setAttribute("map", map);

            request.setAttribute("year", TimeTools.now("yyyy"));
            request.setAttribute("month", TimeTools.now("MM"));
            request.setAttribute("day", TimeTools.now("dd"));

            request.setAttribute("index_pos", 0);

            request.setAttribute("pickupCount", pickupCount);

            request.setAttribute("compose", compose);

            return mapping.findForward("printBankPickup");
        } else{
            //<key,value> as <productId,PackageItemBean>
            Map<String, PackageItemBean> map = new HashMap<String, PackageItemBean>();
            for (PackageVO each : packageList)
            {
                sb.append(each.getId()).append("<br>");

                List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(each.getId());

                // 根据产品分组: 1.判断是否为合成,如是,则要找出子产品;2.数量合并
                for (PackageItemBean eachItem : itemList)
                {
                    if (map.containsKey(eachItem.getProductId()))
                    {
                        PackageItemBean itemBean = map.get(eachItem.getProductId());

                        itemBean.setAmount(itemBean.getAmount() + eachItem.getAmount());
                    }else{
                        PackageItemBean itemBean = new PackageItemBean();

                        itemBean.setProductId(eachItem.getProductId());
                        itemBean.setProductName(eachItem.getProductName());
                        itemBean.setAmount(eachItem.getAmount());
                        //itemBean.setShowSubProductName(showSubProductName);

                        checkCompose(eachItem, itemBean, compose);

                        map.put(eachItem.getProductId(), itemBean);
                    }
                }
            }

            PackageVO batchVO = new PackageVO();

            batchVO.setId(sb.toString());
            batchVO.setPickupId(pickupId);
            batchVO.setRepTime(TimeTools.now_short());

            List<PackageItemBean> lastList = new ArrayList<PackageItemBean>();

            for (Entry<String, PackageItemBean> entry : map.entrySet())
            {
                lastList.add(entry.getValue());
            }

            //2015/1/14 按照名称排序
            Collections.sort(lastList, new Comparator(){
                @Override
                public int compare(Object o1, Object o2) {
                    PackageItemBean i1 = (PackageItemBean)o1;
                    PackageItemBean i2 = (PackageItemBean)o2;
                    return i1.getProductName().compareTo(i2.getProductName());
                }
            });
            batchVO.setItemList(lastList);

            //2016/10/12 #328 检查临时发票号码
            for (PackageItemBean item : lastList){
                String productName = item.getProductName();
                if (productName!= null && productName.startsWith("发票号：XN")){
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, item.getPackageId()+"商品名中存在虚拟发票号:"+productName);

                    return mapping.findForward("error");
                }
            }

            // key:以批次号做为key ?
            request.setAttribute("bean", batchVO);

            request.setAttribute("year", TimeTools.now("yyyy"));
            request.setAttribute("month", TimeTools.now("MM"));
            request.setAttribute("day", TimeTools.now("dd"));

            request.setAttribute("index_pos", 0);

            request.setAttribute("pickupCount", pickupCount);

            request.setAttribute("compose", compose);

            this.generateQRCode(pickupId);
            request.setAttribute("qrcode", this.getQrcodeUrl(pickupId));
            return mapping.findForward("printPickup");
        }

    }

    private void checkCompose(PackageItemBean eachItem, PackageItemBean itemBean, String com)
    {
        if ("1".equals(com))
        {
            ProductBean product = productDAO.find(eachItem.getProductId());

            if (null != product){
                if (product.getCtype() == ProductConstant.CTYPE_YES)
                {
                    ComposeProductBean compose = composeProductDAO.queryLatestByProduct(eachItem.getProductId());

                    if (null != compose)
                    {
                        List<ComposeItemVO> citemList = composeItemDAO.queryEntityVOsByFK(compose.getId());

                        StringBuilder sb2 = new StringBuilder();

                        for (ComposeItemVO eachc : citemList)
                        {
                            sb2.append(eachc.getProductName()).append("<br>");
                        }

                        itemBean.setShowSubProductName(sb2.toString());
                    }
                }
            }
        }
    }


    /**
     * findNextPackage
     *
     * 根据批次号(拣配号)及index获取包package
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findNextPackage(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
            throws ServletException
    {
        CommonTools.saveParamers(request);

        String pickupId = request.getParameter("pickupId");

        String sindex_pos = request.getParameter("index_pos");

        String printMode = request.getParameter("printMode");

        // 0 连打 1 单打
        String printSmode = request.getParameter("printSmode");

        if (StringTools.isNullOrNone(printSmode))
            printSmode = "";

        String compose = request.getParameter("compose");

        if (StringTools.isNullOrNone(compose))
            compose = "1";

        int index_pos = 0;

        if (!StringTools.isNullOrNone(sindex_pos))
        {
            index_pos = MathTools.parseInt(sindex_pos);
        }

        index_pos += 1;

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("PackageBean.pickupId", "=", pickupId);
        condtion.addIntCondition("PackageBean.index_pos", "=", index_pos);

        _logger.info("**********findNextPackage with pickupId:"+pickupId+" index_pos:"+index_pos);

        List<PackageVO> packageList = packageDAO.queryVOsByCondition(condtion);

        _logger.info("****findNextPackage packageList size***"+packageList.size());

        if (ListTools.isEmptyOrNull(packageList) || packageList.size() > 1)
        {
            // 只有连打时,才跳转到回执单打印
            if (printMode.equals("0") && printSmode.equals("0"))
            {
                CommonTools.removeParamers(request);

                request.setAttribute("pickupId2", pickupId);

                request.setAttribute("compose1", compose);

                //连打模式
                request.setAttribute("batchPrint", "0");

                //连打模式
                request.setAttribute("printMode", "0");
                request.setAttribute("printSmode", "0");
//                request.getSession().setAttribute("printMode", "0");
//                request.getSession().setAttribute("printSmode", "0");

                _logger.info("****redirect to findOutForReceipt print**********");
                return findOutForReceipt(mapping, form, request, response);
            }else
            {
                _logger.info("**** print finished*****");
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

                return mapping.findForward("error");
            }
        }

        PackageVO vo = packageList.get(0);

        Map<String, PackageItemBean> map = new HashMap<String, PackageItemBean>();

        Map<String, PackageWrap> map1 = new HashMap<String, PackageWrap>();

        List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(vo.getId());

        // 根据产品分组: 1.判断是否为合成,如是,则要找出子产品;2.数量合并
        for (PackageItemBean eachItem : itemList)
        {
            //2016/10/12 #328 检查临时发票号码
            for (PackageItemBean item : itemList){
                String productName = item.getProductName();
                if (productName!= null && productName.startsWith("发票号：XN")){
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, item.getPackageId()+"商品名中存在虚拟发票号:"+productName);

                    return mapping.findForward("error");
                }
            }

            if (map.containsKey(eachItem.getProductId()))
            {
                PackageItemBean itemBean = map.get(eachItem.getProductId());

                itemBean.setAmount(itemBean.getAmount() + eachItem.getAmount());
            }else{
                PackageItemBean itemBean = new PackageItemBean();

                itemBean.setProductId(eachItem.getProductId());
                itemBean.setProductName(eachItem.getProductName());
                itemBean.setAmount(eachItem.getAmount());
                //itemBean.setShowSubProductName(showSubProductName);

                checkCompose(eachItem, itemBean, compose);

                map.put(eachItem.getProductId(), itemBean);
            }

            if (!map1.containsKey(eachItem.getOutId()))
            {
                PackageWrap wrap = new PackageWrap();

                wrap.setOutId(eachItem.getOutId());

                //#315
                OutBean out = this.outDAO.find(eachItem.getOutId());
                if (out!= null){
                    if (out.getType() == OutConstant.OUT_TYPE_INBILL && out.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT){
                        wrap.setDescription(out.getDescription());
                    } else {
                        wrap.setDescription(out.getSwbz());
                    }
                }

                if (StringTools.isNullOrNone(wrap.getDescription())){
                    wrap.setDescription(eachItem.getDescription());
                }

                map1.put(eachItem.getOutId(), wrap);
            }
        }

        vo.setRepTime(TimeTools.now_short());

        List<PackageItemBean> lastList = new ArrayList<PackageItemBean>();

        for (Entry<String, PackageItemBean> entry : map.entrySet())
        {
            lastList.add(entry.getValue());
        }

        vo.setItemList(lastList);

        List<PackageWrap> wrapList = new ArrayList<PackageWrap>();

        for (Entry<String, PackageWrap> entry : map1.entrySet())
        {
            wrapList.add(entry.getValue());
        }

        vo.setItemList(lastList);

        vo.setWrapList(wrapList);

        request.setAttribute("bean", vo);

        request.setAttribute("year", TimeTools.now("yyyy"));
        request.setAttribute("month", TimeTools.now("MM"));
        request.setAttribute("day", TimeTools.now("dd"));

        request.setAttribute("index_pos", index_pos);

        request.setAttribute("compose", compose);

        this.generateQRCode(vo.getId());
        request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
        return mapping.findForward("printPackage");
    }

    /**
     * #262 统一回执单打印格式
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findOutForReceipt(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException {
        CommonTools.saveParamers(request);

        String compose = RequestTools.getValueFromRequest(request, "compose");

        String compose1 = RequestTools.getValueFromRequest(request, "compose1");

        if (!StringTools.isNullOrNone(compose1))
            compose = compose1;

        String pickupId = RequestTools.getValueFromRequest(request, "pickupId");

        String pickupId2 = RequestTools.getValueFromRequest(request, "pickupId2");

        // 第一次跳转
        if (!StringTools.isNullOrNone(pickupId2)) {
            pickupId = pickupId2;
        }

        String sindex_pos = RequestTools.getValueFromRequest(request, "index_pos");

        int index_pos = 0;

        if (!StringTools.isNullOrNone(sindex_pos)) {
            index_pos = MathTools.parseInt(sindex_pos);
        }

        String packageId = RequestTools.getValueFromRequest(request, "packageId");

        String printMode = (String) request.getAttribute("printMode");
        String printSmode = (String) request.getAttribute("printSmode");

        String batchPrint = RequestTools.getValueFromRequest(request, "batchPrint");
        String print2 = (String) request.getSession().getAttribute("printMode");

        String msg1 = "***batchPrint***" + batchPrint + "***print2***" + print2 + "***pickupId***" + pickupId + "***packageId***" + packageId + "***index_pos***" + index_pos + "***printMode***" + printMode + "***printSmode***" + printSmode;
        _logger.info(msg1);
        //2015/3/25 批量打印标志
        request.setAttribute("batchPrint", batchPrint);

        //可去掉？很奇怪在页面上读不到printMode参数
        request.setAttribute("printMode", printMode);
        request.setAttribute("printSmode", printSmode);

        // 第一次打印时，找出第一个出库单，一个出库单对应多个客户 1:n
        if (index_pos == 0) {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            condtion.addCondition("PackageBean.pickupId", "=", pickupId);
            condtion.addIntCondition("PackageBean.index_pos", "=", 1);

            List<PackageVO> packageList = packageDAO.queryVOsByCondition(condtion);

            if (ListTools.isEmptyOrNull(packageList) || packageList.size() > 1) {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

                return mapping.findForward("error");
            }

            packageId = packageList.get(0).getId();

            index_pos = 1;
        }
        String subindex_pos = request.getParameter("subindex_pos");

        int subindexpos = 0;

        if (!StringTools.isNullOrNone(subindex_pos)) {
            subindexpos = MathTools.parseInt(subindex_pos);
        }

        subindexpos += 1;

        String customerId = "";
        String customerName = "";

        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("PackageVSCustomerBean.packageId", "=", packageId);
        con.addIntCondition("PackageVSCustomerBean.indexPos", "=", subindexpos);
        _logger.info("=======con========" + con.toString());
        List<PackageVSCustomerBean> vsList = packageVSCustomerDAO.queryEntityBeansByCondition(con);

        if (!ListTools.isEmptyOrNull(vsList)) {
            customerId = vsList.get(0).getCustomerId();
            customerName = vsList.get(0).getCustomerName();

            request.setAttribute("subindex_pos", subindexpos);
            String msg2 = "**********vsList size****" + vsList.size();
            _logger.info(msg2);
        } else {
            // 更新状态
            try {
                shipManager.updatePrintStatus(pickupId, index_pos);
                _logger.info(pickupId + ":" + index_pos + " print finished***");
            } catch (MYException e) {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                return mapping.findForward("error");
            }

            index_pos += 1;
        }

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("PackageBean.pickupId", "=", pickupId);
        condtion.addIntCondition("PackageBean.index_pos", "=", index_pos);

        List<PackageVO> packageList = packageDAO.queryVOsByCondition(condtion);

        if (ListTools.isEmptyOrNull(packageList) || packageList.size() > 1) {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

            return mapping.findForward("error");
        }

        // 取出包
        PackageVO vo = packageList.get(0);

        vo.setRepTime(TimeTools.now_short());

        vo.setCustomerId(customerId);
        vo.setCustomerName(customerName);

        if (StringTools.isNullOrNone(customerId)) {
            ConditionParse con1 = new ConditionParse();

            con1.addWhereStr();

            con1.addCondition("PackageVSCustomerBean.packageId", "=", vo.getId());
            con1.addIntCondition("PackageVSCustomerBean.indexPos", "=", 1);
            List<PackageVSCustomerBean> vsList1 = packageVSCustomerDAO.queryEntityBeansByCondition(con1);

            if (!ListTools.isEmptyOrNull(vsList1)) {
                customerId = vsList1.get(0).getCustomerId();
                customerName = vsList1.get(0).getCustomerName();

                vo.setCustomerId(customerId);
                vo.setCustomerName(customerName);

                request.setAttribute("subindex_pos", 1);
                String msg3 = "**********vsList1 size****" + vsList1.size() + "***customerId***" + customerId;
                _logger.info(msg3);

            }
        }

        //2016/12/23 回执单把黄河银行四个字从客户名称里面去掉
//        if (vo.getCustomerName().contains("黄河银行")){
//            vo.setCustomerName(vo.getCustomerName().replace("黄河银行",""));
//        }
        if (vo.getCustomerName().contains("黄河银行")){
            request.setAttribute("customerName", vo.getCustomerName().replace("黄河银行",""));
        } else{
            request.setAttribute("customerName", vo.getCustomerName());
        }


        List<PackageItemBean> itemList = packageItemDAO.
                queryEntityBeansByCondition(" where PackageItemBean.packageId = ? order by PackageItemBean.productName", vo.getId()); //  .queryEntityBeansByFK(vo.getId());
        String template = "CK:%s customerID:%s customer:%s item size:%d";
        _logger.info(String.format(template, vo.getId(), vo.getCustomerId(), vo.getCustomerName(), itemList.size()));

        //2016/10/12 #328 检查临时发票号码
        for (PackageItemBean item : itemList){
            String productName = item.getProductName();
            if (productName!= null && productName.startsWith("发票号：XN")){
                request.setAttribute(KeyConstant.ERROR_MESSAGE, item.getPackageId()+"商品名中存在虚拟发票号:"+productName);

                return mapping.findForward("error");
            }
        }
        request.setAttribute("bean", vo);

        request.setAttribute("pickupId", pickupId);

        request.setAttribute("index_pos", index_pos);

        request.setAttribute("compose", compose);

        request.setAttribute("year", TimeTools.now("yyyy"));
        request.setAttribute("month", TimeTools.now("MM"));
        request.setAttribute("day", TimeTools.now("dd"));

        int totalAmount = 0;
        double total = 0.0d;

        //中原银行客户
        if (vo.getCustomerName().indexOf("中原银行") != -1) {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "贵金属交接单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)) {
                _logger.info("****allPackages size****" + allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos) {
                    // 更新状态
                    try {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId + ":" + index_pos + " print finished***");
                    } catch (MYException e) {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try {
                String msg5 = "**********before prepareForZyPrint****";
                _logger.info(msg5);
                this.prepareForZyPrint(request, vo, itemList, compose);
                String msg6 = "**********after prepareForZyPrint****";
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****printZyReceipt exception***", e);
            }

            return mapping.findForward("printZyReceipt");
        }
        //宁波银行客户
        else if (vo.getCustomerName().indexOf("宁波银行") != -1) {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "发货确认单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)) {
                _logger.info("****allPackages size****" + allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos) {
                    // 更新状态
                    try {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId + ":" + index_pos + " print finished***");
                    } catch (MYException e) {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try {
                String msg5 = "**********before prepareForNbPrint****";
                _logger.info(msg5);
                this.prepareForNbPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForNbPrint****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****prepareForNbPrint exception***", e);
            }

            return mapping.findForward("printNbReceipt");
        }
        //紫金银行客户
        else if (vo.getCustomerName().indexOf("紫金") != -1) {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "永银文化公司贵金属实物交接清单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)) {
                _logger.info("****allPackages size****" + allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos) {
                    // 更新状态
                    try {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId + ":" + index_pos + " print finished***");
                    } catch (MYException e) {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try {
                String msg5 = "**********before prepareForZjPrint****";
                _logger.info(msg5);
                this.prepareForZjPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForZjPrint****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****prepareForZjPrint exception***", e);
            }

            return mapping.findForward("printZjReceipt");
        } else{
            //其他所有银行
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "永银文化——发货清单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)) {
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos) {
                    // 更新状态
                    try {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                    } catch (MYException e) {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try {
                String msg5 = "**********before prepareForUnified****";
                _logger.info(msg5);
                prepareForUnified(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForUnified****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (vo.getCustomerName().indexOf("吉林银行") != -1){
                return mapping.findForward("printJlReceipt");
            } else{
                return mapping.findForward("printUnifiedReceipt");
            }
        }
    }

    /**
     * #413 单个CK单打印回执单
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findOutForSingleReceipt(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException {
        CommonTools.saveParamers(request);

        String compose = RequestTools.getValueFromRequest(request, "compose");

        String compose1 = RequestTools.getValueFromRequest(request, "compose1");

        if (!StringTools.isNullOrNone(compose1))
            compose = compose1;


        String sindex_pos = RequestTools.getValueFromRequest(request, "index_pos");

        int index_pos = 0;

        if (!StringTools.isNullOrNone(sindex_pos)) {
            index_pos = MathTools.parseInt(sindex_pos);
        }

        String packageId = RequestTools.getValueFromRequest(request, "packageId");

        String printMode = (String) request.getAttribute("printMode");
        String printSmode = (String) request.getAttribute("printSmode");

        String batchPrint = RequestTools.getValueFromRequest(request, "batchPrint");
        String print2 = (String) request.getSession().getAttribute("printMode");

        String msg1 = "***batchPrint***" + batchPrint + "***print2***" + print2 +  "***packageId***" + packageId + "***index_pos***" + index_pos + "***printMode***" + printMode + "***printSmode***" + printSmode;
        _logger.info(msg1);
        //2015/3/25 批量打印标志
        request.setAttribute("batchPrint", batchPrint);

        //可去掉？很奇怪在页面上读不到printMode参数
        request.setAttribute("printMode", printMode);
        request.setAttribute("printSmode", printSmode);

        String subindex_pos = request.getParameter("subindex_pos");

        int subindexpos = 0;

        if (!StringTools.isNullOrNone(subindex_pos)) {
            subindexpos = MathTools.parseInt(subindex_pos);
        }

        subindexpos += 1;

        String customerId = "";
        String customerName = "";

        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("PackageVSCustomerBean.packageId", "=", packageId);
        _logger.info("=======con========" + con.toString());
        List<PackageVSCustomerBean> vsList = packageVSCustomerDAO.queryEntityBeansByCondition(con);

        if (!ListTools.isEmptyOrNull(vsList)) {
            customerId = vsList.get(0).getCustomerId();
            customerName = vsList.get(0).getCustomerName();

            request.setAttribute("subindex_pos", subindexpos);
            String msg2 = "**********vsList size****" + vsList.size();
            _logger.info(msg2);
        }


        // 取出包
        PackageVO vo = packageDAO.findVO(packageId);

        vo.setRepTime(TimeTools.now_short());

        vo.setCustomerId(customerId);
        vo.setCustomerName(customerName);

        if (vo.getCustomerName().contains("黄河银行")){
            request.setAttribute("customerName", vo.getCustomerName().replace("黄河银行",""));
        } else{
            request.setAttribute("customerName", vo.getCustomerName());
        }


        List<PackageItemBean> itemList = packageItemDAO.
                queryEntityBeansByCondition(" where PackageItemBean.packageId = ? order by PackageItemBean.productName", vo.getId()); //  .queryEntityBeansByFK(vo.getId());
        String template = "CK:%s customerID:%s customer:%s item size:%d";
        _logger.info(String.format(template, vo.getId(), vo.getCustomerId(), vo.getCustomerName(), itemList.size()));

        //2016/10/12 #328 检查临时发票号码
        for (PackageItemBean item : itemList){
            String productName = item.getProductName();
            if (productName!= null && productName.startsWith("发票号：XN")){
                request.setAttribute(KeyConstant.ERROR_MESSAGE, item.getPackageId()+"商品名中存在虚拟发票号:"+productName);

                return mapping.findForward("error");
            }
        }

        request.setAttribute("bean", vo);

        request.setAttribute("index_pos", index_pos);

        request.setAttribute("compose", compose);

        request.setAttribute("year", TimeTools.now("yyyy"));
        request.setAttribute("month", TimeTools.now("MM"));
        request.setAttribute("day", TimeTools.now("dd"));

        int totalAmount = 0;
        double total = 0.0d;

        //中原银行客户
        if (vo.getCustomerName().indexOf("中原银行") != -1) {
            request.setAttribute("packageId", vo.getId());
            request.setAttribute("title", "贵金属交接单");

            try {
                String msg5 = "**********before prepareForZyPrint****";
                _logger.info(msg5);
                this.prepareForZyPrint(request, vo, itemList, compose);
                String msg6 = "**********after prepareForZyPrint****";
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****printZyReceipt exception***", e);
            }

            return mapping.findForward("printZyReceipt");
        }
        //宁波银行客户
        else if (vo.getCustomerName().indexOf("宁波银行") != -1) {
            request.setAttribute("packageId", vo.getId());
            request.setAttribute("title", "发货确认单");

            try {
                String msg5 = "**********before prepareForNbPrint****";
                _logger.info(msg5);
                this.prepareForNbPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForNbPrint****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****prepareForNbPrint exception***", e);
            }

            return mapping.findForward("printNbReceipt");
        }
        //紫金银行客户
        else if (vo.getCustomerName().indexOf("紫金") != -1) {
            request.setAttribute("packageId", vo.getId());
            request.setAttribute("title", "永银文化公司贵金属实物交接清单");

            try {
                String msg5 = "**********before prepareForZjPrint****";
                _logger.info(msg5);
                this.prepareForZjPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForZjPrint****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.error("****prepareForZjPrint exception***", e);
            }

            return mapping.findForward("printZjReceipt");
        } else{
            //其他所有银行
            request.setAttribute("packageId", vo.getId());
            request.setAttribute("title", "永银文化——发货清单");

            try {
                String msg5 = "**********before prepareForUnified****";
                _logger.info(msg5);
                prepareForUnified(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForUnified****";
                _logger.info(msg6);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (vo.getCustomerName().indexOf("吉林银行") != -1){
                return mapping.findForward("printJlReceipt");
            } else{
                return mapping.findForward("printUnifiedReceipt");
            }
        }
    }

    /**
     * 显示要打印的回执单
     * 邮政、浦发、中信
     * 一个包(package)一个打印单
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @Deprecated
    public ActionForward findOutForReceipt2(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException
    {
        CommonTools.saveParamers(request);

        String compose =  RequestTools.getValueFromRequest(request, "compose");

        String compose1 =  RequestTools.getValueFromRequest(request, "compose1");

        if (!StringTools.isNullOrNone(compose1))
            compose = compose1;

        String pickupId =  RequestTools.getValueFromRequest(request, "pickupId");

        String pickupId2 =  RequestTools.getValueFromRequest(request, "pickupId2");

        // 第一次跳转
        if (!StringTools.isNullOrNone(pickupId2))
        {
            pickupId = pickupId2;
        }

        String sindex_pos =  RequestTools.getValueFromRequest(request, "index_pos");

        int index_pos = 0;

        if (!StringTools.isNullOrNone(sindex_pos))
        {
            index_pos = MathTools.parseInt(sindex_pos);
        }

        String packageId =  RequestTools.getValueFromRequest(request, "packageId");

        String printMode =  (String)request.getAttribute("printMode");
        String printSmode =  (String)request.getAttribute("printSmode");

        String batchPrint =  RequestTools.getValueFromRequest(request, "batchPrint");
        String print2 = (String)request.getSession().getAttribute("printMode");

        String msg1 = "***batchPrint***"+batchPrint+"***print2***"+print2+"***pickupId***"+pickupId+"***packageId***"+packageId+"***index_pos***"+index_pos+"***printMode***"+printMode+"***printSmode***"+printSmode;
        _logger.info(msg1);
        //2015/3/25 批量打印标志
        request.setAttribute("batchPrint", batchPrint);

        //可去掉？很奇怪在页面上读不到printMode参数
        request.setAttribute("printMode", printMode);
        request.setAttribute("printSmode", printSmode);

        // 第一次打印时，找出第一个出库单，一个出库单对应多个客户 1:n
        if (index_pos == 0)
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            condtion.addCondition("PackageBean.pickupId", "=", pickupId);
            condtion.addIntCondition("PackageBean.index_pos", "=", 1);

            List<PackageVO> packageList = packageDAO.queryVOsByCondition(condtion);

            if (ListTools.isEmptyOrNull(packageList) || packageList.size() > 1)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

                return mapping.findForward("error");
            }

            packageId = packageList.get(0).getId();

            index_pos = 1;
        }
        String subindex_pos =  request.getParameter("subindex_pos");

        int subindexpos = 0;

        if (!StringTools.isNullOrNone(subindex_pos))
        {
            subindexpos = MathTools.parseInt(subindex_pos);
        }

        subindexpos += 1;

        String customerId = "";
        String customerName = "";

        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("PackageVSCustomerBean.packageId", "=", packageId);
        con.addIntCondition("PackageVSCustomerBean.indexPos", "=", subindexpos);
        _logger.info("=======con========" + con.toString());
        List<PackageVSCustomerBean> vsList = packageVSCustomerDAO.queryEntityBeansByCondition(con);

        if (!ListTools.isEmptyOrNull(vsList))
        {
            customerId = vsList.get(0).getCustomerId();
            customerName = vsList.get(0).getCustomerName();

            request.setAttribute("subindex_pos", subindexpos);
            String msg2 = "**********vsList size****"+vsList.size();
            _logger.info(msg2);
        }else
        {
            // 更新状态
            try
            {
                shipManager.updatePrintStatus(pickupId, index_pos);
                _logger.info(pickupId+":"+index_pos+" print finished***");
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                return mapping.findForward("error");
            }

            index_pos += 1;
        }

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("PackageBean.pickupId", "=", pickupId);
        condtion.addIntCondition("PackageBean.index_pos", "=", index_pos);

        List<PackageVO> packageList = packageDAO.queryVOsByCondition(condtion);

        if (ListTools.isEmptyOrNull(packageList) || packageList.size() > 1)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

            return mapping.findForward("error");
        }

        // 取出包
        PackageVO vo = packageList.get(0);

        vo.setRepTime(TimeTools.now_short());

        vo.setCustomerId(customerId);
        vo.setCustomerName(customerName);

        if (StringTools.isNullOrNone(customerId))
        {
            ConditionParse con1 = new ConditionParse();

            con1.addWhereStr();

            con1.addCondition("PackageVSCustomerBean.packageId", "=", vo.getId());
            con1.addIntCondition("PackageVSCustomerBean.indexPos", "=", 1);
            List<PackageVSCustomerBean> vsList1 = packageVSCustomerDAO.queryEntityBeansByCondition(con1);

            if (!ListTools.isEmptyOrNull(vsList1))
            {
                customerId = vsList1.get(0).getCustomerId();
                customerName = vsList1.get(0).getCustomerName();

                vo.setCustomerId(customerId);
                vo.setCustomerName(customerName);

                request.setAttribute("subindex_pos", 1);
                String msg3 = "**********vsList1 size****"+vsList1.size()+"***customerId***"+customerId;
                _logger.info(msg3);

            }
        }

        List<PackageItemBean> itemList = packageItemDAO.
                queryEntityBeansByCondition(" where PackageItemBean.packageId = ? order by PackageItemBean.productName", vo.getId()); //  .queryEntityBeansByFK(vo.getId());
        String template = "CK:%s customerID:%s customer:%s item size:%d";
        _logger.info(String.format(template, vo.getId(), vo.getCustomerId(), vo.getCustomerName(), itemList.size()));
        request.setAttribute("bean", vo);

        request.setAttribute("pickupId", pickupId);

        request.setAttribute("index_pos", index_pos);

        request.setAttribute("compose", compose);

        request.setAttribute("year", TimeTools.now("yyyy"));
        request.setAttribute("month", TimeTools.now("MM"));
        request.setAttribute("day", TimeTools.now("dd"));

        int totalAmount = 0;
        double total = 0.0d;

        if (vo.getIndustryName().indexOf("邮政") != -1)
        {
            request.setAttribute("packageId", vo.getId());

            List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

            Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

            for (PackageItemBean each : itemList)
            {
                if (!each.getCustomerId().equals(vo.getCustomerId()))
                {
                    continue;
                }

                String key = each.getProductId()+ "~" + each.getPrice();

                if (!map1.containsKey(key))
                {
                    checkCompose(each, each, compose);

                    map1.put(each.getProductId(), each);
                }else{
                    PackageItemBean itemBean = map1.get(key);

                    itemBean.setAmount(itemBean.getAmount() + each.getAmount());
                    itemBean.setValue(itemBean.getValue() + each.getValue());

                    itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());
                }

                total += each.getValue();
            }

            for(Entry<String, PackageItemBean> each : map1.entrySet())
            {
                PackageItemBean item = each.getValue();
                String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
                if (!StringTools.isNullOrNone(productName)){
                    item.setProductName(productName);
                }
                itemList1.add(item);
            }

            vo.setItemList(itemList1);

            request.setAttribute("total", total);
            this.generateQRCode(vo.getId());
            request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));

            return mapping.findForward("printPostReceipt");
        }else if (vo.getCustomerName().indexOf("中信银行") != -1 || vo.getCustomerName().indexOf("招商银行") != -1)
        {
             request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "永银文化创意产业发展有限责任公司产品发货清单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)){
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos){
                    // 更新状态
                    try
                    {
                        shipManager.updatePrintStatus(pickupId, index_pos);
//                        _logger.info(pickupId+":"+index_pos+" print finished***");
                    }
                    catch (MYException e)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try{
                String msg5 = "**********before prepareForBankPrint****";
                _logger.info(msg5);
                prepareForBankPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForBankPrint****";
                _logger.info(msg6);
            }catch(Exception e){
                e.printStackTrace();
            }

            return mapping.findForward("printBankReceipt");
        }else if (vo.getCustomerName().indexOf("浦发银行") != -1)
        {
            request.setAttribute("packageId", vo.getId());

            //2015/12/14 把浦发银行的回执单抬头改成：永银文化贵金属产品发货清单
            request.setAttribute("title", "永银文化贵金属产品发货清单");

            Map<String,String> aToOutMap = new HashMap<String,String>();
            for (Iterator<PackageItemBean> iterator = itemList.iterator(); iterator.hasNext();)
            {
                PackageItemBean each = iterator.next();

                if (!each.getCustomerId().equals(vo.getCustomerId()))
                {
                    iterator.remove();

                    continue;
                }

                String outId = each.getOutId();
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    each.setRefId(outiList.get(0).getCiticNo());
                    each.setOutTime(outiList.get(0).getCiticOrderDate());
                    aToOutMap.put(outId, each.getRefId());
                } else if (outId.startsWith("A")){
                    each.setRefId(this.getCiticNoFromA(aToOutMap,outId));
                }

                if (each.getDescription().indexOf("赠品") != -1)
                {
                    each.setDescription("赠品");
                }else
                {
                    each.setDescription(each.getPrintText());
                }

                totalAmount += each.getAmount();

                String productName = this.convertProductNameForZj(each, this.getCustomerName(vo.getCustomerName()));
                if (!StringTools.isNullOrNone(productName)){
                    each.setProductName(productName);
                }
            }

            vo.setItemList(itemList);

            //2015/12/24 取浦发银行回执单商务联系人及电话
            if (!ListTools.isEmptyOrNull(itemList)){
                _logger.info("******itemList size****"+itemList.size());
                PackageItemBean first = itemList.get(0);
                String outId = first.getOutId();
                String stafferName = "刘倩（华东） 凌燕（华中） 周晓辉（华南） 岳元梅（华北、西部）";
                String phone = "18994031103 13925070986  18680580807  18980089190";
                _logger.info(first+"******first****"+outId);
                if (StringTools.isNullOrNone(outId)){
                    _logger.warn("****Empty OutId***********"+first.getId());
                } else if(outId.startsWith("A")){
                    InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                    if (bean!= null){
                        String refIds = bean.getRefIds();
                        _logger.info(outId+"*****refIds found********"+refIds);
                        if (!StringTools.isNullOrNone(refIds)){
                            String[] temp = refIds.split(";");
                            String refOutId = null;
                            for (String out: temp){
                                if (out.startsWith("SO")){
                                    refOutId = out;
                                    break;
                                }
                            }
                            String[] result2 = this.getStafferNameAndPhone(refOutId);
                            if (result2.length>=2){
                                stafferName = result2[0];
                                phone = result2[1];
                            }
                        }
                    }
                } else {
                    String[] result = this.getStafferNameAndPhone(outId);
                    if (result.length>=2){
                        stafferName = result[0];
                        phone = result[1];
                    }
                }
                _logger.info("***stafferName****"+stafferName+"****phone****"+phone);
                request.setAttribute("stafferName", stafferName);
                request.setAttribute("phone",phone);
            }

            request.setAttribute("total", totalAmount);
            this.generateQRCode(vo.getId());
            request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));

            return mapping.findForward("printPufaReceipt");
        }
        //2015/11/13 新增中原银行客户
        else if (vo.getCustomerName().indexOf("中原银行") != -1)
        {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "贵金属交接单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)){
                _logger.info("****allPackages size****"+allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos){
                    // 更新状态
                    try
                    {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId+":"+index_pos+" print finished***");
                    }
                    catch (MYException e)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try{
                String msg5 = "**********before prepareForZyPrint****";
                _logger.info(msg5);
                this.prepareForZyPrint(request, vo, itemList, compose);
                String msg6 = "**********after prepareForZyPrint****";
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                _logger.info(msg6);
            }catch(Exception e){
                e.printStackTrace();
                _logger.error("****printZyReceipt exception***",e);
            }

            return mapping.findForward("printZyReceipt");
        }
        //2015/11/20 新增宁波银行客户
        else if (vo.getCustomerName().indexOf("宁波银行") != -1)
        {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "发货确认单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)){
                _logger.info("****allPackages size****"+allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos){
                    // 更新状态
                    try
                    {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId+":"+index_pos+" print finished***");
                    }
                    catch (MYException e)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try{
                String msg5 = "**********before prepareForNbPrint****";
                _logger.info(msg5);
                this.prepareForNbPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForNbPrint****";
                _logger.info(msg6);
            }catch(Exception e){
                e.printStackTrace();
                _logger.error("****prepareForNbPrint exception***",e);
            }

            return mapping.findForward("printNbReceipt");
        }
        //#173 2016/2/17 兴业银行
        else if(vo.getCustomerName().indexOf("兴业银行") != -1)
        {
            request.setAttribute("packageId", vo.getId());

            request.setAttribute("title", "永银文化贵金属产品收货回执单");

            ConditionParse con2 = new ConditionParse();
            con2.addWhereStr();
            con2.addCondition("PackageBean.pickupId", "=", pickupId);

            List<PackageVO> allPackages = packageDAO.queryVOsByCondition(con2);
            if (!ListTools.isEmptyOrNull(allPackages)){
                _logger.info("****allPackages size****"+allPackages.size());
                request.setAttribute("allPackages", allPackages.size());

                //2015/3/30 批量打印最后一张回执单时，因定向到交接单打印，需要此时把最后一张CK单状态设置为“已打印"
                if ("0".equals(batchPrint) && allPackages.size() == index_pos){
                    // 更新状态
                    try
                    {
                        shipManager.updatePrintStatus(pickupId, index_pos);
                        _logger.info(pickupId+":"+index_pos+" print finished***");
                    }
                    catch (MYException e)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印出错." + e.getErrorContent());

                        return mapping.findForward("error");
                    }
                }
            }

            try{
                String msg5 = "**********before prepareForBankPrint****";
                _logger.info(msg5);
                this.prepareForXyPrint(request, vo, itemList, compose);
                this.generateQRCode(vo.getId());
                request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));
                String msg6 = "**********after prepareForBankPrint****";
                _logger.info(msg6);
            }catch(Exception e){
                e.printStackTrace();
                _logger.error("****printBankReceipt exception***",e);
            }

            return mapping.findForward("printXyReceipt");
        }
        else{  // 打印发货单
            //request.setAttribute("packageId", "None");
            request.setAttribute("packageId", vo.getId());

            List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

            Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

            for (PackageItemBean each : itemList)
            {

                if (!each.getCustomerId().equals(vo.getCustomerId()))
                {
                    _logger.info("***each.getCustomerId() "+each.getCustomerId()+" vs "+vo.getCustomerId());
                    continue;
                }

                String key = each.getProductId();

                if (!map1.containsKey(key))
                {
                    map1.put(each.getProductId(), each);
                }else{
                    PackageItemBean itemBean = map1.get(key);

                    itemBean.setAmount(itemBean.getAmount() + each.getAmount());
                }
            }

            for(Entry<String, PackageItemBean> each : map1.entrySet())
            {
                PackageItemBean item = each.getValue();
                String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
                if (!StringTools.isNullOrNone(productName)){
                    item.setProductName(productName);
                }

                itemList1.add(item);
            }

            vo.setItemList(itemList1);

            this.generateQRCode(vo.getId());
            request.setAttribute("qrcode", this.getQrcodeUrl(vo.getId()));

            return mapping.findForward("printShipment");
        }
    }

    private String getQrcodeUrl(String image){
        String ip = ConfigLoader.getProperty("httpServerIP");
        if (StringTools.isNullOrNone(ip)){
            try {
                InetAddress thisIp =InetAddress.getLocalHost();
                ip = thisIp.getHostAddress();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return "http://"+ip+":8000/images/"+image+".png";
    }

    private void generateQRCode(String packagId){
        try{
//            ZxingUtils.generateQRCode(packagId);
            ZxingUtils.generateBarCode(packagId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getCiticNoFromA(Map<String,String> aToOutMap,String a){
        //#181 回执单需要打印发票和赠品对应的客户姓名及对应的银行订单号
        if (aToOutMap!= null && aToOutMap.containsKey(a)){
            return aToOutMap.get(a);
        }
        String citicNo = "";
        InvoiceinsBean bean = this.invoiceinsDAO.find(a);
        if (bean!= null){
            String refIds = bean.getRefIds();
            if (!StringTools.isNullOrNone(refIds)){
                String[] temp = refIds.split(";");
                String refOutId = null;
                for (String out: temp){
                    if (out.startsWith("SO")){
                        refOutId = out;
                        _logger.info(a+"*****refIds found********"+refOutId);
                        break;
                    }
                }

                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(refOutId, AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    citicNo = outiList.get(0).getCiticNo();
                }
            }
        }
        return citicNo;
    }

//    //2015/7/26 打印回执单时根据客户名前四位，取银行品名对照表中对应的银行品名
    @Deprecated
    private void convertProductName(PackageItemBean item, String customerName){
        ProductBean product = productDAO.find(item.getProductId());
        if (product!= null) {
            ConditionParse conditionParse =  new ConditionParse();
            conditionParse.addCondition("code", "=", product.getCode());
            List<ProductVSBankBean> beans = this.productVSBankDAO.queryEntityBeansByCondition(conditionParse);
            if (!ListTools.isEmptyOrNull(beans)){
                ProductVSBankBean bean = beans.get(0);
                String productName = null;
                if (bean!= null){
                    if (customerName.indexOf("浦发银行")!= -1){
                        productName = bean.getPufaProductName();
                    } else if (customerName.indexOf("中信银行")!= -1) {
                        productName = bean.getCiticProductName();
                    } else if (customerName.indexOf("招商银行")!= -1) {
                        productName = bean.getZhProductName();
                    }
                }

                if (!StringTools.isNullOrNone(productName)){
                    String template = "print receipt convertProductName from:%s to:%s for customer:%s";
                    _logger.info(String.format(template, item.getProductName(), productName, customerName));
                    item.setProductName(productName);
                }
            }
        }
    }


    //2015/10/13 此规则只针对银行类回执单
    // 产品名称取订单对应的导入表中的订单明细行中的productid取对应的CiticProduct表中的CITICNAME字段值，如无对应订单或对应商品则仍取OA品名
    //如果产品在CiticProduct中无对应产品，取其名称的10位以后（字符之前的中间字符
    //YG0021100 2015年1盎司熊猫银币含包装（普17） 这个产品找不到对应的银行品名，则回执单上的名称取 2015年1盎司熊猫银币含包装
    //2016/4/5 所有银行品名规则都参考中信银行,productcode取自out import表
    @Deprecated
    private void convertProductName(PackageItemBean item){
        ProductBean product = productDAO.find(item.getProductId());
        String productName = null;
        String productCode = "";

        if (product!= null) {
            productCode = product.getCode();
            ConditionParse conditionParse =  new ConditionParse();
            conditionParse.addCondition("productCode", "=", productCode);
            List<CiticVSOAProductBean> beans = this.citicVSOAProductDAO.queryEntityBeansByCondition(conditionParse);
            if (!ListTools.isEmptyOrNull(beans)){
                CiticVSOAProductBean bean = beans.get(0);
                if (bean!= null){
                    productName = bean.getCiticProductName();
                }
            } else{
                productName = item.getProductName();
            }
        }

        if (!StringTools.isNullOrNone(productName)){
            String template = "print receipt convertProductName from:%s to:%s with productCode:%s";
            _logger.info(String.format(template, item.getProductName(), productName, productCode));
            item.setProductName(productName);
        }
    }

    //#183 2016/6

    /**
     * 招行的回执单上品名的取值逻辑要改下：按销售订单号到out_import表中取对应行中的productcode,
     * 再到t_center_vs_citic_product 表中拿productcode对应相同的citicproductcode字段行，取citicproductname
     * @param item
     */
    @Deprecated
    private String convertProductNameForBank(PackageItemBean item){
        String productName = "";
        String outId = item.getOutId();
        if (outId.startsWith("ZS")){
            //2016/5/19 赠送单直接取品名
            return item.getProductName();
        } else if (outId.contains("<br>")){
            //2016/8/16 合并商品行的outId也合并过了
            String[] outIds = item.getOutId().split("<br>");
            outId = outIds[0];
        }

        List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);

        if (!ListTools.isEmptyOrNull(importBeans))
        {
            OutImportBean bean = importBeans.get(0);
            String productCode = bean.getProductCode();

            //#291
            if (!StringTools.isNullOrNone(productCode)){
                ConditionParse conditionParse =  new ConditionParse();
                conditionParse.addCondition("bankProductCode", "=", productCode);
                List<ProductImportBean> beans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(beans)){
                    ProductImportBean productImportBean = beans.get(0);
                    productName = productImportBean.getBankProductName();
                    _logger.info("***getBankProductName***"+productName);
                    try {
                        item.setProductWeight(Double.valueOf(productImportBean.getWeight()));
                    }catch(Exception e){}
                }
            }
        }

        //default pick from package item table
        if (StringTools.isNullOrNone(productName)){
            productName = item.getProductName();
        }

        String template = "fullID %s product name %s converted to %s";
        _logger.info(String.format(template, outId, item.getProductName(), productName));

        return productName;
    }

    private String convertProductNameForZj(PackageItemBean item, String customerName){
        String productName = "";
        String outId = item.getOutId();
        if (outId.startsWith("ZS")){
            //2016/5/19 赠送单直接取品名
            return item.getProductName();
        } else if (outId.contains("<br>")){
            //2016/8/16 合并商品行的outId也合并过了
            String[] outIds = item.getOutId().split("<br>");
            outId = outIds[0];
        } else if (outId.startsWith("A")){
            //发票号不用转换
            return item.getProductName();
        }

        String productId = item.getProductId();
        ProductBean productBean = this.productDAO.find(productId);
        if (productBean!= null){
            String productCode = productBean.getCode();
            //#291
            if (!StringTools.isNullOrNone(productCode)){
                ConditionParse conditionParse =  new ConditionParse();
                conditionParse.addCondition("code", "=", productCode);
                if (customerName.length()>=4) {
                    conditionParse.addCondition("bank", "=", customerName.substring(0, 4));
                }else{
                    conditionParse.addCondition("bank", "=", customerName);
                }

                List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(importBeans)) {
                    OutImportBean bean = importBeans.get(0);
                    conditionParse.addCondition("bankProductCode", "=", bean.getProductCode());
                }

                List<ProductImportBean> beans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(beans)){
                    ProductImportBean productImportBean = beans.get(0);
                    productName = productImportBean.getBankProductName();

                    //#310
                    String material = productImportBean.getMaterial();

                    _logger.info("***getBankProductName***"+productName+"***material"+material);
                    item.setMateriaType(material);
                    try {
                        item.setProductWeight(Double.valueOf(productImportBean.getWeight()));
                    }catch(Exception e){}
                }
            }
        }

        //default pick from package item table
        if (StringTools.isNullOrNone(productName)){
            productName = item.getProductName();
        }

        String template = "fullID %s product name %s converted to %s";
        _logger.info(String.format(template, outId, item.getProductName(), productName));

        return productName;
    }

    /**
     * 2015/11/23 把新产品申请里的销售周期/销售对象/纸币类型/外型栏位，分别改为 实物数量、包装数量、证书数量、产品克重
     * @param item
     */
    @Deprecated
    private void convertProductNameForNb(PackageItemBean item){
        ProductBean product = productDAO.find(item.getProductId());
        String productName = null;
        String productCode = "";

        if (product!= null) {
            productCode = product.getCode();
            ConditionParse conditionParse =  new ConditionParse();
            conditionParse.addCondition("productCode", "=", productCode);
            List<CiticVSOAProductBean> beans = this.citicVSOAProductDAO.queryEntityBeansByCondition(conditionParse);
            if (!ListTools.isEmptyOrNull(beans)){
                CiticVSOAProductBean bean = beans.get(0);
                if (bean!= null){
                    productName = bean.getCiticProductName();
                }
            } else{
                productName = item.getProductName();
            }

            this.setProductInfoForNb(item, product);
        }

        if (!StringTools.isNullOrNone(productName)){
            String template = "print receipt convertProductName from:%s to:%s with productCode:%s";
            _logger.info(String.format(template, item.getProductName(), productName, productCode));
            item.setProductName(productName);
        }
    }

    private void setProductInfoForNb(PackageItemBean item, ProductBean product){
        if(item != null && product!= null){
            _logger.info("****product amount***"+item.getAmount()+"***"+product.getProductAmount()+"***"+product.getPackageAmount()+"***");
            item.setProductAmount(Math.abs(item.getAmount()*product.getProductAmount()));
            item.setPackageAmount(Math.abs(item.getAmount()*product.getPackageAmount()));
            item.setCertificateAmount(Math.abs(item.getAmount()*product.getCertificateAmount()));
//            item.setProductWeight(Math.abs(product.getProductWeight()));

            if (StringTools.isNullOrNone(item.getMateriaType())){
                //材质类型
                int checkDays = product.getCheckDays();
                String materialType = "";
                ConditionParse conditionParse = new ConditionParse();
                conditionParse.addWhereStr();
                conditionParse.addCondition("type", "=", "201");
                conditionParse.addCondition("keyss", "=", checkDays);

                List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(enumBeans)){
                    EnumBean enumBean = enumBeans.get(0);
                    String value = enumBean.getValue();
                    if ("贵金属纪念章_金".equals(value) || "贵金属纪念章金".equals(value)){
                        materialType = "Au.999";
                    } else if ("贵金属纪念章_银".equals(value) || "贵金属纪念章银".equals(value)){
                        materialType = "Ag.999";
                    } else if ("贵金属纪念章_金银".equals(value) || "贵金属纪念章金银".equals(value)){
                        materialType = "Au.999+Ag.999";
                    }
                }
                item.setMateriaType(materialType);
            }
        }
    }

    //2016/5/27 去掉此品名规则
    @Deprecated
    public String getProductName(String original){
        String name = "";
        try {
            String[] l1 = original.split(" ");
            if (l1.length == 1) {
                name = original;
            } else {
                String word = l1[1];
                String[] l2 = word.split("（");
                if (l2.length == 1) {
                    name = word;
                } else {
                    name = l2[0];
                }
            }
        }catch(Exception e){
            name = original;
        }

        return name;
    }

    //2015/9/29 打印银行回执单时增加客户姓名栏位
    private void getCustomerName(PackageItemBean item){
        String outId = item.getOutId();
        if (StringTools.isNullOrNone(outId)){
            _logger.warn("****Empty OutId***********"+outId);
        } else if (outId.startsWith("SO")){
            item.setCustomerName(this.getCustomerNameFromOutImport(outId));
        } else if(outId.startsWith("ZS")){
            OutBean out = outDAO.find(outId);
            if (out!= null){
                String sourceOutId = out.getRefOutFullId();
                if (!StringTools.isNullOrNone(sourceOutId)){
                    item.setCustomerName(this.getCustomerNameFromOutImport(sourceOutId));
                }

                if (StringTools.isNullOrNone(item.getCustomerName())) {
                    //#181默认就读取out表中客户名
                    item.setCustomerName(out.getCustomerName());
                }

            }
        } else if(outId.startsWith("A")){
            InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
            if (bean!= null){
                String refIds = bean.getRefIds();
                if (!StringTools.isNullOrNone(refIds)){
                    String[] temp = refIds.split(";");
                    String refOutId = null;
                    for (String out: temp){
                        if (out.startsWith("SO")){
                            refOutId = out;
                            _logger.info(outId+"*****refIds found********"+refOutId);
                            break;
                        }
                    }

                    item.setCustomerName(this.getCustomerNameFromOutImport(refOutId));
                }
            }
        }
    }

    private void getCustomerNameForXy(PackageItemBean item){
        String outId = item.getOutId();
        if (StringTools.isNullOrNone(outId)){
            _logger.warn("****Empty OutId***********"+outId);
        } else if (outId.startsWith("SO")){
            item.setCustomerName(this.getCustomerNameFromOutImport(outId));
        } else if(outId.startsWith("ZS")){
            OutBean out = outDAO.find(outId);
            if (out!= null){
                String sourceOutId = out.getRefOutFullId();
                if (!StringTools.isNullOrNone(sourceOutId)){
                    item.setCustomerName(this.getCustomerNameFromOutImport(sourceOutId));
                }
            }
        }
    }

    private String getCustomerNameFromOutImport(String outId){
        StringBuilder sb = new StringBuilder();
        List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);

        if (!ListTools.isEmptyOrNull(importBeans))
        {
            for (OutImportBean outImportBean: importBeans){
                String customerName = outImportBean.getCustomerName();
                if (!StringTools.isNullOrNone(customerName)){
                    sb.append(customerName).append(";");
                }
            }
        }
        String customerName = sb.toString();
        if (StringTools.isNullOrNone(customerName)){
            return customerName;
        } else{
            //remove last ";" char
            return customerName.substring(0,customerName.length()-1);
        }
    }

    //#409 吉林银行联行网点
    private String[] getProductCodeAndLhwdFromOutImport(String outId){
        String[] result = new String[2];
        String productCode = "";
        String lhwd = "";
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addWhereStr();
        conditionParse.addCondition("OANo", "=", outId);
        List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByCondition(conditionParse);

        if (!ListTools.isEmptyOrNull(importBeans))
        {
            for (OutImportBean outImportBean: importBeans){
                if (!StringTools.isNullOrNone(outImportBean.getProductCode())){
                    productCode = outImportBean.getProductCode();
                    lhwd = outImportBean.getLhwd();
                    break;
                }
            }
        }
        result[0] = productCode;
        result[1] = lhwd;
        return result;
    }


    /**
     * 2015/11/13 中原银行产品代码
     * @param outId
     * @return
     */
    private String getProductCodeFromOutImport(String outId){
        String productCode = "";
        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addWhereStr();
        conditionParse.addCondition("OANo", "=", outId);
        List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByCondition(conditionParse);

        if (!ListTools.isEmptyOrNull(importBeans))
        {
            for (OutImportBean outImportBean: importBeans){
                if (!StringTools.isNullOrNone(outImportBean.getProductCode())){
                    productCode = outImportBean.getProductCode();
                    break;
                }
            }
        }
        return productCode;
    }

    /**
     * 2015/12/16 中原银行调入分行：，字段值取out_import中根据OANO对应的branchname 字段值，
     * 如果是发票，则根据发票对应的销售单关联OANO字段，取其行项目上的branchname字段值
     * @param outId
     * @return
     */
    private String getBranchNameFromOutImport(String outId){
        String branchName = "";
        List<OutImportBean> importBeans = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);
        if (!ListTools.isEmptyOrNull(importBeans))
        {
            for (OutImportBean outImportBean: importBeans){
                if (!StringTools.isNullOrNone(outImportBean.getBranchName())){
                    branchName = outImportBean.getBranchName();
                    break;
                }
            }
        }
        return branchName;
    }

    /**
     * 2015/11/13 中原银行回执单打印:调入单位就取客户名称，但到（ 和 -符号后面的字条 去掉
     * @param original
     * @return
     */
    private String getCustomerName(String original){
        String name = original;
        String[] l1 = original.split("（|-");
        if (l1.length >= 1){
            name = l1[0];
        }

        return name;
    }

    enum Bank{
        ZY, OTHER
    }

    /**
     * 2015/11/13 中原银行回执单产品编码取 out_import表里的商品编码字段productCode
     * @param item
     */
    private void getProductCode(PackageItemBean item, Bank bank){
        String outId = item.getOutId();
        if (StringTools.isNullOrNone(outId) || outId.startsWith("A")){
            _logger.warn("****Empty OutId or invoiceins***********"+outId);
        }else {
            if (bank == Bank.ZY && outId.startsWith("ZS")){
                _logger.info("****ZS orders of ZY bank***********"+outId);
                item.setProductCode("");
            }else{
                String[] temp = this.getProductCodeAndLhwdFromOutImport(outId);
                item.setProductCode(temp[0]);
                item.setLhwd(temp[1]);
            }
        }
    }

    private String[] getStafferNameAndPhone(String outId){
        String stafferName = "永银商务部";
        String phone = "4006518859";
        OutBean out = outDAO.find(outId);
        if (out!= null){
            String stafferId = out.getStafferId();
            StafferBean staffer = this.stafferDAO.find(stafferId);
            if (staffer!= null){
                if (!StringTools.isNullOrNone(staffer.getName())){
                    stafferName = staffer.getName();
                    if (!StringTools.isNullOrNone(stafferName) && stafferName.indexOf("-")!=-1){
                        String[] temp = stafferName.split("-");
                        stafferName = temp[1];
                    }
                }
                if (!StringTools.isNullOrNone(staffer.getHandphone())){
                    phone = staffer.getHandphone();
                }
            }
        } else{
            _logger.warn("******OutBean not found*****" + outId);
        }
        return new String[]{stafferName,phone};
    }


    /**#173兴业银行回执单
     * prepareForXyPrint
     *
     * @param request
     * @param vo
     * @param itemList
     */
    private void prepareForXyPrint(HttpServletRequest request, PackageVO vo,
                                     List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList)){
            PackageItemBean first = itemList.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            _logger.info(first+"******first****"+outId);
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (this.isOut(outId)){
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }
            _logger.info("**stafferName***"+stafferName+"**phone**"+phone);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);
        }

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId()+"****iterate package item:"+"***"+each.getOutId()+"***"+each.getDescription()+"***"+each.getRefId());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                _logger.info("*************each.getCustomerId()***"+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #145:回执单打印CK单合并多客户名称问题
            this.getCustomerNameForXy(each);

            //2015/12/26 #152: 产品编码要在此处获取，因为相同的产品会合并，在后边处理会有问题。
            this.getProductCode(each, Bank.OTHER);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
            }

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                _logger.info("******赠品类型*****"+each.getOutId());
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:" + refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }
            }

            String key = each.getProductId();

            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }

                //2015/1/25 注释掉
//				each.setDescription("");

                map1.put(each.getProductId(), each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                //#145: 2015/12/27 回执单CK单合并多客户名称问题
                if (StringTools.isNullOrNone(itemBean.getCustomerName())){
                    itemBean.setCustomerName(each.getCustomerName());
                }else{
                    itemBean.setCustomerName(itemBean.getCustomerName()+"<br>"+each.getCustomerName());
                }

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
//                        String refId3 = itemBean.getRefId() + "<br>" + refId;
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("**********refId3**********"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
//                        String description = itemBean.getDescription() + "<br>" + each.getDescription();
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        Set<String> refIdSet = new HashSet<String>();
        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }

            itemList1.add(item);

            if (!StringTools.isNullOrNone(item.getRefId()) && !refIdSet.contains(item.getRefId())){
                refIdSet.add(item.getRefId());
            }
        }

        vo.setRefId(this.concat(refIdSet));
        vo.setItemList(itemList1);

        request.setAttribute("total", totalAmount);
    }

    private String concat(Collection<String> collections){
        StringBuilder sb = new StringBuilder();
        for (String element:collections){
            sb.append(element).append(",");
        }
        String str = sb.toString();
        if (str.length() > 0 && str.charAt(str.length()-1)==',') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }


    private boolean isOut(String outId){
        return outId.startsWith("SO") || outId.startsWith("ZS") || outId.startsWith("XZ");
    }

    /**
     * #262 2016/6/21 统一回执单打印格式
     * @param request
     * @param vo
     * @param itemList
     * @param compose
     */
    private void prepareForUnified(HttpServletRequest request, PackageVO vo,
                                     List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        //#189 <productId_itemType,PackageItemBean>
        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId() + "****iterate package item:" + "***" + each.getOutId() + "***" + each.getDescription() + "***" + each.getRefId());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                _logger.info("*************each.getCustomerId() "+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #145:回执单打印CK单合并多客户名称问题
            this.getCustomerName(each);
            this.getProductCode(each, Bank.OTHER);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
            }
            //#169
            else if(out == null && outId.startsWith("A")){
                each.setPoDate(each.getOutTime());
            }

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                _logger.info("******赠品类型*****"+each.getOutId());
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:"+refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }
            }

//            String key = each.getProductId();
            String key = each.getProductId()+"_"+each.getItemType();

            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }

                //2015/1/25 注释掉
//				each.setDescription("");

                map1.put(key, each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                //#145: 2015/12/27 回执单CK单合并多客户名称问题
                if (StringTools.isNullOrNone(itemBean.getCustomerName())){
                    itemBean.setCustomerName(each.getCustomerName());
                }else{
                    itemBean.setCustomerName(itemBean.getCustomerName()+"<br>"+each.getCustomerName());
                }

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("*****refId3*******"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }

            itemList1.add(item);
            _logger.info("***convertProductNameForBank***" + item.getProductName());
        }

        vo.setItemList(itemList1);

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList1)){
            PackageItemBean first = itemList1.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (this.isOut(outId)){
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }
            _logger.info("*****stafferName***********"+stafferName+"*******phone*************"+phone);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);
        }

        request.setAttribute("total", totalAmount);
    }

    private void prepareForBankPrint(HttpServletRequest request, PackageVO vo,
                                     List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        //#189 <productId_itemType,PackageItemBean>
        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList)){
            PackageItemBean first = itemList.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (this.isOut(outId)){
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }
            _logger.info("*****stafferName***********"+stafferName+"*******phone*************"+phone);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);
        }

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId()+"****iterate package item:"+"***"+each.getOutId()+"***"+each.getDescription()+"***"+each.getRefId());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                _logger.info("*************each.getCustomerId() "+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #145:回执单打印CK单合并多客户名称问题
            this.getCustomerName(each);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
            }
            //#169
            else if(out == null && outId.startsWith("A")){
                each.setPoDate(each.getOutTime());
            }

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                _logger.info("******赠品类型*****"+each.getOutId());
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:"+refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }
            }

//            String key = each.getProductId();
            String key = each.getProductId()+"_"+each.getItemType();

            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }

                //2015/1/25 注释掉
//				each.setDescription("");

                map1.put(key, each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                //#145: 2015/12/27 回执单CK单合并多客户名称问题
                if (StringTools.isNullOrNone(itemBean.getCustomerName())){
                    itemBean.setCustomerName(each.getCustomerName());
                }else{
                    itemBean.setCustomerName(itemBean.getCustomerName()+"<br>"+each.getCustomerName());
                }

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("*****refId3*******"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }

            itemList1.add(item);
            _logger.info("***convertProductNameForBank***" + item.getProductName());
        }

        vo.setItemList(itemList1);

        request.setAttribute("total", totalAmount);
    }

    private String concat(String str1, String str2, String deliminator){
        if (!StringTools.isNullOrNone(str1) && str1.equals(str2)){
            return str1;
        } else if (!StringTools.isNullOrNone(str1) && !StringTools.isNullOrNone(str2)){
            return str1+deliminator+str2;
        } else{
            return "";
        }
    }

    /**
     * 2015/11/13 新增中原银行回执单打印，暂时和中信打印逻辑一样
     * @param request
     * @param vo
     * @param itemList
     * @param compose
     */
    private void prepareForZyPrint(HttpServletRequest request, PackageVO vo,
                                     List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        //#189 <productId_itemType,PackageItemBean>
        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList)){
            PackageItemBean first = itemList.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            String firstOutId = outId;
            String branchName = "";
            _logger.info(first+"******first****"+outId);
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (outId.startsWith("SO")){
                firstOutId = outId;
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            }else if (outId.startsWith("ZS") || outId.startsWith("XZ")){
                //2015/12/21 中原银行打印回执单，赠送单调入分行内容有误
                firstOutId = outId;
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                firstOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }


            branchName = this.getBranchNameFromOutImport(firstOutId);
            _logger.info("*****stafferName***"+stafferName+"***phone***"+phone+"**branchName*"+branchName);
            request.setAttribute("branchName",branchName);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);


            //2015/11/17 取第一个SO单的outTime作为中原银行回执单交接时间
            String outTime = first.getOutTime();
            if (!StringTools.isNullOrNone(outTime)){
                String[] temp = outTime.split("-");
                if (temp.length == 3){
                    request.setAttribute("year", temp[0]);
                    request.setAttribute("month", temp[1]);
                    request.setAttribute("day", temp[2]);
                }
            }
        }

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId()+"****iterate package item:"+"***"+each.getOutId()+"***"+each.getDescription()+"***"+each.getRefId());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                //过滤掉其他客户的销售单
                _logger.warn("***each.getCustomerId()***"+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #153: 中原银行回执单，合并同一产品后，无法取到产品代码
            this.getProductCode(each, Bank.ZY);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
                each.setOutType(out.getOutType());
            }            //#169
            else if(out == null && outId.startsWith("A")){
                each.setPoDate(each.getOutTime());
                each.setOutType(OutConstant.OUTTYPE_INVOICE);
            }

            each.getProductId();

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                _logger.info("******赠品类型*****"+each.getOutId());
                if (!ListTools.isEmptyOrNull(outiList)) {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:"+refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }


            }

//            String key = each.getProductId();
            String key = each.getProductId()+"_"+each.getItemType();
            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }
//                map1.put(each.getProductId(), each);
                map1.put(key, each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
//                        String refId3 = itemBean.getRefId() + "<br>" + refId;
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("**********refId3**********"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
//                        String description = itemBean.getDescription() + "<br>" + each.getDescription();
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForBank(item);
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }
            itemList1.add(item);
            _logger.info("***get product code***" + item.getProductCode());
        }

        //2015/11/13 中原银行回执单：调入单位就取客户名称，但到（ 和 -符号后面的字条 去掉
        vo.setCustomerName(this.getCustomerName(vo.getCustomerName()));
        vo.setItemList(itemList1);

        request.setAttribute("total", totalAmount);
    }

    /**
     * 2015/11/20 宁波银行回执单打印
     * @param request
     * @param vo
     * @param itemList
     * @param compose
     */
    private void prepareForNbPrint(HttpServletRequest request, PackageVO vo,
                                   List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList)){
            PackageItemBean first = itemList.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            _logger.info(first+"******first****"+outId);
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (this.isOut(outId)){
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }
            _logger.info("*****stafferName***********"+stafferName+"**phone**"+phone);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);

            //2015/11/21 取打印时间作为宁波银行回执单送货时间
            String shipTime = TimeTools.now_short();
            request.setAttribute("shipTime", shipTime);
        }

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId()+"****iterate package item:"+"***"+each.getOutId()+"***"+each.getDescription()+"***"+each.getRefId()+"***"+each.getAmount());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                _logger.info("*************each.getCustomerId()***"+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #152: 产品编码要在此处获取，因为相同的产品会合并，在后边处理会有问题。
            this.getProductCode(each, Bank.OTHER);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
            }

            each.getProductId();

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                _logger.info("******赠品类型*****"+each.getOutId());
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:"+refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }
            }

            String key = each.getProductId();

            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }

                //2015/1/25 注释掉
//				each.setDescription("");

                map1.put(each.getProductId(), each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
//                        String refId3 = itemBean.getRefId() + "<br>" + refId;
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("**********refId3**********"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
//                        String description = itemBean.getDescription() + "<br>" + each.getDescription();
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForZj(item, this.getCustomerName(vo.getCustomerName()));
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }

            ProductBean product = productDAO.find(item.getProductId());
            if (product!= null) {
                this.setProductInfoForNb(item, product);
            }
            itemList1.add(item);
        }

        //2015/11/13 中原银行回执单：调入单位就取客户名称，但到（ 和 -符号后面的字条 去掉
        vo.setCustomerName(this.getCustomerName(vo.getCustomerName()));
        vo.setItemList(itemList1);

        request.setAttribute("total", totalAmount);
    }

    /**
     * #310 紫金农商
     * @param request
     * @param vo
     * @param itemList
     * @param compose
     */
    private void prepareForZjPrint(HttpServletRequest request, PackageVO vo,
                                   List<PackageItemBean> itemList, String compose)
    {
        int totalAmount = 0 ;

        List<PackageItemBean> itemList1 = new ArrayList<PackageItemBean>();

        Map<String, PackageItemBean> map1 = new HashMap<String, PackageItemBean>();

        //2015/1/25 取商务联系人及电话
        if (!ListTools.isEmptyOrNull(itemList)){
            PackageItemBean first = itemList.get(0);
            String outId = first.getOutId();
            String stafferName = "永银商务部";
            String phone = "4006518859";
            _logger.info(first+"******first****"+outId);
            if (StringTools.isNullOrNone(outId)){
                _logger.warn("****Empty OutId***********"+first.getId());
            }else if (this.isOut(outId)){
                String[] result = this.getStafferNameAndPhone(outId);
                if (result.length>=2){
                    stafferName = result[0];
                    phone = result[1];
                }
            } else if(outId.startsWith("A")){
                InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
                if (bean!= null){
                    String refIds = bean.getRefIds();
                    _logger.info(outId+"*****refIds found********"+refIds);
                    if (!StringTools.isNullOrNone(refIds)){
                        String[] temp = refIds.split(";");
                        String refOutId = null;
                        for (String out: temp){
                            if (out.startsWith("SO")){
                                refOutId = out;
                                break;
                            }
                        }
                        String[] result2 = this.getStafferNameAndPhone(refOutId);
                        if (result2.length>=2){
                            stafferName = result2[0];
                            phone = result2[1];
                        }
                    }
                }
            }
            _logger.info("*****stafferName***********"+stafferName+"**phone**"+phone);
            request.setAttribute("stafferName", stafferName);
            request.setAttribute("phone",phone);

            //2015/11/21 取打印时间作为宁波银行回执单送货时间
            String shipTime = TimeTools.now_short();
            request.setAttribute("shipTime", shipTime);
        }

        for (PackageItemBean each : itemList)
        {
            _logger.info(each.getId()+"****iterate package item:"+"***"+each.getOutId()+"***"+each.getDescription()+"***"+each.getRefId()+"***"+each.getAmount());
            if (!each.getCustomerId().equals(vo.getCustomerId()))
            {
                _logger.info("*************each.getCustomerId()***"+each.getCustomerId()+"****"+vo.getCustomerId());
                continue;
            }

            //2015/12/26 #152: 产品编码要在此处获取，因为相同的产品会合并，在后边处理会有问题。
            this.getProductCode(each, Bank.OTHER);

            // 针对赠品,且有备注的订单,单独显示
            String outId = each.getOutId();

            OutBean out = outDAO.find(outId);

            //2015/10/13 商品性质根据销售单类型显示不同的名称：销售出库--销售，XX领样-领样，XX铺货--铺货，赠送--赠品，单号为A开头的显示 发票
            if (out!= null && out.getType() == OutConstant.OUT_TYPE_OUTBILL){
                if (out.getOutType() == OutConstant.OUTTYPE_OUT_COMMON){
                    each.setItemType("销售");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_SHOWSWATCH
                        || out.getOutType() == OutConstant.OUTTYPE_OUT_BANK_SWATCH){
                    each.setItemType("领样");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_SHOW){
                    each.setItemType("铺货");
                } else if (out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT){
                    each.setItemType("赠品");
                }
            }

            if (StringTools.isNullOrNone(each.getItemType()) && outId.startsWith("A")){
                each.setItemType("发票");
            }

            //2015/10/13 销售时间取out表中的podate
            if (out!= null){
                each.setPoDate(out.getPodate());
            }

            each.getProductId();

            if (out != null && out.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
            {
                _logger.info("******赠品类型*****"+each.getOutId());
                List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(each.getOutId(), AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(outiList))
                {
                    String refId = outiList.get(0).getCiticNo();
                    _logger.info("****refId:"+refId);
                    each.setRefId(refId);

                    if (!StringTools.isNullOrNone(outiList.get(0).getDescription()))
                    {
                        checkCompose(each, each, compose);

                        String description = outiList.get(0).getDescription();
                        _logger.info("****Description****"+description);
                        each.setDescription(description);

                        itemList1.add(each);

                        totalAmount += each.getAmount();

                        continue;
                    }
                }
            }

            String key = each.getProductId();

            if (!map1.containsKey(key))
            {
                checkCompose(each, each, compose);

                String refId = this.getRefId(out, each.getOutId());
                if (!StringTools.isNullOrNone(refId)){
                    each.setRefId(refId);
                }

                //2015/1/25 注释掉
//				each.setDescription("");

                map1.put(each.getProductId(), each);
            }else{
                PackageItemBean itemBean = map1.get(key);

                itemBean.setAmount(itemBean.getAmount() + each.getAmount());

                itemBean.setOutId(itemBean.getOutId() + "<br>" + each.getOutId());

                if (!StringTools.isNullOrNone(itemBean.getRefId()))
                {
                    String refId = this.getRefId(out, each.getOutId());
                    if (!StringTools.isNullOrNone(refId))
                    {
//                        String refId3 = itemBean.getRefId() + "<br>" + refId;
                        String refId3 = this.concat(itemBean.getRefId(),refId,"<br>");
                        _logger.info("**********refId3**********"+refId3);
                        itemBean.setRefId(refId3);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getRefId()))
                    {
                        _logger.info("**********refId4**********"+each.getRefId());
                        itemBean.setRefId(each.getRefId());
                    }
                }

                //2015/1/29 合并Description
                if (!StringTools.isNullOrNone(itemBean.getDescription()))
                {
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
//                        String description = itemBean.getDescription() + "<br>" + each.getDescription();
                        String description = this.concat(itemBean.getDescription(), each.getDescription(), "<br>");
                        _logger.info("**********description2**********"+description);
                        itemBean.setDescription(description);
                    }
                }else{
                    if (!StringTools.isNullOrNone(each.getDescription()))
                    {
                        itemBean.setDescription(each.getDescription());
                    }
                }
            }

            totalAmount += each.getAmount();
        }

        String customerName = this.getCustomerName(vo.getCustomerName());
        vo.setCustomerName(customerName);
        for(Entry<String, PackageItemBean> each : map1.entrySet())
        {
            PackageItemBean item = each.getValue();
            String productName = this.convertProductNameForZj(item, customerName);
            if (!StringTools.isNullOrNone(productName)){
                item.setProductName(productName);
            }

            ProductBean product = productDAO.find(item.getProductId());
            if (product!= null) {
                item.setProductWeight(product.getProductWeight());
            }
            itemList1.add(item);
        }

        //2015/11/13 中原银行回执单：调入单位就取客户名称，但到（ 和 -符号后面的字条 去掉

        vo.setItemList(itemList1);

        request.setAttribute("total", totalAmount);
    }

    /**
     * 获取银行单号
     * 2015/10/13 赠送订单与发票都取关联的SO销售单号至 outimport表中取对应的CITINO字段值
     * @param out
     * @param outId
     * @return
     */
    private String getRefId(OutBean out, String outId){
        String refId = "";
        List<OutImportBean> outiList = outImportDAO.queryEntityBeansByFK(outId, AnoConstant.FK_FIRST);

        if (!ListTools.isEmptyOrNull(outiList))
        {
            refId = outiList.get(0).getCiticNo();
            _logger.info("**************redId2*****"+refId);
        } else if (outId.startsWith("A")){
            InvoiceinsBean bean = this.invoiceinsDAO.find(outId);
            if (bean!= null){
                String refIds = bean.getRefIds();
                _logger.info(outId+"*****refIds found********"+refIds);
                if (!StringTools.isNullOrNone(refIds)){
                    String[] temp = refIds.split(";");
                    String refOutId = null;
                    for (String id: temp){
                        if (id.startsWith("SO")){
                            refOutId = id;
                            break;
                        }
                    }

                    if (!StringTools.isNullOrNone(refOutId)){
                        List<OutImportBean> list2 = outImportDAO.queryEntityBeansByFK(refOutId, AnoConstant.FK_FIRST);

                        if (!ListTools.isEmptyOrNull(list2))
                        {
                            refId = list2.get(0).getCiticNo();
                            _logger.info("**************refId for A*****"+refId);
                        }
                    }
                }
            }
        } else if(outId.startsWith("ZS") && out!= null){
            String sourceOutId = out.getRefOutFullId();
            //自动生成赠品订单可从备注中获取关联单号
            if (StringTools.isNullOrNone(sourceOutId)){
                String description = out.getDescription();
                sourceOutId = this.getRefOutId(description);
            }

            if (!StringTools.isNullOrNone(sourceOutId)){
                List<OutImportBean> list2 = outImportDAO.queryEntityBeansByFK(sourceOutId, AnoConstant.FK_FIRST);

                if (!ListTools.isEmptyOrNull(list2))
                {
                    refId = list2.get(0).getCiticNo();
                    _logger.info("**************refId for ZS*****"+refId);
                }
            }
        }
        return refId;
    }

    public String getRefOutId(String description){
        String refOutId = "";
        if (!StringTools.isNullOrNone(description)){
            String[] words = description.split("：");
            if (words.length ==2){
                refOutId = words[1].trim();
            }
        }

        return refOutId;
    }

    /**
     * updateStatus
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateStatus(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
            throws ServletException
    {
        String pickupId = request.getParameter("pickupId");

        User user = Helper.getUser(request);

        AjaxResult ajax = new AjaxResult();

        try{
            shipManager.updateStatus(user, pickupId);

            ajax.setSuccess("更新成功");
        }catch(MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("更新失败");
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /** 确认发货
     * mUpdateStatus
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward mUpdateStatus(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException
    {
        String pickupId = request.getParameter("pickupId");
        String packageIds = request.getParameter("packageIds");

        User user = Helper.getUser(request);

        if (StringTools.isNullOrNone(pickupId)){
            _logger.info("****mUpdateStatus with packageIds****"+packageIds);
            try{
                shipManager.updatePackagesStatus(user, packageIds);
                request.setAttribute(KeyConstant.MESSAGE, "发货确认成功");
            }catch(MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "确认发货失败:"+e.getErrorContent());
            }
        } else {
            _logger.info("****mUpdateStatus with pickupId****"+pickupId);
            try{
                shipManager.updateStatus(user, pickupId);
                request.setAttribute(KeyConstant.MESSAGE, "发货确认成功");
            }catch(MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "确认发货失败:"+e.getErrorContent());
            }
        }



//        return mapping.findForward("queryPickup");
        return this.queryPickup(mapping, form, request, response);
    }

    /**
     * 批量导入分支行对应关系信息
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward importBranchRelation(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request, HttpServletResponse response)
            throws ServletException
    {
        RequestDataStream rds = new RequestDataStream(request);

        boolean importError = false;

        List<BranchRelationBean> importItemList = new ArrayList<BranchRelationBean>();

        StringBuilder builder = new StringBuilder();
        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("importBranchRelation");
        }

        if ( !rds.haveStream())
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("importBranchRelation");
        }

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
                    BranchRelationBean bean = new BranchRelationBean();

                    // 客户ID
                    if ( !StringTools.isNullOrNone(obj[0]))
                    {
                        bean.setId(obj[0]);
                    }
                    else
                    {
                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("客户ID不能为空")
                                .append("<br>");

                        importError = true;
                    }

                    //支行名称
                    if ( !StringTools.isNullOrNone(obj[1]))
                    {
                        bean.setSubBranchName(obj[1]);
                    } else
                    {
                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("支行名称不能为空")
                                .append("<br>");

                        importError = true;
                    }

                    //分行名称
                    if ( !StringTools.isNullOrNone(obj[2]))
                    {
                        bean.setBranchName(obj[2]);
                    }  else
                    {
                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("分行名称不能为空")
                                .append("<br>");

                        importError = true;
                    }

                    //分行邮件地址
                    if ( !StringTools.isNullOrNone(obj[3]))
                    {
                        bean.setBranchMail(obj[3]);
                    }


                    //支行邮件地址
                    if ( !StringTools.isNullOrNone(obj[4]))
                    {
                        bean.setSubBranchMail(obj[4]);
                    }else
                    {
                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("支行邮件地址不能为空")
                                .append("<br>");

                        importError = true;
                    }

                    //发送邮件标志
                    if ( !StringTools.isNullOrNone(obj[5]))
                    {
                        bean.setSendMailFlag(Integer.valueOf(obj[5]));
                    }else
                    {
                        builder
                                .append("第[" + currentNumber + "]错误:")
                                .append("发送邮件标志不能为空，比如为0或1")
                                .append("<br>");

                        importError = true;
                    }

                    //抄送分行标志
                    if ( !StringTools.isNullOrNone(obj[6]))
                    {
                        bean.setCopyToBranchFlag(Integer.valueOf(obj[6]));
                    }
                    importItemList.add(bean);

                }
                else
                {
                    builder
                            .append("第[" + currentNumber + "]错误:")
                            .append("数据长度不足7格错误")
                            .append("<br>");

                    importError = true;
                }
            }
        }catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.toString());

            return mapping.findForward("importBranchRelation");
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

            return mapping.findForward("importBranchRelation");
        }

        try
        {
            this.shipManager.saveAllEntityBeans(importItemList);
            request.setAttribute(KeyConstant.MESSAGE, "批量更新成功");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导入出错:"+ e.getMessage());

            return mapping.findForward("importBranchRelation");
        }
        return mapping.findForward("importBranchRelation");
    }

    /**
     *
     * @param obj
     * @return
     */
    private String[] fillObj(String[] obj)
    {
        String[] result = new String[50];

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

    public ActionForward printBatch(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException
    {
        CommonTools.saveParamers(request);

        String pickupId = request.getParameter("pickupId");

        // 先找出该批次下所有index大于index_pos的CK单
        ConditionParse condtion = new ConditionParse();
        condtion.addWhereStr();
        condtion.addCondition("PackageBean.pickupId", "=", pickupId);

        List<PackageVO> packageList = this.packageDAO.queryVOsByCondition(condtion);
        boolean flag = false;
        List<PackageItemBean> items = new ArrayList<PackageItemBean>();
        for (PackageVO each : packageList)
        {
            //2016/10/12 #328 检查临时发票号码
            List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(each.getId());
            for (PackageItemBean item : itemList){
                String productName = item.getProductName();
                if (productName!= null && productName.startsWith("发票号：XN")){
                    flag = true;
                    items.add(item);
                }
            }
        }

        if (!flag){
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "不存在虚拟发票号无法打印:"+pickupId);

            return mapping.findForward("error");
        }

        PackageVO batchVO = new PackageVO();
        batchVO.setPickupId(pickupId);
        batchVO.setItemList(items);
        batchVO.setPickupTime(packageList.get(0).getPickupTime());

        request.setAttribute("bean", batchVO);


        this.generateQRCode(pickupId);
        request.setAttribute("qrcode", this.getQrcodeUrl(pickupId));

        return mapping.findForward("printBatch");
    }

    /**
     * 2015/3/11 打印货物交接清单
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward printHandover(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request,
                                           HttpServletResponse response)
            throws ServletException
    {
        CommonTools.saveParamers(request);

        String pickupId = request.getParameter("pickupId");
        String printMode = request.getParameter("printMode");
        String sindex_pos = request.getParameter("index_pos");

        _logger.info("printHandover with pickupId***"+pickupId+"***printMode***"+printMode+"***sindex_pos***"+sindex_pos);

        int index_pos = 0;

        if (!StringTools.isNullOrNone(sindex_pos))
        {
            index_pos = MathTools.parseInt(sindex_pos);
        }

        index_pos += 1;

        // 先找出该批次下所有index大于index_pos的CK单
        ConditionParse condtion = new ConditionParse();
        condtion.addWhereStr();
        condtion.addCondition("PackageBean.pickupId", "=", pickupId);
        condtion.addIntCondition("PackageBean.index_pos", ">=", index_pos);
        condtion.addCondition("order by PackageBean.index_pos asc");

        List<PackageVO> packageList = this.packageDAO.queryVOsByCondition(condtion);

        if (ListTools.isEmptyOrNull(packageList))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

            return mapping.findForward("error");
        }

        _logger.info("****printHandover packageList size***"+packageList.size());

//        List<PackageVO> packageList = packageDAO.queryEntityVOsByFK(pickupId);

        StringBuilder sb = new StringBuilder();


        int pickupCount = packageList.size();

        //<key,value> as <productId,PackageItemBean>
        Map<String, PackageItemBean> map = new HashMap<String, PackageItemBean>();
        boolean printFlag = false;
        for (PackageVO each : packageList)
        {
            //2015/3/23 #45 判断业务员为“叶百韬”且 销售单上的客户名称为“中信银行XXXX”类型的才打印
            String stafferName = each.getStafferName();
            String customerName = each.getCustomerName();
            _logger.info("****stafferName***"+stafferName+"***customerName***"+customerName);
            //2017/2/23 #421 只有在配置项中的城市和银行 发货时才打印这张清单
//            if (customerName.indexOf("江南环保")!= -1){
            if (this.needPrintHandover(each)){
                _logger.info(each.getId()+" print handover for CK with index:"+index_pos);

                index_pos = each.getIndex_pos();

                sb.append(each.getId()).append("<br>");

                List<PackageItemBean> itemList = packageItemDAO.queryEntityBeansByFK(each.getId());

                // 根据产品分组: 1.判断是否为合成,如是,则要找出子产品;2.数量合并
                for (PackageItemBean eachItem : itemList)
                {
                    if (map.containsKey(eachItem.getProductId()))
                    {
                        PackageItemBean itemBean = map.get(eachItem.getProductId());

                        itemBean.setAmount(itemBean.getAmount() + eachItem.getAmount());
                    }else{
                        PackageItemBean itemBean = new PackageItemBean();

                        itemBean.setProductId(eachItem.getProductId());
                        itemBean.setProductName(eachItem.getProductName());
                        itemBean.setAmount(eachItem.getAmount());
                        //itemBean.setShowSubProductName(showSubProductName);

                        map.put(eachItem.getProductId(), itemBean);
                    }
                }

                printFlag = true;
                break;
            } else{
                continue;
            }
        }

        if (!printFlag){
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已打印完毕");

            return mapping.findForward("error");
        }

        PackageVO batchVO = new PackageVO();

        batchVO.setId(sb.toString());
        batchVO.setPickupId(pickupId);
        batchVO.setRepTime(TimeTools.now_short());

        List<PackageItemBean> lastList = new ArrayList<PackageItemBean>();

        for (Entry<String, PackageItemBean> entry : map.entrySet())
        {
            lastList.add(entry.getValue());
        }

        //2015/1/14 按照名称排序
        Collections.sort(lastList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                PackageItemBean i1 = (PackageItemBean)o1;
                PackageItemBean i2 = (PackageItemBean)o2;
                return i1.getProductName().compareTo(i2.getProductName());
            }
        });

        //2016/10/12 #328 检查临时发票号码
        for (PackageItemBean item : lastList){
            String productName = item.getProductName();
            if (productName!= null && productName.startsWith("发票号：XN")){
                request.setAttribute(KeyConstant.ERROR_MESSAGE, item.getPackageId()+"商品名中存在虚拟发票号:"+productName);

                return mapping.findForward("error");
            }
        }
        batchVO.setItemList(lastList);

        // key:以批次号做为key ?
        request.setAttribute("bean", batchVO);

        request.setAttribute("year", TimeTools.now("yyyy"));
        request.setAttribute("month", TimeTools.now("MM"));
        request.setAttribute("day", TimeTools.now("dd"));

        request.setAttribute("index_pos", index_pos);
        request.setAttribute("pickupCount", pickupCount);

        return mapping.findForward("printHandover");

    }

    private boolean needPrintHandover(PackageVO vo){
        boolean result = false;
        CityBean city = this.cityDAO.find(vo.getCityId());
        if (city!= null){
            List<BankConfigForShip> bankConfigForShips = this.bankConfigForShipDAO.listEntityBeans();
            for (BankConfigForShip config: bankConfigForShips){
                if (vo.getCustomerName().contains(config.getBank())
                        && this.containsCity(config.getCity(), city.getName())){
                    return true;
                }
            }
        }

        return result;
    }

    private boolean containsCity(String city1, String city2){
        String[] cities = city1.split(",");
        for (String city : cities){
            if (city2.equals(city)){
                return true;
            }
        }
        return false;
    }

    public ActionForward preForMergePackages(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response){
        String packageIds = request.getParameter("packageIds");
        request.getSession().setAttribute("packageIds", packageIds);
        _logger.info("***************preForMergePackages**************"+packageIds);

        //运输方式
        List<ExpressBean> expressList = this.expressDAO.listEntityBeans();
        request.setAttribute("expressList", expressList);

        //省市
        List<ProvinceBean> provinceList = this.provinceDAO.listEntityBeans();
        request.setAttribute("provinceList", provinceList);
        List<CityBean> cityList = this.cityDAO.listEntityBeans();
        request.setAttribute("cityList", cityList);
        return mapping.findForward("mergePackages");
    }

    public ActionForward mergePackages(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        String shippingStr = request.getParameter("shipping");
        int shipping = 0;
        if (!StringTools.isNullOrNone(shippingStr)){
            shipping = Integer.valueOf(shippingStr);
        }
        String transport1Str = request.getParameter("transport1");
        int transport1 = 0;
        if (!StringTools.isNullOrNone(transport1Str)){
            try{
                transport1 = Integer.valueOf(transport1Str);
            }catch(Exception e){}
        }
        String transport2Str = request.getParameter("transport2");
        int transport2 = -1;
        if (!StringTools.isNullOrNone(transport2Str)){
            try{
                transport2 = Integer.valueOf(transport2Str);
            }catch(Exception e){}
        }

        String expressPayStr = request.getParameter("expressPay");
        int expressPay = -1;
        if (!StringTools.isNullOrNone(expressPayStr)){
            try{
                expressPay = Integer.valueOf(expressPayStr);
            }catch(Exception e){}
        }

        String transportPayStr = request.getParameter("transportPay");
        int transportPay = -1;
        if (!StringTools.isNullOrNone(transportPayStr)){
            try{
                transportPay = Integer.valueOf(transportPayStr);
            }catch(Exception e){}
        }

        String cityId = request.getParameter("cityId");
        String address = request.getParameter("address");
        String receiver = request.getParameter("receiver");
        String phone = request.getParameter("phone");
        String packageIds = (String)request.getSession().getAttribute("packageIds");
        _logger.info("***mergePickups with param***"+shipping+":"+transport1+":"+transport2+":"+expressPay+":"+transportPay+":"+address+";"+receiver+":"+phone+":"+packageIds);
        try
        {
            this.shipManager.mergePackages(null,packageIds, shipping, transport1, transport2, expressPay, transportPay,cityId, address, receiver, phone );
            request.setAttribute(KeyConstant.MESSAGE, "手动合并出库单成功");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _logger.error("手动合并出库单失败:", e);
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "手动合并出库单出错:"+ e.getMessage());

            return mapping.findForward("queryPickup");
        }

        return mapping.findForward("queryPickup");
//        return this.queryPackage(mapping,form, request, response);
    }

    public ActionForward preForUpdateShipping(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        //运输方式
        List<ExpressBean> expressList = this.expressDAO.listEntityBeans();
        request.setAttribute("expressList", expressList);

        //省市
        List<ProvinceBean> provinceList = this.provinceDAO.listEntityBeans();
        request.setAttribute("provinceList", provinceList);
        List<CityBean> cityList = this.cityDAO.listEntityBeans();
        request.setAttribute("cityList", cityList);
        request.setAttribute("id", id);
        _logger.info("***preForUpdateShipping "+id);
        return mapping.findForward("updateShipping");
    }

    private void fillDistribution(HttpServletRequest rds)
    {
        DistributionBean distributionBean = new DistributionBean();
        BeanUtil.getBean(distributionBean, rds);
    }

    public ActionForward updateShipping(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        try
        {
            DistributionBean distributionBean = new DistributionBean();
            BeanUtil.getBean(distributionBean, request);
            _logger.info(id+"***distribution bean***"+distributionBean);
            //TODO
            this.shipManager.updateShipping(id, distributionBean);
            request.setAttribute(KeyConstant.MESSAGE, "更新发货方式成功");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "更新发货方式出错:"+ e.getMessage());

            return mapping.findForward("queryPickup");
        }

        return mapping.findForward("queryPickup");
    }

    public ActionForward preForAutoPickup(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response){
        _logger.info("***************preForAutoPickup**************");
        return mapping.findForward("addAutoPickup");
    }

    public ActionForward autoPickup(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request, HttpServletResponse response){
        String pickupCount = request.getParameter("pickupCount");
        String productName = request.getParameter("productName");
        String productId = request.getParameter("productId");
        _logger.info("*****autoPickup****************"+pickupCount+";"+productName+":"+productId);
        try
        {
            List<String> pickupList = this.shipManager.autoPickup(Integer.valueOf(pickupCount), productName);
            StringBuilder sb = new StringBuilder();
            for (String pickupId: pickupList){
                sb.append(pickupId)
                    .append(";");
            }
            request.setAttribute(KeyConstant.MESSAGE, "自动捡配成功:"+sb.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            _logger.error("自动捡配失败:", e);
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "自动捡配出错:"+ e.getMessage());

            return mapping.findForward("queryPickup");
        }

        return mapping.findForward("queryPickup");
    }

    public PackageDAO getPackageDAO()
    {
        return packageDAO;
    }

    public void setPackageDAO(PackageDAO packageDAO)
    {
        this.packageDAO = packageDAO;
    }

    public PackageItemDAO getPackageItemDAO()
    {
        return packageItemDAO;
    }

    public void setPackageItemDAO(PackageItemDAO packageItemDAO)
    {
        this.packageItemDAO = packageItemDAO;
    }

    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
    }

    public DistributionDAO getDistributionDAO()
    {
        return distributionDAO;
    }

    public void setDistributionDAO(DistributionDAO distributionDAO)
    {
        this.distributionDAO = distributionDAO;
    }

    public ExpressDAO getExpressDAO()
    {
        return expressDAO;
    }

    public void setExpressDAO(ExpressDAO expressDAO)
    {
        this.expressDAO = expressDAO;
    }

    public ShipManager getShipManager()
    {
        return shipManager;
    }

    public void setShipManager(ShipManager shipManager)
    {
        this.shipManager = shipManager;
    }

    public ProductDAO getProductDAO()
    {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    public ComposeProductDAO getComposeProductDAO()
    {
        return composeProductDAO;
    }

    public void setComposeProductDAO(ComposeProductDAO composeProductDAO)
    {
        this.composeProductDAO = composeProductDAO;
    }

    public ComposeItemDAO getComposeItemDAO()
    {
        return composeItemDAO;
    }

    public void setComposeItemDAO(ComposeItemDAO composeItemDAO)
    {
        this.composeItemDAO = composeItemDAO;
    }

    /**
     * @return the outImportDAO
     */
    public OutImportDAO getOutImportDAO()
    {
        return outImportDAO;
    }

    /**
     * @param outImportDAO the outImportDAO to set
     */
    public void setOutImportDAO(OutImportDAO outImportDAO)
    {
        this.outImportDAO = outImportDAO;
    }

    /**
     * @return the packageVSCustomerDAO
     */
    public PackageVSCustomerDAO getPackageVSCustomerDAO()
    {
        return packageVSCustomerDAO;
    }

    /**
     * @param packageVSCustomerDAO the packageVSCustomerDAO to set
     */
    public void setPackageVSCustomerDAO(PackageVSCustomerDAO packageVSCustomerDAO)
    {
        this.packageVSCustomerDAO = packageVSCustomerDAO;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }

    /**
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
    }

    public BranchRelationDAO getBranchRelationDAO() {
        return branchRelationDAO;
    }

    public void setBranchRelationDAO(BranchRelationDAO branchRelationDAO) {
        this.branchRelationDAO = branchRelationDAO;
    }

    public StafferDAO getStafferDAO() {
        return stafferDAO;
    }

    public void setStafferDAO(StafferDAO stafferDAO) {
        this.stafferDAO = stafferDAO;
    }

    public InvoiceinsDAO getInvoiceinsDAO() {
        return invoiceinsDAO;
    }

    public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO) {
        this.invoiceinsDAO = invoiceinsDAO;
    }

    public CustomerMainDAO getCustomerMainDAO() {
        return customerMainDAO;
    }

    public void setCustomerMainDAO(CustomerMainDAO customerMainDAO) {
        this.customerMainDAO = customerMainDAO;
    }

    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    public CityDAO getCityDAO() {
        return cityDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    public ProductVSBankDAO getProductVSBankDAO() {
        return productVSBankDAO;
    }

    public void setProductVSBankDAO(ProductVSBankDAO productVSBankDAO) {
        this.productVSBankDAO = productVSBankDAO;
    }

    public CiticVSOAProductDAO getCiticVSOAProductDAO() {
        return citicVSOAProductDAO;
    }

    public void setCiticVSOAProductDAO(CiticVSOAProductDAO citicVSOAProductDAO) {
        this.citicVSOAProductDAO = citicVSOAProductDAO;
    }

    public EnumDAO getEnumDAO() {
        return enumDAO;
    }

    public void setEnumDAO(EnumDAO enumDAO) {
        this.enumDAO = enumDAO;
    }

    public ProductImportDAO getProductImportDAO() {
        return productImportDAO;
    }

    public void setProductImportDAO(ProductImportDAO productImportDAO) {
        this.productImportDAO = productImportDAO;
    }

    public InvoiceDAO getInvoiceDAO() {
        return invoiceDAO;
    }

    public void setInvoiceDAO(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    public InvoiceinsItemDAO getInvoiceinsItemDAO() {
        return invoiceinsItemDAO;
    }

    public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO) {
        this.invoiceinsItemDAO = invoiceinsItemDAO;
    }

    public BankConfigForShipDAO getBankConfigForShipDAO() {
        return bankConfigForShipDAO;
    }

    public void setBankConfigForShipDAO(BankConfigForShipDAO bankConfigForShipDAO) {
        this.bankConfigForShipDAO = bankConfigForShipDAO;
    }
}