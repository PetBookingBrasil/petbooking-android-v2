package com.petbooking.UI.Menu.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SearchActivity extends AppCompatActivity {

    private BusinessService mBusinessService;

    private EditText mEdtSearchName;

    private ArrayList<Category> mCategoryList;
    private RecyclerView mRvCategories;
    private FilterCategoryListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mBusinessService = new BusinessService();

        mCategoryList = new ArrayList<>();
        getCategories();

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
}
