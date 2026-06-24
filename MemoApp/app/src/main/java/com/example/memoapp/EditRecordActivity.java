package com.example.memoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.memoapp.db.DatabaseHelper;
import com.example.memoapp.model.Record;
import com.google.android.material.textfield.TextInputEditText;

/**
 * 修改/查看记录界面
 * - 从 Intent 获取记录 ID，加载数据显示在输入框中
 * - 保存按钮：更新数据库并返回主界面
 * - 取消按钮：直接返回主界面
 */
public class EditRecordActivity extends AppCompatActivity {

    private TextInputEditText etTitle;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etContent;
    private Button btnSave;
    private Button btnCancel;
    private DatabaseHelper dbHelper;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        dbHelper = new DatabaseHelper(this);

        // 获取传入的记录 ID
        recordId = getIntent().getIntExtra("record_id", -1);
        if (recordId == -1) {
            Toast.makeText(this, "记录不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 设置红色标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // 绑定视图
        etTitle = findViewById(R.id.et_title);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        etContent = findViewById(R.id.et_content);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // 加载记录数据到表单
        loadRecordData();

        // 取消按钮 — 返回主界面
        btnCancel.setOnClickListener(v -> finish());

        // 保存按钮 — 校验并更新数据库
        btnSave.setOnClickListener(v -> updateRecord());
    }

    /**
     * 从数据库加载记录数据并填充到输入框
     */
    private void loadRecordData() {
        Record record = dbHelper.getRecordById(recordId);
        if (record != null) {
            etTitle.setText(record.getTitle());
            etDate.setText(record.getDate());
            etTime.setText(record.getTime());
            etContent.setText(record.getContent());
        } else {
            Toast.makeText(this, "记录不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * 校验输入并更新数据库记录
     */
    private void updateRecord() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        // 校验标题不能为空
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("请输入事件标题");
            etTitle.requestFocus();
            return;
        }

        // 更新记录
        Record record = new Record(recordId, title, content, date, time);
        int rows = dbHelper.updateRecord(record);

        if (rows > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            finish();  // 返回主界面
        } else {
            Toast.makeText(this, "修改失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
