package com.petbooking.UI.SignUp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
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
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.FormUtils;
import com.petbooking.databinding.UserFormBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private CircleImageView mCiUserPhoto;
    private EditText mEdtBirthday;
    private EditText mEdtCpf;
    private EditText mEdtZipcode;
    private EditText mEdtCity;
    private EditText mEdtNeighborhood;
    private EditText mEdtStreet;
    private EditText mEdtState;
    private EditText mEdtPhone;
    private EditText mEdtRepeatPass;
    private Button mBtnSubmit;
    private ImageButton mIBtnSelectPicture;

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
        if (isSocialLogin) {
            String userWrapped = getIntent().getStringExtra(AppConstants.USER_WRAPPED);
            user = new Gson().fromJson(userWrapped, User.class);
        } else {
            user = new User();
        }

        mFragmentManager = getSupportFragmentManager();
        mUserService = new UserService();
        mSessionManager = SessionManager.getInstance();
        mDialogFragmentPictureSelect = PictureSelectDialogFragment.newInstance();
        mDialogFragmentFeedback = FeedbackDialogFragment.newInstance();
        mDatePicker = DatePickerFragment.newInstance();

        mCiUserPhoto = (CircleImageView) findViewById(R.id.user_photo);
        mEdtBirthday = (EditText) findViewById(R.id.user_birthday);
        mEdtCpf = (EditText) findViewById(R.id.user_cpf);
        mEdtPhone = (EditText) findViewById(R.id.user_phone);
        mEdtZipcode = (EditText) findViewById(R.id.user_zipcode);
        mEdtStreet = (EditText) findViewById(R.id.user_street);
        mEdtCity = (EditText) findViewById(R.id.user_city);
        mEdtNeighborhood = (EditText) findViewById(R.id.user_neighborhood);
        mEdtState = (EditText) findViewById(R.id.user_state);
        mEdtRepeatPass = (EditText) findViewById(R.id.repeat_password);

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
        mCiUserPhoto.setOnClickListener(mSelectListener);
        mBinding.setUser(user);
    }

    /**
     * Register User
     */
    public void registerUser() {
        int message = FormUtils.validateUser(user, true);
        String repeatPassword = mEdtRepeatPass.getText().toString();
        
        if (message == -1 && (user.password.equals(repeatPassword))) {
            createRequest(user);
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
        mIBtnSelectPicture.setVisibility(View.GONE);
        mCiUserPhoto.setVisibility(View.VISIBLE);
        mCiUserPhoto.setImageBitmap(photo);
    }

    /**
     * Request to Create a new User
     *
     * @param user
     */
    public void createRequest(User user) {
        mUserService.createUser(user, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthUserResp authUserResp = (AuthUserResp) response;
                User user = APIUtils.parseUser(authUserResp);
                mSessionManager.setUserLogged(user);
                mDialogFragmentFeedback.setDialogInfo(R.string.register_dialog_title, R.string.success_create_user,
                        R.string.dialog_button_ok, AppConstants.OK_ACTION);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }

            @Override
            public void onError(Object error) {
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
