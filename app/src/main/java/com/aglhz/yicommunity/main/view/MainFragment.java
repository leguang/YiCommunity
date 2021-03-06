package com.aglhz.yicommunity.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aglhz.abase.mvp.view.base.BaseFragment;
import com.aglhz.abase.utils.ToastUtils;
import com.aglhz.yicommunity.BaseApplication;
import com.aglhz.yicommunity.R;
import com.aglhz.yicommunity.common.Constants;
import com.aglhz.yicommunity.home.view.HomeFragment;
import com.aglhz.yicommunity.mine.view.MineFragment;
import com.aglhz.yicommunity.neighbour.view.NeighbourFragment;
import com.aglhz.yicommunity.steward.view.StewardFragment;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Author：leguang on 2017/4/12 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    @BindView(R.id.ahbn_main_fragment)
    AHBottomNavigation ahbn;

    private ArrayList<AHBottomNavigationItem> bottomItems = new ArrayList<>();
    private int prePosition = 0;
    private SupportFragment[] mFragments = new SupportFragment[4];

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            mFragments[0] = HomeFragment.newInstance();
            mFragments[1] = StewardFragment.newInstance();
            mFragments[2] = NeighbourFragment.newInstance();
            mFragments[3] = MineFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container_main_fragment, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3]);
        } else {
            mFragments[0] = findFragment(HomeFragment.class);
            mFragments[1] = findFragment(StewardFragment.class);
            mFragments[2] = findFragment(NeighbourFragment.class);
            mFragments[3] = findFragment(MineFragment.class);
        }
        initData();
    }

    private void initData() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.community, R.drawable.ic_home_black_78px, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.steward, R.drawable.ic_guanjia_black_79px, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.neighbour, R.drawable.ic_neighbour_black_78px, R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.mine, R.drawable.ic_mine_black_78px, R.color.white);
        bottomItems.add(item1);
        bottomItems.add(item2);
        bottomItems.add(item3);
        bottomItems.add(item4);
        ahbn.addItems(bottomItems);
        ahbn.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        ahbn.setBehaviorTranslationEnabled(false);
        ahbn.setColored(true);
        ahbn.setForceTint(false);
        ahbn.setAccentColor(getResources().getColor(R.color.base_color));
        ahbn.setInactiveColor(getResources().getColor(R.color.black));
        ahbn.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        ahbn.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, final boolean wasSelected) {

                showHideFragment(mFragments[position], mFragments[prePosition]);
                prePosition = position;
                return true;
            }
        });
        ahbn.setCurrentItem(0);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtils.showToast(BaseApplication.mContext, Constants.PRESS_AGAIN);
        }
        return true;
    }
}
