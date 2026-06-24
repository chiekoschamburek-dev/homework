package com.example.memoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoapp.adapter.RecordAdapter;
import com.example.memoapp.db.DatabaseHelper;
import com.example.memoapp.model.Record;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * 记事本主界面
 * - RecyclerView 展示所有记录
 * - 点击条目进入修改/查看详情
 * - 长按条目弹出删除确认对话框
 * - FAB / 菜单按钮进入添加记录界面
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private RecordAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // 设置红色标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化添加按钮
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            startActivity(intent);
        });

        // 加载数据并设置适配器
        loadRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回主界面时刷新列表
        loadRecords();
    }

    /**
     * 从数据库加载所有记录并更新列表
     */
    private void loadRecords() {
        recordList = dbHelper.getAllRecords();
        if (adapter == null) {
            adapter = new RecordAdapter(recordList);
            recyclerView.setAdapter(adapter);

            // 点击条目 — 进入修改/查看界面
            adapter.setOnItemClickListener(record -> {
                Intent intent = new Intent(MainActivity.this, EditRecordActivity.class);
                intent.putExtra("record_id", record.getId());
                startActivity(intent);
            });

            // 长按条目 — 弹出删除确认对话框
            adapter.setOnItemLongClickListener(record -> {
                showDeleteDialog(record);
            });
        } else {
            adapter.setRecords(recordList);
        }
    }

    /**
     * 长按删除确认对话框
     */
    private void showDeleteDialog(Record record) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除 「" + record.getTitle() + "」 吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    dbHelper.deleteRecord(record.getId());
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    loadRecords();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 标题栏上的添加按钮
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
