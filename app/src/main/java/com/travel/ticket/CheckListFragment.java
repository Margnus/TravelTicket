package com.travel.ticket;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travel.ticket.entity.DepartureBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李小凡 on 2018/4/4.
 */

public class CheckListFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager mLinearLayoutManager;

    PortAdapter adapter;
    @BindView(R.id.expand)
    TextView expand;

    private Handler handler;

    private boolean showMore = false;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private List<DepartureBean> departureBeans;

    public void setDate(List<DepartureBean> list) {
        showMore = true;
        departureBeans = list;
        adapter.getData().clear();
        if (!list.isEmpty()) {
            adapter.getData().add(departureBeans.get(0));
        }
        if(departureBeans.size() > 1){
            expand.setVisibility(View.VISIBLE);
        }else{
            expand.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
        expand.setText(R.string.more);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View ll = inflater.inflate(R.layout.fragment_ckeck_ship, null);
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
        adapter = new PortAdapter(getActivity(), handler);
        recyclerView.setAdapter(adapter);
        adapter.setEmptyView(getEmptyView());
    }

    private View getEmptyView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.expand)
    public void onViewClicked() {
        if(showMore){
            showMore = false;
            expand.setText(R.string.expand);
            adapter.getData().clear();
            adapter.getData().addAll(departureBeans);
            adapter.notifyDataSetChanged();
        }else{
            showMore = true;
            expand.setText(R.string.more);
            adapter.getData().clear();
            if (!departureBeans.isEmpty()) {
                adapter.getData().add(departureBeans.get(0));
            }
            adapter.notifyDataSetChanged();
        }
    }
}
