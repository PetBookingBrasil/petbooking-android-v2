package com.petbooking.UI.Dashboard.Cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Appointment.Models.CartResp;
import com.petbooking.API.Appointment.Models.CartRqt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.CartItem;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private AppointmentManager mAppointmentManager;
    private AppointmentService mAppointmentService;
    private ArrayList<CartItem> mCart;
    private String userId;

    /**
     * Components
     */
    private TextView mTvTotalPrice;
    private Button mBtnSchedule;

    /**
     * Cart RecyclerView
     */
    private LinearLayoutManager mCartLayoutManager;
    private CartListAdapter mCartAdapter;
    private RecyclerView mRvCartItens;

    View.OnClickListener btnScheduleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createCartRequest();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String totalPrice;

        mAppointmentManager = AppointmentManager.getInstance();
        mAppointmentService = new AppointmentService();
        mCart = mAppointmentManager.getCart();
        userId = SessionManager.getInstance().getUserLogged().id;

        getSupportActionBar().setTitle(R.string.cart_title);
        getSupportActionBar().setSubtitle(mAppointmentManager.getCurrentBusinessName());

        mTvTotalPrice = (TextView) findViewById(R.id.total_price);
        mBtnSchedule = (Button) findViewById(R.id.confirm_schedule);
        mBtnSchedule.setOnClickListener(btnScheduleListener);

        mCartLayoutManager = new LinearLayoutManager(this);
        mCartLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mCartAdapter = new CartListAdapter(this, mCart);

        mRvCartItens = (RecyclerView) findViewById(R.id.cart_itens_list);
        mRvCartItens.setHasFixedSize(true);
        mRvCartItens.setLayoutManager(mCartLayoutManager);
        mRvCartItens.setAdapter(mCartAdapter);

        totalPrice = getResources().getString(R.string.business_service_price, String.format("%.2f", getTotalPrice()));
        mTvTotalPrice.setText(totalPrice);
    }

    /**
     * Get Total Cart price
     *
     * @return
     */
    public double getTotalPrice() {
        double totalPrice = 0;

        for (CartItem item : mCart) {
            totalPrice += item.totalPrice;
        }

        return totalPrice;
    }

    /**
     * Create cart request
     */
    public void createCartRequest() {
        AppUtils.showLoadingDialog(this);
        //TODO: Se for sucesso abrir webview
        mAppointmentService.createCart(userId, mCart, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CartResp resp = (CartResp) response;
                Log.i("CART_ID", resp.data.id);
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }
}
