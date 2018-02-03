package com.travel.ticket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 李小凡 on 2018/2/1.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private CompositeSubscription mCompositeSubscription;

    protected void showDialog() {
        dismiss();
        dialog = new ProgressDialog(this);
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
    public void onDestroy() {
        super.onDestroy();
        dismiss();
        removeSubscription();
    }

    protected void removeSubscription() {
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}

