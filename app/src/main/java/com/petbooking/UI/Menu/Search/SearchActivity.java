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
        initCategoryList();

        mEdtSearchName = (EditText) findViewById(R.id.search_name);

        mRvCategories = (RecyclerView) findViewById(R.id.category_list);
        mRvCategories.setHasFixedSize(true);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        mAdapter = new FilterCategoryListAdapter(mCategoryList);
        mRvCategories.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvCategories.setAdapter(mAdapter);
        }
    }

    private void initCategoryList() {
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_CLINIC, R.drawable.ic_category_clinic));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_TRAINING, R.drawable.ic_category_trainer));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_BATH, R.drawable.ic_category_bath));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_TRANSPORT, R.drawable.ic_category_transport));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_WALKER, R.drawable.ic_category_walker));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_DAYCARE, R.drawable.ic_category_daycare));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_HOTEL, R.drawable.ic_category_hotel));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_EMERGENCY, R.drawable.ic_category_emergency));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_EXAMS, R.drawable.ic_category_exam));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_HOSPITAL, R.drawable.ic_category_hospital));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_DIAGNOSIS, R.drawable.ic_category_diagnosis));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_PETSHOP, R.drawable.ic_category_petshop));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_PETSHOP_MOVEL, R.drawable.ic_category_petshop_delivery));
        mCategoryList.add(new Category("1", "Outros", R.drawable.ic_category_other));
    }
}
