package com.example.vanhay.fragmentstudy.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vanhay.fragmentstudy.R;

/**
 * Created by Van Hay on 29-May-17.
 */

public class NodeJSFragment extends android.support.v4.app.Fragment{
    public NodeJSFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nodejs_fragment_layout,null);
    }
}
