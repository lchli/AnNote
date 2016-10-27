package com.lchli.studydiscuss.common.base;

import android.support.v4.app.Fragment;

/**
 * Created by lchli on 2016/8/10.
 */
public abstract class BaseFragment extends Fragment {

    public boolean isInitLoadDataCalled=false;

    public void initLoadData() {
        //def impl.
    }
}
