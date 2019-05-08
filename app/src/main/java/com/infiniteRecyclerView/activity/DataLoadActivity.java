package com.infiniteRecyclerView.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.infiniteRecyclerView.OnLoadMoreListener;
import com.infiniteRecyclerView.R;
import com.infiniteRecyclerView.adapter.DataAdapter;
import com.infiniteRecyclerView.modal.Student;

import java.util.ArrayList;
import java.util.List;

public class DataLoadActivity extends AppCompatActivity {
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Student> studentList;
    private Handler handler;
    private static final String TAG = "DataLoadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_load);
        setInIt();
        loadData();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapter(studentList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i(TAG, "onLoadMore: ");
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                    }
                },2000);
            }
        });
    }

    // load initial data
    private void loadData() {
        for (int i = 1; i <= 20; i++) {
            studentList.add(new Student("Student " + i, "AndroidStudent" + i + "@gmail.com"));
        }
    }

    private void setInIt() {
        tvEmptyView = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<>();
        handler = new Handler();
    }
}
