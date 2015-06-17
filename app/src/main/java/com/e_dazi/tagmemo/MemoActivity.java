package com.e_dazi.tagmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MemoActivity extends ActionBarActivity {

    private Memo mMemo;         // この画面のMemoオブジェクト

    // 画面上の各View
    private EditText mEtTitle;
    private EditText mEtText;
    private EditText mEtTag;
    private Button mCanButton;
    private Button mSaveButton;

    private boolean mIsAddMode; // 新規画面追加モードの時true
    private long mOrgMemoId;    // -1:新規 else:編集するMemoのID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // TODO:完成したら画面タイトルを消す
        setTitle("Memo編集画面");

        // TODO: savedInstanceState 関連の処理が必要

        String tagStrings;
        if(( savedInstanceState == null ) || ( savedInstanceState.isEmpty()) ) {
            // インテントから追加データを取り出す
            mOrgMemoId = getIntent().getExtras().getLong(getString(R.string.param_memo_id));

            if (mOrgMemoId == (long)getResources().getInteger(R.integer.memo_id_none)) {
                // 新規追加処理の場合
                // 空のMemoオブジェクトを作成し
                // 追加された時のTagを設定する
                mIsAddMode = true;
                mMemo = new Memo("", "");

                // 入力されたTagIdから表示用文字列を設定します
                long orgTagId = getIntent().getExtras().getLong(getString(R.string.param_tag_id));
                Tag tag = new Select()
                            .from(Tag.class)
                            .where("id = ?", orgTagId)
                            .executeSingle();
                tagStrings = tag.name;
            } else {
                // 編集処理の場合
                // MemoオブジェクトをDBから取得し
                // メモに関連付けられたTagを取得、設定する
                mIsAddMode = false;
                mMemo = new Select()
                        .from(Memo.class)
                        .where("id = ?", mOrgMemoId)
                        .executeSingle();

                // メモに関連付けられたタグを取得します
                ArrayList<Tag> orgTags = getTagsFromDB(mOrgMemoId);

                // タグをスペースで連結し、文字列化
                StringBuilder sb = new StringBuilder();
                for(Tag tag: orgTags) {
                    sb.append(tag.name).append(" ");
                }
                if (sb.length() > 0) {
                    sb.delete(sb.length()-1,sb.length());
                }
                tagStrings = sb.toString();
            }

            // 各種Viewの取得
            findViews();
            mEtTag.setText(tagStrings);
        }

        mEtTitle.setText(mMemo.title);
        mEtText.setText(mMemo.text);

        // ボタンの動作を定義する
        // キャンセルボタン
        mCanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
            }
        });

        // 保存ボタン
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onClickSaveButtonListener();

            }
        });

    }

    private ArrayList<Tag> getTagsFromDB(long memoId) {
        ArrayList<Item> itemList = new ArrayList<>(
            new Select()
                    .from(Item.class)
                    .where("Memo = ?", memoId)
                    .<Item>execute()
        );

        ArrayList<Tag> tagList = new ArrayList<>();
        for (Item item : itemList) {
            tagList.add(item.tag);
        }

        return tagList;
    }

    /**
     * 各種Viewの取得
     */
    private void findViews() {
        mEtTitle = (EditText)findViewById(R.id.memo_title);
        mEtText = (EditText)findViewById(R.id.memo_text);
        mEtTag = (EditText)findViewById(R.id.memo_tag);
        mCanButton = (Button)findViewById(R.id.btnCancel);
        mSaveButton = (Button)findViewById(R.id.btnSave);
    }

    private void onClickSaveButtonListener() {
        // 各種データの作成＆削除を行う
        // TODO: トランザクション化の必要がある？
        // Memoデータを保存
        // Tagデータの差分を取り
        // Tagデータの追加
        // Itemデータの追加＆削除

        // Memoデータを保存
        mMemo.title = mEtTitle.getText().toString();
        mMemo.text = mEtText.getText().toString();
        mMemo.updated_at = new Date(System.currentTimeMillis());
        mMemo.save();

        // Tagデータの作成処理 開始

        // 画面に設定されたTagから、タグ名の配列を作成
        String tagStrings = mEtTag.getText().toString();
        String[] newTagNameArr = tagStrings.split(" |　", 10);
        String[] incTagNameArr = newTagNameArr;     // 新規に追加するタグの配列

        // 編集前のTagの取得し
        // 削除されたタグのデータを削除する
        // 新規追加されたタグのデータを設定する
        // (編集処理時のみ）
        if (!mIsAddMode) {
            // メモに関連付けられたタグを取得
            ArrayList<Tag> orgTags = getTagsFromDB(mOrgMemoId);
            Tag[] oldTagArr = orgTags.toArray(new Tag[orgTags.size()]);
            String[] oldTagNameArr = new String[orgTags.size()];
            int i = 0;
            for (Tag tag: orgTags) {
                oldTagNameArr[i++] = tag.name;
            }

            // Itemから削除するTag
            String[] decTagArr = extractDecFactor(oldTagNameArr, newTagNameArr);
            for (String tagName : decTagArr) {
                // タグ名からtagIdを取得し、Itemレコードを削除する
                long tagId = -1;
                for (Tag tag : oldTagArr) {
                    if (tag.name.equals(tagName)) {
                        tagId = tag.getId();
                        break;
                    }
                }

                new Delete().from(Item.class).where("Memo = ? and Tag = ?", mMemo.getId(), tagId).execute();
            }

            // 新規に追加するタグの配列
            incTagNameArr = extractIncFactor(oldTagNameArr, newTagNameArr);
        }

        // Item, Tagデータの追加処理
        boolean doRefrash = false;
        for (String tagName : incTagNameArr) {
            // Tagレコードを取得し、存在しなければ新規に追加
            Tag tag = new Select()
                    .from(Tag.class)
                    .where("name = ?", tagName)
                    .executeSingle();

            if (tag == null) {
                tag = new Tag(tagName);
                tag.save();

                // ドロワーへ、タグリスト変更を通知
                doRefrash = true;
            }

            Item item = new Item(mMemo, tag);
            item.save();
        }

        // Tagデータの作成処理 終了

        // 返すデータ(Intent&Bundle)の作成
        Intent data = new Intent();
        Bundle bundle = new Bundle();
//        bundle.putString(getString(R.string.param_title), mMemo.title);
        bundle.putBoolean(getString(R.string.param_doRefreshDrawer), doRefrash);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * arr1に無くて、arr2にある要素を返す
     * @param arr1 array1
     * @param arr2 array2
     * @return extracted factor.
     */
    private String[] extractIncFactor(String[] arr1, String[] arr2) {
        ArrayList<String> incList = new ArrayList<>();
        List<String> univList = Arrays.asList(arr1);
        for (String s : arr2) {
            if (!univList.contains(s)) {
                incList.add(s);
            }
        }

        return incList.toArray(new String[incList.size()]);
    }

    /**
     * arr1にあって、arr2に無い要素を返す
     * @param arr1 array1
     * @param arr2 array2
     * @return extracted factor.
     */
    private String[] extractDecFactor(String[] arr1, String[] arr2) {
        ArrayList<String> decList = new ArrayList<>();
        List<String> univList = Arrays.asList(arr2);
        for (String s : arr1) {
            if (!univList.contains(s)) {
                decList.add(s);
            }
        }

        return decList.toArray(new String[decList.size()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
