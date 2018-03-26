package com.travel.ticket;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.travel.ticket.entity.DepartureBean;
import com.travel.ticket.entity.MineResult;
import com.travel.ticket.entity.StringBean;
import com.travel.ticket.entity.UpdateResult;
import com.travel.ticket.http.AuthObserver;
import com.travel.ticket.iscan.ScannerInterface;
import com.travel.ticket.util.AccountUtil;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 李小凡 on 2018/2/6.
 */

public class DepartureListActivity extends BaseActivity {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.checked_num)
    TextView checkedNum;

    PortAdapter portAdapter;
    LinearLayoutManager mLinearLayoutManager;
    List<DepartureBean> departureBeans;
    Map<String, List<DepartureBean>> maps = new HashMap<>();
    List<String> ports = new ArrayList<>();
    MineResult mineResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        ButterKnife.bind(this);
        initView();
        getMe();
//        getDeparture(true);
        update();
        initScanner();
    }

    ScannerInterface scanner;
    IntentFilter intentFilter;
    BroadcastReceiver scanReceiver;

    private static final String RES_ACTION = "android.intent.action.SCANRESULT";

    private void initScanner() {
        scanner = new ScannerInterface(this);
        scanner.open();    //打开扫描头上电   scanner.close();//打开扫描头下电
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


    private void initView() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeparture(false);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        portAdapter = new PortAdapter(this, handler);
        recyclerView.setAdapter(portAdapter);
    }

    ArrayAdapter<String> adapter;

    private void initSpinner(String[] mItems) {
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    List<DepartureBean> list = maps.get(ports.get(pos));
                    int count = 0, child = 0;
                    for (DepartureBean bean : list) {
                        count += bean.getCheckIn();
                        child += bean.getChildCheckIn();
                    }
                    checkedNum.setText(getString(R.string.checked_num, count, child));
                    portAdapter.getData().clear();
                    portAdapter.getData().addAll(list);
                    portAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
        } else {
            adapter.notifyDataSetChanged();

            List<DepartureBean> list = maps.get(ports.get(spinner.getSelectedItemPosition()));
            int count = 0, child = 0;
            for (DepartureBean bean : list) {
                count += bean.getCheckIn();
                child += bean.getChildCheckIn();
            }
            checkedNum.setText(getString(R.string.checked_num, count, child));
            portAdapter.getData().clear();
            portAdapter.getData().addAll(list);
            portAdapter.notifyDataSetChanged();
        }
    }

    private void getDeparture(boolean showDialog) {
        if (showDialog) {
            showDialog();
        }
        Subscription subscription = HttpClient.Builder.getTravelService().getDeparture().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AuthObserver<List<DepartureBean>>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        dismiss();
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            String error = ((HttpException) e).response().errorBody().string();
                            JSONObject object = new JSONObject(error);
                            DebugUtil.toast(DepartureListActivity.this, object.getString("error_description"));
                            return;
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        DebugUtil.toast(DepartureListActivity.this, "网络连接失败，请检查网络设置~");
                    }

                    @Override
                    public void reLogin() {
                        AccountUtil.logout();
                        Intent intent = new Intent(DepartureListActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onNext(List<DepartureBean> result) {
                        if (result != null) {
                            departureBeans = result;
                            shortData();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 筛选码头
     */
    private void shortData() {
        ports.clear();
        maps.clear();
        for (DepartureBean bean : departureBeans) {
            if (maps.containsKey(bean.getDocker().getName())) {
                maps.get(bean.getDocker().getName()).add(bean);
            } else {
                List<DepartureBean> list = new ArrayList<>();
                list.add(bean);
                maps.put(bean.getDocker().getName(), list);
                ports.add(bean.getDocker().getName());
            }
        }
        initSpinner(ports.toArray(new String[ports.size()]));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getDeparture(false);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出登录吗？");
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AccountUtil.logout();
                Intent intent = new Intent(DepartureListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.image)
    public void onViewClicked() {
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScanActivity.class) // 设置自定义的activity是ScanActivity
                .initiateScan(); // 初始化扫描
    }

    @Override
// 通过 onActivityResult的方法获取扫描回来的值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String scanResult = intentResult.getContents();
            if (TextUtils.isEmpty(scanResult)) {
                DebugUtil.toast(this, "扫描内容为空");
            } else {
                ticketOn(scanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ticketOn(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().ticketNo(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AuthObserver<StringBean>() {
            @Override
            public void onCompleted() {
                dismiss();
            }

            @Override
            public void onFailed(Throwable e) {
                dismiss();
                if (e != null && e instanceof HttpException) {
                    try {
                        String error = ((HttpException) e).response().errorBody().string();
                        JSONObject object = new JSONObject(error);
                        showTicketOnDialog(object.getString("message"));
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                showTicketOnDialog("网络连接失败，请检查网络设置~");
            }

            @Override
            public void reLogin() {
                AccountUtil.logout();
                Intent intent = new Intent(DepartureListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNext(StringBean result) {
                if (result != null) {
                    getDeparture(false);
                    showTicketOnDialog(result.getMsg());
                }
            }
        });
    }

    private void showTicketOnDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("")
        .setMessage(message)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void getMe() {
        showDialog();
        Subscription subscription = HttpClient.Builder.getTravelService().getMine()
                .filter(new Func1<MineResult, Boolean>() {
                    @Override
                    public Boolean call(MineResult result) {
                        if ("sys_port".equalsIgnoreCase(result.getRole())) {
                            mineResult = result;
                            return true;
                        }
                        throw new RoleException();
                    }
                }).flatMap(new Func1<MineResult, Observable<List<DepartureBean>>>() {
                    @Override
                    public Observable<List<DepartureBean>> call(MineResult mineResult) {
                        return HttpClient.Builder.getTravelService().getDeparture();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AuthObserver<List<DepartureBean>>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        dismiss();
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        if (e instanceof RoleException) {
                            showRoleDialog();
                        } else {
                            DebugUtil.toast(DepartureListActivity.this, "网络连接失败，请检查网络设置~");
                        }
                    }

                    @Override
                    public void reLogin() {
                        AccountUtil.logout();
                        Intent intent = new Intent(DepartureListActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onNext(List<DepartureBean> result) {
                        if (mineResult != null && mineResult.getPortCoporation() != null) {
                            if (!TextUtils.isEmpty(mineResult.getPortCoporation().getName())) {
                                setTitle(mineResult.getPortCoporation().getName());
                            }
                        }
                        if (result != null) {
                            departureBeans = result;
                            shortData();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void showRoleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("登录失败，无权限操作");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AccountUtil.reLogin(DepartureListActivity.this);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AccountUtil.reLogin(DepartureListActivity.this);
            }
        });
        dialog.show();
    }

    private void update() {
        HttpClient.Builder.getTravelService().update().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new AuthObserver<UpdateResult>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onFailed(Throwable e) {

            }

            @Override
            public void reLogin() {
                AccountUtil.logout();
                Intent intent = new Intent(DepartureListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNext(final UpdateResult result) {
                if (result != null) {
                    PackageManager pm = getPackageManager();//context为当前Activity上下文
                    PackageInfo pi = null;
                    try {
                        pi = pm.getPackageInfo(getPackageName(), 0);
                        int code = Integer.parseInt(result.getVersionNo());
                        if (pi.versionCode < code) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DepartureListActivity.this);
                            builder.setMessage("检查到新版本，是否升级？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(result.getUpgradeURL()));
                                    startActivity(intent);
                                }
                            });
                            builder.create().show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**---------------------------------------------------------------------*/
    /**
     * 以下为客户自定义按键处理方式:
     * 指定只能按键键值为139的物理按键（中间黄色按键）按下来触发扫描
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 && event.getRepeatCount() == 0) {
            scanner.scan_start();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 139) {    /**按键弹起，停止扫描*/
            scanner.scan_stop();
        } else if (keyCode == 140) {
            scanner.scan_stop();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * ---------------------------------------------------------------------
     */


    private class ScannerResultReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RES_ACTION)) {
                //获取扫描结果
                final String scanResult = intent.getStringExtra("value");
                if (TextUtils.isEmpty(scanResult)) {
                    DebugUtil.toast(DepartureListActivity.this, "内容为空");
                } else {
                    // ScanResult 为 获取到的字符串

                    DebugUtil.toast(DepartureListActivity.this, "扫描成功 ==> " + scanResult);
                    ticketOn(scanResult);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishScanner();
        scanner.lockScanKey();//锁定iScan扫描按键
    }

    /**
     * 结束扫描
     */
    private void finishScanner() {
        scanner.scan_stop();
//		scanner.close();	//关闭iscan  非正常关闭会造成iScan异常退出
        unregisterReceiver(scanReceiver);    //反注册广播接收者
        scanner.continceScan(false);
    }
}

