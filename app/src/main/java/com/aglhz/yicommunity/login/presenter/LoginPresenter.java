package com.aglhz.yicommunity.login.presenter;

import android.support.annotation.NonNull;

import com.aglhz.abase.log.ALog;
import com.aglhz.abase.mvp.presenter.base.BasePresenter;
import com.aglhz.yicommunity.common.Params;
import com.aglhz.yicommunity.common.UserHelper;
import com.aglhz.yicommunity.login.contract.LoginContract;
import com.aglhz.yicommunity.login.model.LoginModel;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.aglhz.yicommunity.common.UserHelper.setUserInfo;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected LoginContract.Model createModel() {
        return new LoginModel();
    }

    @Override
    public void start(Object request) {
        mRxManager.add(mModel.login((Params) request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userBean -> {
                    if (userBean.getOther().getCode() == 200) {
                        getView().start(UserHelper.setUserInfo(userBean.getData().getMemberInfo()));
                    } else {
                        getView().error(userBean.getOther().getMessage());
                    }
                }, throwable -> {
                    if (throwable == null) {
                        getView().error("数据异常");
                        return;
                    }
                    if (throwable instanceof ConnectException) {
                        getView().error("网络异常");
                    } else if (throwable instanceof HttpException) {
                        getView().error("服务器异常");
                    } else if (throwable instanceof SocketTimeoutException) {
                        getView().error("连接超时");
                    } else if (throwable instanceof JSONException) {
                        getView().error("解析异常");
                    } else {
                        getView().error("数据异常");
                    }

                })
        );
    }
}