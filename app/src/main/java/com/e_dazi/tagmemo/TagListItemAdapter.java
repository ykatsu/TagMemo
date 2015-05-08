package com.e_dazi.tagmemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for Drawer ListView.
 *
 * Created by yoshi on 2015/04/21.
 */
public class TagListItemAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;

    public TagListItemAdapter(Context context, String[] objects) {
        // 第2引数はtextViewResourceIdとされていますが、カスタムリストアイテムを使用する場合は特に意識する必要のない引数です
        super(context, 0, objects);
        // レイアウト生成に使用するインフレーター
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view;

        // ListViewに表示する分のレイアウトが生成されていない場合レイアウトを作成する
        if (convertView == null) {
            // レイアウトファイルからViewを生成する
            view = mLayoutInflater.inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        } else {
            // レイアウトが存在する場合は再利用する
            view = convertView;
        }

        // リストアイテムに対応するデータを取得する
        String item = getItem(position);

        // 各Viewに表示する情報を設定
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setText("Tag" + item);

        return view;
    }
}

