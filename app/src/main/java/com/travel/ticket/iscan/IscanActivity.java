package com.travel.ticket.iscan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.travel.ticket.R;

/**
 * 工程说明：
 * 		1,该开发包是基于iScan开发，需要预装iScan4.2.0以上版本。
 *      2,基本设置功能如下：左扫描键连扫   右扫描键置空    中间扫描键单扫
 */

public class IscanActivity extends Activity {

	int i;

	TextView tvScanResult;
	ScannerInterface scanner;
	IntentFilter intentFilter;
	BroadcastReceiver scanReceiver;

	private boolean isContinue = false;	//连续扫描的标志

	private static final String RES_ACTION = "android.intent.action.SCANRESULT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iscan);
		tvScanResult = (TextView) this.findViewById(R.id.tv_scan_result);
		initScanner();
	}

	private void initScanner(){
		i=0;
		scanner = new ScannerInterface(this);
		scanner.open();	//打开扫描头上电   scanner.close();//打开扫描头下电
		scanner.enablePlayBeep(true);//是否允许蜂鸣反馈
		scanner.enableFailurePlayBeep(false);//扫描失败蜂鸣反馈
		scanner.enablePlayVibrate(true);//是否允许震动反馈
		scanner.enableAddKeyValue(1);/**附加无、回车、Teble、换行*/
		scanner.timeOutSet(2);//设置扫描延时2秒
		scanner.intervalSet(1000); //设置连续扫描间隔时间
		scanner.lightSet(false);//关闭右上角扫描指示灯
		scanner.enablePower(true);//省电模式
		scanner.setMaxMultireadCount(5);//设置一次最多解码5个
		//		scanner.addPrefix("AAA");//添加前缀
		//		scanner.addSuffix("BBB");//添加后缀
		//		scanner.interceptTrimleft(2); //截取条码左边字符
		//		scanner.interceptTrimright(3);//截取条码右边字符
		//		scanner.filterCharacter("R");//过滤特定字符
		scanner.SetErrorBroadCast(true);//扫描错误换行
		//scanner.resultScan();//恢复iScan默认设置

		//		scanner.lockScanKey();
		//锁定设备的扫描按键,通过iScan定义扫描键扫描，用户也可以自定义按键。
		scanner.unlockScanKey();
		//释放扫描按键的锁定，释放后iScan无法控制扫描按键，用户可自定义按键扫描。

		/**设置扫描结果的输出模式，参数为0和1：
		 * 0为模拟输出（在光标停留的地方输出扫描结果）；
		 * 1为广播输出（由应用程序编写广播接收者来获得扫描结果，并在指定的控件上显示扫描结果）
		 * 这里采用接收扫描结果广播并在TextView中显示*/
		scanner.setOutputMode(1);

		//扫描结果的意图过滤器的动作一定要使用"android.intent.action.SCANRESULT"
		intentFilter = new IntentFilter(RES_ACTION);
		//注册广播接受者
		scanReceiver = new ScannerResultReceiver();
		registerReceiver(scanReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finishScanner();
		scanner.lockScanKey();//锁定iScan扫描按键
	}

	/**
	 * 结束扫描
	 */
	private void finishScanner(){
		scanner.scan_stop();
//		scanner.close();	//关闭iscan  非正常关闭会造成iScan异常退出
		unregisterReceiver(scanReceiver);	//反注册广播接收者
		scanner.continceScan(false);
	}

	/**---------------------------------------------------------------------*/
	/**以下为客户自定义按键处理方式:
	 * 指定只能按键键值为139的物理按键（中间黄色按键）按下来触发扫描*/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 139&&event.getRepeatCount()==0){
			scanner.scan_start();
		}
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == 139){	/**按键弹起，停止扫描*/
			scanner.scan_stop();
		}else if (keyCode == 140){
			scanner.scan_stop();

			isContinue=!isContinue;
			if(isContinue){
				scanner.continceScan(true);
			}else{
				scanner.continceScan(false);
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	/**---------------------------------------------------------------------*/


	//单次扫描按钮的的响应函数
	public void singleScan(View v){
		scanner.scan_start();
	}

	//连续扫描的响应函数
	public void continueScan(View v){
		isContinue=!isContinue;
		if(isContinue){
			scanner.continceScan(true);
		}else{
			scanner.continceScan(false);
		}
	}

	//自定义按键设置
	public void scanSet(View v){
		scanner.scanKeySet(139,0);
		scanner.scanKeySet(140,1);
		scanner.scanKeySet(141,1);
	}

	/**
	 * 扫描结果的广播接收者
	 */



	private class ScannerResultReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(RES_ACTION)){
				//获取扫描结果
				final String scanResult = intent.getStringExtra("value") + "\n";
				int barocodelen=intent.getIntExtra("length",0);
				int type =intent.getIntExtra("type", 0);
				String myType =  String.format("%c", type);
				tvScanResult.append("Length："+barocodelen+"  Type:"+myType+"  第"+i+"个CodeBar："+scanResult);
//				tvScanResult.append("Length："+barocodelen+"  第"+i+"个CodeBar："+scanResult);
				i++;
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
