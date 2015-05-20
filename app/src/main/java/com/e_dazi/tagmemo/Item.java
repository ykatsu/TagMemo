package com.e_dazi.tagmemo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Item model
 * This model is base of Data. This is set of Item and Tag.
 *
 * Created by yoshi on 2015/05/11.
 */

@Table(name = "Items")
public class Item extends Model {
    @Column(name = "Memo", notNull = true)
    public Memo memo;

    @Column(name = "Tag")
    public Tag tag;

    public Item(){
        super();
    }

    public Item(Memo memo, Tag tag){
        super();

        this.memo = memo;
        this.tag  = tag;
    }
}
