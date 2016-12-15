package cn.edu.seu.cse.seualarm.controler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.seu.cse.seualarm.R;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class RunInfoFragment extends BaseFragment {
    public View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_run_info, container, false);
        mView = view;
        return view;
    }

    @Override
    public View initView() {
        return null;
    }
}
