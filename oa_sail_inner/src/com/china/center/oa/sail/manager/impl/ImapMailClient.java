package com.china.center.oa.sail.manager.impl;

/**
 * Created by user on 2016/4/8.
 */
import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.common.MYException;
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
import com.china.center.oa.sail.bean.CiticOrderBean;
import com.china.center.oa.sail.bean.OutImportBean;
import com.china.center.oa.sail.bean.ZyOrderBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.CiticOrderDAO;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ImapMailClient {
    private final Log _logger = LogFactory.getLog(getClass());

    public static final String IMAP = "imap";

    public static final String CITIC = "citic";

    public static enum MailType {unknown, citic, zy}

    private CiticOrderDAO citicOrderDAO = null;

    private ZyOrderDAO zyOrderDAO = null;

    private ProductImportDAO productImportDAO = null;

    private ProductDAO productDAO = null;

    private CustomerMainDAO customerMainDAO = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    private CustomerDistAddrDAO  customerDistAddrDAO = null;

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
//                msg.setFlag(Flags.Flag.SEEN, true);
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
            _logger.info("***import orders with size:"+citicOrderBeans.size());
//            Set<String> citicOrders = new HashSet<String>();
            if (!ListTools.isEmptyOrNull(citicOrderBeans)){
                 for(CiticOrderBean citicOrderBean: citicOrderBeans){
                     String citicNo = citicOrderBean.getCiticNo();
//                     _logger.info(ImapMailClient.citicNoSet.size()+"***"+ImapMailClient.citicNoSet);
//                     if (ImapMailClient.citicNoSet.contains(citicNo)){
//                         _logger.error(citicOrderBean+" is duplicate***");
//                     } else{
//
//                     }
                     //TODO check DB
//                         ImapMailClient.citicNoSet.add(citicNo);

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
                     }catch(MYException e){
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
        bean.setComunicationBranch(orderBean.getComunicationBranch());

        //订单类型默认"销售出库"
        bean.setOutType(0);
        String custName = orderBean.getComunicatonBranchName()+"-银行";
        bean.setComunicatonBranchName(custName);

        bean.setFirstName("N/A");
        bean.setAmount(orderBean.getAmount());
        bean.setPrice(orderBean.getPrice());

        //TODO 规格
//        bean.setStyle(orderBean.getst);
        bean.setValue(orderBean.getValue());

        bean.setIbMoney(orderBean.getFee());
        bean.setArriveDate(orderBean.getArriveDate());

        //TODO 库存类型
//        bean.setStorageType(orderBean);


        //TODO 开票性质
//        bean.setInvoiceNature(orderBean.getInvoiceNature());
        bean.setInvoiceHead(orderBean.getInvoiceHead());
        bean.setInvoiceCondition(orderBean.getInvoiceCondition());

        //TODO 开票类型   开票品名    开票金额
        //TODO 开票先不考虑
//        bean.setInvoiceType(orderBean);
//        bean.setInvoiceName(orderBean);
//        bean.setInvoiceMoney(orderBean.getmo);

        //TODO   职员 备注
        //库存默认 公共库-南京物流中心
        bean.setDepotId(DepotConstant.CENTER_DEPOT_ID);

        //TODO  仓区

        bean.setProductName(orderBean.getProductName());
        bean.setProductCode(orderBean.getProductCode());
        bean.setDescription("Mail_" + orderBean.getMailId());

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
            List<CustomerDistAddrBean> customerDistAddrBeans = this.customerDistAddrDAO.queryEntityBeansByFK(bean.getCustomerId());
            if (!ListTools.isEmptyOrNull(customerDistAddrBeans)){
                for (CustomerDistAddrBean addr : customerDistAddrBeans){
                    if (addr.getAtype() == 1){
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
                    }
                }
            }
        }

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

    private MailType getOrderTypeByEmail(String from){
        MailType type = MailType.unknown;
        try{
            String citicEL = "\\w+@citicbank.com";
            String citicOrderMail = ConfigLoader.getProperty("citicOrderMail");
            if (!StringTools.isNullOrNone(citicOrderMail)){
                citicEL = citicOrderMail;
            }

            Pattern citicPattern = Pattern.compile(citicEL);
            Matcher citicMatcher = citicPattern.matcher(from);
//            _logger.info(citicEL+"***citicEL**"+from);
            //中信
            if (citicMatcher.matches()){
                type = MailType.citic;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        _logger.info(from+"***getOrderTypeByEmail "+type);

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
                _logger.info(bodyPart.getContentType());
                _logger.info("***fileName***" + bodyPart.getFileName());
                if (bodyPart.isMimeType("text/plain")) {
                    _logger.info("plain................." + bodyPart.getContent());
                } else if (bodyPart.isMimeType("text/html")) {
                    _logger.info("html..................." + bodyPart.getContent());
                } else if (bodyPart.isMimeType("multipart/*")) {
                    Multipart mpart = (Multipart) bodyPart.getContent();
                    parseMultipart(mpart, type, subject);
                } else if (bodyPart.isMimeType("application/octet-stream")) {
                    String disposition = bodyPart.getDisposition();
                    _logger.info(disposition + "***disposition***" + bodyPart.getInputStream());
                    if (BodyPart.ATTACHMENT.equalsIgnoreCase(disposition) || bodyPart.getInputStream()!= null) {
                        String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                        _logger.info("****fileName***" + fileName + "***size" + bodyPart.getSize());
                        if (fileName.contains("xls")){
                            InputStream is = bodyPart.getInputStream();
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
                                                this.zyOrderDAO.saveEntityBean(item);
                                            }
                                        } catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String[] fillObj(String[] obj)
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
                        bean.setComunicatonBranchName(commBranchName);
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
                        bean.setSerialNumber(sn);
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
                        bean.setDealAgent(dealAgent);
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

                    //TODO
                    items.add(bean);
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
        //中信订单
        if (mailId.indexOf(CITIC)!= -1) {
            for(OutImportBean bean : beans){
                _logger.info("***onCreateOA***"+bean.getCiticNo());
                this.citicOrderDAO.updateStatus(bean.getCiticNo());
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