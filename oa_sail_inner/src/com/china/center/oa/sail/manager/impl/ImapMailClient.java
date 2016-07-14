package com.china.center.oa.sail.manager.impl;

/**
 * Created by user on 2016/4/8.
 */
import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.client.bean.CustomerBean;
import com.china.center.oa.client.bean.CustomerDistAddrBean;
import com.china.center.oa.client.dao.CustomerDistAddrDAO;
import com.china.center.oa.client.dao.CustomerMainDAO;
import com.china.center.oa.client.dao.StafferVSCustomerDAO;
import com.china.center.oa.client.vs.StafferVSCustomerBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.ProductImportBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductImportDAO;
import com.china.center.oa.publics.bean.EnumBean;
import com.china.center.oa.publics.dao.EnumDAO;
import com.china.center.oa.sail.bean.*;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.CiticOrderDAO;
import com.china.center.oa.sail.dao.PfOrderDAO;
import com.china.center.oa.sail.dao.ZsOrderDAO;
import com.china.center.oa.sail.dao.ZyOrderDAO;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.sun.mail.imap.IMAPMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class ImapMailClient {
    private final Log _logger = LogFactory.getLog(getClass());

    public static final String IMAP = "imap";

    public static final String CITIC = "citic";

    public static final String ZS = "zhaoshang";

    public static final String PF = "pufa";

    public static final String ZY = "zhongyuan";

    public static enum MailType {unknown, citic, zs, pf, zy}

    private CiticOrderDAO citicOrderDAO = null;

    private ZsOrderDAO zsOrderDAO = null;

    private PfOrderDAO pfOrderDAO = null;

    private ZyOrderDAO zyOrderDAO = null;

    private ProductImportDAO productImportDAO = null;

    private ProductDAO productDAO = null;

    private CustomerMainDAO customerMainDAO = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    private CustomerDistAddrDAO  customerDistAddrDAO = null;

    private EnumDAO enumDAO = null;

    public EnumDAO getEnumDAO() {
        return enumDAO;
    }

    public void setEnumDAO(EnumDAO enumDAO) {
        this.enumDAO = enumDAO;
    }

    public CustomerDistAddrDAO getCustomerDistAddrDAO() {
        return customerDistAddrDAO;
    }

    public void setCustomerDistAddrDAO(CustomerDistAddrDAO customerDistAddrDAO) {
        this.customerDistAddrDAO = customerDistAddrDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductImportDAO getProductImportDAO() {
        return productImportDAO;
    }

    public void setProductImportDAO(ProductImportDAO productImportDAO) {
        this.productImportDAO = productImportDAO;
    }

    public CustomerMainDAO getCustomerMainDAO() {
        return customerMainDAO;
    }

    public void setCustomerMainDAO(CustomerMainDAO customerMainDAO) {
        this.customerMainDAO = customerMainDAO;
    }

    public StafferVSCustomerDAO getStafferVSCustomerDAO() {
        return stafferVSCustomerDAO;
    }

    public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO) {
        this.stafferVSCustomerDAO = stafferVSCustomerDAO;
    }

    public CiticOrderDAO getCiticOrderDAO() {
        return citicOrderDAO;
    }

    public void setCiticOrderDAO(CiticOrderDAO citicOrderDAO) {
        this.citicOrderDAO = citicOrderDAO;
    }

    public ZsOrderDAO getZsOrderDAO() {
        return zsOrderDAO;
    }

    public void setZsOrderDAO(ZsOrderDAO zsOrderDAO) {
        this.zsOrderDAO = zsOrderDAO;
    }

    public PfOrderDAO getPfOrderDAO() {
        return pfOrderDAO;
    }

    public void setPfOrderDAO(PfOrderDAO pfOrderDAO) {
        this.pfOrderDAO = pfOrderDAO;
    }

    public ZyOrderDAO getZyOrderDAO() {
        return zyOrderDAO;
    }

    public void setZyOrderDAO(ZyOrderDAO zyOrderDAO) {
        this.zyOrderDAO = zyOrderDAO;
    }

//    private static Set<String> citicNoSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public static void main(String[] args) throws Exception{
        ImapMailClient client = new ImapMailClient();
//        client.receiveEmail("imap.163.com", "yycoindd@163.com", "yycoin1234");
        client.receiveEmail("imap.exmail.qq.com", "yycoinoa@yycoin.com", "Yycoin135");

//        InputStream is = new FileInputStream("G:\\Download\\贵金属订单明细报表(永银文化0425).xls");
//        InputStream is = new FileInputStream("D:\\oa_attachment\\贵金属订单明细报表(永银文化0425).xls");
//        List<CiticOrderBean> beans = client.parseCiticOrder(is);
//        System.out.println(beans.size());

    }

    /**
     *
     * @param host
     * @param username
     * @param password
     * @return lis of mailId
     * @throws Exception
     */
    public List<String> receiveEmail(String host, String username, String password) throws Exception {
        List<String> mailList = new ArrayList<String>();
        String port = "993";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.imap.socketFactory.port", port);
        props.setProperty("mail.smtp.starttls.enable", "true");

        props.setProperty("mail.store.protocol", ImapMailClient.IMAP);
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", port);
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.auth.plain.disable", "true");
        props.setProperty("mail.imap.auth.login.disable", "true");

        //You must add these two settings, otherwize large attachment will not be downloaded
        props.put("mail.imap.partialfetch", "true");
        props.put("mail.imap.fetchsize", "819200");
        Session session = Session.getDefaultInstance(props, null);
//        session.setDebug(true);
        Store store = session.getStore(IMAP);
        Folder inbox = null;

        try {
            store.connect(host, username, password);
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.ENVELOPE);
//            Message[] messages = inbox.getMessages();
            //only receive unread mails
            Message messages[] = inbox.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
            inbox.fetch(messages, profile);
            _logger.info("***unread mail count***" + inbox.getUnreadMessageCount());
            System.out.println("***unread mail count***" + inbox.getUnreadMessageCount());

            int count = 0 ;
            for (Message message : messages) {
                IMAPMessage msg = (IMAPMessage) message;
                Flags flags = message.getFlags();
                if (flags.contains(Flags.Flag.SEEN)){
                    continue;
                }
//                String from = decodeText(msg.getFrom()[0].toString());
//                InternetAddress ia = new InternetAddress(from);
//                Enumeration headers = msg.getAllHeaders();
//                while (headers.hasMoreElements()) {
//                    Header header = (Header) headers.nextElement();
//                }
                try{
                    count ++;
                    String fromEmail = ((InternetAddress) msg.getFrom()[0]).getAddress();
                    MailType mailType = this.getOrderTypeByEmail(fromEmail);
                    String subject = msg.getSubject();
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String mailId = "";
                    if (mailType == MailType.citic){
                        mailId = subject+"_citic_"+sdf.format(now);
                    } else if (mailType == MailType.zs){
                        mailId = subject+"_zhaoshang_"+sdf.format(now);
                    } else if (mailType == MailType.pf){
                        mailId = subject+"_pufa_"+sdf.format(now);
                    } else if (mailType == MailType.zy){
                        mailId = subject+"_"+ZY+"_"+sdf.format(now);
                    } else{
                        mailId = subject+"_"+sdf.format(now);
                    }

                    _logger.info(count+" begin download mailId "+mailId);
                    if(mailType == MailType.unknown){
                        continue;
                    }

                    parseMultipart(msg.getContent(), mailType, mailId);
                    mailList.add(mailId);
                }catch(Exception e){
                    e.printStackTrace();
                }
                msg.setFlag(Flags.Flag.SEEN, true);
            }
        } catch(Exception e){
           e.printStackTrace();
        } finally {
            try {
                if (inbox != null) {
                    inbox.close(false);
                }
            } catch (Exception ignored) {
            }
            try {
                store.close();
            } catch (Exception ignored) {}
        }

        _logger.info("***Finish download mail***");
        return mailList;
    }

    /**
     * convertCitic temp order table to OA table with import method
     * @param mailId
     */
    public Map<String,List<OutImportBean>> convertToOutImport(String mailId){
        _logger.info("***convertToOutImport with mailId "+mailId);
        Map<String,List<OutImportBean>> mail2ImportMap = new HashMap<String,List<OutImportBean>>();
        //成功和失败分成两个批次
        List<OutImportBean> importItemListSuccess = new ArrayList<OutImportBean>();
        List<OutImportBean> importItemListFail = new ArrayList<OutImportBean>();
        mail2ImportMap.put(mailId+"_success", importItemListSuccess);
        mail2ImportMap.put(mailId+"_fail", importItemListFail);

        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("mailId","=",mailId);
        //status=0 not imported
        conditionParse.addCondition("status", "=", 0);
        if (mailId.indexOf(CITIC)!= -1) {
            List<CiticOrderBean> citicOrderBeans = this.citicOrderDAO.queryEntityBeansByCondition(conditionParse);
            _logger.info("***import citic orders with size:"+citicOrderBeans.size());
            if (!ListTools.isEmptyOrNull(citicOrderBeans)){
                 for(CiticOrderBean citicOrderBean: citicOrderBeans){
                     String citicNo = citicOrderBean.getCiticNo();
                     ConditionParse conditionParse1 = new ConditionParse();
                     conditionParse1.addCondition("citicNo","=",citicNo);
                     conditionParse1.addCondition("status","=",1);
                     List<CiticOrderBean> beans = this.citicOrderDAO.queryEntityBeansByCondition(conditionParse1);
                     if (!ListTools.isEmptyOrNull(beans)){
                         _logger.error(citicOrderBean+" is duplicate***");
                         continue;
                     }
                     try {
                         OutImportBean bean = this.convertCitic(citicOrderBean);
                         importItemListSuccess.add(bean);
                     }catch(Exception e){
                         _logger.error(e);
                         if (e instanceof MailOrderException){
                             MailOrderException moe = (MailOrderException)e;
                             if(moe.getOrder() instanceof OutImportBean){
                                 OutImportBean order = (OutImportBean)moe.getOrder();
                                 order.setResult(moe.getErrorContent());
                                 //失败批次
                                 order.setStatus(3);
                                 importItemListFail.add(order);
                             }
                         }
                     }
                 }
            }
        } else if (mailId.indexOf(ZS)!= -1) {
            List<ZsOrderBean> zsOrderBeans = this.zsOrderDAO.queryEntityBeansByCondition(conditionParse);
            _logger.info("***import zhaoshang orders with size:" + zsOrderBeans.size());
            if (!ListTools.isEmptyOrNull(zsOrderBeans)){
                for(ZsOrderBean zsOrderBean : zsOrderBeans){
                    String citicNo = zsOrderBean.getCiticNo();
                    ConditionParse conditionParse1 = new ConditionParse();
                    conditionParse1.addCondition("citicNo","=",citicNo);
                    conditionParse1.addCondition("status","=",1);
                    List<ZsOrderBean> beans = this.zsOrderDAO.queryEntityBeansByCondition(conditionParse1);
                    if (!ListTools.isEmptyOrNull(beans)){
                        _logger.error(zsOrderBean +" is duplicate***");
                        continue;
                    }
                    try {
                        OutImportBean bean = this.convertZs(zsOrderBean);
                        importItemListSuccess.add(bean);
                    }catch(Exception e){
                        _logger.error(e);
                        if (e instanceof MailOrderException){
                            MailOrderException moe = (MailOrderException)e;
                            if(moe.getOrder() instanceof OutImportBean){
                                OutImportBean order = (OutImportBean)moe.getOrder();
                                order.setResult(moe.getErrorContent());
                                //失败批次
                                order.setStatus(3);
                                importItemListFail.add(order);
                            }
                        }
                    }
                }
            }
        }  else if (mailId.indexOf(PF)!= -1) {
            List<PfOrderBean> zsOrderBeans = this.pfOrderDAO.queryEntityBeansByCondition(conditionParse);
            _logger.info("***import pufa orders with size:" + zsOrderBeans.size());
            if (!ListTools.isEmptyOrNull(zsOrderBeans)){
                for(PfOrderBean zsOrderBean : zsOrderBeans){
                    String citicNo = zsOrderBean.getCiticNo();
                    ConditionParse conditionParse1 = new ConditionParse();
                    conditionParse1.addCondition("citicNo","=",citicNo);
                    conditionParse1.addCondition("status","=",1);
                    List<PfOrderBean> beans = this.pfOrderDAO.queryEntityBeansByCondition(conditionParse1);
                    if (!ListTools.isEmptyOrNull(beans)){
                        _logger.error(zsOrderBean +" is duplicate***");
                        continue;
                    }
                    try {
                        OutImportBean bean = this.convertPf(zsOrderBean);
                        importItemListSuccess.add(bean);
                    }catch(Exception e){
                        _logger.error(e);
                        if (e instanceof MailOrderException){
                            MailOrderException moe = (MailOrderException)e;
                            if(moe.getOrder() instanceof OutImportBean){
                                OutImportBean order = (OutImportBean)moe.getOrder();
                                order.setResult(moe.getErrorContent());
                                //失败批次
                                order.setStatus(3);
                                importItemListFail.add(order);
                            }
                        }
                    }
                }
            }
        } else if (mailId.indexOf(ZY)!= -1) {
            List<ZyOrderBean> zsOrderBeans = this.zyOrderDAO.queryEntityBeansByCondition(conditionParse);
            _logger.info("***import zhongyuan orders with size:" + zsOrderBeans.size());
            if (!ListTools.isEmptyOrNull(zsOrderBeans)){
                for(ZyOrderBean zsOrderBean : zsOrderBeans){
                    String citicNo = zsOrderBean.getCiticNo();
                    ConditionParse conditionParse1 = new ConditionParse();
                    conditionParse1.addCondition("citicNo","=",citicNo);
                    conditionParse1.addCondition("status","=",1);
                    List<ZyOrderBean> beans = this.zyOrderDAO.queryEntityBeansByCondition(conditionParse1);
                    if (!ListTools.isEmptyOrNull(beans)){
                        _logger.error(zsOrderBean +" is duplicate***");
                        continue;
                    }
                    try {
                        OutImportBean bean = this.convertZy(zsOrderBean);
                        importItemListSuccess.add(bean);
                    }catch(Exception e){
                        _logger.error(e);
                        if (e instanceof MailOrderException){
                            MailOrderException moe = (MailOrderException)e;
                            if(moe.getOrder() instanceof OutImportBean){
                                OutImportBean order = (OutImportBean)moe.getOrder();
                                order.setResult(moe.getErrorContent());
                                //失败批次
                                order.setStatus(3);
                                importItemListFail.add(order);
                            }
                        }
                    }
                }
            }
        }

        _logger.info("***import orders size***"+importItemListSuccess.size()+"***"+importItemListFail.size());
        return mail2ImportMap;
    }

    private   OutImportBean convertCitic(CiticOrderBean orderBean) throws MailOrderException{
        OutImportBean bean = new OutImportBean();

        bean.setImportFromMail(1);
        bean.setLogTime(TimeTools.now());
        // 操作人
        bean.setReason("import_from_mail");
        bean.setCiticNo(orderBean.getCiticNo());
        bean.setCiticOrderDate(orderBean.getCiticOrderDate());

        bean.setBranchName(orderBean.getBranchName());
        bean.setSecondBranch(orderBean.getSecondBranch());


        //订单类型默认"销售出库"
        bean.setOutType(0);
        String custName = orderBean.getComunicatonBranchName()+"-银行";
        bean.setComunicatonBranchName(custName);

        bean.setFirstName("N/A");
        bean.setAmount(orderBean.getAmount());
        bean.setPrice(orderBean.getPrice());
        bean.setValue(orderBean.getValue());

        bean.setIbMoney(orderBean.getFee()/orderBean.getAmount());
        bean.setArriveDate(orderBean.getArriveDate());


        //TODO 开票性质
//        bean.setInvoiceNature(orderBean.getInvoiceNature());
        bean.setInvoiceHead(orderBean.getInvoiceHead());
        bean.setInvoiceCondition(orderBean.getInvoiceCondition());


        //库存默认 公共库-南京物流中心
        bean.setDepotId(DepotConstant.CENTER_DEPOT_ID);
        //默认为南京物流中心-物流中心库(销售可发)仓区
        bean.setDepotpartId("1");
        bean.setComunicationBranch(DepotConstant.DEFAULT_DEPOT_PART);

        bean.setProductName(orderBean.getProductName());
        bean.setProductCode(orderBean.getProductCode());
        bean.setDescription("Mail_" + orderBean.getMailId());

        CustomerBean cBean = customerMainDAO.findByUnique(custName);
        if (null == cBean)
        {
            String msg = "网点名称不存在："+custName;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }else{
            bean.setCustomerId(cBean.getId());
            if (bean.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH)
            {

                StafferVSCustomerBean vsBean = stafferVSCustomerDAO.findByUnique(cBean.getId());
                if (null == vsBean)
                {
                    String msg = "网点名称没有与业务员挂靠关系："+custName;
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }else{
                    //职员取客户对应的业务员，如果没有，此单和姓氏一样处理
                    bean.setStafferId(vsBean.getStafferId());
                }
            }else{
                bean.setComunicatonBranchName("公共客户");
            }
        }

        //TODO 凡是 中信银行重庆XXXX的就取邮件里的地址信息,其他的取客户的默认办公地址
        if (custName.contains("中信银行重庆")){
            //TODO 发货方式 运输方式 支付方式
            bean.setShipping(0);
            bean.setProvinceId(orderBean.getProvinceId());
            bean.setCityId(orderBean.getCityId());
            bean.setAddress(orderBean.getAddress());
            bean.setReceiver(orderBean.getReceiver());
            bean.setHandPhone(orderBean.getReceiverMobile());
        } else{
            String customerId = bean.getCustomerId();
            boolean found = false;

            List<CustomerDistAddrBean> customerDistAddrBeans = this.customerDistAddrDAO.queryEntityBeansByFK(customerId);
            if (!ListTools.isEmptyOrNull(customerDistAddrBeans)){
                for (CustomerDistAddrBean addr : customerDistAddrBeans){
                    ConditionParse conditionParse = new ConditionParse();
                    conditionParse.addWhereStr();
                    conditionParse.addCondition("type","=","303");
                    conditionParse.addCondition("keyss","=",addr.getAtype());
                    List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                    if (!ListTools.isEmptyOrNull(enumBeans)){
                        EnumBean enumBean = enumBeans.get(0);
                        if ("办公地址".equals(enumBean.getValue())){
                            found = true;
                            bean.setShipping(addr.getShipping());
                            bean.setTransport1(addr.getTransport1());
                            bean.setExpressPay(addr.getExpressPay());
                            bean.setTransport2(addr.getTransport2());
                            bean.setTransportPay(addr.getTransportPay());
                            bean.setProvinceId(addr.getProvinceId());
                            bean.setCityId(addr.getCityId());
                            bean.setAddress(addr.getAddress());
                            bean.setReceiver(addr.getContact());
                            bean.setHandPhone(addr.getTelephone());
                            break;
                        }
                    }
                }
            }

            if (!found){
                String msg = "没有默认办公地址："+customerId;
                _logger.error(msg);
                throw new MailOrderException(msg, bean);
            }
        }

        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("bankProductCode", "=", orderBean.getProductCode());
        List<ProductImportBean> productImportBeans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
        if (!ListTools.isEmptyOrNull(productImportBeans)){
            ProductImportBean productImportBean = productImportBeans.get(0);
            String code = productImportBean.getCode();
            _logger.info(orderBean.getProductCode()+" product import vs product code***"+code);
            ProductBean productBean = this.productDAO.findByUnique(code);
            if (productBean == null){
                String msg = "产品编码不存在:"+orderBean.getProductCode();
                _logger.error(msg);
                throw new MailOrderException(msg, bean);
            } else{
                bean.setProductId(productBean.getId());
                bean.setProductName(productBean.getName());
                bean.setProductCode(code);
                //激励金额取t_center_product_import中的motivationmoney
                bean.setMotivationMoney(productImportBean.getMotivationMoney());

                //购买日期必须满足(上线时间，下线时间)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date citicOrderDate = null;
                try{
                    citicOrderDate = sdf.parse(bean.getCiticOrderDate());
                }catch(Exception e){
                    String msg = "购买日期必须为XXXX-XX-XX格式:"+orderBean.getCiticOrderDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date begin = null;
                try{
                    begin = sdf.parse(productImportBean.getOnMarketDate());
                }catch(Exception e){
                    String msg = "上线时间必须为XXXX-XX-XX格式:"+productImportBean.getOnMarketDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date end = null;
                if (!StringTools.isNullOrNone(productImportBean.getOfflineDate())) {
                    try{
                        end = sdf.parse(productImportBean.getOfflineDate());
                    }catch(Exception e){
                        String msg = "下线时间必须为XXXX-XX-XX格式:"+productImportBean.getOfflineDate();
                        _logger.error(msg);
                        throw new MailOrderException(msg, bean);
                    }
                }

                if (citicOrderDate!= null && begin!= null && end!= null
                        && !citicOrderDate.before(begin) && !citicOrderDate.after(end)){
                    _logger.info(citicOrderDate+"***citicOrderDate in range***"+begin+"**"+end);
                } else if (citicOrderDate!= null && begin!= null && end == null
                        && !citicOrderDate.before(begin)){
                    _logger.info(citicOrderDate+"***citicOrderDate >=***"+begin);
                } else{
                    String msg = "购买日期必须位于:"+productImportBean.getOnMarketDate()+"-"+productImportBean.getOfflineDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }
            }
        } else{
            String msg = "产品编码不存在:"+orderBean.getProductCode();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }


        //加一条规则，如果银行品名里含"姓氏“字符的，单独拆出来为一个批次导入，导入结果为失败，就放那
        if(bean.getProductName().contains("姓氏")){
            String msg = "银行品名不能包含姓氏:"+orderBean.getProductName();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        _logger.info("***convertCitic bean success***"+bean);
        return bean;
    }

    private   OutImportBean convertZs(ZsOrderBean orderBean) throws MailOrderException{
        OutImportBean bean = new OutImportBean();

        bean.setImportFromMail(1);
        bean.setLogTime(TimeTools.now());
        // 操作人
        bean.setReason("import_from_mail");
        bean.setCiticNo(orderBean.getCiticNo());
        bean.setCiticOrderDate(orderBean.getDealDate());

        bean.setBranchName(orderBean.getBranchName());
//        bean.setSecondBranch(orderBean.getSecondBranch());


        //订单类型默认"销售出库"
        bean.setOutType(0);
        String branchName = orderBean.getComunicatonBranchName();
        String custName = orderBean.getComunicatonBranchName();
        String[] temp = branchName.split(":");
        if (temp.length == 2){
            custName = "招商银行"+temp[1]+"-银行";
        }

        bean.setComunicatonBranchName(custName);

        bean.setFirstName("N/A");
        bean.setAmount(orderBean.getAmount());
        bean.setValue(orderBean.getValue());
        bean.setPrice(orderBean.getValue() / bean.getAmount());
        bean.setIbMoney(orderBean.getFee() / bean.getAmount());

        //TODO 开票性质
        bean.setInvoiceHead(orderBean.getInvoiceHead());
        bean.setInvoiceCondition(orderBean.getInvoiceCondition());

        //库存默认 公共库-南京物流中心
        bean.setDepotId(DepotConstant.CENTER_DEPOT_ID);
        //默认为南京物流中心-物流中心库(销售可发)仓区
        bean.setDepotpartId("1");
        bean.setComunicationBranch(DepotConstant.DEFAULT_DEPOT_PART);

        bean.setProductName(orderBean.getProductName());
        bean.setProductCode(orderBean.getProductCode());
        bean.setDescription("Mail_" + orderBean.getMailId());

        CustomerBean cBean = customerMainDAO.findByUnique(custName);
        if (null == cBean)
        {
            String msg = "网点名称不存在："+custName;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }else{
            bean.setCustomerId(cBean.getId());
            if (bean.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH)
            {

                StafferVSCustomerBean vsBean = stafferVSCustomerDAO.findByUnique(cBean.getId());
                if (null == vsBean)
                {
                    String msg = "网点名称没有与业务员挂靠关系："+custName;
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }else{
                    //职员取客户对应的业务员，如果没有，此单和姓氏一样处理
                    bean.setStafferId(vsBean.getStafferId());
                }
            }else{
                bean.setComunicatonBranchName("公共客户");
            }
        }

        //取客户的默认办公地址
        String customerId = bean.getCustomerId();
        boolean found = false;
        List<CustomerDistAddrBean> customerDistAddrBeans = this.customerDistAddrDAO.queryEntityBeansByFK(customerId);
        if (!ListTools.isEmptyOrNull(customerDistAddrBeans)){
            for (CustomerDistAddrBean addr : customerDistAddrBeans){
                ConditionParse conditionParse = new ConditionParse();
                conditionParse.addWhereStr();
                conditionParse.addCondition("type","=","303");
                conditionParse.addCondition("keyss","=",addr.getAtype());
                List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(enumBeans)){
                    EnumBean enumBean = enumBeans.get(0);
                    if ("办公地址".equals(enumBean.getValue())){
                        found = true;
                        bean.setShipping(addr.getShipping());
                        bean.setTransport1(addr.getTransport1());
                        bean.setExpressPay(addr.getExpressPay());
                        bean.setTransport2(addr.getTransport2());
                        bean.setTransportPay(addr.getTransportPay());
                        bean.setProvinceId(addr.getProvinceId());
                        bean.setCityId(addr.getCityId());
                        bean.setAddress(addr.getAddress());
                        bean.setReceiver(addr.getContact());
                        bean.setHandPhone(addr.getTelephone());
                        break;
                    }
                }
            }
        }

        if (!found){
            String msg = "没有默认办公地址："+customerId;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("bankProductCode", "=", orderBean.getProductCode());
        List<ProductImportBean> productImportBeans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
        if (!ListTools.isEmptyOrNull(productImportBeans)){
            ProductImportBean productImportBean = productImportBeans.get(0);
            String code = productImportBean.getCode();
            _logger.info(orderBean.getProductCode()+" product import vs product code***"+code);
            ProductBean productBean = this.productDAO.findByUnique(code);
            if (productBean == null){
                String msg = "产品编码不存在:"+orderBean.getProductCode();
                _logger.error(msg);
                throw new MailOrderException(msg, bean);
            } else{
                bean.setProductId(productBean.getId());
                bean.setProductName(productBean.getName());
                bean.setProductCode(code);
                //激励金额取t_center_product_import中的motivationmoney
                bean.setMotivationMoney(productImportBean.getMotivationMoney());

                //购买日期必须满足(上线时间，下线时间)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date citicOrderDate = null;
                try{
                    citicOrderDate = sdf.parse(bean.getCiticOrderDate());
                }catch(Exception e){
                    String msg = "购买日期必须为XXXX-XX-XX格式:"+orderBean.getDealDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date begin = null;
                try{
                    begin = sdf.parse(productImportBean.getOnMarketDate());
                }catch(Exception e){
                    String msg = "上线时间必须为XXXX-XX-XX格式:"+productImportBean.getOnMarketDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date end = null;
                if (!StringTools.isNullOrNone(productImportBean.getOfflineDate())) {
                    try{
                        end = sdf.parse(productImportBean.getOfflineDate());
                    }catch(Exception e){
                        String msg = "下线时间必须为XXXX-XX-XX格式:"+productImportBean.getOfflineDate();
                        _logger.error(msg);
                        throw new MailOrderException(msg, bean);
                    }
                }

                if (citicOrderDate!= null && begin!= null && end!= null
                        && !citicOrderDate.before(begin) && !citicOrderDate.after(end)){
                    _logger.info(citicOrderDate+"***citicOrderDate in range***"+begin+"**"+end);
                } else if (citicOrderDate!= null && begin!= null && end == null
                        && !citicOrderDate.before(begin)){
                    _logger.info(citicOrderDate+"***citicOrderDate >=***"+begin);
                } else{
                    String msg = "购买日期必须位于:"+productImportBean.getOnMarketDate()+"-"+productImportBean.getOfflineDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }
            }
        } else{
            String msg = "产品编码不存在:"+orderBean.getProductCode();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }


        //加一条规则，如果银行品名里含"姓氏“字符的，单独拆出来为一个批次导入，导入结果为失败，就放那
        if(bean.getProductName().contains("姓氏")){
            String msg = "银行品名不能包含姓氏:"+orderBean.getProductName();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        _logger.info("***convert zhaoshang bean success***"+bean);
        return bean;
    }

    private   OutImportBean convertPf(PfOrderBean orderBean) throws MailOrderException{
        OutImportBean bean = new OutImportBean();

        bean.setImportFromMail(1);
        bean.setLogTime(TimeTools.now());
        // 操作人
        bean.setReason("import_from_mail");
        bean.setCiticNo(orderBean.getCiticNo());
        //转为XXXX-XX-XX格式
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date dealDate = null;
        try{
            dealDate = sdf1.parse(orderBean.getDealDate());
        }catch(Exception e){
           _logger.error(e);
        }

        if (dealDate == null){
            bean.setCiticOrderDate(orderBean.getDealDate());
        } else{
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            bean.setCiticOrderDate(sdf2.format(dealDate));
        }

        bean.setBranchName(orderBean.getBranchName());


        //订单类型默认"销售出库"
        bean.setOutType(0);

        String custName = orderBean.getComunicatonBranchName()+"-银行";
        String customerName = orderBean.getComunicatonBranchName();
        if (customerName.startsWith("浦发银行")){
            custName = orderBean.getComunicatonBranchName()+"-银行";
        } else if (customerName.startsWith("浦发")){
            //如没有“银行”字样，则将“浦发”更改为“浦发银行”
            custName = orderBean.getComunicatonBranchName().replace("浦发","浦发银行")+"-银行";
        } else if (customerName.startsWith("上海浦东发展银行")){
            //例如有“上海浦发发展银行XXXX”，自动将前缀改为 "浦发银行XXXX"
            custName = orderBean.getComunicatonBranchName().replace("上海浦东发展银行","浦发银行")+"-银行";
        }  else{
            //加上前缀"浦发银行"
            custName = "浦发银行"+orderBean.getComunicatonBranchName()+"-银行";
        }
        bean.setComunicatonBranchName(custName);

        bean.setFirstName("N/A");
        bean.setAmount(orderBean.getAmount());
        bean.setPrice(orderBean.getPrice());
        bean.setValue(orderBean.getAmount() * orderBean.getPrice());

        //库存默认 公共库-南京物流中心
        bean.setDepotId(DepotConstant.CENTER_DEPOT_ID);
        //默认为南京物流中心-物流中心库(销售可发)仓区
        bean.setDepotpartId("1");
        bean.setComunicationBranch(DepotConstant.DEFAULT_DEPOT_PART);

        bean.setProductName(orderBean.getProductName());
        bean.setProductCode(orderBean.getProductCode());
        bean.setDescription(orderBean.getDescription() + "_Mail_" + orderBean.getMailId());

        CustomerBean cBean = customerMainDAO.findByUnique(custName);
        if (null == cBean)
        {
            String msg = "网点名称不存在："+custName;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }else{
            bean.setCustomerId(cBean.getId());
            if (bean.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH)
            {

                StafferVSCustomerBean vsBean = stafferVSCustomerDAO.findByUnique(cBean.getId());
                if (null == vsBean)
                {
                    String msg = "网点名称没有与业务员挂靠关系："+custName;
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }else{
                    //职员取客户对应的业务员，如果没有，此单和姓氏一样处理
                    bean.setStafferId(vsBean.getStafferId());
                }
            }else{
                bean.setComunicatonBranchName("公共客户");
            }
        }

        //取客户的默认办公地址
        String customerId = bean.getCustomerId();
        boolean found = false;
        List<CustomerDistAddrBean> customerDistAddrBeans = this.customerDistAddrDAO.queryEntityBeansByFK(customerId);
        if (!ListTools.isEmptyOrNull(customerDistAddrBeans)){
            for (CustomerDistAddrBean addr : customerDistAddrBeans){
                ConditionParse conditionParse = new ConditionParse();
                conditionParse.addWhereStr();
                conditionParse.addCondition("type","=","303");
                conditionParse.addCondition("keyss","=",addr.getAtype());
                List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(enumBeans)){
                    EnumBean enumBean = enumBeans.get(0);
                    if ("办公地址".equals(enumBean.getValue())){
                        found = true;
                        bean.setShipping(addr.getShipping());
                        bean.setTransport1(addr.getTransport1());
                        bean.setExpressPay(addr.getExpressPay());
                        bean.setTransport2(addr.getTransport2());
                        bean.setTransportPay(addr.getTransportPay());
                        bean.setProvinceId(addr.getProvinceId());
                        bean.setCityId(addr.getCityId());
                        bean.setAddress(addr.getAddress());
                        bean.setReceiver(addr.getContact());
                        bean.setHandPhone(addr.getTelephone());
                        break;
                    }
                }
            }
        }

        if (!found){
            String msg = "没有默认办公地址："+customerId;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("bankProductCode", "=", orderBean.getProductCode());
        List<ProductImportBean> productImportBeans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
        if (!ListTools.isEmptyOrNull(productImportBeans)){
            ProductImportBean productImportBean = productImportBeans.get(0);
            String code = productImportBean.getCode();
            _logger.info(orderBean.getProductCode()+" product import vs product code***"+code);
            ProductBean productBean = this.productDAO.findByUnique(code);
            if (productBean == null){
                String msg = "产品编码不存在:"+orderBean.getProductCode();
                _logger.error(msg);
                throw new MailOrderException(msg, bean);
            } else{
                bean.setProductId(productBean.getId());
                bean.setProductName(productBean.getName());
                bean.setProductCode(code);
                //激励金额取t_center_product_import中的motivationmoney
                bean.setMotivationMoney(productImportBean.getMotivationMoney());
                //TODO 中收金额
                bean.setIbMoney(productImportBean.getIbMoney());

                //购买日期必须满足(上线时间，下线时间)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date citicOrderDate = null;
                try{
                    citicOrderDate = sdf.parse(bean.getCiticOrderDate());
                }catch(Exception e){
                    String msg = "购买日期必须为XXXX-XX-XX格式:"+orderBean.getDealDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date begin = null;
                try{
                    begin = sdf.parse(productImportBean.getOnMarketDate());
                }catch(Exception e){
                    String msg = "上线时间必须为XXXX-XX-XX格式:"+productImportBean.getOnMarketDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date end = null;
                if (!StringTools.isNullOrNone(productImportBean.getOfflineDate())) {
                    try{
                        end = sdf.parse(productImportBean.getOfflineDate());
                    }catch(Exception e){
                        String msg = "下线时间必须为XXXX-XX-XX格式:"+productImportBean.getOfflineDate();
                        _logger.error(msg);
                        throw new MailOrderException(msg, bean);
                    }
                }

                if (citicOrderDate!= null && begin!= null && end!= null
                        && !citicOrderDate.before(begin) && !citicOrderDate.after(end)){
                    _logger.info(citicOrderDate+"***citicOrderDate in range***"+begin+"**"+end);
                } else if (citicOrderDate!= null && begin!= null && end == null
                        && !citicOrderDate.before(begin)){
                    _logger.info(citicOrderDate+"***citicOrderDate >=***"+begin);
                } else{
                    String msg = "购买日期必须位于:"+productImportBean.getOnMarketDate()+"-"+productImportBean.getOfflineDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }
            }
        } else{
            String msg = "产品编码不存在:"+orderBean.getProductCode();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }


        //加一条规则，如果银行品名里含"姓氏“字符的，单独拆出来为一个批次导入，导入结果为失败，就放那
        if(bean.getProductName().contains("姓氏")){
            String msg = "银行品名不能包含姓氏:"+orderBean.getProductName();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        _logger.info("***convert pufa order bean success***"+bean);
        return bean;
    }

    private   OutImportBean convertZy(ZyOrderBean orderBean) throws MailOrderException{
        OutImportBean bean = new OutImportBean();

        bean.setImportFromMail(1);
        bean.setLogTime(TimeTools.now());
        // 操作人
        bean.setReason("import_from_mail");
        bean.setCiticNo(orderBean.getCiticNo());
        bean.setCiticOrderDate(orderBean.getDealDate());

//        bean.setBranchName(orderBean.getBranchName());


        //订单类型默认"销售出库"
        bean.setOutType(0);
        String branchName = orderBean.getComunicatonBranchName();
        String custName = orderBean.getComunicatonBranchName();
        String[] temp = branchName.split(":");
        if (temp.length == 2){
            custName = temp[1].trim()+"-银行";
        }

        bean.setComunicatonBranchName(custName);

        bean.setFirstName("N/A");
        bean.setAmount(orderBean.getAmount());
        bean.setPrice(orderBean.getPrice());
        bean.setValue(orderBean.getAmount()*orderBean.getPrice());
        bean.setIbMoney(orderBean.getFee() / bean.getAmount());


        //库存默认 公共库-南京物流中心
        bean.setDepotId(DepotConstant.CENTER_DEPOT_ID);
        //默认为南京物流中心-物流中心库(销售可发)仓区
        bean.setDepotpartId("1");
        bean.setComunicationBranch(DepotConstant.DEFAULT_DEPOT_PART);

        bean.setProductName(orderBean.getProductName());
        bean.setProductCode(orderBean.getProductCode());
        bean.setDescription("Mail_" + orderBean.getMailId());

        CustomerBean cBean = customerMainDAO.findByUnique(custName);
        if (null == cBean)
        {
            String msg = "网点名称不存在："+custName;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }else{
            bean.setCustomerId(cBean.getId());
            if (bean.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH)
            {

                StafferVSCustomerBean vsBean = stafferVSCustomerDAO.findByUnique(cBean.getId());
                if (null == vsBean)
                {
                    String msg = "网点名称没有与业务员挂靠关系："+custName;
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }else{
                    //职员取客户对应的业务员，如果没有，此单和姓氏一样处理
                    bean.setStafferId(vsBean.getStafferId());
                }
            }else{
                bean.setComunicatonBranchName("公共客户");
            }
        }

        //取客户的默认办公地址
        String customerId = bean.getCustomerId();
        boolean found = false;
        List<CustomerDistAddrBean> customerDistAddrBeans = this.customerDistAddrDAO.queryEntityBeansByFK(customerId);
        if (!ListTools.isEmptyOrNull(customerDistAddrBeans)){
            for (CustomerDistAddrBean addr : customerDistAddrBeans){
                ConditionParse conditionParse = new ConditionParse();
                conditionParse.addWhereStr();
                conditionParse.addCondition("type","=","303");
                conditionParse.addCondition("keyss","=",addr.getAtype());
                List<EnumBean> enumBeans = this.enumDAO.queryEntityBeansByCondition(conditionParse);
                if (!ListTools.isEmptyOrNull(enumBeans)){
                    EnumBean enumBean = enumBeans.get(0);
                    if ("办公地址".equals(enumBean.getValue())){
                        found = true;
                        bean.setShipping(addr.getShipping());
                        bean.setTransport1(addr.getTransport1());
                        bean.setExpressPay(addr.getExpressPay());
                        bean.setTransport2(addr.getTransport2());
                        bean.setTransportPay(addr.getTransportPay());
                        bean.setProvinceId(addr.getProvinceId());
                        bean.setCityId(addr.getCityId());
                        bean.setAddress(addr.getAddress());
                        bean.setReceiver(addr.getContact());
                        bean.setHandPhone(addr.getTelephone());
                        break;
                    }
                }
            }
        }

        if (!found){
            String msg = "没有默认办公地址："+customerId;
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        ConditionParse conditionParse = new ConditionParse();
        conditionParse.addCondition("bankProductCode", "=", orderBean.getProductCode());
        List<ProductImportBean> productImportBeans = this.productImportDAO.queryEntityBeansByCondition(conditionParse);
        if (!ListTools.isEmptyOrNull(productImportBeans)){
            ProductImportBean productImportBean = productImportBeans.get(0);
            String code = productImportBean.getCode();
            _logger.info(orderBean.getProductCode()+" product import vs product code***"+code);
            ProductBean productBean = this.productDAO.findByUnique(code);
            if (productBean == null){
                String msg = "产品编码不存在:"+orderBean.getProductCode();
                _logger.error(msg);
                throw new MailOrderException(msg, bean);
            } else{
                bean.setProductId(productBean.getId());
                bean.setProductName(productBean.getName());
                bean.setProductCode(code);
                //激励金额取t_center_product_import中的motivationmoney
                bean.setMotivationMoney(productImportBean.getMotivationMoney());

                //购买日期必须满足(上线时间，下线时间)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date citicOrderDate = null;
                try{
                    citicOrderDate = sdf.parse(bean.getCiticOrderDate());
                }catch(Exception e){
                    String msg = "购买日期必须为XXXX-XX-XX格式:"+orderBean.getDealDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date begin = null;
                try{
                    begin = sdf.parse(productImportBean.getOnMarketDate());
                }catch(Exception e){
                    String msg = "上线时间必须为XXXX-XX-XX格式:"+productImportBean.getOnMarketDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }

                Date end = null;
                if (!StringTools.isNullOrNone(productImportBean.getOfflineDate())) {
                    try{
                        end = sdf.parse(productImportBean.getOfflineDate());
                    }catch(Exception e){
                        String msg = "下线时间必须为XXXX-XX-XX格式:"+productImportBean.getOfflineDate();
                        _logger.error(msg);
                        throw new MailOrderException(msg, bean);
                    }
                }

                if (citicOrderDate!= null && begin!= null && end!= null
                        && !citicOrderDate.before(begin) && !citicOrderDate.after(end)){
                    _logger.info(citicOrderDate+"***citicOrderDate in range***"+begin+"**"+end);
                } else if (citicOrderDate!= null && begin!= null && end == null
                        && !citicOrderDate.before(begin)){
                    _logger.info(citicOrderDate+"***citicOrderDate >=***"+begin);
                } else{
                    String msg = "购买日期必须位于:"+productImportBean.getOnMarketDate()+"-"+productImportBean.getOfflineDate();
                    _logger.error(msg);
                    throw new MailOrderException(msg, bean);
                }
            }
        } else{
            String msg = "产品编码不存在:"+orderBean.getProductCode();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }


        //加一条规则，如果银行品名里含"姓氏“字符的，单独拆出来为一个批次导入，导入结果为失败，就放那
        if(bean.getProductName().contains("姓氏")){
            String msg = "银行品名不能包含姓氏:"+orderBean.getProductName();
            _logger.error(msg);
            throw new MailOrderException(msg, bean);
        }

        _logger.info("***convert zhongyuan order bean success***"+bean);
        return bean;
    }

    private MailType getOrderTypeByEmail(String from){
        MailType type = MailType.unknown;
        try{
            //citic
            String citicEL = "\\w+@citicbank.com";
            String citicOrderMail = ConfigLoader.getProperty("citicOrderMail");
            if (!StringTools.isNullOrNone(citicOrderMail)){
                citicEL = citicOrderMail;
            }

            Pattern citicPattern = Pattern.compile(citicEL.trim());
            Matcher citicMatcher = citicPattern.matcher(from);
            boolean citicMatches = citicMatcher.matches();

            //zhongyuan
            String zyEL = "yinlujie@yycoin.com";
            String zyOrderMail = ConfigLoader.getProperty("zyOrderMail");
            if (!StringTools.isNullOrNone(zyOrderMail)){
                zyEL = zyOrderMail;
            }

            Pattern zyPattern = Pattern.compile(zyEL.trim());
            Matcher zyMatcher = zyPattern.matcher(from);
            boolean zyMatches = zyMatcher.matches();

            //zhaoshang
            String zsEL = "\\w+@yycoin.com";
            String zsOrderMail = ConfigLoader.getProperty("zsOrderMail");
            if (!StringTools.isNullOrNone(zsOrderMail)){
                zsEL = zsOrderMail;
            }

            Pattern zsPattern = Pattern.compile(zsEL.trim());
            Matcher zsMatcher = zsPattern.matcher(from);
            boolean zsMatches = zsMatcher.matches();

            //pufa
//            String pfEL = "ebank@eb.spdb.com.cn";
            String pfEL = "liangjuanjuan@yycoin.com";
            String pfOrderMail = ConfigLoader.getProperty("pfOrderMail");
            if (!StringTools.isNullOrNone(pfOrderMail)){
                pfEL = pfOrderMail;
            }

            Pattern pfPattern = Pattern.compile(pfEL.trim());
            Matcher pfMatcher = pfPattern.matcher(from);
            boolean pfMatches = pfMatcher.matches();

            if (citicMatches){
                type = MailType.citic;
            } else if (pfMatches){
                type = MailType.pf;
            } else if (zyMatches){
                type = MailType.zy;
            } else if (zsMatches){
                type = MailType.zs;
            }

            String msg = from+"***"+"***getOrderTypeByEmail "+type;
            _logger.info(msg);
            System.out.println(msg);
        }catch(Exception e){
            e.printStackTrace();
            _logger.error(e);
        }

        return type;
    }

    String decodeText(String text) throws UnsupportedEncodingException {
        if (text == null)
            return null;
//        if (text.startsWith("=?GB") || text.startsWith("=?gb"))
        if (text.indexOf("=?GB")!=-1 || text.indexOf("=?gb")!= -1)
//            text = MimeUtility.decodeText(text);
            text = MimeUtility.encodeText(text);
        else
            text = new String(text.getBytes("ISO8859_1"));
        System.out.println("****text***"+text);
        return text;
    }

    /**
     *
     * @param content
     * @param type 1:中信 2:中原
     * @param subject 邮件标题
     * @throws MessagingException
     * @throws IOException
     */
    public void parseMultipart(Object content, MailType type, String subject) throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            int count = multipart.getCount();
            for (int idx = 0; idx < count; idx++) {
                BodyPart bodyPart = multipart.getBodyPart(idx);
                String disposition = bodyPart.getDisposition();
//                _logger.info(bodyPart.getContentType());
                _logger.info("disposition***"+disposition+"***fileName***" + bodyPart.getFileName());
                System.out.println("content type***"+bodyPart.getContentType()+"***fileName***" + bodyPart.getFileName());
                System.out.println(idx+"***fileName2***" + this.decodeText(bodyPart.getFileName()));
                System.out.println(disposition+"***disposition***" + bodyPart.getInputStream());
                if (bodyPart.isMimeType("text/plain")) {
//                    _logger.info("plain................." + bodyPart.getContent());
                    System.out.println("****1111");
                } else if (bodyPart.isMimeType("text/html")) {
                    _logger.info("html..................." + bodyPart.getContent());
                    System.out.println("****2222");
                } else if (bodyPart.isMimeType("multipart/*")) {
                    System.out.println("****3333");
                    Multipart mpart = (Multipart) bodyPart.getContent();
                    parseMultipart(mpart, type, subject);
                } else if (bodyPart.isMimeType("application/octet-stream")
                        || bodyPart.isMimeType("APPLICATION/VND.MS-EXCEL")) {
                    System.out.println("****4444");
                    if (BodyPart.ATTACHMENT.equalsIgnoreCase(disposition) || bodyPart.getInputStream()!= null) {
                        String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                        InputStream is = bodyPart.getInputStream();
//                        _logger.info("****fileName***" + fileName + "***size" + bodyPart.getSize());
                        System.out.println("****fileName3***" + fileName + "***size" + bodyPart.getSize());
                        if (fileName.contains("xls")){
//                        String fullPath = "D:\\oa_attachment\\"+fileName;
//                        this.copy(is, new FileOutputStream(fullPath));
                            if (type == MailType.citic) {
//                            List<CiticOrderBean> items = parseCiticOrder(new FileInputStream(fullPath));
                                List<CiticOrderBean> items = parseCiticOrder(is);
                                if (this.citicOrderDAO!= null && !ListTools.isEmptyOrNull(items)){
                                    for(CiticOrderBean item :items){
                                        try{
                                            item.setMailId(subject);
                                            item.setLogTime(TimeTools.now());
                                            this.citicOrderDAO.saveEntityBean(item);
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else if (type == MailType.zs) {
                                List<ZsOrderBean> items = parseZsOrder(is);
                                if (this.zsOrderDAO!= null && !ListTools.isEmptyOrNull(items)){
                                    for(ZsOrderBean item :items){
                                        try{
                                            item.setMailId(subject);
                                            item.setLogTime(TimeTools.now());
                                            this.zsOrderDAO.saveEntityBean(item);
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else if (type == MailType.pf) {
                                List<PfOrderBean> items = parsePfOrder(is);
                                if (this.pfOrderDAO!= null && !ListTools.isEmptyOrNull(items)){
                                    for(PfOrderBean item :items){
                                        try{
                                            item.setMailId(subject);
                                            item.setLogTime(TimeTools.now());
                                            this.pfOrderDAO.saveEntityBean(item);
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else if (type == MailType.zy){
                                List<ZyOrderBean> items = parseZyOrder(is);
                                if (this.zyOrderDAO!= null && !ListTools.isEmptyOrNull(items)){
                                    for(ZyOrderBean item :items){
                                        try{
                                            if (StringTools.isNullOrNone(item.getCustomerName()) ||
                                                    StringTools.isNullOrNone(item.getProductName())){
                                                //ignore
                                                _logger.warn("item not saved***"+item);
                                                continue;
                                            } else{
                                                item.setMailId(subject);
                                                item.setLogTime(TimeTools.now());
                                                this.zyOrderDAO.saveEntityBean(item);
                                            }
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }else if (fileName.contains(".rar")){
                            //TODO
                        }
                    }
                } else{
                    System.out.println("****5555");
                }
            }
        }
    }

    public static String[] fillObj(String[] obj)
    {
        String[] result = new String[60];

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

    /**
     * 解析中信银行附件
     * @param is
     * @return
     * @throws IOException
     */
    public List<CiticOrderBean> parseCiticOrder(InputStream is) throws IOException{
        ReaderFile reader = ReadeFileFactory.getXLSReader();
        List<CiticOrderBean> items = new ArrayList<CiticOrderBean>();
        try
        {
            reader.readFile(is);

            while (reader.hasNext())
            {
                String[] obj = fillObj((String[])reader.next());
                int currentNumber = reader.getCurrentLineNumber();
                System.out.println("****currentNumber***"+currentNumber);

                // 前三行忽略
                if (currentNumber <= 3)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }


                if (obj.length >= 2 )
                {
                    CiticOrderBean bean = new CiticOrderBean();
                    int i = 0;

                    // 购买分行号
                    String branchId = obj[i++];
                    if ( !StringTools.isNullOrNone(branchId))
                    {
                        bean.setBranchId(branchId);
                    }

                    //购买分行名称
                    String branchName = obj[i++];
                    if ( !StringTools.isNullOrNone(branchName))
                    {
                        bean.setBranchName(branchName);
                    }

                    //二级分行名称
                    String secondBranch = obj[i++];
                    if ( !StringTools.isNullOrNone(secondBranch))
                    {
                        bean.setSecondBranch(secondBranch);
                    }

                    //网点号
                    String commBranch = obj[i++];
                    if(!StringTools.isNullOrNone(commBranch)){
                        bean.setComunicationBranch(commBranch);
                    }

                    //网点名称
                    String commBranchName = obj[i++];
                    if(!StringTools.isNullOrNone(commBranchName)){
                        //去掉全角字符的空格
                        commBranchName = commBranchName.replace((char) 12288, ' ');
                        bean.setComunicatonBranchName(commBranchName.trim());
                    }
//                    System.out.println("************"+bean.getComunicatonBranchName());
//                    System.out.println("************"+commBranchName.trim());
//                    char ch = commBranchName.charAt(commBranchName.length()-1);
//                    System.out.println("************"+ch+"***"+(int)ch);
//                    char ch2 = ' ';
//                    System.out.println("************"+ch2+"***"+(int)ch2);

                    //商品编码
                    String productCode = obj[i++];
                    if(!StringTools.isNullOrNone(productCode)){
                        bean.setProductCode(productCode);
                    }

                    //商品名称
                    String productName = obj[i++];
                    if(!StringTools.isNullOrNone(productName)){
                        bean.setProductName(productName);
                    }

                    //数量
                    String amount = obj[i++];
                    if(!StringTools.isNullOrNone(amount)){
                        bean.setAmount(Integer.valueOf(amount.trim()));
                    }

                    //单价
                    String price = obj[i++];
                    if(!StringTools.isNullOrNone(price)){
                        bean.setPrice(Double.valueOf(price));
                    }

                    //产品克重
                    String productWeight = obj[i++];
                    if (!StringTools.isNullOrNone(productWeight)){
                        bean.setProductWeight(Double.valueOf(productWeight));
                    }

                    //金额
                    String value = obj[i++];
                    if(!StringTools.isNullOrNone(value)){
                        bean.setValue(Double.valueOf(value));
                    }

                    //费用
                    String fee = obj[i++];
                    if(!StringTools.isNullOrNone(fee)){
                        bean.setFee(Double.valueOf(fee));
                    }

                    //计划交付日期
                    String arrivalDate = obj[i++];
                    if(!StringTools.isNullOrNone(arrivalDate)){
                        bean.setArriveDate(arrivalDate);
                    }

                    //订铺货标志
                    String show = obj[i++];
                    if(!StringTools.isNullOrNone(show)){
                        bean.setOrderOrShow(show);
                    }

                    //是否现货
                    String spotFlag = obj[i++];
                    if(!StringTools.isNullOrNone(spotFlag)){
                        if("是".equals(spotFlag)){
                            bean.setSpotFlag(1);
                        }
                    }

                    //中信订单号
                    String citicNo = obj[i++];
                    if(!StringTools.isNullOrNone(citicNo)){
                        bean.setCiticNo(citicNo);
                    }

                    //开票性质
                    String invoiceNature = obj[i++];
                    if(!StringTools.isNullOrNone(invoiceNature)){
                        bean.setInvoiceNature(invoiceNature);
                    }

                    //开票抬头
                    String head = obj[i++];
                    if(!StringTools.isNullOrNone(head)){
                        bean.setInvoiceHead(head);
                    }

                    //开票条件
                    String con = obj[i++];
                    if(!StringTools.isNullOrNone(con)){
                        bean.setInvoiceCondition(con);
                    }

                    //客户经理
                    String managerId = obj[i++];
                    if(!StringTools.isNullOrNone(managerId)){
                        bean.setManagerId(managerId);
                    }

                    //客户经理姓名
                    String manager = obj[i++];
                    if(!StringTools.isNullOrNone(manager)){
                        bean.setManager(manager);
                    }

                    //发起方标志
                    String originator = obj[i++];
                    if(!StringTools.isNullOrNone(originator)){
                        bean.setOriginator(originator);
                    }

                    //购买日期
                    String citicDate = obj[i++];
                    if(!StringTools.isNullOrNone(citicDate)){
                        bean.setCiticOrderDate(citicDate);
                    }

                    //贵金属企业
                    String company = obj[i++];
                    if(!StringTools.isNullOrNone(company)){
                        bean.setEnterpriseName(company);
                    }

                    //客户姓名
                    String customer = obj[i++].trim();
                    if(!StringTools.isNullOrNone(customer)){
                        bean.setCustomerName(customer);
                    }

                    //身份证
                    String id = obj[i++];
                    if(!StringTools.isNullOrNone(id)){
                        bean.setIdCard(id);
                    }

                    //客户号
                    String customerId = obj[i++];
                    if(!StringTools.isNullOrNone(customerId)){
                        bean.setCustomerId(customerId);
                    }

                    //买卖时间
                    String dealDate = obj[i++];
                    if(!StringTools.isNullOrNone(dealDate)){
                        bean.setDealDate(dealDate);
                    }

                    //提货时间
                    String pickupDate = obj[i++];
                    if(!StringTools.isNullOrNone(pickupDate)){
                        bean.setPickupDate(pickupDate);
                    }

                    //提货标志
                    String pickupFlag = obj[i++];
                    if(!StringTools.isNullOrNone(pickupFlag)){
                        if("Y".equalsIgnoreCase(pickupFlag)){
                            bean.setPickupFlag(1);
                        }
                    }

                    //提货柜员号
                    String tellerId = obj[i++];
                    if(!StringTools.isNullOrNone(tellerId)){
                        bean.setTellerId(tellerId);
                    }

                    //提货网点号
                    String node = obj[i++];
                    if(!StringTools.isNullOrNone(node)){
                        bean.setPickupNode(node);
                    }

                    //提货网点地址
                    String pickupAddress = obj[i++];
                    if(!StringTools.isNullOrNone(pickupAddress)){
                        bean.setPickupAddress(pickupAddress);
                    }


                    //客户地址
                    String address = obj[i++];
                    if(!StringTools.isNullOrNone(address)){
                        bean.setAddress(address);
                    }

                    //联系电话
                    String handPhone = obj[i++];
                    if(!StringTools.isNullOrNone(handPhone)){
                        bean.setHandPhone(handPhone);
                    }

                    //重量
                    String weight = obj[i++];
                    if(!StringTools.isNullOrNone(weight)){
                        bean.setWeight(Double.valueOf(weight));
                    }

                    //基础金价
                    String goldPrice = obj[i++];
                    if(!StringTools.isNullOrNone(goldPrice)){
                        bean.setGoldPrice(Double.valueOf(goldPrice));
                    }

                    //金银标志
                    String materialType = obj[i++];
                    if(!StringTools.isNullOrNone(materialType)){
                        bean.setMaterialType(materialType);
                    }

                    //产品属性
                    String productType = obj[i++];
                    if(!StringTools.isNullOrNone(productType)){
                        bean.setProductType(productType);
                    }

                    //取货方式
                    String pickupType = obj[i++];
                    if(!StringTools.isNullOrNone(pickupType)){
                        bean.setPickupType(pickupType);
                    }

                    //操作柜员
                    String teller = obj[i++];
                    if(!StringTools.isNullOrNone(teller)){
                        bean.setTeller(teller);
                    }

                    //省
                    String province = obj[i++];
                    if(!StringTools.isNullOrNone(province)){
                        bean.setProvinceName(province);
                    }

                    //市
                    String city = obj[i++];
                    if(!StringTools.isNullOrNone(city)){
                        bean.setCity(city);
                    }

                    //配送地址
                    String address2 = obj[i++];
                    if(!StringTools.isNullOrNone(address2)){
                        bean.setAddress(address2);
                    }

                    //分行收货人
                    String receiver = obj[i++];
                    if(!StringTools.isNullOrNone(receiver)){
                        bean.setReceiver(receiver);
                    }

                    //分行收货人手机
                    String mobile = obj[i++];
                    if(!StringTools.isNullOrNone(mobile)){
                        bean.setReceiverMobile(mobile);
                    }

                    System.out.println(bean);
                    items.add(bean);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return items;
    }

    /** #269
     * 招商银行订单
     * @param is
     * @return
     * @throws IOException
     */
    public List<ZsOrderBean> parseZsOrder(InputStream is) throws IOException{
        ReaderFile reader = ReadeFileFactory.getXLSReader();
        List<ZsOrderBean> items = new ArrayList<ZsOrderBean>();
        try
        {
            reader.readFile(is);

            while (reader.hasNext())
            {
                String[] obj = fillObj((String[]) reader.next());
                int currentNumber = reader.getCurrentLineNumber();
                System.out.println("****currentNumber***"+currentNumber);

                // 前1行忽略
                if (currentNumber <= 1)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }


                if (obj.length >= 2 )
                {
                    ZsOrderBean bean = new ZsOrderBean();
                    int i = 0;

                    //订单号码
                    String citicNo = obj[i++];
                    if(!StringTools.isNullOrNone(citicNo)){
                        bean.setCiticNo(citicNo);
                    }

                    //订单状态
                    String orderStatus = obj[i++];
                    if(!StringTools.isNullOrNone(orderStatus)){
                        bean.setOrderStatus(orderStatus);
                    }

                    //序号
                    String sn = obj[i++];
                    if (!StringTools.isNullOrNone(sn)){
                        bean.setSn(sn);
                    }

                    //交易日期
                    String dealDate = obj[i++];
                    if(!StringTools.isNullOrNone(dealDate)){
                        bean.setDealDate(dealDate);
                    }

                    //交易时间
                    String dealTime = obj[i++];
                    if (!StringTools.isNullOrNone(dealTime)){
                        bean.setDealTime(dealTime);
                    }

                    //交易户名
                    String account = obj[i++];
                    if (!StringTools.isNullOrNone(account)){
                        bean.setAccount(account);
                    }

                    //供应商编码
                    String providerId = obj[i++];
                    if (!StringTools.isNullOrNone(providerId)){
                        bean.setProviderId(providerId);
                    }

                    //产品类型
                    String materialType = obj[i++];
                    if (!StringTools.isNullOrNone(materialType)){
                        bean.setMaterialType(materialType);
                    }


                    //商品编码
                    String productCode = obj[i++];
                    if(!StringTools.isNullOrNone(productCode)){
                        bean.setProductCode(productCode);
                    }

                    //商品名称
                    String productName = obj[i++];
                    if(!StringTools.isNullOrNone(productName)){
                        bean.setProductName(productName);
                    }

                    //产品规格
                    String productSpec = obj[i++];
                    if (!StringTools.isNullOrNone(productSpec)){
                        bean.setProductSpec(productSpec);
                    }


                    //数量
                    String amount = obj[i++];
                    if(!StringTools.isNullOrNone(amount)){
                        bean.setAmount(Integer.valueOf(amount.trim()));
                    }

                    //金额
                    String value = obj[i++];
                    if(!StringTools.isNullOrNone(value)){
                        bean.setValue(Double.valueOf(value.replaceAll(",","")));
                    }

                    //费用
                    String fee = obj[i++];
                    if(!StringTools.isNullOrNone(fee)){
                        bean.setFee(Double.valueOf(fee.replaceAll(",","")));
                    }


                    //@Deprecated 交易价格
                    String price = obj[i++];
                    if(!StringTools.isNullOrNone(price)){
//                        bean.setPrice(Double.valueOf(price));
                    }

                    //交易分行
                    String branchName = obj[i++];
                    if ( !StringTools.isNullOrNone(branchName))
                    {
                        bean.setBranchName(branchName);
                    }

                    //交易机构
                    String commBranchName = obj[i++];
                    if(!StringTools.isNullOrNone(commBranchName)){
                        bean.setComunicatonBranchName(commBranchName.trim());
                    }


                    //库存控制类型
                    String storageControlType = obj[i++];
                    if (!StringTools.isNullOrNone(storageControlType)){
                        bean.setStorageControlType(storageControlType);
                    }


                    //领取机构
                    String node = obj[i++];
                    if(!StringTools.isNullOrNone(node)){
                        bean.setPickupNode(node);
                    }

                    //开票抬头
                    String head = obj[i++];
                    if(!StringTools.isNullOrNone(head)){
                        bean.setInvoiceHead(head);
                    }


                    //开票方式
                    String invoiceNature = obj[i++];
                    if(!StringTools.isNullOrNone(invoiceNature)){
                        bean.setInvoiceNature(invoiceNature);
                    }

                    //开票备注
                    String con = obj[i++];
                    if(!StringTools.isNullOrNone(con)){
                        bean.setInvoiceCondition(con);
                    }

                    //TODO
                    System.out.println(bean);
                    items.add(bean);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return items;
    }

    /**
     * 浦发银行订单
     * @param is
     * @return
     * @throws IOException
     */
    public List<PfOrderBean> parsePfOrder(InputStream is) throws IOException{
//        ZipInputStream zin = new ZipInputStream(is);
//        System.out.println("***parsePfOrder***"+zin);
//        System.out.println(zin.getNextEntry());

        //TODO check commons-compress   ZipArchiveInputStream
        //http://stackoverflow.com/questions/15521966/zipinputstream-getnextentry-returns-null-on-some-zip-files
        //TODO it is hard to extract .rar format in Java,maybe junrar https://github.com/edmund-wagner/junrar
        //TODO download rar file to local system, and open it with 7-zip
        //TODO 7z e 20160618_reportToOrg_10035.rar -og:\Download\rar  (ProcessBuilder)
        //TODO JNA call 7z.dll?
//        for (ZipEntry zipEntry;(zipEntry = zin.getNextEntry()) != null; )
//        {
//            System.out.println("reading zipEntry " + zipEntry.getName());
//            Scanner sc = new Scanner(zin);
//            while (sc.hasNextLine())
//            {
//                System.out.println(sc.nextLine());
//            }
//            System.out.println("reading " + zipEntry.getName() + " completed");
//        }
//        zin.close();

        ReaderFile reader = ReadeFileFactory.getXLSReader();
        List<PfOrderBean> items = new ArrayList<PfOrderBean>();
        try
        {
            reader.readFile(is);

            while (reader.hasNext())
            {
                String[] obj = fillObj((String[]) reader.next());
                int currentNumber = reader.getCurrentLineNumber();
                System.out.println("****currentNumber***"+currentNumber);

                // 前三行忽略
                if (currentNumber <= 2)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }

                if (obj.length >= 2 )
                {
                    PfOrderBean bean = new PfOrderBean();
                    int i = 0;


                    //交易分行
                    String branchName = obj[i++];
                    if ( !StringTools.isNullOrNone(branchName))
                    {
                        bean.setBranchName(branchName);
                    }

                    //订单号码
                    String citicNo = obj[i++];
                    if(!StringTools.isNullOrNone(citicNo)){
                        bean.setCiticNo(citicNo);
                    }


                    //商品编码
                    String productCode = obj[i++];
                    if(!StringTools.isNullOrNone(productCode)){
                        bean.setProductCode(productCode);
                    }

                    //商品名称
                    String productName = obj[i++];
                    if(!StringTools.isNullOrNone(productName)){
                        bean.setProductName(productName);
                    }

                    //价格
                    String price = obj[i++];
                    if(!StringTools.isNullOrNone(price)){
                        bean.setPrice(Double.valueOf(price));
                    }

                    //数量
                    String amount = obj[i++];
                    if(!StringTools.isNullOrNone(amount)){
                        bean.setAmount(Integer.valueOf(amount.trim()));
                    }


                    //交易日期
                    String dealDate = obj[i++];
                    if(!StringTools.isNullOrNone(dealDate)){
                        bean.setDealDate(dealDate);
                    }

                    //交易时间
                    String dealTime = obj[i++];
                    if (!StringTools.isNullOrNone(dealTime)){
                        bean.setDealTime(dealTime);
                    }

                    //到货日
                    String arrivalDate = obj[i++];
                    if (!StringTools.isNullOrNone(arrivalDate)){
                        bean.setArrivalDate(arrivalDate);
                    }

                    //方式
                    String method = obj[i++];
                    if (!StringTools.isNullOrNone(method)){
                        bean.setMethod(method);
                    }

                     //POS付款方
                    String pos = obj[i++];
                    if (!StringTools.isNullOrNone(pos)){
                        bean.setPos(pos);
                    }

                    //备注
                    String description = obj[i++];
                    if (!StringTools.isNullOrNone(description)){
                        bean.setDescription(description);
                    }

                    //交易机构
                    String commBranchName = obj[i++];
                    if(!StringTools.isNullOrNone(commBranchName)){
                        bean.setComunicatonBranchName(commBranchName.trim());
                    }

                    //配货机构
                    String shippingOrg = obj[i++];
                    if (!StringTools.isNullOrNone(shippingOrg)){
                        bean.setShippingOrg(shippingOrg);
                    }

                    //TODO
                    System.out.println(bean);
                    items.add(bean);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return items;
    }


    /**
     * 解析中原银行订单
     * @param is
     * @return
     * @throws IOException
     */
    public List<ZyOrderBean> parseZyOrder(InputStream is) throws IOException{
        ReaderFile reader = ReadeFileFactory.getXLSReader();
        List<ZyOrderBean> items = new ArrayList<ZyOrderBean>();
        try
        {
            reader.readFile(is);

            while (reader.hasNext())
            {
                int currentNumber = reader.getCurrentLineNumber();
                String[] obj = fillObj((String[])reader.next());

//                System.out.println("***currentNumber***"+currentNumber);
                // 前5行忽略
                if (currentNumber <=5)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }

                if (obj.length >= 2 )
                {
                    try{
                    ZyOrderBean bean = new ZyOrderBean();
                    int i = 0;

                    // 流水号
                    String sn = obj[i++];
                    if ( !StringTools.isNullOrNone(sn))
                    {
                        bean.setCiticNo(sn);
                    }

                    //渠道流水号
                    String channelSn = obj[i++];
                    if ( !StringTools.isNullOrNone(channelSn))
                    {
                        bean.setChannelSerialNumber(channelSn);
                    }

                    //交易日期
                    String dealDate = obj[i++];
                    if ( !StringTools.isNullOrNone(dealDate))
                    {
                        bean.setDealDate(dealDate);
                    }

                    //交易时间
                    String dealTime = obj[i++];
                    if(!StringTools.isNullOrNone(dealTime)){
                        bean.setDealTime(dealTime);
                    }

                    //交易代码
                    String dealCode = obj[i++];
                    if(!StringTools.isNullOrNone(dealCode)){
                        bean.setDealCode(dealCode);
                    }

                    //交易机构
                    String dealAgent = obj[i++];
                    if(!StringTools.isNullOrNone(dealAgent)){
                        bean.setComunicatonBranchName(dealAgent);
                    }

                    //内部客户号
                    String innerCustomerId = obj[i++];
                    if(!StringTools.isNullOrNone(innerCustomerId)){
                        bean.setInnerCustomerId(innerCustomerId);
                    }

                    //客户类型
                    String customerType = obj[i++];
                    if(!StringTools.isNullOrNone(customerType)){
                        bean.setCustomerType(customerType);
                    }

                    //客户组别
                    String customerGroup = obj[i++];
                    if(!StringTools.isNullOrNone(customerGroup)){
                        bean.setCustomerGroup(customerGroup);
                    }

                    //客户号
                    String customerId = obj[i++];
                    if (!StringTools.isNullOrNone(customerId)){
                        bean.setCustomerId(customerId);
                    }

                    //客户名称
                    String customerName = obj[i++];
                    if(!StringTools.isNullOrNone(customerName)){
                        bean.setCustomerName(customerName);
                    }

                    //证件类型
                    String idType = obj[i++];
                    if(!StringTools.isNullOrNone(idType)){
                        bean.setIdType(idType);
                    }


                    //证件号码
                    String idCard = obj[i++];
                    if(!StringTools.isNullOrNone(idCard)){
                        bean.setIdCard(idCard);
                    }

                    //代理人证件类型
                    String proxyIdType = obj[i++];
                    if(!StringTools.isNullOrNone(proxyIdType)){
                        bean.setProxyIdType(proxyIdType);
                    }

                    //代理人证件号码
                    String proxyIdCard = obj[i++];
                    if(!StringTools.isNullOrNone(proxyIdCard)){
                        bean.setProxyIdCard(proxyIdCard);
                    }

                    //银行帐号
                    String bankAccount = obj[i++];
                    if(!StringTools.isNullOrNone(bankAccount)){
                        bean.setBankAccount(bankAccount);
                    }

                    //交易渠道
                    String tradeChannel = obj[i++];
                    if(!StringTools.isNullOrNone(tradeChannel)){
                        bean.setChannel(tradeChannel);
                    }

                    //终端号
                    String terminal = obj[i++];
                    if(!StringTools.isNullOrNone(terminal)){
                        bean.setTerminal(terminal);
                    }

                    //柜员号
                    String tellerId = obj[i++];
                    if(!StringTools.isNullOrNone(tellerId)){
                        bean.setTellerId(tellerId);
                    }

                    //授权柜员
                    String teller = obj[i++];
                    if(!StringTools.isNullOrNone(teller)){
                        bean.setTeller(teller);
                    }

                    //公司代码
                    String enterpriseCode = obj[i++];
                    if(!StringTools.isNullOrNone(enterpriseCode)){
                        bean.setEnterpriseCode(enterpriseCode);
                    }

                    //公司名称
                    String enterpriseName = obj[i++];
                    if(!StringTools.isNullOrNone(enterpriseName)){
                        bean.setEnterpriseName(enterpriseName);
                    }

                    //产品代码
                    String productCode = obj[i++];
                    if(!StringTools.isNullOrNone(productCode)){
                        bean.setProductCode(productCode);
                    }

                    //产品名称
                    String productName = obj[i++];
                    if (!StringTools.isNullOrNone(productName)){
                        bean.setProductName(productName);
                    }

                    //规格代码
                    String specCode = obj[i++];
                    if(!StringTools.isNullOrNone(specCode)){
                        bean.setSpecCode(specCode);
                    }

                    //规格名称
                    String specName = obj[i++];
                    if(!StringTools.isNullOrNone(specName)){
                        bean.setSpecName(specName);
                    }

                    //规格
                    String spec = obj[i++];
                    if(!StringTools.isNullOrNone(spec)){
                        bean.setSpec(Double.valueOf(spec));
                    }

                    //业务类型
                    String businessType = obj[i++];
                    if(!StringTools.isNullOrNone(businessType)){
                        bean.setBusinessType(businessType);
                    }

                    //关联日期
                    String associateDate = obj[i++];
                    if(!StringTools.isNullOrNone(associateDate)){
                        bean.setAssociateDate(associateDate);
                    }

                    //关联流水号
                    String associateSn = obj[i++];
                    if(!StringTools.isNullOrNone(associateSn)){
                        bean.setAssociateId(associateSn);
                    }

                    //数量
                    String amount = obj[i++];
                    if (!StringTools.isNullOrNone(amount)){
                        bean.setAmount(Integer.valueOf(amount));
                    }

                    //购买单位数
                    String buyUnit = obj[i++];
                    if(!StringTools.isNullOrNone(buyUnit)){
                        bean.setBuyUnit(Integer.valueOf(buyUnit));
                    }

                    //单价
                    String price = obj[i++];
                    if(!StringTools.isNullOrNone(price)){
                        bean.setPrice(Double.valueOf(price.replace(",","")));
                    }

                    //金额
                    String value = obj[i++];
                    if(!StringTools.isNullOrNone(value)){
                        bean.setValue(Double.valueOf(value.replace(",","")));
                    }

                    //币种
                    String currency = obj[i++];
                    if(!StringTools.isNullOrNone(currency)){
                        bean.setCurrency(currency);
                    }

                    //钞汇标志
                    String paymentMethod = obj[i++];
                    if(!StringTools.isNullOrNone(paymentMethod)){
                        bean.setPaymentMethod(paymentMethod);
                    }

                    //剩余可提金量
                    String remainAmount = obj[i++];
                    if(!StringTools.isNullOrNone(remainAmount)){
                        bean.setRemainAmount(Integer.valueOf(remainAmount));
                    }

                    //保管费
                    String storageCost = obj[i++];
                    if(!StringTools.isNullOrNone(storageCost)){
                        bean.setStorageCost(Double.valueOf(storageCost.replace(",","")));
                    }

                    //手续费
                    String fee = obj[i++];
                    if(!StringTools.isNullOrNone(fee)){
                        //TODO 是否/数量
                        bean.setFee(Double.valueOf(fee.replace(",","")));
                    }

                    //折扣率
                    String discountRate = obj[i++];
                    if(!StringTools.isNullOrNone(discountRate)){
                        bean.setDiscountRate(Double.valueOf(discountRate));
                    }

                    //客户经理
                    String manager = obj[i++];
                    if(!StringTools.isNullOrNone(manager)){
                        bean.setManager(manager);
                    }

                    //发票信息
                    String invoiceHead = obj[i++];
                    if(!StringTools.isNullOrNone(invoiceHead)){
                        bean.setInvoiceHead(invoiceHead);
                    }

                    //财务状态
                    String financialStatus = obj[i++];
                    if(!StringTools.isNullOrNone(financialStatus)){
                        bean.setFinancialStatus(financialStatus);
                    }

                    //原交易渠道
                    String originalChannel = obj[i++];
                    if(!StringTools.isNullOrNone(originalChannel)){
                        bean.setOriginalChannel(originalChannel);
                    }

                    //原交易机构
                    String originalDealAgent = obj[i++];
                    if(!StringTools.isNullOrNone(originalDealAgent)){
                        bean.setOriginalDealAgent(originalDealAgent);
                    }

                    //提金网点
                    String pickupNode = obj[i++];
                    if(!StringTools.isNullOrNone(pickupNode)){
                        bean.setPickupNode(pickupNode);
                    }

                    //发主机流水号
                    String sendMainframeId = obj[i++];
                    if (!StringTools.isNullOrNone(sendMainframeId)){
                        bean.setSendMainframeId(sendMainframeId);
                    }

                    // 主机对账日期
                     String mainframeCheckDate = obj[i++];
                     if (!StringTools.isNullOrNone(mainframeCheckDate)){
                         bean.setMainframeCheckDate(mainframeCheckDate);
                     }

                     //主机交易码
                     String mainframeTradeCode = obj[i++];
                     if (!StringTools.isNullOrNone(mainframeTradeCode)){
                         bean.setMainframeTradeCode(mainframeTradeCode);
                     }

                     //主机日期
                     String mainframeDate = obj[i++];
                     if (!StringTools.isNullOrNone(mainframeDate)){
                         bean.setMainframeDate(mainframeDate);
                     }

                     //主机流水号
                     String mainframeId = obj[i++];
                     if (!StringTools.isNullOrNone(mainframeId)){
                         bean.setMainframeId(mainframeId);
                     }

                     //返回码
                     String returnCode = obj[i++];
                     if (!StringTools.isNullOrNone(returnCode)){
                         bean.setReturnCode(returnCode);
                     }

                     // 返回信息
                     String returnMessage = obj[i++];
                     if (!StringTools.isNullOrNone(returnMessage)){
                         bean.setReturnMessage(returnMessage);
                     }

                     //受理方式
                     String acceptMethod = obj[i++];
                     if (!StringTools.isNullOrNone(acceptMethod)){
                         bean.setAcceptMethod(acceptMethod);
                     }

                     //交易状态
                     String tradeStatus = obj[i++];
                     if (!StringTools.isNullOrNone(tradeStatus)){
                         bean.setTradeStatus(tradeStatus);
                     }

                     //对公帐号
                     String corporateAccount = obj[i++];
                     if (!StringTools.isNullOrNone(corporateAccount)){
                         bean.setCorporateAccount(corporateAccount);
                     }

                     //是否提金结束
                     String finished = obj[i++];
                     if (!StringTools.isNullOrNone(finished) && finished.contains("是")){
                          bean.setFinished(1);
                     }

                     //资金处理是否异常
                     String exceptional = obj[i++];
//                     if (!StringTools.isNullOrNone(exceptional)){
//                         bean.setExceptional(1);
//                     }

                     //是否预约到货
                     String appointmentOfArrival = obj[i++];

                     //购买提金方式
                     String pickupType = obj[i++];
                     if (!StringTools.isNullOrNone(pickupType)){
                         bean.setPickupType(pickupType);
                     }

//                    1.交易代码：890010:贵金属购买
//                    2.公司名称：永银文化创意产业发展有限公司
//                    3.返回信息：交易成功
//                    4.交易状态：S:成功
                    if ("890010:贵金属购买".equals(bean.getDealCode())
                            && "永银文化创意产业发展有限公司".equals(bean.getEnterpriseName())
                            && "交易成功".equals(bean.getReturnMessage())
                            && "S:成功".equals(bean.getTradeStatus())){
                        System.out.println(bean);
                        items.add(bean);
                    } else{
                        _logger.warn("Ignore zy order***"+bean);
                        System.out.println("Ignore zy order***"+bean);
                    }
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return items;
    }

    /**
     * callback to update status after create OA order
     * @param mailId
     * @param beans
     */
    public void onCreateOA(String mailId,List<OutImportBean> beans){
        if (mailId.indexOf(CITIC)!= -1) {
            for(OutImportBean bean : beans){
                _logger.info("***onCreateOA***"+bean.getCiticNo());
                this.citicOrderDAO.updateStatus(bean.getCiticNo());
            }
        } else if ( mailId.indexOf(ZS)!= -1) {
            for(OutImportBean bean : beans){
                _logger.info("***onCreateOA***"+bean.getCiticNo());
                this.zsOrderDAO.updateStatus(bean.getCiticNo());
            }
        }
    }

    public void copy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[65536];
        int len;
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes, 0, len);
//            os.write(bytes);
//            os.write(len);
        }
        if (os != null)
            os.close();
        is.close();
    }

    public static void saveParts(Object content, String filename)
            throws IOException, MessagingException
    {
        OutputStream out = null;
        InputStream in = null;
        try {
            if (content instanceof Multipart) {
                Multipart multi = ((Multipart)content);
                int parts = multi.getCount();
                for (int j=0; j < parts; ++j) {
                    MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
                    if (part.getContent() instanceof Multipart) {
                        // part-within-a-part, do some recursion...
                        saveParts(part.getContent(), filename);
                    }
                    else {
                        String extension = "";
                        if (part.isMimeType("text/html")) {
                            extension = "html";
                        }
                        else {
                            if (part.isMimeType("text/plain")) {
                                extension = "txt";
                            }
                            else {
                                //  Try to get the name of the attachment
                                extension = part.getDataHandler().getName();
                            }
                            filename = filename + "." + extension;
                            System.out.println("... " + filename);
                            out = new FileOutputStream(new File(filename));
                            in = part.getInputStream();
                            int k;
                            while ((k = in.read()) != -1) {
                                out.write(k);
                            }
                        }
                    }
                }
            }
        }
        finally {
            if (in != null) { in.close(); }
            if (out != null) { out.flush(); out.close(); }
        }
    }
}