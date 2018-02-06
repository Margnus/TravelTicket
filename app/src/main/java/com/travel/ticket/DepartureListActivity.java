package com.travel.ticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.travel.ticket.entity.DepartureBean;
import com.travel.ticket.util.AccountUtil;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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

    PortAdapter portAdapter;
    LinearLayoutManager mLinearLayoutManager;
    List<DepartureBean> departureBeans;
    Map<String, List<DepartureBean>> maps = new HashMap<>();
    List<String> ports = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        ButterKnife.bind(this);
        initView();
        getDeparture(true);
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
        portAdapter = new PortAdapter(handler);
        recyclerView.setAdapter(portAdapter);
    }

    private void initSpinner(String[] mItems){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                portAdapter.getData().clear();
                portAdapter.getData().addAll(maps.get(ports.get(pos)));
                portAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void getDeparture(boolean showDialog) {
        if(showDialog){
            showDialog();
        }
        Subscription subscription = HttpClient.Builder.getTravelService().getDeparture().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<DepartureBean>>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        DebugUtil.toast(DepartureListActivity.this, "网络连接失败，请检查网络设置~");
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
    private void shortData(){
        ports.clear();
        maps.clear();
        for(DepartureBean bean : departureBeans){
            if(maps.containsKey(bean.getDocker().getName())){
                maps.get(bean.getDocker().getName()).add(bean);
            }else {
                List<DepartureBean> list = new ArrayList<>();
                list.add(bean);
                maps.put(bean.getDocker().getName(), list);
                ports.add(bean.getDocker().getName());
            }
        }

        initSpinner(ports.toArray(new String[ports.size()]));
        portAdapter.getData().clear();
        portAdapter.getData().addAll(maps.get(ports.get(0)));
        portAdapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getDeparture(true);
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
}

