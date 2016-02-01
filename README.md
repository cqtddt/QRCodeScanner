# QRCodeScanner
二维码扫描，生成二维码，生成条形码，生成类似天猫微信分享图片

设置参数如下：
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

调用方法：
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

分享图片如下：
![image](qr_1454033085851.jpg) 
