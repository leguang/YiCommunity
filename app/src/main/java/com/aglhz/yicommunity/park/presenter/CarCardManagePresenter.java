package com.aglhz.yicommunity.park.presenter;

import android.support.annotation.NonNull;

import com.aglhz.abase.log.ALog;
import com.aglhz.abase.mvp.presenter.base.BasePresenter;
import com.aglhz.yicommunity.park.contract.CarCardManageContract;
import com.aglhz.yicommunity.park.model.CarCardManageModel;

/**
 * Author：leguang on 2016/10/9 0009 10:35
 * Email：langmanleguang@qq.com
 * <p>
 * 负责邻里模块Presenter层内容。
 */

public class CarCardManagePresenter extends BasePresenter<CarCardManageContract.View, CarCardManageContract.Model> implements CarCardManageContract.Presenter {
    private final String TAG = CarCardManagePresenter.class.getSimpleName();

    public CarCardManagePresenter(CarCardManageContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected CarCardManageContract.Model createModel() {
        return new CarCardManageModel();
    }


    @Override
    public void start(Object request) {

    }
}