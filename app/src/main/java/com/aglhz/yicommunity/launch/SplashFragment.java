package com.aglhz.yicommunity.launch;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aglhz.abase.log.ALog;
import com.aglhz.abase.mvp.view.base.BaseFragment;
import com.aglhz.abase.network.http.HttpHelper;
import com.aglhz.abase.utils.ToastUtils;
import com.aglhz.yicommunity.BaseApplication;
import com.aglhz.yicommunity.R;
import com.aglhz.yicommunity.common.ApiService;
import com.aglhz.yicommunity.common.UserHelper;
import com.aglhz.yicommunity.door.sdk.DoorManager;
import com.aglhz.yicommunity.main.MainActivity;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class SplashFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = SplashFragment.class.getSimpleName();
    private static final int LOCATION = 122;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        UserHelper.init();
        location();
        checkLogin();
        initDoorManager();
    }

    private void initDoorManager() {
        DoorManager.getInstance();
    }

    @AfterPermissionGranted(LOCATION)
    private void location() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(_mActivity, perms)) {
            //有权限就直接进行定位操作
//            ToastUtils.showToast(BaseApplication.mContext, "正在定位……");
        } else {
            EasyPermissions.requestPermissions(this, "亿社区需要定位权限", LOCATION, perms);
            ToastUtils.showToast(BaseApplication.mContext, "申请权限……");
        }
    }

    private void checkLogin() {
        HttpHelper.getService(ApiService.class).checkToken(UserHelper.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkTokenBean -> {
                    ALog.e(checkTokenBean.getOther().getCode());
                    if (checkTokenBean.getData().getStatus() == 1) {
                        UserHelper.clear();
                    }
                    go2Main();
                }, throwable -> {
                    ALog.e(throwable);
                    go2Main();
                });
    }

    private void go2Main() {
        ALog.e("1111111111111");
        startActivity(new Intent(_mActivity, MainActivity.class));
        _mActivity.overridePendingTransition(0, 0);
        //此处之所以延迟退出是因为立即退出在小米手机上会有一个退出跳转动画，而我不想要这个垂直退出的跳转动画。
        new Handler().postDelayed(() -> _mActivity.finish(), 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.e(TAG, "onPermissionsGranted:" + requestCode + ":" + resultCode);
        }
    }
}
