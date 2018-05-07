package com.travel.ticket;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travel.ticket.entity.DepartureBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lixiaofan on 2018/2/4.
 */

public class OtherListFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager mLinearLayoutManager;

    PortAdapter2 adapter;

    private Handler handler;

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void setDate(List<DepartureBean> list){
        adapter.getData().clear();
        adapter.getData().addAll(list);
        adapter.notifyDataSetChanged();
    }

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
    }

    private void initView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new PortAdapter2(getActivity(), handler);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(getEmptyView());
    }

    private View getEmptyView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null);
        return view;
    }
}
