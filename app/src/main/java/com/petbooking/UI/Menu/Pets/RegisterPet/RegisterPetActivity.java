package com.petbooking.UI.Menu.Pets.RegisterPet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.R;
import com.petbooking.UI.Widget.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;


public class RegisterPetActivity extends AppCompatActivity {

    private PetService mPetService;

    private List<String> dogBreeds;
    private List<String> catBreeds;

    private MaterialSpinner mSpGender;
    private MaterialSpinner mSpType;
    private MaterialSpinner mSpSize;
    private MaterialSpinner mSpBreed;

    AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mSpBreed.setItems(dogBreeds);
                mSpSize.setItems(R.array.size_dog_array);
            } else {
                mSpBreed.setItems(catBreeds);
                mSpSize.setItems(R.array.size_cat_array);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener infoSizeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("DIALOG", "SIZE DIALOG");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_form);

        mPetService = new PetService();

        mSpGender = (MaterialSpinner) findViewById(R.id.pet_gender);
        mSpType = (MaterialSpinner) findViewById(R.id.pet_type);
        mSpSize = (MaterialSpinner) findViewById(R.id.pet_size);
        mSpBreed = (MaterialSpinner) findViewById(R.id.pet_breed);

        mSpType.setOnItemSelectedListener(typeListener);
        mSpSize.setOnInfoClickListener(infoSizeListener);

        dogBreeds = new ArrayList<>();
        catBreeds = new ArrayList<>();

        listDogBreeds();
        listCatBreeds();
    }

    /**
     * Init Spinners Option
     */
    public void initSpinners() {
       /* setItems(R.array.gender_array, mSpGender);
        setItems(R.array.type_array, mSpType);
        setItems(R.array.size_dog_array, mSpSize);*/
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
                    dogBreeds.add(breed.attributes.name);
                    //dogBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
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
                    catBreeds.add(breed.attributes.name);
                    //catBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

}
