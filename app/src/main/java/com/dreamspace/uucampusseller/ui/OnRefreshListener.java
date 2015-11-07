package com.dreamspace.uucampusseller.ui;

import java.util.List;

/**
 * Created by wufan on 2015/10/27.
 */
public interface OnRefreshListener<T> {
    void onFinish(List<T> mEntities);

    void onError();
}

