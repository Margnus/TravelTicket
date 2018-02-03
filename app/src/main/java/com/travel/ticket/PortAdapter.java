package com.travel.ticket;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.travel.ticket.entity.PortResult;

import java.util.ArrayList;

/**
 * Created by lixiaofan on 2018/2/3.
 */

public class PortAdapter extends BaseQuickAdapter<PortResult, BaseViewHolder> {

    public PortAdapter(){
        super(R.layout.list_port, new ArrayList<PortResult>());
    }

    @Override
    protected void convert(BaseViewHolder helper, PortResult item) {

    }
}
