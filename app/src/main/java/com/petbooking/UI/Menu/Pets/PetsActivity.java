package com.petbooking.UI.Menu.Pets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.petbooking.API.Pet.Models.ListPetsResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.BaseActivity;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Menu.Pets.RegisterPet.RegisterPetActivity;

import java.util.ArrayList;
import java.util.List;

public class PetsActivity extends BaseActivity {

    private SessionManager mSessionManager;
    private PetService mPetService;
    private User currentUser;

    private RecyclerView mRvPets;
    private PetListAdapter mAdapter;
    private ArrayList<Pet> mPets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        mSessionManager = SessionManager.getInstance();
        mPetService = new PetService();
        currentUser = mSessionManager.getUserLogged();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.new_pet) {
            Intent newPetIntent = new Intent(this, RegisterPetActivity.class);
            startActivity(newPetIntent);
        }

        return true;
    }

    /**
     * List Pets
     *
     * @param userId
     */
    private void listPets(String userId) {
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                ListPetsResp pets = (ListPetsResp) response;
                for (ListPetsResp.Item pet : pets.data) {
                    mPets.add(new Pet(pet.id, pet.attributes.name, pet.attributes.description, pet.attributes.photo));
                }
                mAdapter.updateList(mPets);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

}
