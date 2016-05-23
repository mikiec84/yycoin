package com.china.center.oa.sail.portal;

import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ComposeFeeBean;
import com.china.center.oa.product.bean.ComposeItemBean;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.bean.PackageItemBean;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 15-1-3
 * Time: 上午11:15
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    private void generateQRCode_general(String myCodeText)throws WriterException {
        try {
            InetAddress thisIp =InetAddress.getLocalHost();
            System.out.println("IP:"+thisIp.getHostAddress());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String filePath = "D:/CK201410201617123517.png";
        int size = 250;
        String fileType = "png";
        File myFile = new File(filePath);
        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
//                    size, hintMap);

            com.google.zxing. MultiFormatWriter writer =new MultiFormatWriter();
            BitMatrix byteMatrix = writer.encode(myCodeText, BarcodeFormat.CODE_128,150, 150);

            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, myFile);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");

    }

    public String getInCondition(String value){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String specifiedDay = sdf.format(date);
        System.out.println(specifiedDay);
        //SO1602230844017663321;SO1602231628017664378;
        StringBuilder sb = new StringBuilder();
        String[] values = value.split(";");
        sb.append("(");
        for(String str: values){
            sb.append("'").append(str).append("',");
        }

        String str = sb.toString();
        return str.substring(0,str.length()-1)+")";
    }
    public String getProductName(String original){
        System.out.println("hello"+null);
        String test ="AND PackageBean.status =0 AND PackageBean.logTime >= '2016-02-18 00:00:00' AND PackageItemBean.productName like '%a%'";
        test = test.replace("AND PackageItemBean.productName like '%a%'","");
//        test = test.replace("d","haha");
        System.out.println(test);
        System.out.println(6*6/9);
        System.out.println(6*6%9);
        String name = "";
        String[] l1 = original.split(" ");
        if (l1.length == 1){
            name = original;
        } else{
            String word = l1[1];
            String[] l2 = word.split("（");
            if (l2.length == 1){
                name = word;
            } else{
                name = l2[0];
            }
        }
//        for (String s:l1){
//            System.out.println(s);
//        }
        return name;
    }


    public String getCustomerName(String original){
        String name = original;
        String[] l1 = original.split("（|-");
        if (l1.length >= 1){
            name = l1[0];
        }

        return name;
    }

    public String getRefOutId(String description){
        String refOutId = "";
        if (!StringTools.isNullOrNone(description) && description.indexOf("自动生成赠品订单")!= -1){
            String[] words = description.split("：");
            if (words.length ==2){
                refOutId = words[1].trim();
            }
        }

        return refOutId;
    }
    public static void main(String[] args) throws Exception{
        String n1 = "YG0021100 2015年1盎司熊猫银币含包装（普17）(17556733)";
        String n2 = "YG0021100 2015年1盎司熊猫银币含包装（普17）";
        String n3 = "YH092 2014年熊猫金银币套装1/20盎司金+1盎司银（普0）(13361456)";
        String n4 = "YH2012003四季平安金条100克(永银）";
        String desc2 = "自动生成赠品订单，关联销售单：SO1509061033238507733";

        String c1 = "中原银行驻马店分行（银行）";
        String c2 = "中原银行驻马店分行-零售";
        String c3 = "中原银行驻马店分行1";

        String date ="2015-03-19";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf1.parse(date);

        Test t = new Test();
        t.generateQRCode_general("CK111222");
        System.out.println(t.getInCondition("SO1602230844017663321;SO1602231628017664378;"));
        System.out.println(t.getInCondition("SO1603311420017767434;"));
        String name = null;
        name = t.getCustomerName(c1);
        System.out.println("c1:"+name);

        name = t.getCustomerName(c2);
        System.out.println("c2:"+name);

        name = t.getCustomerName(c3);
        System.out.println("c3:"+name);

        name = t.getProductName(n2);
        System.out.println("n2:"+name);

        name = t.getProductName(n1);
        System.out.println("n1:"+name);

        name = t.getProductName(n3);
        System.out.println("n3:"+name);

        name = t.getProductName(n4);
        System.out.println("n4:"+name);

        System.out.println("refOutID:"+t.getRefOutId(desc2));

        double v2 = -3*3.3;
        System.out.println(v2);
        BigDecimal bd = new BigDecimal(v2);
        double v1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(v1);
//        double giftPercentage = ((double)3)/7;
//        int amount = (int)(giftPercentage*9);
//        System.out.println(giftPercentage);
//        System.out.println(giftPercentage*9);
//        System.out.println(amount);
//        String outIds = "SO1504161858017551344;SO1504161858017551345;";
//        StringTokenizer  st = new  StringTokenizer(outIds,";");
//        while(st.hasMoreTokens()) {
//            String outId = st.nextToken();
//            System.out.println(outId);
//        }
        System.out.println("SO1503162214017510324;".replace("SO1503162214017510324", "SO201508026"));
        double d1 = 2.3;
        double d2 = 2.3;
        System.out.println(d1==d2);
        double value2 = 0.585;
        System.out.println(new BigDecimal(value2));
        System.out.println(BigDecimal.valueOf(value2));
        System.out.println(Double.valueOf(d1).equals(Double.valueOf(d2)));
        System.out.println(Double.valueOf(d1)==(Double.valueOf(d2)));
        String endDate ="2015-03-19 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date end = sdf.parse(endDate);
        Date now = new Date();
        System.out.println(end.compareTo(now));
        endDate ="2015-05-19 23:59:59";
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        end = sdf.parse(endDate);
        System.out.println(end.compareTo(now));
        System.out.println(end.after(now));
        String sql1 = "<= '2015-03-19 23:59:59' AND PackageBean.status =0 AND PackageBean.insFollowOut =1 AND PackageBean.logTime >= '2015-03-12" ;
        String placeholder = "AND PackageBean.insFollowOut =";
        int index3 = sql1.indexOf(placeholder);
        System.out.println(index3);
        sql1 = sql1.substring(0, index3)+sql1.substring(index3+placeholder.length()+1);
        System.out.println(sql1);
//        sql1 = "hello";
//        System.out.println(sql1);
        String desc = "紫金农商订单转OA订单'. ZJ1502242118017451319'.发货备注：.发票抬头：，发票明细:,发票备注:.销售单备注：";
        String desc1 = desc;
        desc1 = "haha";
        System.out.println(desc);
        System.out.println(desc1);
        System.out.println(desc);
        String[] temp1 = desc.split("\\.");
        int limit = 2;
        List<String> packageList = Arrays.asList(temp1);
        int batchCount = temp1.length/limit+1;
        System.out.println(batchCount);
        for (int i=0;i<batchCount;i++){
            List<String> soList = new ArrayList<String>();
            if (i== batchCount-1){
                soList = packageList.subList(i*limit,packageList.size());
            }   else{
                soList = packageList.subList(i*limit,(i+1)*limit);
            }
        }
        System.out.println(temp1.length);
        System.out.println(11/2+1);
        System.out.println(temp1[1].trim());

        String test = "a1~a2~a3~";
        String[] arrays = test.split("~");
        System.out.println(arrays.length);
        for (String str: arrays){
            System.out.println(str);
        }
//        System.out.println()
        System.out.println(test.split(";").length);
        List<OutBean>  beans = new ArrayList<OutBean>();
        OutBean bean1 = new OutBean();
        bean1.setId("1");
        OutBean bean2 = new OutBean();
        bean2.setId("2");
        beans.add(bean1);
        beans.add(bean2);
        for(OutBean bean: beans){
            System.out.println("1111*********"+bean.getId());
        }
        OutBean bean3 = beans.get(0);
        bean3.setId("3");
        for(OutBean bean: beans){
            System.out.println("2222*************"+bean.getId());
        }
        String str1 = "test1 AND test2 AND PackageItemBean.productName like '%啊%'";
        int index2 = str1.lastIndexOf("AND");
        String prefix = str1.substring(0,index2);
        System.out.println("index***"+index2);
        System.out.println("data***"+prefix);
//        String[] temp2 = str1.split("AND");
//        String  str2 = temp1[1];
        String str3 = prefix+"and exists (select PackageItemBean.id from t_center_package_item PackageItemBean where PackageItemBean.productName like '%金马%')";
        System.out.println("**********str3**************"+str3);
        String bank ="浦发银行上海静安支行办公室";
        int index = bank.indexOf("银行");
        System.out.println(bank.split("银行")[0]+"银行");

        List<PackageItemBean> lastList = new ArrayList<PackageItemBean>();
        PackageItemBean p1 = new PackageItemBean();
        p1.setProductName("a");
        PackageItemBean p2 = new PackageItemBean();
        p2.setProductName("b");
        PackageItemBean p3 = new PackageItemBean();
        p3.setProductName("a22");
        lastList.add(p1);
        lastList.add(p2);
        lastList.add(p3);
        System.out.println("11111111111111111111");
        for (PackageItemBean p : lastList){
            System.out.println(p.getProductName());
        }
        Collections.sort(lastList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                PackageItemBean i1 = (PackageItemBean)o1;
                PackageItemBean i2 = (PackageItemBean)o2;
                return i1.getProductName().compareTo(i2.getProductName());  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        System.out.println("222222222222222222222222222");
        for (PackageItemBean p : lastList){
            System.out.println(p.getProductName());
        }


        String str2 = "能不能判断地址后6个字符一致";
        String temp = str2.substring(str2.length()-6);
        System.out.println(temp);
        System.out.println(2==Integer.valueOf("2"));
        String str ="16524963:bomProductName=&amount=2&location=&bomProductName=16524967&amount=2&location=10201103130001000167&bomProductName=16725384&amount=2&location=10201103130001000179;16525315:bomProductName=&amount=3&location=&bomProductName=16725448&amount=3&location=99000000000000000001&bomProductName=16725452&amount=3&location=A1201204171505676333:";

        String[] arr1 = str.split(";");
        System.out.println(arr1.length);
        for (String s : arr1){
            System.out.println(s);
            String[] arr2 = s.split(":");
            String productId = arr2[0];
            String[] arr3 = arr2[1].split("&");
            System.out.println("productId:"+productId+":"+arr3.length);
            ComposeProductBean bean = new ComposeProductBean();
            bean.setProductId(productId);

            if (arr3.length>=3){
                List<ComposeItemBean> itemList = new ArrayList<ComposeItemBean>();
                for (int i=3;i<arr3.length;i+=3){
                    System.out.println(arr3[i]);
                    ComposeItemBean each = new ComposeItemBean();
                    for (int j=0;j<3;j++){
                        System.out.println(arr3[i+j]);
                        String value = arr3[i+j].split("=")[1];
                        if (j==0){
                            each.setProductId(value);
                        } else if (j==1){
                            each.setAmount(Integer.valueOf(value));
                        } else if (j==2){
                            each.setDeportId(value);
                        }
                    }
                    itemList.add(each);
                }
                System.out.println("itemList size****"+itemList.size());
                bean.setItemList(itemList);
            }

        }
    }

}
