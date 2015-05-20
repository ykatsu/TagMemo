package com.e_dazi.tagmemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Date;
import java.util.List;

/**
 * Memo data's model.
 *
 * Created by yoshi on 2015/05/11.
 */

@Table(name = "Memos")
public class Memo extends Model {
    @Column(name = "Title")
    public String title;

    @Column(name = "Text")
    public String text;

    @Column(name = "created_at")
    public Date created_at;

    @Column(name = "updated_at")
    public Date updated_at;

    public Memo(){
        super();
    }
    public Memo(String title, String text){
        super();
        this.title = title;
        this.text = text;
        this.created_at = new Date(System.currentTimeMillis());
        this.updated_at = this.created_at;
    }

    public List<Item> items() {
        return getMany(Item.class, "memo");
    }
}
