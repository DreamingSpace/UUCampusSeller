package com.dreamspace.uucampusseller.ui.activity.order;

import android.app.ProgressDialog;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.TLog;
import com.dreamspace.uucampusseller.model.api.GetShopOrderDetailRes;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wufan on 2015/10/21.
 */
public class OrderDetailActivity extends AbsActivity{

    @Bind(R.id.order_detail_name_tv)
    TextView mGoodsNameTv;
    @Bind(R.id.order_detail_id_tv)
    TextView mOrderIdTv;
    @Bind(R.id.order_detail_time_tv)
    TextView mOrderTimeTv;
    @Bind(R.id.order_detail_phone_tv)
    TextView mPhoneTv;
    @Bind(R.id.order_detail_location_tv)
    TextView mLocationTv;
    @Bind(R.id.order_detail_quantity_tv)
    TextView mQuantityTv;
    @Bind(R.id.order_detail_total_price_tv)
    TextView mTotalPriceTv;
    @Bind(R.id.order_detail_pay_tv)
    TextView mPayTv;
    @Bind(R.id.order_detail_discount_tv)
    TextView mDiscountTv;
    @Bind(R.id.order_detail_memorandum_tv)
    TextView mMemorandumTv;

    public static final String EXTRA_ORDER_ID="order_id";
    private String order_id=null;

    @Override
    protected int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void prepareDatas() {
        order_id = getIntent().getExtras().getString(EXTRA_ORDER_ID);
        TLog.i("order_id:",order_id);
    }

    @Override
    protected void initViews() {
        getInitData();
    }

    private void getInitData() {
        final ProgressDialog pd = ProgressDialog.show(this,"","正在加载",true,false);
        if(NetUtils.isNetworkConnected(getApplicationContext())){
            ApiManager.getService(getApplicationContext()).getShopOrderDetail(order_id, new Callback<GetShopOrderDetailRes>() {
                @Override
                public void success(GetShopOrderDetailRes getShopOrderDetailRes, Response response) {
                    setDataToView(getShopOrderDetailRes);
                    pd.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                    pd.dismiss();
                }
            });
        }else{
            showNetWorkError();
            pd.dismiss();
        }
    }

    public void setDataToView(GetShopOrderDetailRes getShopOrderDetailRes) {
        mGoodsNameTv.setText(" : "+getShopOrderDetailRes.getGood().getName());
        mOrderIdTv.setText(" : "+getShopOrderDetailRes.get_id());
        mOrderTimeTv.setText(" : "+getShopOrderDetailRes.getTime());
        mPhoneTv.setText(" : "+getShopOrderDetailRes.getBuyer().getPhone_num());
        mLocationTv.setText(" : "+getShopOrderDetailRes.getBuyer().getLocation());
        mQuantityTv.setText(" : "+String.valueOf(getShopOrderDetailRes.getQuantity()));
        mTotalPriceTv.setText(" : "+getShopOrderDetailRes.getTotal_price());
        mPayTv.setText(" : "+String.valueOf(getShopOrderDetailRes.getTotal_price()));
        mDiscountTv.setText(" : "+String.valueOf(getShopOrderDetailRes.getGood().getDiscount()));
        mMemorandumTv.setText(" : "+getShopOrderDetailRes.getGood().getLabel());
    }
}
