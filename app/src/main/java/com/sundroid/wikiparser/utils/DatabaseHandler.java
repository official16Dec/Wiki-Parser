package com.sundroid.wikiparser.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sundroid.wikiparser.models.CategoryModel;
import com.sundroid.wikiparser.models.Content;
import com.sundroid.wikiparser.models.ContentText;
import com.sundroid.wikiparser.models.FeaturedImage;
import com.sundroid.wikiparser.models.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wikiDb";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_FEATURED = "featured";
    private static final String TABLE_ARTICLE = "article";

    private static final String CATEGORY_TITLE = "category_title";
    private static final String PAGE_ID = "page_id";
    private static final String FEATURED_TITLE = "featured_title";
    private static final String FEATURED_IMAGE = "featured_image";
    private static final String CONTENT_TITLE = "content_title";
    private static final String CONTENT_TEXT = "content_text";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
//
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATE_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("+ CATEGORY_TITLE + " TEXT " + ")";
        db.execSQL(CREATE_CATE_TABLE);

        String CREATE_FEATURED_TABLE = "CREATE TABLE " + TABLE_FEATURED + "("+ PAGE_ID + " TEXT, "+ FEATURED_TITLE + " TEXT, " + FEATURED_IMAGE + " TEXT " + ")";
        db.execSQL(CREATE_FEATURED_TABLE);

        String CREATE_ARTICLE_TABLE = "CREATE TABLE " + TABLE_ARTICLE + "("+ PAGE_ID + " TEXT, "+ CONTENT_TITLE + " TEXT, " + CONTENT_TEXT + " TEXT " + ")";
        db.execSQL(CREATE_ARTICLE_TABLE);
    }
//
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        // Create tables again
        onCreate(db);
    }

    public void addCategory(CategoryModel cm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_TITLE, cm.getCategoryTitle().replaceAll("[^a-zA-Z0-9]", " "));
        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addFeatured(String pageId, String featuredTitle, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAGE_ID, pageId);
        values.put(FEATURED_TITLE, featuredTitle);
        values.put(FEATURED_IMAGE, imageUrl);
        // Inserting Row
        db.insert(TABLE_FEATURED, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addArticle(String pageId, String contentTitle, String contentText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAGE_ID, pageId);
        values.put(CONTENT_TITLE, contentTitle);
        values.put(CONTENT_TEXT, contentText);
        // Inserting Row
        db.insert(TABLE_ARTICLE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public CategoryModel getCategory(String categoryTitle){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CATEGORY+" WHERE "+CATEGORY_TITLE+" = '"+categoryTitle+"'", null);
        if (cursor.moveToFirst()){
            CategoryModel cm = new CategoryModel();
            cursor.moveToFirst();
            cm.setCategoryTitle(cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE)));
            return cm;
        }
        return null;
    }


    public FeaturedImage getFeatured(String pageId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_FEATURED+" WHERE "+PAGE_ID+" = '"+pageId+"'", null);
        if (cursor.moveToFirst()){
            FeaturedImage fi = new FeaturedImage();
            cursor.moveToFirst();
            fi.setPageid(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
            fi.setTitle(cursor.getString(cursor.getColumnIndex(FEATURED_TITLE)));
            List<ImageInfo> infolist = new ArrayList<>();
            ImageInfo info = new ImageInfo();
            info.setUrl(cursor.getString(cursor.getColumnIndex(FEATURED_IMAGE)));
            infolist.add(info);
            fi.setImageinfo(infolist);
            return fi;
        }
        return null;
    }

    public List<CategoryModel> getAllCategories(){
        List<CategoryModel> catList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                CategoryModel mm = new CategoryModel();
                mm.setCategoryTitle(cursor.getString(cursor.getColumnIndex(CATEGORY_TITLE)));
                catList.add(mm);
                cursor.moveToNext();
            }
            return catList;
        }
        return catList;
    }

    public List<FeaturedImage> getFeaturedImages(){
        List<FeaturedImage> featList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FEATURED;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                FeaturedImage fi = new FeaturedImage();
                fi.setPageid(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
                fi.setTitle(cursor.getString(cursor.getColumnIndex(FEATURED_TITLE)));
                List<ImageInfo> infolist = new ArrayList<>();
                ImageInfo info = new ImageInfo();
                info.setUrl(cursor.getString(cursor.getColumnIndex(FEATURED_IMAGE)));
                infolist.add(info);
                fi.setImageinfo(infolist);
                featList.add(fi);
                cursor.moveToNext();
            }
            return featList;
        }
        return featList;
    }


    public Content getArticle(String pageId){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_ARTICLE+" WHERE "+PAGE_ID+" = '"+pageId+"'", null);
        if (cursor.moveToFirst()){
            Content cnt = new Content();
            cursor.moveToFirst();
            cnt.setPageid(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
            cnt.setTitle(cursor.getString(cursor.getColumnIndex(CONTENT_TITLE)));
            List<ContentText> revisions = new ArrayList<>();
            ContentText ct = new ContentText();
            ct.setContentText(cursor.getString(cursor.getColumnIndex(CONTENT_TEXT)));
            revisions.add(ct);
            cnt.setRevisions(revisions);
            return cnt;
        }
        return null;
    }

    public List<Content> getAllArticles(){
        List<Content> cnList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Content cnt = new Content();
                cnt.setPageid(cursor.getString(cursor.getColumnIndex(PAGE_ID)));
                cnt.setTitle(cursor.getString(cursor.getColumnIndex(CONTENT_TITLE)));
                List<ContentText> revisions = new ArrayList<>();
                ContentText ct = new ContentText();
                ct.setContentText(cursor.getString(cursor.getColumnIndex(CONTENT_TEXT)));
                revisions.add(ct);
                cnt.setRevisions(revisions);
                cnList.add(cnt);
                cursor.moveToNext();
            }
            return cnList;
        }
        return cnList;
    }

    public void deleteCategory(CategoryModel im) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, CATEGORY_TITLE+" = ?",
                new String[] {String.valueOf(im.getCategoryTitle()) });
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CATEGORY);
//        db.execSQL("delete from "+ TABLE_CART);
    }

}
