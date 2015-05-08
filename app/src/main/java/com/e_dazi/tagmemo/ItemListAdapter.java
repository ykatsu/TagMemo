package com.e_dazi.tagmemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for Main Item ListView
 *
 * Created by yoshi on 2015/05/08.
 */
public class ItemListAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;

    public ItemListAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view;

        // ListViewに表示する分のレイアウトが生成されていない場合レイアウトを作成する
        if (convertView == null) {
            // レイアウトファイルからViewを生成する
            view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
        } else {
            // レイアウトが存在する場合は再利用する
            view = convertView;
        }

        // リストアイテムに対応するデータを取得する
        String item = getItem(position);

        TextView mainText = (TextView) view.findViewById(R.id.main_text);
        mainText.setText(item);

        // 各Viewに表示する情報を設定
//        TextView text1 = (TextView) view.findViewById(R.id.TitleText);
//        text1.setText("Title:" + item);
//        TextView text2 = (TextView) view.findViewById(R.id.SubTitleText);
//        text2.setText("SubTitle:" + item);

        return view;
    }

}
