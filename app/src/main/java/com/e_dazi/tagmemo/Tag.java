package com.e_dazi.tagmemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Tag model
 *
 * Created by yoshi on 2015/05/11.
 */

@Table(name = "Tags")
public class Tag extends Model {
    @Column(name = "Name", notNull = true, unique = true)
    public String name;

    public Tag(){
        super();
    }
    public Tag(String name){
        super();
        this.name = name;
    }

    public List<Item> items() {
        return getMany(Item.class, "tag");
    }
}
