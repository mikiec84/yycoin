package com.china.center.oa.sail.manager.impl;

/**
 * Created by user on 2016/4/8.
 */
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.oa.sail.bean.CiticOrderBean;
import com.china.center.oa.sail.dao.CiticOrderDAO;
import com.china.center.tools.ListTools;
import com.china.center.tools.StringTools;
import com.sun.mail.imap.IMAPMessage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.*;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


public class ImapMailClient {
    public static final String IMAP = "imap";

    private CiticOrderDAO citicOrderDAO = null;

    public CiticOrderDAO getCiticOrderDAO() {
        return citicOrderDAO;
    }

    public void setCiticOrderDAO(CiticOrderDAO citicOrderDAO) {
        this.citicOrderDAO = citicOrderDAO;
    }

    public static void main(String[] args) throws Exception{
        ImapMailClient client = new ImapMailClient();
        client.receiveEmail("imap.163.com", "yycoindd@163.com", "yycoin1234");
    }

    public void receiveEmail(String host, String username, String password) throws Exception {
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
            System.out.println("***mail count***" + messages.length);
            System.out.println("***unread mail count***" + inbox.getUnreadMessageCount());

            IMAPMessage msg;
            for (Message message : messages) {
                msg = (IMAPMessage) message;
                Flags flags = message.getFlags();
                if (flags.contains(Flags.Flag.SEEN)){
                    System.out.println("这是一封已读邮件");
                    continue;
                }
                else {
                    System.out.println("未读邮件");
                }
                String from = decodeText(msg.getFrom()[0].toString());
                InternetAddress ia = new InternetAddress(from);
                System.out.println("FROM:" + ia.getPersonal() + '(' + ia.getAddress() + ')');
                System.out.println("TITLE:" + msg.getSubject());
                System.out.println("SIZE:" + msg.getSize());
                System.out.println("DATE:" + msg.getSentDate());
                System.out.println("Content:" + msg.getContent());
                System.out.println("ContentType:" + msg.getContentType());
                Enumeration headers = msg.getAllHeaders();
                System.out.println("----------------------allHeaders-----------------------------");
                while (headers.hasMoreElements()) {
                    Header header = (Header) headers.nextElement();
                    System.out.println(header.getName() + " ======= " + header.getValue());
                }
                parseMultipart(msg.getContent());
//                msg.setFlag(Flags.Flag.SEEN, true);
                System.out.println("***finished***");
//                String filename = "d:/temp/" + decodeText(msg.getSubject());
//                System.out.println(filename);
//                saveParts(msg.getContent(), filename);
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
            } catch (Exception ignored) {
            }
        }
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
     * �Ը����ʼ��Ľ���
     *
     * @param content
     * @throws MessagingException
     * @throws IOException
     */
    public void parseMultipart(Object content) throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            int count = multipart.getCount();
            System.out.println("***parseMultipart count =  " + count);
            for (int idx = 0; idx < count; idx++) {
                BodyPart bodyPart = multipart.getBodyPart(idx);
                System.out.println(bodyPart.getContentType());
                System.out.println("***fileName***" + bodyPart.getFileName());
                if (bodyPart.isMimeType("text/plain")) {
                    System.out.println("plain................." + bodyPart.getContent());
                } else if (bodyPart.isMimeType("text/html")) {
                    System.out.println("html..................." + bodyPart.getContent());
                } else if (bodyPart.isMimeType("multipart/*")) {
                    Multipart mpart = (Multipart) bodyPart.getContent();
                    parseMultipart(mpart);
                } else if (bodyPart.isMimeType("application/octet-stream")) {
                    String disposition = bodyPart.getDisposition();
                    System.out.println("***disposition***" + disposition);
                    System.out.println("***disposition***" + bodyPart.getInputStream());
                    if (BodyPart.ATTACHMENT.equalsIgnoreCase(disposition) || bodyPart.getInputStream()!= null) {
                        String fileName = bodyPart.getFileName();
                        System.out.println("****fileName***" + fileName);
                        fileName = MimeUtility.decodeText(fileName);
                        System.out.println("****name2***" + fileName + "***size" + bodyPart.getSize());
                        InputStream is = bodyPart.getInputStream();
                        List<CiticOrderBean> items = parse(is);
                        if (this.citicOrderDAO!= null && !ListTools.isEmptyOrNull(items)){
                            this.citicOrderDAO.saveAllEntityBeans(items);
                        }
//                        copy(is, new FileOutputStream("D:\\" + fileName));
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

    public static List<CiticOrderBean> parse(InputStream is) throws IOException{
        ReaderFile reader = ReadeFileFactory.getXLSReader();
        List<CiticOrderBean> items = new ArrayList<CiticOrderBean>();
        try
        {
            reader.readFile(is);

            while (reader.hasNext())
            {
                String[] obj = fillObj((String[])reader.next());

                // 前三行忽略
                if (reader.getCurrentLineNumber() <= 3)
                {
                    continue;
                }

                if (StringTools.isNullOrNone(obj[0]))
                {
                    continue;
                }

                int currentNumber = reader.getCurrentLineNumber();

                System.out.println("****11111***"+currentNumber);
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
                    String customer = obj[i++];
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

    public static void copy(InputStream is, OutputStream os) throws IOException {
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