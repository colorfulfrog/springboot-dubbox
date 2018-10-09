package com.yxhl.platform.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.InvalidAtributeException;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PrintBarcodeUtil {

	/**
	 * @FunctionName generateBarCode
	 *               （转自：http://blog.csdn.net/shb/article/details/）
	 * @Description 生成返回值为Base编码的条形码
	 * @param strBarCode
	 * @param dimension
	 * @param barheight
	 * @return
	 */
	public static String generateBarCode128(String strBarCode, String dimension, String barheight) {
		try {
			ByteArrayOutputStream outputStream = null;
			BufferedImage bi = null;
			JBarcode productBarcode = new JBarcode(Code128Encoder.getInstance(), WidthCodedPainter.getInstance(),
					EAN13TextPainter.getInstance());

			// 尺寸，面积，大小 密集程度
			productBarcode.setXDimension(Double.valueOf(dimension).doubleValue());
			// 高度 10.0 = 1cm 默认1.5cm
			productBarcode.setBarHeight(Double.valueOf(barheight).doubleValue());
			// 宽度
			productBarcode.setWideRatio(Double.valueOf(30).doubleValue());
			// 是否显示字体
			productBarcode.setShowText(false);
			// 显示字体样式
			productBarcode.setTextPainter(BaseLineTextPainter.getInstance());

			// 生成二维码
			bi = productBarcode.createBarcode(strBarCode);

			outputStream = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", outputStream);
			// 创建输出流
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return "encodeError";
		}
	}

	/**
	 * @FunctionName outImgToPath
	 * @Description 生成条形码并输出到本地
	 * @author 魔方Cube
	 * @param strBarCode
	 *            条形码：-位
	 * @param dimension
	 *            商品条形码：尺寸
	 * @param barheight
	 *            商品条形码：高度
	 * @param path
	 *            存储路径
	 */
	public static void outImgToPath(String strBarCode, String dimension, String barheight, String path) {
		String encoder = generateBarCode128(strBarCode, dimension, barheight);
		if (encoder == null) {
			return;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b;
		try {
			b = decoder.decodeBuffer(encoder);
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws InvalidAtributeException
	 */
	public static void main(String[] args) throws InvalidAtributeException {

		String encode = PrintBarcodeUtil.generateBarCode128("9978901991376", "0.4", "15");
		System.out.println(encode);

	}
}
