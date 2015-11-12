package com.dreamspace.uucampusseller.ui.activity.Personal;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.model.api.CommitSuggestionRes;
import com.dreamspace.uucampusseller.model.api.ContentReq;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/11/10.
 */
public class FeedBackAct extends AbsActivity {
    @Bind(R.id.feedback_et)
    EditText feedbackEt;
    @Bind(R.id.feedback_commit_btn)
    Button feedbackCommitBtn;

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        feedbackCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentReq req = new ContentReq();
                String content = feedbackEt.getText().toString();
                if(isValid(content)){
                    req.setContent(content);
                    commitSuggestion(req);
                }
            }
        });
    }

    //意见提交
    private void commitSuggestion(ContentReq req){
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(this).commitSuggestion(req, new Callback<CommitSuggestionRes>() {
                @Override
                public void success(CommitSuggestionRes commitSuggestionRes, Response response) {
                    showToast("意见提交成功，感谢您的反馈~");
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task,1000);
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        }else{
            showNetWorkError();
        }
    }

    //检查意见框内容是否为空
    private boolean isValid(String s){
        if(CommonUtils.isEmpty(s)){
            showToast("意见不能为空~");
            return false;
        }
        return true;
    }
}
