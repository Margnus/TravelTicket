package com.travel.ticket;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.travel.ticket.entity.TokenBean;
import com.travel.ticket.util.DebugUtil;
import com.travel.ticket.util.HttpClient;
import com.travel.ticket.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.commit)
    Button commit;

    private static final String client_id = "pda-port";
    private static final String client_secret = "28BLJRQDVQKWCVBKRKGL";
    private static final String grant_type = "password";
    private static final String scope = "read write";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void login(String name, String psw) {
        showDialog();
        Subscription subscription = HttpClient.Builder.getTravelService().login(client_id, client_secret, grant_type, scope, name, psw).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<TokenBean>() {
                    @Override
                    public void onCompleted() {
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        if(e != null && e instanceof HttpException){
                            try {
                                String error = ((HttpException) e).response().errorBody().string();
                                JSONObject object = new JSONObject(error);
                                DebugUtil.toast(MainActivity.this, object.getString("error_description"));
                                return;
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                         DebugUtil.toast(MainActivity.this, "网络连接失败，请检查网络设置~");

                    }

                    @Override
                    public void onNext(TokenBean result) {
                        if(result != null){
                            PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(getApplicationContext());
                            preferenceUtil.saveString(TokenBean.ACCESS_TOKEN, result.getAccessToken());
                            preferenceUtil.saveString(TokenBean.REFRESH_TOKEN, result.getRefreshToken());
                            preferenceUtil.saveString(TokenBean.TOKEN_TYPE, result.getTokenType());
                            preferenceUtil.saveInt(TokenBean.EXPIRES_IN, result.getExpiresIn());
                            DebugUtil.toast(MainActivity.this, "登录成功~");
                        }else {
                            DebugUtil.toast(MainActivity.this, "网络连接失败，请检查网络设置~");
                        }
                    }
                });
        addSubscription(subscription);
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if(TextUtils.isEmpty(etName.getText())){
            DebugUtil.toast(this, "请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(etPsw.getText())){
            DebugUtil.toast(this, "请输入密码");
            return;
        }
        login(etName.getText().toString(), etPsw.getText().toString());
    }
}
