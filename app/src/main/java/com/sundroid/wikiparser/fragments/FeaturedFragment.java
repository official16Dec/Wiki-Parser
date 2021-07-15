package com.sundroid.wikiparser.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.sundroid.wikiparser.adapters.FeaturedImageAdapter;
import com.sundroid.wikiparser.models.FeaturedImage;
import com.sundroid.wikiparser.models.ImageInfo;
import com.sundroid.wikiparser.utils.BaseApiService2;
import com.sundroid.wikiparser.utils.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeaturedFragment extends Fragment {
    @BindView(R.id.featured_recycler) RecyclerView featured_recycler;
    @BindView(R.id.no_data) LinearLayout no_data;
    @BindView(R.id.continue_btn) Button continue_btn;
    BaseApiService2 service;
    DatabaseHandler db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);

        ButterKnife.bind(this, view);
        db = new DatabaseHandler(getActivity());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://commons.wikimedia.org/w/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(BaseApiService2.class);

        if(db.getFeaturedImages().size()>0) {
            no_data.setVisibility(View.GONE);
            setDataInFeaturedRecycler(db.getFeaturedImages());
        }else{
            if(isNetworkConnected()){
                getFeaturedImages();
            }
            no_data.setVisibility(View.VISIBLE);
        }

        continue_btn.setOnClickListener(v -> {
            if(isNetworkConnected()){
                no_data.setVisibility(View.GONE);
                getFeaturedImages();
            }else{
                no_data.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void setDataInFeaturedRecycler(List<FeaturedImage> flist){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        featured_recycler.setLayoutManager(layoutManager);
        FeaturedImageAdapter adapter=new FeaturedImageAdapter(getActivity(), flist);
        featured_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getFeaturedImages(){
        Log.d("Function", "Called");
        service.getFeaturedImages("query",
                "imageinfo",
                "timestamp|user|url",
                "categorymembers",
                "file",
                "Category:Featured_pictures_on_Wikimedia_Commons",
                "json",
                "").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("Response", "Successful");
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        JSONObject query = result.getJSONObject("query");
                        JSONObject pages = query.getJSONObject("pages");
                        JSONArray jsonArray = pages.toJSONArray(pages.names());
                        Log.d("Pages", jsonArray.toString());
                        List<FeaturedImage> flist = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            FeaturedImage fi = new FeaturedImage();
                            fi.setPageid(jsonArray.getJSONObject(i).getString("pageid"));
                            fi.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            List<ImageInfo> ilist = new ArrayList<>();
                            JSONArray imageInfo = jsonArray.getJSONObject(i).getJSONArray("imageinfo");
                            for(int j = 0; j < imageInfo.length(); j++){
                                ImageInfo ii = new ImageInfo();
                                ii.setUrl(imageInfo.getJSONObject(j).getString("url"));
                                ilist.add(ii);
                            }
                            fi.setImageinfo(ilist);
                            if(db.getFeatured(fi.getPageid())==null){
                                db.addFeatured(fi.getPageid(), fi.getTitle(), fi.getImageinfo().get(0).getUrl());
                            }
                            flist.add(fi);
                        }

                        if(db.getFeaturedImages().size()>0) {
                            no_data.setVisibility(View.GONE);
                            setDataInFeaturedRecycler(db.getFeaturedImages());
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
