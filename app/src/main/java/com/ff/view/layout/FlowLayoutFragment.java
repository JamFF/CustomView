package com.ff.view.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: FF
 * time: 2019-05-04 20:25
 */
public class FlowLayoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FlowLayout flowLayout = new FlowLayout(getContext());
        List<String> tags = new ArrayList<>();
        tags.add("网易");
        tags.add("网易");
        tags.add("网易课堂");
        tags.add("网易云音乐");
        tags.add("有道云");
        tags.add("高级UI自定义控件");
        tags.add("继承控件");
        tags.add("今天天气真的好好");
        tags.add("杭州天气也不错~");
        tags.add("好好学习   天天向上");
        tags.add("你是最棒的");
        tags.add("加油");
        flowLayout.addTag(tags);
        return flowLayout;
    }
}
