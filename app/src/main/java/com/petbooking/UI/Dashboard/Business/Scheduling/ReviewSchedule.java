package com.petbooking.UI.Dashboard.Business.Scheduling;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.API.Review.ReviewRequest;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Pet;
import com.petbooking.Models.ReviewServices;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewSchedule extends AppCompatActivity {
    @BindView(R.id.text_quant_bussines)
    TextView quantBussines;
    @BindView(R.id.pet_photo)
    ImageView petPhoto;
    @BindView(R.id.category_photo)
    ImageView categoryPhoto;
    @BindView(R.id.bussines_photo)
    ImageView bussinesPhoto;
    @BindView(R.id.professional_photo)
    ImageView professionalPhoto;
    @BindView(R.id.pet_name)
    TextView petName;
    @BindView(R.id.category_name)
    TextView categoryName;
    @BindView(R.id.bussines_name)
    TextView bussinesName;
    @BindView(R.id.professional_name)
    TextView professionalName;
    @BindView(R.id.date_service)
    TextView dateService;
    @BindView(R.id.layoutCategory)
    LinearLayout layoutBackGround;
    @BindView(R.id.edt_comment)
    EditText editComment;
    @BindView(R.id.button_send_review)
    Button send;
    @BindView(R.id.rating)
    MaterialRatingBar ratingBarAttendance;
    @BindView(R.id.ratingBarBussines)
    MaterialRatingBar ratingBarBussines;
    @BindView(R.id.ratingBarEnvironment)
    MaterialRatingBar ratingBarEnvironment;

    @OnClick(R.id.button_send_review)
    void sendReview() {

        //Send Review
        int attendance = Math.round(ratingBarAttendance.getRating());
        int bussisnes = Math.round(ratingBarBussines.getRating());
        int enviroment = Math.round(ratingBarEnvironment.getRating());
        String comment = editComment.getText().toString();
        ReviewRequest reviewRequest = new ReviewRequest(comment, bussisnes, enviroment, attendance, services.get(position).id);
        sendReview(reviewRequest);
    }

    int position = 0;
    int quantity;
    ArrayList<ReviewServices> services = new ArrayList<ReviewServices>();
    PetService mPetService;
    String userId;
    ArrayList<Pet> mPetList;
    String petAvatar = "";
    Pet pet;
    RequestManager mGlide;
    private BusinessService mBusinessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_schedule);
        ButterKnife.bind(this);
        mPetService = new PetService();
        mBusinessService = new BusinessService();
        services = getIntent().getParcelableArrayListExtra("services");
        quantity = services.size();
        this.userId = SessionManager.getInstance().getUserLogged().id;
        this.mPetList = new ArrayList<>();
        mGlide = Glide.with(this);
        //updateFields();
        getPets();
    }

    private void getReviews() {

    }

    private void updateFields() {

        if (quantity == 1) {
            quantBussines.setVisibility(View.INVISIBLE);
            send.setText(R.string.sendReview);
        } else if (position < quantity) {
            send.setText(R.string.send_and_next);
        } else {
            send.setText(R.string.sendReview);
        }
        quantBussines.setText(String.valueOf(position) + " de " + quantity);
        ratingBarAttendance.setRating(0);
        ratingBarBussines.setRating(0);
        ratingBarEnvironment.setRating(0);
        professionalName.setText(services.get(position).professionalName);
        categoryName.setText(services.get(position).serviceName);
        petName.setText(getPetName(services.get(position).petName));
        bussinesName.setText(services.get(position).bussinesName);

        Date date = CommonUtils.parseDate(CommonUtils.DATEFORMATDEFAULT, services.get(position).date);
        String formatedDate = CommonUtils.formatDate(CommonUtils.DAY_FORMAT_DESCRIOTION, date).toUpperCase(CommonUtils.BRAZIL) + " , " +
                CommonUtils.formatDate(CommonUtils.DAY_FORMAT, date) + " de " + CommonUtils.formatDate(CommonUtils.MONTH_DESCRIPTION_FORMAT, date).toUpperCase();
        dateService.setText(formatedDate);

        int color = AppUtils.getCategoryColor(this, services.get(position).serviceName);
        GradientDrawable iconBackground = (GradientDrawable) layoutBackGround.getBackground();
        categoryPhoto.setImageDrawable(AppUtils.getBusinessIcon(this, services.get(position).serviceName));
        iconBackground.setColor(color);

        int petAvatar;
        if (pet == null) {
            petAvatar = R.drawable.ic_placeholder_dog;
        } else if (pet.type != null && pet.type.equals("dog")) {
            petAvatar = R.drawable.ic_placeholder_dog;
        } else {
            petAvatar = R.drawable.ic_placeholder_cat;
        }
        mGlide.load(APIUtils.getAssetEndpoint(this.petAvatar))
                .error(petAvatar)
                .placeholder(petAvatar)
                .bitmapTransform(new CircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(petPhoto);

        int professionalAvatar;
        professionalAvatar = R.drawable.ic_placeholder_man;

        mGlide.load(APIUtils.getAssetEndpoint(services.get(position).professionalAvatar))
                .error(professionalAvatar)
                .placeholder(professionalAvatar)
                .bitmapTransform(new CircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(professionalPhoto);

    }

    private boolean hasNext() {
        if (quantity == 1) {
            return false;
        }
        if ((position + 1) > quantity) {
            return false;
        }
        return true;
    }

    public void getPets() {
        AppUtils.showLoadingDialog(this);
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPetList = (ArrayList<Pet>) response;
                AppUtils.hideDialog();
                updateFields();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
                updateFields();
            }
        });
    }

    public String getPetName(String petId) {
        for (Pet pet : mPetList) {
            if (pet.id.equals(petId)) {
                petAvatar = pet.avatar.url;
                this.pet = pet;
                return pet.name;
            }
        }
        petAvatar = "";
        return "";
    }

    public void sendReview(ReviewRequest request) {
        AppUtils.showLoadingDialog(this);
        mBusinessService.sendReviews(request, userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                if (hasNext()) {
                    AppUtils.hideDialog();
                    position = position + 1;
                    updateFields();
                } else {
                    AppUtils.hideDialog();
                    onBackPressed();
                }

            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();

            }
        });
    }


}
