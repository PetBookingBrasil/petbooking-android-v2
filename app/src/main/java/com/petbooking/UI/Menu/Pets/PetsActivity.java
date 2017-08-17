package com.petbooking.UI.Menu.Pets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.petbooking.API.Pet.Models.ListPetsResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.App;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dialogs.ConfirmDialogFragment;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Menu.Pets.RegisterPet.RegisterPetActivity;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class PetsActivity extends BaseActivity implements
        PetListAdapter.AdapterInterface,
        ConfirmDialogFragment.FinishDialogListener,
        FeedbackDialogFragment.FinishDialogListener {

    private SessionManager mSessionManager;
    private PetService mPetService;
    private User currentUser;
    private String petId;

    private View mPetsPlaceholder;
    private RecyclerView mRvPets;
    private PetListAdapter mAdapter;
    private ArrayList<Pet> mPets;

    private ConfirmDialogFragment mConfirmDialogFragment;
    private FeedbackDialogFragment mFeedbackDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        mSessionManager = SessionManager.getInstance();
        mPetService = new PetService();
        currentUser = mSessionManager.getUserLogged();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        mConfirmDialogFragment = ConfirmDialogFragment.newInstance();
        mFeedbackDialogFragment = FeedbackDialogFragment.newInstance();

        mPetsPlaceholder = findViewById(R.id.pets_placeholder);
        mRvPets = (RecyclerView) findViewById(R.id.pet_list);
        mRvPets.setHasFixedSize(true);

        mPets = new ArrayList<>();
        mAdapter = new PetListAdapter(this, mPets);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (mAdapter != null) {
            mRvPets.setAdapter(mAdapter);
        }

        mRvPets.setLayoutManager(mLayoutManager);
        mRvPets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        listPets(currentUser.id);
    }


    @Override
    protected void onResume() {
        super.onResume();
        listPets(currentUser.id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_pet) {
            Intent newPetIntent = new Intent(this, RegisterPetActivity.class);
            startActivity(newPetIntent);
        } else if (id == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    /**
     * List Pets
     *
     * @param userId
     */
    private void listPets(String userId) {
        AppUtils.showLoadingDialog(this);
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPets = (ArrayList<Pet>) response;
                if (mPets.size() == 0) {
                    mPetsPlaceholder.setVisibility(View.VISIBLE);
                    mRvPets.setVisibility(View.GONE);
                } else {
                    mPetsPlaceholder.setVisibility(View.GONE);
                    mRvPets.setVisibility(View.VISIBLE);
                    mAdapter.updateList(mPets);
                    mAdapter.notifyDataSetChanged();
                }

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Remove Pet
     *
     * @param userId
     * @param petId
     */
    public void removePet(String userId, String petId) {
        AppUtils.showLoadingDialog(this);
        mPetService.removePet(userId, petId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AppUtils.hideDialog();
                mFeedbackDialogFragment.setDialogInfo(R.string.remove_pet_title, R.string.success_remove_pet,
                        R.string.dialog_button_ok, AppConstants.OK_ACTION);
                mFeedbackDialogFragment.show(getSupportFragmentManager(), "FEEDBACK");
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    @Override
    public void onRemoveClick(String petId) {
        this.petId = petId;
        mConfirmDialogFragment.setDialogInfo(R.string.remove_pet_title, R.string.remove_pet_text, R.string.confirm_remove_pet);
        mConfirmDialogFragment.show(getSupportFragmentManager(), "REMOVE_PET");
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.CONFIRM_ACTION) {
            removePet(currentUser.id, this.petId);
        } else if (action == AppConstants.OK_ACTION) {
            listPets(currentUser.id);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
