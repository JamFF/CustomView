package com.ff.view.toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ff.view.R;

/**
 * description:
 * author: FF
 * time: 2019-05-04 16:08
 */
public class ToolbarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toolbar, container, false);
        ToolBar toolBar = view.findViewById(R.id.toolbar);
        toolBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击返回", Toast.LENGTH_SHORT).show();
            }
        });
        toolBar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击右键", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
