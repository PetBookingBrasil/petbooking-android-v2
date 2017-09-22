package com.petbooking.UI.Menu.Pets.RegisterPet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Breed;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Dialogs.DatePickerFragment;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Dialogs.PictureSelectDialogFragment;
import com.petbooking.UI.Dialogs.TableDialogFragment;
import com.petbooking.UI.Widget.MaterialSpinner;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;
import com.petbooking.Utils.FormUtils;
import com.petbooking.Utils.ImageUtils;
import com.petbooking.databinding.PetFormBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;


public class RegisterPetActivity extends BaseActivity implements
        PictureSelectDialogFragment.FinishDialogListener,
        FeedbackDialogFragment.FinishDialogListener,
        DatePickerFragment.DatePickerListener {

    private PetService mPetService;
    private SessionManager mSessionManager;
    private FragmentManager mFragmentManager;

    /**
     * Binding
     */
    private User mUser;
    private PetFormBinding mBinding;
    private Pet pet;


    private List<Breed> dogBreeds;
    private List<Breed> catBreeds;
    private List<String> dogBreedsString;
    private List<String> catBreedsString;

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
     * Table Fragment
     */
    TableDialogFragment mTableDialogFragment = new TableDialogFragment();

    /**
     * Form Inputs
     */
    private EditText mEdtName;
    private MaterialSpinner mSpGender;
    private MaterialSpinner mSpType;
    private MaterialSpinner mSpSize;
    private MaterialSpinner mSpBreed;
    private MaterialSpinner mSpCoat;
    private MaterialSpinner mSpTemper;
    private ImageView mIvPetPhoto;
    private EditText mEdtBirthday;
    private ImageButton mIBtnSelectPicture;
    private Button mBtnSubmit;

    AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mSpBreed.setItems(dogBreedsString);
                mSpSize.setItems(R.array.size_dog_array);
            } else if (position == 1) {
                mSpBreed.setItems(catBreedsString);
                mSpSize.setItems(R.array.size_cat_array);
            } else {
                mSpBreed.setItems(R.array.empty_array);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener infoSizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int layout = mSpType.getPosition() == 1 ? R.layout.dialog_pet_dog_size : R.layout.dialog_pet_cat_size;
            mTableDialogFragment.setLayout(layout);
            mTableDialogFragment.show(mFragmentManager, "TABLE");
        }
    };

    View.OnClickListener mSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialogFragmentPictureSelect.show(mFragmentManager, "SELECT_PICTURE");
        }
    };

    View.OnClickListener mBirthdayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDatePicker.show(mFragmentManager, "DATE_PICKER");
        }
    };

    View.OnClickListener mSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerPet();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.pet_form);
        mSessionManager = SessionManager.getInstance();
        mPetService = new PetService();

        mUser = mSessionManager.getUserLogged();

        mFragmentManager = getSupportFragmentManager();
        mDialogFragmentPictureSelect = PictureSelectDialogFragment.newInstance();
        mDialogFragmentFeedback = FeedbackDialogFragment.newInstance();
        mTableDialogFragment = TableDialogFragment.newInstance();
        mDatePicker = DatePickerFragment.newInstance();

        mIvPetPhoto = (ImageView) findViewById(R.id.pet_photo);
        mIBtnSelectPicture = (ImageButton) findViewById(R.id.select_picture);
        mEdtName = (EditText) findViewById(R.id.pet_name);
        mEdtBirthday = (EditText) findViewById(R.id.pet_birthday);
        mSpGender = (MaterialSpinner) findViewById(R.id.pet_gender);
        mSpType = (MaterialSpinner) findViewById(R.id.pet_type);
        mSpSize = (MaterialSpinner) findViewById(R.id.pet_size);
        mSpBreed = (MaterialSpinner) findViewById(R.id.pet_breed);
        mSpTemper = (MaterialSpinner) findViewById(R.id.pet_temper);
        mSpCoat = (MaterialSpinner) findViewById(R.id.pet_coat);
        mBtnSubmit = (Button) findViewById(R.id.submitButton);

        mSpType.setOnItemSelectedListener(typeListener);
        mSpSize.setOnInfoClickListener(infoSizeListener);

        mIBtnSelectPicture = (ImageButton) findViewById(R.id.select_picture);
        mIBtnSelectPicture.setOnClickListener(mSelectListener);
        mEdtBirthday.setOnClickListener(mBirthdayListener);
        mIvPetPhoto.setOnClickListener(mSelectListener);
        mBtnSubmit.setOnClickListener(mSubmitListener);

        dogBreeds = new ArrayList<>();
        catBreeds = new ArrayList<>();
        dogBreedsString = new ArrayList<>();
        catBreedsString = new ArrayList<>();

        listDogBreeds();
        listCatBreeds();

        pet = new Pet();
        mBinding.setPet(pet);
    }

    /**
     * List All Dog Breeds
     */
    public void listDogBreeds() {
        mPetService.listDogBreeds(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BreedResp breeds = (BreedResp) response;
                for (BreedResp.Item breed : breeds.data) {
                    dogBreedsString.add(breed.attributes.name);
                    dogBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
                }

            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * List All Cat Breeds
     */
    public void listCatBreeds() {
        mPetService.listCatBreeds(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BreedResp breeds = (BreedResp) response;
                for (BreedResp.Item breed : breeds.data) {
                    catBreedsString.add(breed.attributes.name);
                    catBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
                }
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
        mIvPetPhoto.setVisibility(View.VISIBLE);
        mIvPetPhoto.setImageBitmap(photo);
    }

    /**
     * Register Pet
     */
    public void registerPet() {
        parsePet();
        int message = -1;

        try {
            message = FormUtils.validatePet(pet);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (message == -1) {
            createRequest(pet);
        } else {
            EventBus.getDefault().post(new ShowSnackbarEvt(message, Snackbar.LENGTH_LONG));
        }
    }

    /**
     * Create Request to Register pet
     *
     * @param pet
     */
    public void createRequest(Pet pet) {
        clearFocus();
        AppUtils.showLoadingDialog(this);
        mPetService.createPet(mUser.id, pet, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                resetForm();
                AppUtils.hideDialog();
                mDialogFragmentFeedback.setDialogInfo(R.string.success_create_pet, R.string.success_create_pet_text,
                        R.string.dialog_button_start, AppConstants.OK_ACTION);
                mDialogFragmentFeedback.setSecondButton(R.string.dialog_button_add_pet, AppConstants.ADD_PET);
                mDialogFragmentFeedback.showSecondButton(true);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
                mDialogFragmentFeedback.setDialogInfo(R.string.error_create_pet, R.string.error_create_text,
                        R.string.dialog_button_check_info, AppConstants.CANCEL_ACTION);
                mDialogFragmentFeedback.showSecondButton(false);
                mDialogFragmentFeedback.show(mFragmentManager, "FEEDBACK");
            }
        });
    }


    /**
     * Parse Pet Info
     */
    public void parsePet() {
        int breedPosition = mSpBreed.getPosition();

        pet.userId = mUser.id;
        pet.gender = AppUtils.getGender(this, mSpGender.getSelectedItem());
        pet.size = AppUtils.getSize(this, mSpSize.getSelectedItem());
        pet.coatType = AppUtils.getCoatType(this, mSpCoat.getSelectedItem());
        pet.mood = AppUtils.getTemper(this, mSpTemper.getSelectedItem());
        pet.type = AppUtils.getType(this, mSpType.getSelectedItem());

        if (mBitmap != null) {
            pet.photo = CommonUtils.encodeBase64(mBitmap);
            pet.photo = APIPetConstants.DATA_BASE64 + pet.photo;
        }

        if (mSpType.getPosition() > 0 && breedPosition > 0) {
            pet.breedId = mSpType.getPosition() == 1 ? dogBreeds.get(breedPosition - 1).id : catBreeds.get(breedPosition - 1).id;
            pet.breedName = mSpType.getPosition() == 1 ? dogBreeds.get(breedPosition - 1).name : catBreeds.get(breedPosition - 1).name;
        } else {
            pet.breedId = "";
            pet.breedName = "";
        }
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.PICK_PHOTO) {
            openGalery();
        } else if (action == AppConstants.TAKE_PHOTO) {
            openCamera();
        } else if (action == AppConstants.OK_ACTION) {
            goToDashboard();
        } else if (action == AppConstants.ADD_PET) {
            resetForm();
            mEdtName.requestFocus();
        }
    }

    /**
     * Go To dashboard
     */
    private void goToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }

    /**
     * Reset Form
     */
    private void resetForm() {
        pet = new Pet();
        mBinding.setPet(pet);
        mSpGender.selectItem(0);
        mSpType.selectItem(0);
        mSpSize.selectItem(0);
        mSpBreed.selectItem(0);
        mSpCoat.selectItem(0);
        mSpTemper.selectItem(0);
    }

    /**
     * Clear input focus
     */
    private void clearFocus() {
        View current = getCurrentFocus();
        if (current != null) current.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.PICK_PHOTO) {
                mUri = data.getData();

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                    mBitmap = ImageUtils.modifyOrientation(this, mBitmap, mUri);
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
