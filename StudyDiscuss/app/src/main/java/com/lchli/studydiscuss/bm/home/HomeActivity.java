package com.lchli.studydiscuss.bm.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lchli.studydiscuss.R;
import com.lchli.studydiscuss.R2;
import com.lchli.studydiscuss.bm.note.frament.CloudNoteFragment;
import com.lchli.studydiscuss.bm.note.frament.LocalNoteFragment;
import com.lchli.studydiscuss.bm.user.view.fragment.UserFragmentContainer;
import com.lchli.studydiscuss.common.base.BaseAppCompatActivity;
import com.lchli.studydiscuss.common.base.BaseFragment;
import com.lchli.studydiscuss.common.base.FragmentAdapter;
import com.lchli.studydiscuss.common.utils.ResUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/8/10.
 */
public class HomeActivity extends BaseAppCompatActivity {


    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.tabs)
    TabLayout tabs;


    public static void startSelf(Context context) {
        Intent it = new Intent(context, HomeActivity.class);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(it);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.annote_activity_home);
        ButterKnife.bind(this);
        //ToastUtils.systemToast("tinker patch applied test!");

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(LocalNoteFragment.newInstance(), ResUtils.parseString(R.string.note));
        adapter.addFragment(CloudNoteFragment.newInstance(), ResUtils.parseString(R.string.cloud_note));
        adapter.addFragment(UserFragmentContainer.newInstance(), ResUtils.parseString(R.string.user));
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(adapter.getCount());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentAdapter fragmentAdapter = (FragmentAdapter) viewpager.getAdapter();
                BaseFragment fragment = (BaseFragment) fragmentAdapter.getItem(position);
                if (!fragment.isInitLoadDataCalled) {
                    fragment.isInitLoadDataCalled = true;
                    fragment.initLoadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setupWithViewPager(viewpager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Override and do not save.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
