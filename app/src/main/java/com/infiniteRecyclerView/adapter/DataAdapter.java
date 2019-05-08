package com.infiniteRecyclerView.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.infiniteRecyclerView.OnLoadMoreListener;
import com.infiniteRecyclerView.R;
import com.infiniteRecyclerView.modal.Student;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<Student> studentList;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int visibleThreshold = 5;
    private OnLoadMoreListener onLoadMoreListener;

    public DataAdapter(List<Student> studentList, RecyclerView recyclerView) {
        this.studentList = studentList;
        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager)
        {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.student_record_layout, viewGroup, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progressbar_layout, viewGroup, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {

            Student singleStudent= studentList.get(position);

            ((StudentViewHolder) holder).tvName.setText(singleStudent.getName());

            ((StudentViewHolder) holder).tvEmailId.setText(singleStudent.getEmailId());

            ((StudentViewHolder) holder).student= singleStudent;

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    public void setLoaded() {
        loading = false;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public TextView tvEmailId;

        public Student student;

        public StudentViewHolder(View v) {
            super(v);
            tvName =  v.findViewById(R.id.tvName);

            tvEmailId =v.findViewById(R.id.tvEmailId);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                            Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar =v.findViewById(R.id.progressBar1);
        }
    }

}
