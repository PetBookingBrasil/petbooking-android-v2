package com.petbooking.UI.Menu.Pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.petbooking.BaseActivity;
import com.petbooking.R;
import com.petbooking.UI.Menu.Pets.RegisterPet.RegisterPetActivity;

public class PetsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
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

}
