package com.dreamspace.uucampusseller.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.ui.activity.Personal.AboutAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.AliPayAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.DirectionAct;
import com.dreamspace.uucampusseller.ui.activity.Personal.InfoChangeAct;
import com.dreamspace.uucampusseller.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wufan on 2015/10/18.
 */
public class PersonFragment extends BaseFragment {
    @Bind(R.id.personal_relative1)
    RelativeLayout personalRelative1;
    @Bind(R.id.personal_relative2)
    RelativeLayout personalRelative2;
    @Bind(R.id.personal_relative3)
    RelativeLayout personalRelative3;
    @Bind(R.id.personal_relative4)
    RelativeLayout personalRelative4;
    @Bind(R.id.person_linear)
    LinearLayout personLinear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void initViews(View view) {
        initListeners();
    }

    @Override
    public void initDatas() {
    }

    private void initListeners(){
        personLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(InfoChangeAct.class);
            }
        });

        personalRelative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AliPayAct.class);
            }
        });

        personalRelative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(DirectionAct.class);
            }
        });

        personalRelative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置界面
            }
        });

        personalRelative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(AboutAct.class);
            }
        });
    }

}
