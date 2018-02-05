package com.travel.ticket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.travel.ticket.entity.DepartureBean;
import com.travel.ticket.entity.StringBean;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixiaofan on 2018/2/3.
 */

public class PortAdapter extends BaseQuickAdapter<DepartureBean, BaseViewHolder> {

    private ProgressDialog dialog;

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

    public PortAdapter(Handler handler) {
        super(R.layout.list_port, new ArrayList<DepartureBean>());
        this.handler = handler;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DepartureBean item) {
        helper.setText(R.id.time, item.getDepartureDate().substring(11, 16))
                .setText(R.id.ship_name, item.getCruise().getName())
                .setText(R.id.capacity, mContext.getString(R.string.capacity, item.getCapacity()))
                .setText(R.id.check, mContext.getString(R.string.checked, item.getCheckIn()))
                .setText(R.id.sale, mContext.getString(R.string.sale, item.getCheckIn()));
        switch (item.getSailingStatus()){
            case "stay":
                helper.setVisible(R.id.checkin, true);
                helper.setVisible(R.id.sailing, false);
                helper.setText(R.id.checkin, "查验");
                break;
            case "checking":
                helper.setVisible(R.id.checkin, true);
                helper.setVisible(R.id.sailing, true);
                helper.setText(R.id.checkin, "取消查验");
                helper.setText(R.id.sailing, "发航");
                break;
            case "sailing":
                helper.setVisible(R.id.checkin, false);
                helper.setVisible(R.id.sailing, true);
                helper.setText(R.id.sailing, "取消发航");
                break;
            case "end":
                helper.setVisible(R.id.checkin, false);
                helper.setVisible(R.id.sailing, false);
                break;
        }
        helper.getView(R.id.checkin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getSailingStatus()){
                    case "stay":
                        checkIn(item.getCarrierShipId());
                        break;
                    case "checking":
                        cancelCheck(item.getCarrierShipId());
                        break;
                }
            }
        });
        helper.getView(R.id.sailing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getSailingStatus()){
                    case "checking":
                        sailing(item.getCarrierShipId());
                        break;
                    case "sailing":
                        cancelSailing(item.getCarrierShipId());
                        break;
                }

            }
        });
    }

    private void checkIn(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().checkIn(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StringBean>() {
                    @Override
                    public void onCompleted() {
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
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

    private void cancelCheck(String id) {
        showDialog();
        HttpClient.Builder.getTravelService().cancelCheckin(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StringBean>() {
            @Override
            public void onCompleted() {
                dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dismiss();
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
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
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StringBean>() {
            @Override
            public void onCompleted() {
                dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dismiss();
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
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
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StringBean>() {
            @Override
            public void onCompleted() {
                dismiss();
            }

            @Override
            public void onError(Throwable e) {
                dismiss();
                DebugUtil.toast(mContext, "网络连接失败，请检查网络设置~");
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
