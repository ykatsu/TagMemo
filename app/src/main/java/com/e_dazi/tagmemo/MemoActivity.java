package com.e_dazi.tagmemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.query.Select;


public class MemoActivity extends ActionBarActivity {

    private Memo mMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        setTitle("MemoActivityタイトル");

        if(( savedInstanceState == null ) || ( savedInstanceState.isEmpty()) ) {
            // インテントから追加データを取り出す
            long memoId = getIntent().getExtras().getLong(getString(R.string.param_memo_id));
            mMemo = new Select()
                    .from(Memo.class)
                    .where("id = ?", memoId)
                    .executeSingle();
        }

        EditText etTitle = (EditText)findViewById(R.id.memo_title);
        etTitle.setText(mMemo.title);

        EditText etText = (EditText)findViewById(R.id.memo_text);
        etText.setText(mMemo.text);

        // ボタンの動作を定義する
        // キャンセルボタン
        Button canButton = (Button)findViewById(R.id.btnCancel);
        canButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
//                Intent data = new Intent();
//                setResult(RESULT_CANCELED, data);
                finish();
            }
        });

        // 保存ボタン
        Button saveButton = (Button)findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {

                TextView memoTitle = (TextView)findViewById(R.id.memo_title);
                String title = memoTitle.getText().toString();

                EditText memoText = (EditText)findViewById(R.id.memo_text);
                String text = memoText.getText().toString();

                //Log.v("Save!", "title:"+title+" text:"+text);

                // データ保存処理
                if (mMemo == null) {
                    mMemo = new Memo(title, text);
                    mMemo.save();
                    // TODO: Item, Tagの作成処理
                } else {
                    mMemo.title = title;
                    mMemo.text = text;
                    //mMemo.updated_at = new Date(System.currentTimeMillis());
                    mMemo.save();
                }

                // 返すデータ(Intent&Bundle)の作成
//                Intent data = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString(getString(R.string.param_title), "タイトルダミー");
//                data.putExtras(bundle);
//                setResult(RESULT_OK, data);

                finish();
            }
        });

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
