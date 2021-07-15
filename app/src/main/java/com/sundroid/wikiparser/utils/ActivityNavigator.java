package com.sundroid.wikiparser.utils;

import android.content.Context;
import android.content.Intent;

import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.activities.MainActivity;

import java.net.URL;

public class ActivityNavigator {
    public void setNavigate(Context mContext, int id) {
        Intent intent;
        URL uriUrl;
        SharedPrefManager sharedPrefManager = new SharedPrefManager(mContext);
        switch (id) {
            case R.id.nav_home:
                intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pos", "0");
                mContext.startActivity(intent);
                break;
//            case R.id.nav_orders:
//                intent = new Intent(mContext, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("pos", "1");
//                mContext.startActivity(intent);
//                break;
        }
    }
}
