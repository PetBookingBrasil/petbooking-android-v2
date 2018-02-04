package com.petbooking.UI.Dashboard.Business.Scheduling;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.R;

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
    @OnClick(R.id.button_send_review) void sendReview(){
        if(hasNext()){
            updateFields();
        }else{
            //Send Review
            float attendance = ratingBarAttendance.getRating();
            float bussisnes = ratingBarBussines.getRating();
            float enviroment = ratingBarEnvironment.getRating();
            String comment = editComment.getText().toString();
        }
    }
    int position = 0;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_schedule);
        ButterKnife.bind(this);
    }

    private void getReviews(){

    }

    private void updateFields(){

        if(quantity == 1){
            quantBussines.setVisibility(View.INVISIBLE);
        }
        if(position < quantity){
            send.setText(R.string.send_and_next);
        }else {
            send.setText(R.string.sendReview);
        }

        professionalName.setText("Professional name");
        categoryName.setText("Category name");
        petName.setText("Pet name");
        bussinesName.setText("Bussines Service");
    }

    private boolean hasNext(){
        if((position +1) > quantity){
            return false;
        }
        return true;
    }


}
