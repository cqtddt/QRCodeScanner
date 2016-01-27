package cn.hugo.android.scanner.create;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.view.WindowManager;
import cn.hugo.android.scanner.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成工具类
 */
public class QRCodeUtil {
	/**
	 * 生成二维码Bitmap
	 * 
	 * @param content
	 *            内容
	 * @param widthPix
	 *            图片宽度
	 * @param heightPix
	 *            图片高度
	 * @param logoBm
	 *            二维码中心的Logo图标（可以为null）
	 * @param filePath
	 *            用于存储二维码图片的文件路径
	 * @return 生成二维码及保存文件是否成功
	 */
	public static boolean createQRImage(String content, int widthPix,
			int heightPix, Bitmap logoBm, String filePath) {
		try {
			if (content == null || "".equals(content)) {
				return false;
			}

			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置空白边距的宽度
			hints.put(EncodeHintType.MARGIN, 0); // default is 4

			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(content,
					BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
			int[] pixels = new int[widthPix * heightPix];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < heightPix; y++) {
				for (int x = 0; x < widthPix; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * widthPix + x] = 0xff000000;
					} else {
						pixels[y * widthPix + x] = 0xffffffff;
					}
				}
			}

			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

			if (logoBm != null) {
				bitmap = addLogo(bitmap, logoBm);
			}

			// 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			return bitmap != null
					&& bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							new FileOutputStream(filePath));
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		// 获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight,
				Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2,
					(srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

	/**
	 * 生成二维码Bitmap
	 * 
	 * @param content
	 *            内容
	 * @param widthPix
	 *            图片宽度
	 * @param heightPix
	 *            图片高度
	 * @param logoBm
	 *            二维码中心的Logo图标（可以为null）
	 * @param filePath
	 *            用于存储二维码图片的文件路径
	 * @return 生成二维码及保存文件是否成功
	 */
	public static boolean createShareToWeiXinQRImage(String content, int width,
			int height, Bitmap logoBm, Bitmap productImg, String filePath) {
		try {

			int widthPix = 500;
			int heightPix = 500;
			if (content == null || "".equals(content)) {
				return false;
			}

			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置空白边距的宽度
			// hints.put(EncodeHintType.MARGIN, 2); //default is 4

			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(content,
					BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
			int[] pixels = new int[widthPix * heightPix];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < heightPix; y++) {
				for (int x = 0; x < widthPix; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * widthPix + x] = 0xff000000;
					} else {
						pixels[y * widthPix + x] = 0xffffffff;
					}
				}
			}

			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

			if (logoBm != null) {
				bitmap = addZhiwen(bitmap, logoBm, productImg, width, height);
			}

			// 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			return bitmap != null
					&& bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							new FileOutputStream(filePath));
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 在二维码中间添加Logo图案
	 * 
	 * @param height
	 * @param width
	 */
	private static Bitmap addZhiwen(Bitmap qrImg, Bitmap logo, Bitmap prodImg,
			int width, int height) {
		if (qrImg == null) {
			return null;
		}

		if (logo == null) {
			return qrImg;
		}

		// 获取图片的宽高
		int srcWidth = qrImg.getWidth();
		int srcHeight = qrImg.getHeight();

		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		int prodWidth = prodImg.getWidth();
		int prodHeight = prodImg.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return qrImg;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		float prodScaleFactor = width * 3.0f / 4 / prodHeight;

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(prodImg, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2,
					(srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

	/**
	 * 生成二维码Bitmap
	 * 
	 * @param content
	 *            内容
	 * @param widthPix
	 *            图片宽度
	 * @param heightPix
	 *            图片高度
	 * @param logoBm
	 *            二维码中心的Logo图标（可以为null）
	 * @param filePath
	 *            用于存储二维码图片的文件路径
	 * @return 生成二维码及保存文件是否成功
	 */
	public static Bitmap createQRImageBitmap(String content, int widthPix,
			int heightPix, Bitmap logoBm) {
		try {
			if (content == null || "".equals(content)) {
				return null;
			}

			// 配置参数
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 容错级别
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置空白边距的宽度
			hints.put(EncodeHintType.MARGIN, 0); // default is 4

			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix;
			bitMatrix = new QRCodeWriter().encode(content,
					BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
			int[] pixels = new int[widthPix * heightPix];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < heightPix; y++) {
				for (int x = 0; x < widthPix; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * widthPix + x] = 0xff000000;
					} else {
						pixels[y * widthPix + x] = 0xffffffff;
					}
				}
			}

			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

			if (logoBm != null) {
				bitmap = addLogo(bitmap, logoBm);
			}

			// 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
			// return bitmap != null &&
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new
			// FileOutputStream(filePath));
			return bitmap;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("NewApi")
	public static boolean createShareWechatImg(Context context, ShowInfo showInfo, String filePath) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		if (null != showInfo) {
			String logoText = showInfo.getLogoName();
			String storeName = showInfo.getStoreName();
			String name = showInfo.getUserName();
			String hint = showInfo.getWeChatHint();
			String prodName = showInfo.getProdName();
			String prodPrice = showInfo.getProdPrice();
			String prodActivityPrice = showInfo.getProdActivityPrice();
			String qrcodeHint = showInfo.getQrCodeHint();

			// 创建画笔
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, width, height, paint);
			
			paint.setColor(Color.RED);// 设置红色
			// 红色矩形 其高度为屏幕高度的八分之一
			float rectHeight = (float) (height / 8.0);
			// 画红色矩形
			canvas.drawRect(0, 0, width, rectHeight, paint);

			paint.setTextSize(100);
			// 不能设置这个，设置这个需要从计算字体中心坐标
			// paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(Color.WHITE);
			FontMetrics fontMetrics = paint.getFontMetrics();// 计算文字高度

			float baseline = (rectHeight - fontMetrics.bottom - fontMetrics.top) / 2;
			float logoTextMarginLeft = 50;
			// 其中的y坐标不是字体左上角的坐标而是字体的基线y坐标
			canvas.drawText(logoText, logoTextMarginLeft, baseline, paint);

			float logoTextWidth = paint.measureText(logoText);
			float lineX = logoTextMarginLeft * 2 + logoTextWidth;
			float lineY = baseline + fontMetrics.ascent;
			float lineB = baseline + fontMetrics.descent;
			paint.setStrokeWidth(5);
			// 画分割线
			canvas.drawLine(lineX, lineY, lineX, lineB, paint);

			paint.setTextSize(70);
			// paint.setTextAlign(Align.CENTER);
			paint.setColor(Color.WHITE);
			FontMetrics storeFontMetrics = paint.getFontMetrics();// 计算文字高度
			float storeBaseline = (rectHeight - storeFontMetrics.bottom - storeFontMetrics.top) / 2;
			// float fontHeight1 = fontMetrics1.descent -
			// fontMetrics1.ascent;//计算文字baseline
			float storeWidth = paint.measureText(storeName);

			float storeX = (width - logoTextMarginLeft * 2 - logoTextWidth - 5 - storeWidth)
					/ 2 + logoTextMarginLeft * 2 + logoTextWidth + 5;
			// 画店名称字样
			canvas.drawText(storeName, storeX, storeBaseline, paint);

			paint.setColor(Color.RED);// 设置红色
			paint.setTextSize(50);
			float userMarginTopBottom = 25;
			float userMarginLeft = 50;
			FontMetrics userFontMetrics = paint.getFontMetrics();
			float userFontHeight = userFontMetrics.descent
					- userFontMetrics.ascent;
			canvas.drawText(name, userMarginLeft, rectHeight
					+ userMarginTopBottom + userFontHeight, paint);// 画文本

			float nameWidth = paint.measureText(name);
			paint.setColor(Color.BLACK);
			canvas.drawText(hint, nameWidth + userMarginLeft * 2, rectHeight
					+ userMarginTopBottom + userFontHeight, paint);

			Bitmap bg = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.ad_bg);
			// FontMetrics fontMetrics2 = paint.getFontMetrics();//计算文字高度
			// float fontHeight2 = fontMetrics2.descent -
			// fontMetrics2.ascent;//计算文字baseline
			int prodImgX = 0;
			int prodImgY = (int) (rectHeight + 2 * userMarginTopBottom + userFontHeight);
			int prodImgRight = width;
			int prodImgBottom = prodImgY + height / 2;
			Rect rect = new Rect(prodImgX, prodImgY, prodImgRight,
					prodImgBottom);
			canvas.drawBitmap(bg, null, rect, null);

			canvas.drawText(prodName, 0 + userMarginLeft, prodImgBottom
					+ userMarginTopBottom + userFontHeight, paint);
			paint.setColor(Color.RED);

			canvas.drawText(prodActivityPrice, 0 + userMarginLeft,
					prodImgBottom + userMarginTopBottom + userFontHeight
							+ userFontHeight, paint);

			paint.setColor(Color.GRAY);
			int flag = paint.getFlags();
			paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			float priceWidth = paint.measureText(prodActivityPrice);
			canvas.drawText(prodPrice, 0 + userMarginLeft + userMarginLeft
					+ priceWidth, prodImgBottom + userMarginTopBottom + 2
					* userFontHeight, paint);

			float qrCodeMargin = 50;
			// 画红色矩形
			paint.setColor(Color.rgb(0xff, 0xfa, 0xfa));
			float bottomRectLeft = 0;
			float bottomRectRight = width;
			float bottomRectTop = prodImgBottom + 2 * userMarginTopBottom + 2
					* userFontHeight + qrCodeMargin;
			float bottomRectBottom = height;

			canvas.drawRect(bottomRectLeft, bottomRectTop, bottomRectRight,
					bottomRectBottom, paint);

			float LeftHeight = height - bottomRectTop;
			float qrWidthHeight = LeftHeight - 2 * qrCodeMargin;
			Bitmap logoImg = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher);
			Bitmap qrCodeBitmap = createQRImageBitmap(showInfo.getProdUrl(),
					(int) qrWidthHeight, (int) qrWidthHeight, logoImg);
			int qrCodeImgLeft = (int) qrCodeMargin;
			int qrCodeImgRight = (int) (qrWidthHeight + qrCodeMargin);
			int qrCodeImgTop = (int) (bottomRectTop + qrCodeMargin);
			int qrCodeImgBottom = (int) (qrCodeImgTop + qrWidthHeight);
			Rect rect1 = new Rect(qrCodeImgLeft, qrCodeImgTop, qrCodeImgRight,
					qrCodeImgBottom);
			canvas.drawBitmap(qrCodeBitmap, null, rect1, null);

			Bitmap fingerprint = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.fingerprint);
			paint.setColor(Color.BLACK);
			paint.setFlags(flag);

			float qrHintWidth = paint.measureText("长按识别" + qrcodeHint);
			// float testLeft = qrCodeImgRight + qrCodeMargin;
			// float testTop = qrCodeImgTop;
			// float testRight = width - qrCodeMargin;
			// float testBottom = qrCodeImgBottom;
			// canvas.drawRect(testLeft, testTop, testRight, testBottom, paint);

			int fingerLeft = (int) (qrCodeImgRight + qrCodeMargin + (width
					- qrCodeMargin - qrCodeImgRight - qrCodeMargin
					- qrWidthHeight + userFontHeight) / 2);
			int fingerRight = (int) (fingerLeft + qrWidthHeight - userFontHeight);
			int fingerTop = qrCodeImgTop;
			int fingerBottom = (int) (qrCodeImgBottom - userFontHeight);

			// int fingerLeft = (int) (fingerRight - qrWidthHeight +
			// userFontHeight);
			// int fingerRight = (int) (width - qrCodeMargin);
			// int fingerTop = qrCodeImgTop;
			// int fingerBottom = (int) (qrCodeImgBottom - userFontHeight)
			Rect fingerRect = new Rect(fingerLeft, fingerTop, fingerRight,
					fingerBottom);

			canvas.drawBitmap(fingerprint, null, fingerRect, null);

			paint.setColor(Color.RED);
			float fingerHintX = qrCodeImgRight
					+ qrCodeMargin
					+ (width - qrCodeMargin - qrCodeImgRight - qrCodeMargin - qrHintWidth)
					/ 2;
			float fingerHintY = fingerBottom + userFontHeight;
			canvas.drawText("长按识别", fingerHintX, fingerHintY, paint);

			paint.setColor(Color.BLACK);
			canvas.drawText(qrcodeHint,
					fingerHintX + paint.measureText("长按识别"), fingerHintY, paint);

		}
		try {
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
//			Resources res = context.getResources();
//			img.setBackground(new BitmapDrawable(res, bitmap));
//			
			//若参数设置为Bitmap.CompressFormat.JPEG会出现背景为黑色的图片
			return bitmap != null
					&& bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// img.setImageBitmap(bitmap);
		return false;
	}

}
