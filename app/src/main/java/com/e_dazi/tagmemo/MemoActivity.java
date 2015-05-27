package com.e_dazi.tagmemo;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MemoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        setTitle("MemoActivityタイトル");

        String str;
        if(( savedInstanceState == null ) || ( savedInstanceState.isEmpty()) )
        {
            // インテントから追加データを取り出す
            str = getIntent().getExtras().getString( "INTENT_PARAM" );
        }
        else {
            str = "";
        }

        TextView text = (TextView)findViewById(R.id.memo_title);
        text.setText(str);

        // ボタンの動作を定義する
        // キャンセルボタン
        Button canButton = (Button)findViewById(R.id.btnCancel);
        canButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
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
