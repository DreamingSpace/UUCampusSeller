package com.dreamspace.uucampusseller.ui.activity.Goods;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.UploadImage;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.CommonStatusRes;
import com.dreamspace.uucampusseller.model.CreateGoodReq;
import com.dreamspace.uucampusseller.model.CreateGoodRes;
import com.dreamspace.uucampusseller.model.Description;
import com.dreamspace.uucampusseller.model.DescriptionItem;
import com.dreamspace.uucampusseller.model.QnRes;
import com.dreamspace.uucampusseller.model.UpdateGoodReq;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;
import com.dreamspace.uucampusseller.ui.dialog.ProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lx on 2015/10/24.
 */
public class EditGoodLabelAct extends AbsActivity {
    @Bind(R.id.label_content_ll)
    LinearLayout contentLl;
    @Bind(R.id.add_detail_btn)
    Button addLabelBtn;

    private CreateGoodReq goodInfo;
    private ArrayList<View> mLabelViews = new ArrayList<>();//标签项视图
    private int inWay;
    private String goodId;//编辑商品时传入的good_id
    private ProgressDialog progressDialog;
    private String imagePath;

    public static final String GOOD_INFO = "good_info";

    @Override
    protected int getContentView() {
        return R.layout.activitiy_edit_good_label;
    }

    @Override
    protected void prepareDatas() {
        Bundle bundle = getIntent().getExtras();
        inWay = bundle.getInt(EditGoodInfoAct.IN_WAY);
        goodInfo = bundle.getParcelable(EditGoodInfoAct.PART_GOOD_INFO);
        if(goodInfo.getImage() != null){
            imagePath = goodInfo.getImage();
        }
        if(inWay == EditGoodInfoAct.IN_FROM_EDIT){
            goodId = bundle.getString(EditGoodInfoAct.GOOD_ID);
        }
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(R.string.add_good_describe));
        //若没有商品标签则默认显示一条空标签视图
        if(goodInfo.getDescription() == null){
            addLabelDetailView();
        }
        setLabelIntoViews();

        initListeners();
    }

    private void initListeners(){
        addLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLabelDetailView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_good_label, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_finish){
            //点击完成
            if(isValid()){
                finishGoodInfo();
                initProgressDialog();
                if(inWay == EditGoodInfoAct.IN_FROM_EDIT){
                    progressDialog.setContent(getString(R.string.in_update_good));
                    progressDialog.show();
                    if(goodInfo.getImage() == null){
                        updateGood();//用户没有更新图片
                    }else{
                        upLoadImage(2);//上传图片后自动更新
                    }
                }else{
                    progressDialog.setContent(getString(R.string.in_create_good));
                    progressDialog.show();
                    upLoadImage(1);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //添加一个标签
    private void addLabelDetailView(){
        final View labelView = getLayoutInflater().inflate(R.layout.view_label_detail, contentLl, false);
        mLabelViews.add(labelView);
        Button deleteBtn = (Button) labelView.findViewById(R.id.delete_label_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLl.removeView(labelView);
                mLabelViews.remove(labelView);
            }
        });
        int btnIndex = contentLl.indexOfChild(addLabelBtn);
        contentLl.addView(labelView, btnIndex);
        contentLl.setPadding(0, 0, 0, 20);
    }

    //将标签信息加入到goodInfo中
    private void finishGoodInfo(){
        ArrayList<DescriptionItem> descriptionList = new ArrayList<>();
        for(View view:mLabelViews){
            EditText titleEt = (EditText) view.findViewById(R.id.label_et);
            EditText contentEt = (EditText) view.findViewById(R.id.label_detail_et);
            DescriptionItem descriptionItem = new DescriptionItem();
            descriptionItem.setTitle(titleEt.getText().toString());
            descriptionItem.setContent(contentEt.getText().toString());
            descriptionList.add(descriptionItem);
        }
        String desc = new Gson().toJson(descriptionList);
        goodInfo.setDescription(desc);
    }

    //检查标签内容是否合法
    private boolean isValid(){
        for(View view:mLabelViews){
            EditText titleEt = (EditText) view.findViewById(R.id.label_et);
            EditText contentEt = (EditText) view.findViewById(R.id.label_detail_et);

            if(CommonUtils.isEmpty(titleEt.getText().toString()) || CommonUtils.isEmpty(contentEt.getText().toString())){
                showToast(getString(R.string.plz_finish_label_info));
                return false;
            }else if(contentEt.getText().toString().length() < 10){
                showToast(getString(R.string.label_detail_cant_less_10));
                return false;
            }
        }
        return true;
    }

    //若再次进入此页面，将标签信息显示到视图
    private void setLabelIntoViews(){
        if(goodInfo.getDescription() == null){
            return;
        }

        ArrayList<DescriptionItem> description;
        description = new Gson().fromJson(goodInfo.getDescription(), new TypeToken<ArrayList<DescriptionItem>>(){}.getType());
        for(int i = 0;i < description.size();i++){
            DescriptionItem item = description.get(i);
            addLabelDetailView();
            View view = mLabelViews.get(i);
            EditText titleEt = (EditText) view.findViewById(R.id.label_et);
            EditText contentEt = (EditText) view.findViewById(R.id.label_detail_et);
            titleEt.setText(item.getTitle());
            contentEt.setText(item.getContent());
        }
    }

    @Override
    public void onBackPressed() {
        //没有完成编辑，返回第一页,并将当前记录传过去，起到记录缓存的作用
        finishGoodInfo();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(GOOD_INFO,goodInfo);
        intent.putExtras(bundle);
        setResult(RESULT_CANCELED,intent);
        super.onBackPressed();
    }

    private void createGood(){
        if(!NetUtils.isNetworkConnected(this)){
            showNetWorkError();
            progressDialog.dismiss();
            return;
        }

        goodInfo.setShop_id(PreferenceUtils.getString(this,PreferenceUtils.Key.SHOP_ID));
        ApiManager.getService(this).createGood(goodInfo, new Callback<CreateGoodRes>() {
            @Override
            public void success(CreateGoodRes createGoodRes, Response response) {
                if (createGoodRes != null) {
                    setResult(RESULT_OK);
                    progressDialog.dismiss();
                    Toast.makeText(EditGoodLabelAct.this,getString(R.string.good_create_success),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showInnerError(error);
            }
        });
    }
    private void updateGood(){
        if(!NetUtils.isNetworkConnected(this)){
            progressDialog.dismiss();
            showNetWorkError();
            return;
        }

        UpdateGoodReq goodReq = new UpdateGoodReq();
        goodReq.setName(goodInfo.getName());
        goodReq.setLabel(goodInfo.getLabel());
        goodReq.setGroup(goodInfo.getGroup());
        goodReq.setPrice(goodInfo.getPrice());
        goodReq.setOriginal_price(goodInfo.getOriginal_price());
        goodReq.setDiscount(goodInfo.getDiscount());
        goodReq.setDescription(goodInfo.getDescription());
        goodReq.setImage(goodInfo.getImage());
        ApiManager.getService(this).updateGoodInfo(goodId, goodReq, new Callback<CommonStatusRes>() {
            @Override
            public void success(CommonStatusRes commonStatusRes, Response response) {
                if (commonStatusRes != null) {
                    setResult(RESULT_OK);
                    progressDialog.dismiss();
                    Toast.makeText(EditGoodLabelAct.this,getString(R.string.good_update_success),Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showInnerError(error);
            }
        });
    }

    //mode:1创建商品，2更新商品
    private void upLoadImage(final int mode){
        if(!NetUtils.isNetworkConnected(this)){
            progressDialog.dismiss();
            showNetWorkError();
            return;
        }

        ApiManager.getService(this).createQiNiuToken(new Callback<QnRes>() {
            @Override
            public void success(QnRes qnRes, Response response) {
                if (qnRes != null) {
                    UploadImage.upLoadImage(imagePath, qnRes.getKey(), qnRes.getToken(), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                goodInfo.setImage(key);
                                if (mode == 1) {
                                    createGood();
                                } else {
                                    updateGood();
                                }
                            }
                        }
                    }, null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showInnerError(error);
            }
        });
    }

    private void initProgressDialog(){
        if(progressDialog != null){
            return;
        }
        progressDialog = new ProgressDialog(this);
    }
}
