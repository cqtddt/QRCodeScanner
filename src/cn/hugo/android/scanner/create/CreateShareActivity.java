package cn.hugo.android.scanner.create;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import cn.hugo.android.scanner.R;

public class CreateShareActivity extends Activity {
	
	private ImageView qrShowImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_show_layout);
		qrShowImg = (ImageView) findViewById(R.id.qrCodeShowImg);
		createLogQRImg();
//		ShowInfo showInfo = new ShowInfo();
//		showInfo.setLogoName("融e购");
//		showInfo.setStoreName("IPhone 旗舰店");
//		showInfo.setUserName("撑起**天");
//		showInfo.setWeChatHint("分享了一个宝贝给你");
//		showInfo.setProdName("美女一枚，价格优惠");
//		showInfo.setProdActivityPrice("￥47-52");
//		showInfo.setProdPrice("￥129-134");
//		showInfo.setProdUrl("http://www.baidu.com.cn");
//		showInfo.setQrCodeHint("二维码会有惊喜");
//		final String filePath = getFileRoot(CreateShareActivity.this) + File.separator
//				+ "qr_" + System.currentTimeMillis() + ".jpg";
//		QRCodeUtil.createShareWechatImg(CreateShareActivity.this,qrShowImg,showInfo,filePath);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	
	private void createLogQRImg() {
		// TODO Auto-generated method stub
		final String filePath = getFileRoot(CreateShareActivity.this) + File.separator
				+ "qr_" + System.currentTimeMillis() + ".jpg";

		// 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				ShowInfo showInfo = new ShowInfo();
				showInfo.setLogoName("天天");
				showInfo.setStoreName("苹果旗舰店");
				showInfo.setUserName("撑起**天");
				showInfo.setWeChatHint("分享了一个宝贝给你");
				showInfo.setProdName("美女一枚，价格优惠");
				showInfo.setProdActivityPrice("￥47-52");
				showInfo.setProdPrice("￥129-134");
				showInfo.setProdUrl("http://www.baidu.com.cn");
				showInfo.setQrCodeHint("二维码会有惊喜");
				boolean success = QRCodeUtil.createShareWechatImg(CreateShareActivity.this,showInfo,filePath);
				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							qrShowImg.setImageBitmap(BitmapFactory
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

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
