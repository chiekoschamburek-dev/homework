package com.example.memoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.R;
import com.example.memoapp.model.Record;

import java.util.List;

/**
 * RecyclerView 适配器 — 将记录列表绑定到列表视图
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Record> records;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(Record record);
    }

    // 长按事件接口
    public interface OnItemLongClickListener {
        void onItemLongClick(Record record);
    }

    public RecordAdapter(List<Record> records) {
        this.records = records;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    /**
     * 更新数据并刷新列表
     */
    public void setRecords(List<Record> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = records.get(position);

        holder.tvTitle.setText(record.getTitle());
        holder.tvContent.setText(record.getContent());
        holder.tvDateTime.setText(record.getDate() + " " + record.getTime());

        // 点击事件 — 进入查看/修改界面
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(record);
            }
        });

        // 长按事件 — 弹出删除确认对话框
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(record);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return records != null ? records.size() : 0;
    }

    /**
     * ViewHolder — 持有列表项中各视图的引用
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
        }
    }
}
