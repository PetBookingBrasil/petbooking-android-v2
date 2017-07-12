package com.petbooking.UI.Menu.Search;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.petbooking.API.Business.APIBusinessConstants;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements FilterCategoryListAdapter.OnSelectCategoryListener {

    private BusinessService mBusinessService;

    private EditText mEdtSearchName;
    private Button mBtnFilter;

    private ArrayList<Category> mCategoryList;
    private RecyclerView mRvCategories;
    private FilterCategoryListAdapter mAdapter;
    private int currentCategory = -1;


    View.OnClickListener filterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("FILTER_TEXT", mEdtSearchName.getText().toString());
            resultIntent.putExtra("CATEGORY_POSITION", currentCategory);

            if (currentCategory != -1) {
                resultIntent.putExtra("CATEGORY_ID", mCategoryList.get(currentCategory).id);
                resultIntent.putExtra("CATEGORY_NAME", mCategoryList.get(currentCategory).categoryName);
            }

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBusinessService = new BusinessService();

        mCategoryList = new ArrayList<>();
        getCategories();

        mEdtSearchName = (EditText) findViewById(R.id.search_name);
        mBtnFilter = (Button) findViewById(R.id.filter_button);

        mRvCategories = (RecyclerView) findViewById(R.id.category_list);
        mRvCategories.setHasFixedSize(true);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

        mAdapter = new FilterCategoryListAdapter(this, mCategoryList);
        mAdapter.setOnSelectCategoryListener(this);
        mRvCategories.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvCategories.setAdapter(mAdapter);
        }

        mBtnFilter.setOnClickListener(filterListener);
    }

    /**
     * Get Categories
     */
    public void getCategories() {
        mBusinessService.listCategories(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CategoryResp resp = (CategoryResp) response;
                for (CategoryResp.Item item : resp.data) {
                    mCategoryList.add(APIUtils.parseCategory(SearchActivity.this, item));
                }

                mAdapter.updateList(mCategoryList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    @Override
    public void onSelect(int position) {
        currentCategory = position;
    }
}
