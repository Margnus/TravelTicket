package com.travel.ticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.travel.ticket.entity.PortResult;
import com.travel.ticket.util.AccountUtil;
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

public class PortListActivity extends BaseActivity {


    /**
     * tabs
     */
    @BindView(R.id.tabs)
    TabLayout tabs;
    /**
     * viewpager
     */
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    /**
     * viewpage的 adapter
     */
    private Adapter mAdapter;

    LinearLayoutManager mLinearLayoutManager;

    private List<PortResult> portResults;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ports);
        ButterKnife.bind(this);
//        initView();
        getAllPort();
//        getAllShip();
    }

//    private void initView() {
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        rvPorts.setLayoutManager(mLinearLayoutManager);
//        PortAdapter adapter = new PortAdapter();
//        rvPorts.setAdapter(adapter);
//        adapter.addData(initData());
//    }

    private void initView() {
        mAdapter = new Adapter(this.getSupportFragmentManager());
        for (int i = 0; i < portResults.size(); i++) {
            mAdapter.addFragment(getFragment(portResults.get(i).getId()), portResults.get(i).getName());
        }
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(portResults.size());
        mAdapter.notifyDataSetChanged();
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setupWithViewPager(viewpager);
//        viewpager.setCurrentItem(0, true);
    }

    private Fragment getFragment(String id) {
        ShipListFragment fragment = new ShipListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
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
                        if (result != null) {
                            portResults = result;
                            initView();
                        } else {
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
                        if (result != null) {
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        } else {
                            DebugUtil.toast(PortListActivity.this, "网络连接失败，请检查网络设置~");
                        }
                    }
                });
        addSubscription(subscription);

    }

    protected static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<CharSequence> titles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, CharSequence title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


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
                Intent intent = new Intent(PortListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }
}
