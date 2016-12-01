package com.china.center.oa.sail.action;

import com.center.china.osgi.config.ConfigLoader;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 16-5-21
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class ZxingUtils {
    private static final Log _logger = LogFactory.getLog(ZxingUtils.class);

    public static void generateQRCode(String myCodeText)throws WriterException {
        _logger.info("***generateQRCode***"+myCodeText);
        String filePath = "D:\\oa_attachment\\images\\"+myCodeText+".png";
        String fileType = "png";
        File myFile = new File(filePath);
        try {
            _logger.info("***generateQRCode***111111111111"+myCodeText);
            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, 150,
                    150, hintMap);

//            com.google.zxing. MultiFormatWriter writer =new MultiFormatWriter();
//            BitMatrix byteMatrix = writer.encode(myCodeText, BarcodeFormat.CODE_128,100, 200);

            System.out.println(byteMatrix);
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
            _logger.info("***finish generateQRCode***"+myCodeText);
        } catch (WriterException e) {
            e.printStackTrace();
            _logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            _logger.error(e);
        } catch(Exception e){
            e.printStackTrace();
            _logger.error(e);
        }
    }

    public static void generateBarCode(String myCodeText)throws WriterException {
        _logger.info("***generateQRCode***"+myCodeText);
        String rootPath = ConfigLoader.getProperty("root_path");
        String filePath = rootPath+"/images/"+myCodeText+".png";
        String fileType = "png";
        File myFile = new File(filePath);
        try {
            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            Code128Writer writer =new Code128Writer();
            BitMatrix byteMatrix = writer.encode(myCodeText, BarcodeFormat.CODE_128, 200, 50);

            MatrixToImageWriter.writeToStream(byteMatrix, fileType, new FileOutputStream(myFile));
            _logger.info("***finish generate barcode***"+myCodeText);
        } catch (WriterException e) {
            e.printStackTrace();
            _logger.error(e);
        } catch (IOException e) {
            e.printStackTrace();
            _logger.error(e);
        } catch(Exception e){
            e.printStackTrace();
            _logger.error(e);
        }
    }

    public static void main(String[] args) throws Exception{
//        ZxingUtils.generateQRCode("CK201410201617123517");
        ZxingUtils.generateBarCode("CK201410201617123517");
    }
}
