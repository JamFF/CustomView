package com.ff.view.vlayout;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

public class BaseDelegateAdapter extends DelegateAdapter.Adapter<BaseViewHolder> {

    private LayoutHelper mLayoutHelper;
    private int mCount;
    private int mLayoutId;
    private int mViewTypeItem;

    public BaseDelegateAdapter(LayoutHelper layoutHelper, int layoutId,
                               int count, int viewTypeItem) {
        mLayoutHelper = layoutHelper;
        mCount = count;
        mLayoutId = layoutId;
        mViewTypeItem = viewTypeItem;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        /*if (mViewTypeItem == viewType) {
            return new BaseViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(mLayoutId, viewGroup, false));
        }
        return null;*/
        return new BaseViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(mLayoutId, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
