package com.sundroid.wikiparser.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.adapters.CategoryAdapter;
import com.sundroid.wikiparser.models.CategoryModel;
import com.sundroid.wikiparser.utils.DatabaseHandler;
import com.sundroid.wikiparser.utils.UtilsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {
    @BindView(R.id.category_recycler) RecyclerView category_recycler;
    @BindView(R.id.no_data) LinearLayout no_data;
    @BindView(R.id.continue_btn) Button continue_btn;
    DatabaseHandler db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        db = new DatabaseHandler(getActivity());
        if(db.getAllCategories().size()>0) {
            no_data.setVisibility(View.GONE);
            setDataInCategoryRecycler(db.getAllCategories());
            getCategories();
        }else{
            if(isNetworkConnected()){
                getCategories();
            }
            no_data.setVisibility(View.VISIBLE);
        }

        continue_btn.setOnClickListener(v -> {
            if(isNetworkConnected()){
                no_data.setVisibility(View.GONE);
                getCategories();
            }else{
                no_data.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    CategoryAdapter adapter;
    private void setDataInCategoryRecycler(List<CategoryModel> clist){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        category_recycler.setLayoutManager(layoutManager);
        adapter=new CategoryAdapter(getActivity(), clist);
        category_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getCategories(){
        UtilsApi.getAPIService().getCategories("json",
                "query",
                "allcategories",
                "List of",
                "2").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        JSONObject query = result.getJSONObject("query");
                        JSONArray allCategories = query.getJSONArray("allcategories");
                        List<CategoryModel> clist = new ArrayList<>();
                        for(int i = 0; i < allCategories.length(); i++){
                            CategoryModel cm = new CategoryModel();
                            cm.setCategoryTitle(allCategories.getJSONObject(i).getString("category"));
                            if(db.getCategory(cm.getCategoryTitle().replaceAll("[^a-zA-Z0-9]", " "))==null){
                                db.addCategory(cm);
                            }
                            clist.add(cm);
                        }
                        if(db.getAllCategories().size()>0) {
                            no_data.setVisibility(View.GONE);
                            setDataInCategoryRecycler(db.getAllCategories());
                        }else{
                            no_data.setVisibility(View.VISIBLE);
                        }
                    } catch(IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
