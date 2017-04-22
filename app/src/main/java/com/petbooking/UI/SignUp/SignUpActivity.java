package com.petbooking.UI.SignUp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.petbooking.API.User.Models.Address;
import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.MaskManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.Utils.FormUtils;
import com.petbooking.databinding.UserFormBinding;

import org.greenrobot.eventbus.EventBus;

public class SignUpActivity extends BaseActivity {

    private UserService mUserService;

    private EditText mEdtCpf;
    private EditText mEdtZipcode;
    private EditText mEdtCity;
    private EditText mEdtNeighborhood;
    private EditText mEdtStreet;
    private EditText mEdtState;
    private EditText mEdtPhone;
    private EditText mEdtRepeatPass;
    private Button mBtnRegister;

    private UserFormBinding mBinding;
    private User user;

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.user_form);
        user = new User();

        mUserService = new UserService();

        mEdtCpf = (EditText) findViewById(R.id.user_cpf);
        mEdtPhone = (EditText) findViewById(R.id.user_phone);
        mEdtZipcode = (EditText) findViewById(R.id.user_zipcode);
        mEdtStreet = (EditText) findViewById(R.id.user_street);
        mEdtCity = (EditText) findViewById(R.id.user_city);
        mEdtNeighborhood = (EditText) findViewById(R.id.user_neighborhood);
        mEdtState = (EditText) findViewById(R.id.user_state);
        mEdtRepeatPass = (EditText) findViewById(R.id.repeat_password);

        mEdtCpf.addTextChangedListener(MaskManager.insert("###.###.###-##", mEdtCpf));
        mEdtZipcode.addTextChangedListener(MaskManager.insert("##.###-###", mEdtZipcode));
        mEdtPhone.addTextChangedListener(MaskManager.insert("(##) #####.####", mEdtPhone));

        mEdtZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String zipcode = s.toString();
                if (zipcode.length() == 10) {
                    zipcode = zipcode.replace(".", "").replace("-", "");
                    getAdress(zipcode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mBtnRegister = (Button) findViewById(R.id.registerButton);
        mBtnRegister.setOnClickListener(mListener);
        mBinding.setUser(user);
    }

    /**
     * Register User
     */
    public void registerUser() {
        int message = FormUtils.validateUser(user);
        String repeatPassword = mEdtRepeatPass.getText().toString();

        if (message == -1 && (user.password.equals(repeatPassword))) {
            Log.d("USER", new Gson().toJson(user));
        } else if (message == -1 && (!user.password.equals(repeatPassword))) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_different_password, Snackbar.LENGTH_LONG));
        } else if (message != -1) {
            EventBus.getDefault().post(new ShowSnackbarEvt(message, Snackbar.LENGTH_LONG));
        }
    }

    /**
     * Get Address from Zipcode
     *
     * @param zipcode
     */
    public void getAdress(String zipcode) {
        EventBus.getDefault().post(new ShowLoadingEvt());
        mUserService.getAddress(zipcode, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                Address address = (Address) response;

                mEdtCity.setText(address.localidade);
                mEdtStreet.setText(address.logradouro);
                mEdtNeighborhood.setText(address.bairro);
                mEdtState.setText(address.uf);

                EventBus.getDefault().post(new HideLoadingEvt());
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
