package com.petbooking.UI.SignUp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.petbooking.API.User.Models.Address;
import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.MaskManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;
import com.petbooking.Utils.FormUtils;
import com.petbooking.databinding.UserFormBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends BaseActivity implements PictureSelectDialogFragment.FinishDialogListener {

    private UserService mUserService;

    /**
     * Picture Select
     */
    private PictureSelectDialogFragment mFragmentPictureSelect;
    private Uri mUri;
    private Bitmap mBitmap;

    /**
     * Form Inputs
     */
    private CircleImageView mCiUserPhoto;
    private EditText mEdtCpf;
    private EditText mEdtZipcode;
    private EditText mEdtCity;
    private EditText mEdtNeighborhood;
    private EditText mEdtStreet;
    private EditText mEdtState;
    private EditText mEdtPhone;
    private EditText mEdtRepeatPass;
    private Button mBtnRegister;
    private ImageButton mIBtnSelectPicture;

    private UserFormBinding mBinding;
    private User user;

    View.OnClickListener mRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    };

    View.OnClickListener mSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFragmentPictureSelect.show(getSupportFragmentManager(), "SELECT_PICTURE");
        }
    };

    TextWatcher mTextWatcher = new TextWatcher() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.user_form);
        user = new User();

        mUserService = new UserService();
        mFragmentPictureSelect = PictureSelectDialogFragment.newInstance();

        mCiUserPhoto = (CircleImageView) findViewById(R.id.user_photo);
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

        mEdtZipcode.addTextChangedListener(mTextWatcher);

        mBtnRegister = (Button) findViewById(R.id.registerButton);
        mIBtnSelectPicture = (ImageButton) findViewById(R.id.select_picture);
        mBtnRegister.setOnClickListener(mRegisterListener);
        mIBtnSelectPicture.setOnClickListener(mSelectListener);
        mCiUserPhoto.setOnClickListener(mSelectListener);
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

    /**
     * Open Galery to Pick Photo
     */
    public void openGalery() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, AppConstants.PICK_PHOTO);
    }

    /**
     * Open Camera to Take Photo
     */
    public void openCamera() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhoto, AppConstants.TAKE_PHOTO);
    }

    /**
     * Update User Photo
     *
     * @param photo
     */
    public void updatePhoto(Bitmap photo) {
        Log.d("BASE", CommonUtils.toBase64(mBitmap));
        mIBtnSelectPicture.setVisibility(View.GONE);
        mCiUserPhoto.setVisibility(View.VISIBLE);
        mCiUserPhoto.setImageBitmap(photo);
    }


    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.PICK_PHOTO) {
            openGalery();
        } else {
            openCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.PICK_PHOTO) {
                mUri = data.getData();

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mBitmap != null) {
                    updatePhoto(mBitmap);
                }
            } else if (requestCode == AppConstants.TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                mBitmap = (Bitmap) extras.get("data");

                if (mBitmap != null) {
                    updatePhoto(mBitmap);
                }
            }
        }
    }
}
