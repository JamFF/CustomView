package com.ff.view.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ff.view.R;

/**
 * description:
 * author: FF
 * time: 2019-05-05 10:44
 */
public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";

    private RecyclerView mRecyclerView;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRecyclerView.setAdapter(new RecyclerView.Adapter() {
                @Override
                public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
                    if (position % 2 == 0) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_table, parent, false);
                        TextView textView = convertView.findViewById(R.id.text1);
                        textView.setText("网易课堂 " + position);
                    } else {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_table2, parent, false);
                        TextView textView = convertView.findViewById(R.id.text2);
                        textView.setText("网易图标 " + position);
                    }
                    return convertView;
                }

                @Override
                public View onBinderViewHolder(int position, View convertView, ViewGroup parent) {
                    if (position % 2 == 0) {
                        TextView textView = convertView.findViewById(R.id.text1);
                        textView.setText("网易课堂 " + position);
                    } else {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(
                                R.layout.item_table2, parent, false);
                        TextView textView = convertView.findViewById(R.id.text2);
                        textView.setText("网易图标 " + position);
                    }
                    return convertView;
                }

                @Override
                public int getItemViewType(int row) {
                    if (row % 2 == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                }

                @Override
                public int getViewTypeCount() {
                    return 2;
                }

                @Override
                public int getCount() {
                    return 30000;
                }

                @Override
                public int getHeight(int index) {
                    return 150;
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_table, parent, false);
                TextView textView = convertView.findViewById(R.id.text1);
                textView.setText("网易课堂 " + position);
                Log.d(TAG, "onCreateViewHolder: " + convertView.hashCode());
                return convertView;
            }

            @Override
            public View onBinderViewHolder(int position, View convertView, ViewGroup parent) {
                TextView textView = convertView.findViewById(R.id.text1);
                textView.setText("网易课堂 " + position);
                Log.i(TAG, "onBinderViewHolder: " + convertView.hashCode());
                return convertView;
            }

            @Override
            public int getItemViewType(int row) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public int getHeight(int index) {
                return 200;
            }
        });

        new Handler().postDelayed(mRunnable, 5000);
        return view;
    }
}
