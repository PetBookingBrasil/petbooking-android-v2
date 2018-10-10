package com.petbooking.UI.Dialogs;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.petbooking.API.Pet.PetService;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Breed;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Widget.MaterialSpinner;
import com.petbooking.UI.Widget.StyledSwitch;
import com.petbooking.databinding.PetFormBinding;

import java.util.List;

/**
 * Created by joice on 25/01/18.
 */

public class CreatePetDialog extends DialogFragment implements DialogInterface.OnCancelListener,
        PictureSelectDialogFragment.FinishDialogListener,
        FeedbackDialogFragment.FinishDialogListener,
        DatePickerFragment.DatePickerListener{

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
    StyledSwitch registerSwitch;
    StyledSwitch chipSwitch;
    EditText chipNumberText;

    public CreatePetDialog() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_form, container);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDateSet(String date) {

    }

    @Override
    public void onFinishDialog(int action) {

    }
}
