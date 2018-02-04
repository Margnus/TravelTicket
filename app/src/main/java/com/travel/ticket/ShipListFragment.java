package com.travel.ticket;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.travel.ticket.entity.DepartureBean;
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
 * Created by lixiaofan on 2018/2/4.
 */

public class ShipListFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager mLinearLayoutManager;

    private String id;

    PortAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ll = inflater.inflate(R.layout.fragment_ship, null);
        ButterKnife.bind(this, ll);
        return ll;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getDeparture();
    }

    private void initView() {
        id = getArguments().getString("id");
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new PortAdapter();
        recyclerView.setAdapter(adapter);
//        adapter.addData(initData());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showTips();
            }
        });
    }

    private List<PortResult> initData() {
        List<PortResult> list = new ArrayList<>();
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        list.add(new PortResult());
        return list;
    }

    private void getDeparture() {
        showDialog();
        Subscription subscription = HttpClient.Builder.getTravelService().getDeparture().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<DepartureBean>>() {
                    @Override
                    public void onCompleted() {
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        DebugUtil.toast(getContext(), "网络连接失败，请检查网络设置~");

                    }

                    @Override
                    public void onNext(List<DepartureBean> result) {
                        if (result != null) {
                            adapter.addData(result);
                            DebugUtil.toast(getContext(), "网络连接失败，请检查网络设置~");
                        } else {
                            DebugUtil.toast(getContext(), "网络连接失败，请检查网络设置~");
                        }
                    }
                });
        addSubscription(subscription);

    }


    private void showTips() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.popuplayout, null);
        PopupWindow popWnd = new PopupWindow(contentView, 600,
                600, true);
        popWnd.setOutsideTouchable(true);
        popWnd.setBackgroundDrawable(new BitmapDrawable());
        popWnd.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}