package com.e_dazi.tagmemo;

import android.util.Log;

import com.activeandroid.query.Delete;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Main Application
 *
 * Created by yoshi on 2015/05/08.
 */

public class MainApplication extends com.activeandroid.app.Application {

    private final String TAG = "MainApplication";
    private ArrayList<Tag> gDrawerTagList;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "--- onCreate() in ---");

        gDrawerTagList = null;
        InitDB();
    }

    private void InitDB() {

//        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
//        configurationBuilder.addModelClasses(Memo.class);
//        configurationBuilder.addModelClasses(Tag.class);
//        configurationBuilder.addModelClasses(Item.class);
//        ActiveAndroid.initialize(configurationBuilder.create());

//        ActiveAndroid.initialize(this);

        // TODO: テストデータ作成処理。後で消してね
        new Delete().from(Item.class).execute();
        new Delete().from(Memo.class).execute();
        new Delete().from(Tag.class).execute();

        Memo memo = new Memo();
        memo.title = "テストタイトル";
        memo.text  = "テスト本文";
        memo.created_at = new Date(System.currentTimeMillis());
        memo.updated_at = memo.created_at;
        memo.save();

        Memo memo2 = new Memo("テストタイトル2", "テスト本文2");
        memo2.save();
        Memo memo3 = new Memo("テストタイトル3", "テスト本文3");
        memo3.save();
        Memo memo13 = new Memo("テストタイトル13", "テスト本文13");
        memo13.save();

        Tag tag1 = new Tag();
        tag1.name = "タグ１";
        tag1.save();

        Tag tag2 = new Tag("タグ２");
        tag2.save();
        Tag tag3 = new Tag("タグ３");
        tag3.save();

        Item item = new Item();
        item.memo = memo;
        item.tag  = tag1;
        item.save();

        Item item2 = new Item(memo2, tag2);
        item2.save();
        Item item3 = new Item(memo3, tag3);
        item3.save();
        Item item131 = new Item(memo13, tag1);
        item131.save();
        Item item133 = new Item(memo13, tag3);
        item133.save();
    }

//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        ActiveAndroid.dispose();
//    }

    public ArrayList<Tag> getDrawerTagList() {
        return this.gDrawerTagList;
    }

    public void setDrawerTagList(ArrayList<Tag> list) {
        this.gDrawerTagList = list;
    }
}
