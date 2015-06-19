package com.e_dazi.tagmemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collections;
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

    public ItemListAdapter(Context context, List<Item> objects, long tagId) {
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        refresh(tagId);
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

    /**
     * Load and sort items from DB.
     * @param tagId tagId of data to load.
     */
    public void refresh(long tagId) {
        ArrayList<Item> list = new ArrayList<>(
                new Select()
                        .from(Item.class)
                        .where("Tag = ?", tagId)
                                //.orderBy("updated_at DESC")
                        .<Item>execute()
        );

        // sort by memo.update_at DESC
        Collections.sort(list, new ItemComparator());

        clear();
        addAll(list);
    }

}
