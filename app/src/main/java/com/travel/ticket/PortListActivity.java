package com.travel.ticket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.travel.ticket.entity.PortResult;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李小凡 on 2018/2/2.
 */

public class PortListActivity extends BaseActivity{

    @BindView(R.id.rv_ports)
    RecyclerView rvPorts;

    LinearLayoutManager mLinearLayoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ports);
        ButterKnife.bind(this);
        initView();
        getAllPort();
        getAllShip();
    }

    private void initView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        rvPorts.setLayoutManager(mLinearLayoutManager);
        PortAdapter adapter = new PortAdapter();
        rvPorts.setAdapter(adapter);
        adapter.addData(initData());
    }

    private List<PortResult> initData(){
        List<PortResult> list = new ArrayList<>();
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        return list;
    }

    private void getAllPort() {
        Subscription subscription = HttpClient.Builder.getTravelService().docker().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<PortResult>>() {
                    @Override
                    public void onCompleted() {
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");

                    }

                    @Override
                    public void onNext(List<PortResult> result) {
                        if(result != null){
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        }else {
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        }
                    }
                });
        addSubscription(subscription);

    }

    private void getAllShip() {
        Subscription subscription = HttpClient.Builder.getTravelService().docker().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<PortResult>>() {
                    @Override
                    public void onCompleted() {
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");

                    }

                    @Override
                    public void onNext(List<PortResult> result) {
                        if(result != null){
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        }else {
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        }
                    }
                });
        addSubscription(subscription);

    }


}
