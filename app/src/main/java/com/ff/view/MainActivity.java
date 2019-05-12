package com.ff.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ff.view.custom.CarFragment;
import com.ff.view.layout.FlowLayoutFragment;
import com.ff.view.recyclerview.RecyclerViewFragment;
import com.ff.view.svg.SVGFragment;
import com.ff.view.toolbar.ToolbarFragment;
import com.ff.view.vlayout.VLayoutFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnListItemClickListener {

    private FrameLayout mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRoot = new FrameLayout(this);
        mRoot.setId(View.generateViewId());
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mRoot);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(mRoot.getId(), new MainFragment(), MainFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Fragment fragment;
        switch (position) {
            case 0:// 自定义View
                fragment = new CarFragment();
                break;
            case 1:// 组合控件Toolbar
                fragment = new ToolbarFragment();
                break;
            case 2:// 流式布局
                fragment = new FlowLayoutFragment();
                break;
            case 3:// 手写RecyclerView
                fragment = new RecyclerViewFragment();
                break;
            case 4:// SVG
                fragment = new SVGFragment();
                break;
            case 5:// VLayout
                fragment = new VLayoutFragment();
                break;
            default:
                return;

        }
        getSupportFragmentManager().beginTransaction()
                .replace(mRoot.getId(), fragment, fragment.getClass().getName())
                .addToBackStack(null)
                .commit();
    }
}
