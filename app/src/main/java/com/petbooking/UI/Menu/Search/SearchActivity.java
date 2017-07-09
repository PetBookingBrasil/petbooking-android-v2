package com.petbooking.UI.Menu.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.petbooking.API.Business.APIBusinessConstants;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText mEdtSearchName;

    private ArrayList<Category> mCategoryList;
    private RecyclerView mRvCategories;
    private FilterCategoryListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCategoryList = new ArrayList<>();
        mCategoryList = AppUtils.getCategoryList();

        mEdtSearchName = (EditText) findViewById(R.id.search_name);

        mRvCategories = (RecyclerView) findViewById(R.id.category_list);
        mRvCategories.setHasFixedSize(true);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        mAdapter = new FilterCategoryListAdapter(this, mCategoryList);
        mRvCategories.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvCategories.setAdapter(mAdapter);
        }
    }
}
