package com.example.massenger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FeedFragment extends Fragment {
    
    private static final String TAG = "FeedFragment";

    public FeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: FeedFragment создается");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: FeedFragment создает view");
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: FeedFragment view создан");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: FeedFragment становится видимым");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: FeedFragment возобновляет работу");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: FeedFragment приостанавливается");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: FeedFragment останавливается");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: FeedFragment view уничтожается");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: FeedFragment уничтожается");
    }
}