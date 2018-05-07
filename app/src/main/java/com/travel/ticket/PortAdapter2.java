package com.travel.ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.travel.ticket.entity.DepartureBean;
import com.travel.ticket.entity.StringBean;
import com.travel.ticket.http.AuthObserver;
import com.travel.ticket.util.AccountUtil;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixiaofan on 2018/2/3.
 */

public class PortAdapter2 extends BaseQuickAdapter<DepartureBean, BaseViewHolder> {

    private ProgressDialog dialog;
    Activity mActivity;

    protected void showDialog() {
        dismiss();
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("加载中...");
        dialog.show();
    }

    protected void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    Handler handler;

    // 班次状态 = ['sailing', 'stay', 'checking', 'end']

    public PortAdapter2(Activity activity, Handler handler) {
        super(R.layout.list_port2, new ArrayList<DepartureBean>());
        this.handler = handler;
        mActivity = activity;
    }

    int[] res = {R.color.looper_1, R.color.looper_2, R.color.looper_3, R.color.looper_4, R.color.looper_5, R.color.looper_6, R.color.looper_7,
            R.color.looper_8};

    Map<String, Integer> map = new HashMap<>();

    @Override
    protected void convert(final BaseViewHolder helper, final DepartureBean item) {
        helper.setText(R.id.ship_name, item.getCruise().getName())
                .setText(R.id.capacity, mContext.getString(R.string.capacity2, item.getCapacity()))
                .setText(R.id.check, mContext.getString(R.string.checked2, item.getCheckIn()))
                .setText(R.id.check_child, mContext.getString(R.string.checked_child2, item.getChildCheckIn()))
                .setText(R.id.sale, mContext.getString(R.string.sale2, item.getSold()));
        TextView shipName = helper.getView(R.id.ship_name);
        TextView time = helper.getView(R.id.time);
        if(item.isLoop()){
            String date = TextUtils.isEmpty(item.getDepartureTimeFrom()) ? "" : item.getDepartureTimeFrom().substring(11, 16) + "\n";
            date += TextUtils.isEmpty(item.getDepartureTimeTo()) ? "" : item.getDepartureTimeTo().substring(11, 16);
            time.setText(date);
            if(map.containsKey(item.getCruisePlanId())){
                time.setTextColor(mContext.getResources().getColor(map.get(item.getCruisePlanId())));
            }else{
                int size = map.size();
                map.put(item.getCruisePlanId(), res[size]);
                time.setTextColor(mContext.getResources().getColor(res[size]));
            }
        }else {
            time.setText(item.getDepartureDate().substring(11, 16));
            time.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        shipName.setCompoundDrawablesWithIntrinsicBounds(item.isLoop() ? R.drawable.ic_loop : 0, 0, 0, 0);
        helper.getView(R.id.checkin).setEnabled(true);
        switch (item.getSailingStatus()) {
            case "stay":
                helper.setVisible(R.id.checkin, true);
                helper.setVisible(R.id.sailing, false);
                helper.setText(R.id.checkin, R.string.checkin);
                helper.setText(R.id.status, "待发航");
                if (!item.getCanChecking()) {
                    helper.getView(R.id.checkin).setEnabled(false);
                }
                break;
            case "checking":
                helper.setVisible(R.id.checkin, true);
                helper.setVisible(R.id.sailing, true);
                helper.setText(R.id.checkin, "取消查验");
                helper.setText(R.id.sailing, R.string.fahang);
                helper.setText(R.id.status, "查验中");
                break;
            case "sailing":
                helper.setVisible(R.id.checkin, false);
                helper.setVisible(R.id.sailing, true);
                helper.setText(R.id.sailing, "取消发航");
                helper.setText(R.id.status, R.string.kaihang);
                break;
            case "end":
                helper.setVisible(R.id.checkin, false);
                helper.setVisible(R.id.sailing, false);
                helper.setText(R.id.status, R.string.over);
                break;
        }
        helper.getView(R.id.checkin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getSailingStatus()) {
                    case "stay":
                        showAlert(item.getCruise().getName() + "开始查验", new OnAlertClick() {
                            @Override
                            public void onClick() {
                                checkIn(item.getCarrierShipId());
                            }
                        });
                        break;
                    case "checking":
                        showAlert(item.getCruise().getName() + "取消查验", new OnAlertClick() {
                            @Override
                            public void onClick() {
                                cancelCheck(item.getCarrierShipId());
                            }
                        });
                        break;
                }
            }
        });
        helper.getView(R.id.sailing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getSailingStatus()) {
                    case "checking":
                        showAlert(item.getCruise().getName() + "发航", new OnAlertClick() {
                            @Override
                            public void onClick() {
                                sailing(item.getCarrierShipId());
                            }
                        });

                        break;
                    case "sailing":
                        showAlert(item.getCruise().getName() + "取消发航", new OnAlertClick() {
                            @Override
                            public void onClick() {
                                cancelSailing(item.getCarrierShipId());
                            }
                        });
                        break;
                }

            }
        });
    }

    void showAlert(String msg, final OnAlertClick onAlertClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(msg);
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
                if (onAlertClick != null) {
                    onAlertClick.onClick();
                }
            }
        });
        builder.create().show();
    }

    interface OnAlertClick {
        void onClick();
    }

    private void checkIn(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().checkIn(id).subscribeOn(Schedulers.io())
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
                        DebugUtil.toast(mContext, object.getString("message"));
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
            }

            @Override
            public void reLogin() {
                login();
            }

            @Override
            public void onNext(StringBean result) {
                if (result != null) {
//                            DebugUtil.toast(mContext, result.getMsg());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void login() {
        AccountUtil.logout();
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void cancelCheck(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().cancelCheckin(id).subscribeOn(Schedulers.io())
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
                        DebugUtil.toast(mContext, object.getString("message"));
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
            }

            @Override
            public void reLogin() {
                login();
            }

            @Override
            public void onNext(StringBean result) {
                if (result != null) {
//                    DebugUtil.toast(mContext, result.getMsg());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void sailing(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().sailing(id).subscribeOn(Schedulers.io())
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
                        DebugUtil.toast(mContext, object.getString("message"));
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
            }

            @Override
            public void reLogin() {
                login();
            }

            @Override
            public void onNext(StringBean result) {
                if (result != null) {
//                    DebugUtil.toast(mContext, result.getMsg());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }


    private void cancelSailing(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().cancelSailing(id).subscribeOn(Schedulers.io())
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
                        DebugUtil.toast(mContext, object.getString("message"));
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
            }

            @Override
            public void reLogin() {
                login();
            }

            @Override
            public void onNext(StringBean result) {
                if (result != null) {
//                    DebugUtil.toast(mContext, result.getMsg());
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }
}
