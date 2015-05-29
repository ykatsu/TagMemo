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

import com.activeandroid.query.Select;

import java.sql.Date;
import java.util.ArrayList;


public class MemoActivity extends ActionBarActivity {

    private Memo mMemo;

    private EditText mEtTitle;
    private EditText mEtText;
    private Button mCanButton;
    private Button mSaveButton;

    private boolean mIsAddMode;
    private long mOrgMemoId;
    private ArrayList<Long> mTagIdList;
    private long mOrgTagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // TODO:完成したら画面タイトルを消す
        setTitle("Memo編集画面");

        if(( savedInstanceState == null ) || ( savedInstanceState.isEmpty()) ) {
            // インテントから追加データを取り出す
            mOrgTagId = getIntent().getExtras().getLong(getString(R.string.param_tag_id));
            mOrgMemoId = getIntent().getExtras().getLong(getString(R.string.param_memo_id));

            if (mOrgMemoId == (long)getResources().getInteger(R.integer.memo_id_none)) {
                // 追加処理の場合
                mIsAddMode = true;
                mMemo = new Memo("", "");
            } else {
                // 編集処理の場合
                mIsAddMode = false;
                mMemo = new Select()
                        .from(Memo.class)
                        .where("id = ?", mOrgMemoId)
                        .executeSingle();
            }

            mEtTitle = (EditText)findViewById(R.id.memo_title);
            mEtText = (EditText)findViewById(R.id.memo_text);
            mCanButton = (Button)findViewById(R.id.btnCancel);
            mSaveButton = (Button)findViewById(R.id.btnSave);
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

    private void onClickSaveButtonListener() {
        //Log.v("Save!", "title:"+title+" text:"+text);
        // データ保存処理
        mMemo.title = mEtTitle.getText().toString();
        mMemo.text = mEtText.getText().toString();
        mMemo.updated_at = new Date(System.currentTimeMillis());
        mMemo.save();

        // TODO: Tagの作成処理が入ったら、正しいものを設定する
        if (mIsAddMode) {
            // この画面に来た時のタグ
            Tag tag = new Select()
                    .from(Tag.class)
                    .where("id = ?", mOrgTagId)
                    .executeSingle();

            Item item = new Item(mMemo, tag);
            item.save();
        }

        // 返すデータ(Intent&Bundle)の作成
        Intent data = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString(getString(R.string.param_title), mMemo.title);
//        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
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
