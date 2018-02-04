package com.travel.ticket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lixiaofan on 2018/2/4.
 */

public class BaseFragment extends Fragment{
    private ProgressDialog dialog;
    private CompositeSubscription mCompositeSubscription;

    protected void showDialog() {
        dismiss();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("加载中...");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                removeSubscription();
            }
        });
        dialog.show();
    }

    protected void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismiss();
        removeSubscription();
    }

    protected void removeSubscription() {
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
