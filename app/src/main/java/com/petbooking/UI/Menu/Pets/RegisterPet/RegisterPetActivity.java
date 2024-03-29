package com.petbooking.UI.Menu.Pets.RegisterPet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.API.Pet.Models.AttributesResponse;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.App;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Breed;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.CreatePetPojo;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Dialogs.DatePickerFragment;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Dialogs.PictureSelectDialogFragment;
import com.petbooking.UI.Dialogs.TableDialogFragment;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.UI.Widget.MaterialSpinner;
import com.petbooking.UI.Widget.StyledSwitch;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;
import com.petbooking.Utils.FormUtils;
import com.petbooking.Utils.ImageUtils;
import com.petbooking.databinding.PetFormBinding;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;


public class RegisterPetActivity extends AppCompatActivity implements
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
    private MaterialSpinner mSpColorPet;
    private ImageView mIvPetPhoto;
    private EditText mEdtBirthday;
    private ImageButton mIBtnSelectPicture;
    private Button mBtnSubmit;
    private TextInputLayout textLayoutChip;
    StyledSwitch castratedSwitch;
    StyledSwitch chipSwitch;
    EditText chipNumberText;
    EditText petDescription;
    boolean schedule;
    private String userId;
    HashMap<String,Integer> colorsType;

    AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mSpBreed.setItems(dogBreedsString);
                mSpSize.setItems(R.array.size_dog_array);
                getAttributes("dog");
            } else if (position == 1) {
                mSpBreed.setItems(catBreedsString);
                mSpSize.setItems(R.array.size_cat_array);
                getAttributes("cat");
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
        schedule = getIntent().getBooleanExtra("schedule",false);

        super.onCreate(savedInstanceState);
        if(!schedule){
            setTheme(R.style.AppTheme);
        }
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
        petDescription = (EditText) findViewById(R.id.pet_observation);
        mEdtBirthday = (EditText) findViewById(R.id.pet_birthday);
        mSpGender = (MaterialSpinner) findViewById(R.id.pet_gender);
        mSpType = (MaterialSpinner) findViewById(R.id.pet_type);
        mSpSize = (MaterialSpinner) findViewById(R.id.pet_size);
        mSpBreed = (MaterialSpinner) findViewById(R.id.pet_breed);
        mSpTemper = (MaterialSpinner) findViewById(R.id.pet_temper);
        mSpCoat = (MaterialSpinner) findViewById(R.id.pet_coat);
        mBtnSubmit = (Button) findViewById(R.id.submitButton);
        chipNumberText = (EditText) findViewById(R.id.pet_chip_number);
        chipSwitch = (StyledSwitch) findViewById(R.id.switch_chip);
        textLayoutChip = (TextInputLayout) findViewById(R.id.chip_number_tl);
        castratedSwitch = (StyledSwitch) findViewById(R.id.switch_castrated);
        mSpType.setOnItemSelectedListener(typeListener);
        mSpSize.setOnInfoClickListener(infoSizeListener);
        mSpColorPet = (MaterialSpinner) findViewById(R.id.color_pet);
        this.userId = SessionManager.getInstance().getUserLogged().id;

        if(!schedule){

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
            toolbar.setVisibility(View.GONE);
            RelativeLayout appPetToolbar = (RelativeLayout) findViewById(R.id.add_pet_toolbar);
            appPetToolbar.setVisibility(View.VISIBLE);
            ImageButton closePetForm = (ImageButton) findViewById(R.id.close_pet_form);
            closePetForm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        if(schedule){
            mSpColorPet.setHintColor(R.color.white);
            mSpBreed.setHintColor(R.color.white);
            mSpCoat.setHintColor(R.color.white);
            mSpGender.setHintColor(R.color.white);
            mSpSize.setHintColor(R.color.white);
            mSpTemper.setHintColor(R.color.white);
            mSpType.setHintColor(R.color.white);
            mSpType.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpColorPet.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpBreed.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpCoat.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpGender.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpSize.getmSpinner().setPaddingSafe(0,0,0,0);
            mSpTemper.getmSpinner().setPaddingSafe(0,0,0,0);
            chipSwitch.getmTvTitle().setTextColor(ContextCompat.getColor(this,R.color.white));
            castratedSwitch.getmTvTitle().setTextColor(ContextCompat.getColor(this,R.color.white));
            mSpColorPet.setIcon(null);
            mSpBreed.setIcon(null);
            mSpCoat.setIcon(null);
            mSpGender.setIcon(null);
            mSpSize.setIcon(null);
            mSpTemper.setIcon(null);
            mSpType.setIcon(null);
            mEdtName.setCompoundDrawables(null,null,null,null);
            mEdtBirthday.setCompoundDrawables(null,null,null,null);
            petDescription.setCompoundDrawables(null,null,null,null);
        }

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
        getAttributes("dog");
        pet = new Pet();
        mBinding.setPet(pet);

       chipSwitch.getmSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   textLayoutChip.setVisibility(View.VISIBLE);
               }else{
                   textLayoutChip.setVisibility(View.INVISIBLE);
               }
           }
       });
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

    public void getAttributes(String typePet){
        mPetService.getAtributtes(typePet,userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AttributesResponse att = (AttributesResponse) response;
                RegisterPetActivity.this.mSpGender.setItems(AppUtils.getGenders(RegisterPetActivity.this,(ArrayList<String>)att.data.attributes.genders));
                RegisterPetActivity.this.mSpSize.setItems(AppUtils.getSizes(RegisterPetActivity.this, (ArrayList<String>) att.data.attributes.sizes));
                RegisterPetActivity.this.mSpCoat.setItems(AppUtils.getCoatTypes(RegisterPetActivity.this,(ArrayList<String>) att.data.attributes.coat_types));
                RegisterPetActivity.this.mSpColorPet.setItems(getColorPet(att.data.attributes.coat_colors));
                RegisterPetActivity.this.colorsType = att.data.attributes.coat_colors;

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

        Glide.with(this)
                .load(ImageUtils.bitmapToByte(photo))
                .bitmapTransform(new CircleTransformation(this))
                .into(mIvPetPhoto);
    }

    /**
     * Register Pet
     */
    public void registerPet() {
        parsePet();
        int message = -1;

        try {
            if(chipSwitch.isChecked())
            message = FormUtils.validatePet(pet,true);
            else
                message = FormUtils.validatePet(pet,false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (message == -1) {
            createRequest(pet);
        } else {
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
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
        pet.colorPet = mSpColorPet.getPosition();

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
            if(!schedule) {
                goToDashboard();
            }else {
                EventBus.getDefault().post(new CreatePetPojo());
                onBackPressed();
            }
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
        mSpColorPet.selectItem(0);
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

    public ArrayList<String> getColorPet(HashMap<String,Integer> hashMap){
        int i = 0;
        ArrayList<String> colors = new ArrayList<>();
        for ( String key : hashMap.keySet() ) {
            colors.add(key);
        }
        return colors;
    }

}
