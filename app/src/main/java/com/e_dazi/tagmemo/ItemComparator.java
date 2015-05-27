package com.e_dazi.tagmemo;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Memoモデルの日付比較用クラス
 */

public class ItemComparator implements Comparator<Item> {
    public int compare(@NonNull Item a, @NonNull Item b) {
//        return a.memo.updated_at.compareTo(b.memo.updated_at);
        return b.memo.updated_at.compareTo(a.memo.updated_at);
    }
}