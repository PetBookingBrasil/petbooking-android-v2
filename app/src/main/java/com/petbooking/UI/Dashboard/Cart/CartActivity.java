package com.petbooking.UI.Dashboard.Cart;

import android.content.Intent;
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
import com.petbooking.UI.Dashboard.PaymentWebview.PaymentActivity;
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

    CartListAdapter.OnCartChange onCartChange = new CartListAdapter.OnCartChange() {
        @Override
        public void onChange() {
            mCart = mAppointmentManager.getCart();
            mCartAdapter.updateList(mCart);
            mCartAdapter.notifyDataSetChanged();
            setTotalPrice();
            handleConfirmButton();
        }

        @Override
        public void onEditItem() {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        mCartAdapter = new CartListAdapter(this, mCart, onCartChange);

        mRvCartItens = (RecyclerView) findViewById(R.id.cart_itens_list);
        mRvCartItens.setHasFixedSize(true);
        mRvCartItens.setLayoutManager(mCartLayoutManager);
        mRvCartItens.setAdapter(mCartAdapter);

        setTotalPrice();
        handleConfirmButton();
    }

    /**
     * Set Total Cart price
     */
    public void setTotalPrice() {
        double totalPrice = 0;
        String totalPriceText;

        for (CartItem item : mCart) {
            totalPrice += item.totalPrice;
        }

        totalPriceText = getResources().getString(R.string.business_service_price, String.format("%.2f", totalPrice));
        mTvTotalPrice.setText(totalPriceText);
    }

    /**
     * Create cart request
     */
    public void createCartRequest() {
        AppUtils.showLoadingDialog(this);
        mAppointmentService.createCart(userId, mCart, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CartResp resp = (CartResp) response;
                mAppointmentManager.saveCartId(resp.data.id);
                goToPayment();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Go to Payment
     */
    public void goToPayment() {
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentIntent);
    }

    /**
     * Enable or disable button
     * on cart change
     */
    public void handleConfirmButton() {
        if (mCart.size() == 0) {
            mBtnSchedule.setEnabled(false);
        } else {
            mBtnSchedule.setEnabled(true);
        }
    }
}
