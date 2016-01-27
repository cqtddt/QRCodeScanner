package cn.hugo.android.scanner;

import java.io.File;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.hugo.android.scanner.create.CreateShareActivity;
import cn.hugo.android.scanner.create.QRCodeUtil;
import cn.hugo.android.scanner.create.ShowInfo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends Activity implements OnClickListener {

	private ImageView mCreateView;
	private ImageView mCreateBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initWidgets();
	}

	private void initWidgets() {
		Button btn = (Button) findViewById(R.id.btn_handle_decode);
		btn.setOnClickListener(this);
		btn = (Button) findViewById(R.id.btn_create_decode);
		btn.setOnClickListener(this);
		btn = (Button) findViewById(R.id.btn_create_barcode);
		btn.setOnClickListener(this);
		mCreateView = (ImageView) findViewById(R.id.iv_result);
		mCreateBarView = (ImageView) findViewById(R.id.iv_result_bar);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_handle_decode:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_create_decode:
//			 mCreateView
//			 .setImageBitmap(createQRImage("http:baidu.com.cn", 600, 600));

			//createLogQRImg();
			createShareToWechatImg();
			break;
		case R.id.btn_create_barcode:
			mCreateBarView.setImageBitmap(creatBarcode(getApplicationContext(),
					"692697307889", 600, 400, true));
			break;
		default:
			break;
		}
	}

	private void createShareToWechatImg() {
		// TODO Auto-generated method stub
		//QRCodeUtil.createShareWechatImg(this, mCreateView);
//		ShowInfo showInfo = new ShowInfo();
//		showInfo.setLogoName("融e购");
//		showInfo.setStoreName("IPhone 旗舰店");
//		showInfo.setUserName("撑起**天");
//		showInfo.setWeChatHint("分享了一个宝贝给你");
//		showInfo.setProdName("美女一枚，价格优惠");
//		showInfo.setProdActivityPrice("￥47-52");
//		showInfo.setProdPrice("￥129-134");
//		showInfo.setProdUrl("www.baidu.com");
//		QRCodeUtil.createShareWechatImg(this, showInfo, mCreateView);
		Intent intent = new Intent(this,CreateShareActivity.class);
		startActivity(intent);
	}

	private void createLogQRImg() {
		// TODO Auto-generated method stub
		final String filePath = getFileRoot(MainActivity.this) + File.separator
				+ "qr_" + System.currentTimeMillis() + ".jpg";

		// 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = QRCodeUtil.createQRImage(
						"http://www.baidu.com.cn", 800, 800, BitmapFactory
								.decodeResource(getResources(),
										R.drawable.ic_launcher), filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mCreateView.setImageBitmap(BitmapFactory
									.decodeFile(filePath));
						}
					});
				}
			}
		}).start();

	}

	private void createLogQRImg1() {
		// TODO Auto-generated method stub
		final String filePath = getFileRoot(MainActivity.this) + File.separator
				+ "qr_" + System.currentTimeMillis() + ".jpg";

		// 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = QRCodeUtil.createShareToWeiXinQRImage(
						"http://www.baidu.com.cn", 800, 800, BitmapFactory
								.decodeResource(getResources(),
										R.drawable.ic_launcher), BitmapFactory
								.decodeResource(getResources(),
										R.drawable.ad_bg), filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mCreateView.setImageBitmap(BitmapFactory
									.decodeFile(filePath));
						}
					});
				}
			}
		}).start();

	}

	// 文件存储根目录
	private String getFileRoot(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File external = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			if (external != null) {
				return external.getAbsolutePath();
			}
		}

		return context.getFilesDir().getAbsolutePath();
	}

	/**
	 * ��ɶ�ά�� Ҫת���ĵ�ַ���ַ�,����������
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap createQRImage(String url, final int width, final int height) {
		try {
			// �ж�URL�Ϸ���
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// ͼ�����ת����ʹ���˾���ת��
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// �������ﰴ�ն�ά����㷨�������ɶ�ά���ͼƬ��
			// ����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// ��ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ɶ�ά�� Ҫת���ĵ�ַ���ַ�,����������
	 * 
	 * @param url
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap createQRImage1(String url, final int width, final int height) {
		try {
			// �ж�URL�Ϸ���
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// ͼ�����ת����ʹ���˾���ת��
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			// �������ﰴ�ն�ά����㷨�������ɶ�ά���ͼƬ��
			// ����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// ��ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������
	 * 
	 * @param context
	 * @param contents
	 *            ��Ҫ��ɵ�����
	 * @param desiredWidth
	 *            ���������Ŀ��
	 * @param desiredHeight
	 *            ���������ĸ߶�
	 * @param displayCode
	 *            �Ƿ����������·���ʾ����
	 * @return
	 */
	private static Bitmap creatBarcode(Context context, String contents,
			int desiredWidth, int desiredHeight, boolean displayCode) {
		Bitmap ruseltBitmap = null;
		/**
		 * ͼƬ���������Ŀհ׵Ŀ��
		 */
		int marginW = 20;
		/**
		 * ������ı�������
		 */
		BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

		if (displayCode) {
			Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
					desiredWidth, desiredHeight);
			Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
					* marginW, desiredHeight, context);
			ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
					0, desiredHeight));
		} else {
			ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
					desiredWidth, desiredHeight);
		}

		return ruseltBitmap;
	}

	/**
	 * ����������Bitmap
	 * 
	 * @param contents
	 *            ��Ҫ��ɵ�����
	 * @param format
	 *            �����ʽ
	 * @param desiredWidth
	 * @param desiredHeight
	 * @return
	 * @throws WriterException
	 */
	private static Bitmap encodeAsBitmap(String contents,
			BarcodeFormat format, int desiredWidth, int desiredHeight) {
		final int WHITE = 0xFFFFFFFF;
		final int BLACK = 0xFF000000;

		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contents, format, desiredWidth,
					desiredHeight, null);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * �����ʾ�����Bitmap
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param context
	 * @return
	 */
	protected static Bitmap creatCodeBitmap(String contents, int width,
			int height, Context context) {
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(layoutParams);
		tv.setText(contents);
		tv.setHeight(height);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setWidth(width);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}

	/**
	 * ������Bitmap�ϲ���һ��
	 * 
	 * @param first
	 * @param second
	 * @param fromPoint
	 *            �ڶ���Bitmap��ʼ���Ƶ���ʼλ�ã�����ڵ�һ��Bitmap��
	 * @return
	 */
	protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
			PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		int marginW = 20;
		Bitmap newBitmap = Bitmap.createBitmap(
				first.getWidth() + second.getWidth() + marginW,
				first.getHeight() + second.getHeight(), Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, marginW, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return newBitmap;
	}
}
