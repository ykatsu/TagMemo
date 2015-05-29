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
 * メモのリスト表示用のアダプタで、メモのタイトル文字列Arrayを保持している
 *
 * Created by yoshi on 2015/05/08.
 */
public class ItemListAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        TextView mainText;

        public ViewHolder(View view) {
            this.mainText = (TextView) view.findViewById(R.id.main_text);
        }
    }

    private LayoutInflater mLayoutInflater;

    public ItemListAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        // ListViewに表示する分のレイアウトが生成されていない場合レイアウトを作成する
        if (convertView == null) {
            // レイアウトファイルからViewを生成する
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // リストアイテムに対応するデータを取得する
        Item item = getItem(position);
        holder.mainText.setText(item.memo.title);

        return convertView;
    }

}
