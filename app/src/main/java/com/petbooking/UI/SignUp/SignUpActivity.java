package com.petbooking.UI.SignUp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.MaskManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.Models.UserAddress;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Dialogs.DatePickerFragment;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Dialogs.PictureSelectDialogFragment;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;
import com.petbooking.Utils.FormUtils;
import com.petbooking.Utils.ImageUtils;
import com.petbooking.databinding.UserFormBinding;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import static android.view.View.GONE;

public class SignUpActivity extends BaseActivity implements
        PictureSelectDialogFragment.FinishDialogListener,
        FeedbackDialogFragment.FinishDialogListener,
        DatePickerFragment.DatePickerListener {

    private FragmentManager mFragmentManager;
    private UserService mUserService;
    private SessionManager mSessionManager;

    private boolean isSocialLogin;

    /**
     * Picture Select
     */
    private PictureSelectDialogFragment mDialogFragmentPictureSelect;
    private Uri mUri;
    private Bitmap mBitmap;

    /**
     * Feedback
     */
    private FeedbackDialogFragment mDialogFragmentFeedback;

    /**
     * Date Picker
     */
    private DatePickerFragment mDatePicker;

    /**
     * Form Inputs
     */
    private ImageView mIvUserPhoto;
    private RadioButton mRbGenderMale;
    private RadioButton mRbGenderFemale;
    private EditText mEdtBirthday;
    private EditText mEdtCpf;
    private EditText mEdtZipcode;
    private EditText mEdtCity;
    private EditText mEdtNeighborhood;
    private EditText mEdtStreet;
    private EditText mEdtState;
    private EditText mEdtPhone;
    private EditText mEdtRepeatPass;
    private EditText mUserPassword;
    private Button mBtnSubmit;
    private TextInputLayout fulNameTL;
    private TextInputLayout emailTL;
    private ImageButton mIBtnSelectPicture;
    private LinearLayout layoutHeader;
    private LinearLayout layoutPlaceHolder;
    private TextInputLayout textInputPassword;

    private UserFormBinding mBinding;
    private User user;

    View.OnClickListener mSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    };

    View.OnClickListener mSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialogFragmentPictureSelect.show(mFragmentManager, "SELECT_PICTURE");
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

    View.OnClickListener mBirthdayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatePicker.show(mFragmentManager, "DATE_PICKER");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.user_form);
        isSocialLogin = getIntent().getBooleanExtra(AppConstants.SOCIAL_LOGIN, false);

        mFragmentManager = getSupportFragmentManager();
        mUserService = new UserService();
        mSessionManager = SessionManager.getInstance();
        mDialogFragmentPictureSelect = PictureSelectDialogFragment.newInstance();
        mDialogFragmentFeedback = FeedbackDialogFragment.newInstance();
        mDatePicker = DatePickerFragment.newInstance();

        mIvUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mRbGenderMale = (RadioButton) findViewById(R.id.gender_male);
        mRbGenderFemale = (RadioButton) findViewById(R.id.gender_female);
        mEdtBirthday = (EditText) findViewById(R.id.user_birthday);
        mEdtCpf = (EditText) findViewById(R.id.user_cpf);
        mEdtPhone = (EditText) findViewById(R.id.user_phone);
        mEdtZipcode = (EditText) findViewById(R.id.user_zipcode);
        mEdtStreet = (EditText) findViewById(R.id.user_street);
        mEdtCity = (EditText) findViewById(R.id.user_city);
        mEdtNeighborhood = (EditText) findViewById(R.id.user_neighborhood);
        mEdtState = (EditText) findViewById(R.id.user_state);
        mEdtRepeatPass = (EditText) findViewById(R.id.repeat_password);
        fulNameTL = (TextInputLayout) findViewById(R.id.fullNameTL);
        emailTL = (TextInputLayout) findViewById(R.id.emailTL);
        mUserPassword = (EditText) findViewById(R.id.user_password);
        layoutHeader = (LinearLayout) findViewById(R.id.layout_header);
        layoutPlaceHolder = (LinearLayout) findViewById(R.id.placeHolder_complete_register);
        textInputPassword = (TextInputLayout) findViewById(R.id.text_input_password);

        mEdtBirthday.addTextChangedListener(MaskManager.insert("##/##/####", mEdtBirthday));
        mEdtBirthday.setOnClickListener(mBirthdayListener);
        mEdtCpf.addTextChangedListener(MaskManager.insert("###.###.###-##", mEdtCpf));
        mEdtZipcode.addTextChangedListener(MaskManager.insert("##.###-###", mEdtZipcode));
        mEdtPhone.addTextChangedListener(MaskManager.insert("(##) #####.####", mEdtPhone));

        mEdtZipcode.addTextChangedListener(mTextWatcher);

        mBtnSubmit = (Button) findViewById(R.id.submitButton);
        mIBtnSelectPicture = (ImageButton) findViewById(R.id.select_picture);
        mBtnSubmit.setOnClickListener(mSubmitListener);
        mIBtnSelectPicture.setOnClickListener(mSelectListener);
        mIvUserPhoto.setOnClickListener(mSelectListener);

        if (isSocialLogin) {
            String userWrapped = getIntent().getStringExtra(AppConstants.USER_WRAPPED);
            user = new Gson().fromJson(userWrapped, User.class);
            mIBtnSelectPicture.setVisibility(View.GONE);
            mIvUserPhoto.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(user.avatar.url)
                    .error(R.drawable.ic_menu_user)
                    .placeholder(R.drawable.ic_menu_user)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .bitmapTransform(new CircleTransformation(this))
                    .dontAnimate()
                    .into(mIvUserPhoto);
            layoutPlaceHolder.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
            fulNameTL.setVisibility(View.INVISIBLE);
            mEdtCpf.setVisibility(GONE);
            emailTL.setVisibility(GONE);
            mUserPassword.setVisibility(GONE);
            textInputPassword.setVisibility(GONE);
            mBtnSubmit.setText(R.string.continue_register);


        } else {
            user = new User();
            mEdtPhone.setNextFocusDownId(R.id.user_password);
            mEdtCpf.setVisibility(GONE);
        }


        mBinding.setUser(user);
    }

    /**
     * Register User
     */
    public void registerUser() {
        int message = -1;

        try {
            if(isSocialLogin){
                message = FormUtils.validateUserSocialLogin(user);
            }else {
                message = FormUtils.newValidateUser(user, true);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String repeatPassword = user.password;

        user.gender = mRbGenderMale.isChecked() ? User.GENDER_MALE : User.GENDER_FEMALE;
        if(isSocialLogin){
            user.password = "";
            repeatPassword = "";
        }
        if (message == -1 && (user.password.equals(repeatPassword))) {
            if (isSocialLogin) {
                createSocialUser(user);
            } else {
                createUser(user,isSocialLogin);
            }
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
                UserAddress address = (UserAddress) response;

                mEdtCity.setText(address.city);
                mEdtStreet.setText(address.street);
                mEdtNeighborhood.setText(address.neighborhood);
                mEdtState.setText(address.state);

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
        mIBtnSelectPicture.setVisibility(GONE);
        mIvUserPhoto.setVisibility(View.VISIBLE);

        user.photo = CommonUtils.encodeBase64(mBitmap);
        user.photo = AppConstants.BASE64 + user.photo;

        Glide.with(this)
                .load(ImageUtils.bitmapToByte(photo))
                .bitmapTransform(new CircleTransformation(this))
                .into(mIvUserPhoto);
    }

    /**
     * Request to Create a new User
     *
     * @param user
     */
    public void createUser(final User user, boolean provider) {
        AppUtils.showLoadingDialog(this);
        mUserService.createUser(user,provider, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AppUtils.hideDialog();
                AuthUserResp authUserResp = (AuthUserResp) response;
                User registeredUser = APIUtils.parseUser(authUserResp);
                mSessionManager.setSessionToken(registeredUser.authToken);
                //mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                mSessionManager.setLastLogin(user.email, user.password);
                //scheduleRefreshToken(AppConstants.SESSION_TOKEN);
                mSessionManager.setUserLogged(registeredUser);
                mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title, R.string.success_create_user,
                        R.string.dialog_button_ok, AppConstants.OK_ACTION);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();

                    APIError apiError = (APIError) error;
                    try{
                        mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title,apiError.detail
                                        .replace("=","").replace("{","").replace("}","")
                                .replace("[","").replace("]","").replace("user","")
                                        .split("identity")[0].replace(",","."),
                                R.string.dialog_button_ok, AppConstants.BACK_SCREEN_ACTION);
                    }catch (Exception e){
                        e.printStackTrace();
                        mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title, R.string.error_create_user,
                                R.string.dialog_button_ok, AppConstants.BACK_SCREEN_ACTION);
                    }



                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }
        });
    }

    /**
     * Request to Create new Social User
     *
     * @param user
     */
    public void createSocialUser(final User user) {
        AppUtils.showLoadingDialog(this);
        mUserService.createSocialUser(user, APIConstants.DATA_PROVIDER_FACEBOOK, user.providerToken, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AppUtils.hideDialog();
                AuthUserResp authUserResp = (AuthUserResp) response;
                User registeredUser = APIUtils.parseUser(authUserResp);
                mSessionManager.setSessionToken(registeredUser.authToken);
                //mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                mSessionManager.setLastLogin(user.email, user.password);
                //scheduleRefreshToken(AppConstants.SESSION_TOKEN);
                mSessionManager.setUserLogged(registeredUser);
                mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title, R.string.success_create_user,
                        R.string.dialog_button_ok, AppConstants.OK_ACTION);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
                mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title, R.string.error_create_user,
                        R.string.dialog_button_ok, AppConstants.BACK_SCREEN_ACTION);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }
        });
    }

    /**
     * Go to Dashboard after logged
     */
    public void goToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }


    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.PICK_PHOTO) {
            openGalery();
        } else if (action == AppConstants.TAKE_PHOTO) {
            openCamera();
        } else if (action == AppConstants.OK_ACTION) {
            goToDashboard();
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

    @Override
    public void onDateSet(String date) {
        mEdtBirthday.setText(date);
    }
}
