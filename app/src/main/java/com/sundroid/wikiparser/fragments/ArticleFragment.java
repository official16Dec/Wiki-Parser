package com.sundroid.wikiparser.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.adapters.ArticleAdapter;
import com.sundroid.wikiparser.models.Content;
import com.sundroid.wikiparser.models.ContentText;
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
import info.bliki.wiki.model.WikiModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleFragment extends Fragment {
    @BindView(R.id.article_recycler) RecyclerView article_recycler;
    DatabaseHandler db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        ButterKnife.bind(this, view);
        db = new DatabaseHandler(getActivity());

        if(db.getAllArticles().size()>0){
            setDataInArticleRecycler(db.getAllArticles());
            getArticles();
        }else{
            if(isNetworkConnected()){
                getArticles();
            }
        }

        return view;
    }

    private void setDataInArticleRecycler(List<Content> clist){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        article_recycler.setLayoutManager(layoutManager);
        ArticleAdapter adapter=new ArticleAdapter(getActivity(), clist);
        article_recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getArticles(){
        UtilsApi.getAPIService().getArticles("json",
                "query",
                "random",
                "0",
                "revisions|images",
                "content",
                "10").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        JSONObject query = result.getJSONObject("query");
                        JSONObject pages = query.getJSONObject("pages");
                        JSONArray jsonArray = pages.toJSONArray(pages.names());
                        List<Content> flist = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            Content cnt = new Content();
                            cnt.setPageid(jsonArray.getJSONObject(i).getString("pageid"));
                            cnt.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            List<ContentText> ilist = new ArrayList<>();
                            JSONArray info = jsonArray.getJSONObject(i).getJSONArray("revisions");
                            for(int j = 0; j < info.length(); j++){
                                ContentText ct = new ContentText();
                                ct.setContentText(getHtmlTextFromWikiText(info.getJSONObject(j).getString("*")));
                                ilist.add(ct);
                            }
                            cnt.setRevisions(ilist);
                            if(db.getArticle(cnt.getPageid())==null){
                                db.addArticle(cnt.getPageid(), cnt.getTitle(), cnt.getRevisions().get(0).getContentText());
                            }
                            flist.add(cnt);
                        }
                        setDataInArticleRecycler(db.getAllArticles());
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

    private String getHtmlTextFromWikiText(String wikiText){
        return WikiModel.toHtml(wikiText);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
