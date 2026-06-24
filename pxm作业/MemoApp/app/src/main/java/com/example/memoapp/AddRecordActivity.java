package com.example.memoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.memoapp.db.DatabaseHelper;
import com.example.memoapp.model.Record;
import com.google.android.material.textfield.TextInputEditText;

/**
 * 添加记录界面
 * - 录入事件标题、日期、时间、详情
 * - 保存按钮：校验非空后插入数据库并返回主界面
 * - 取消按钮：直接返回主界面
 */
public class AddRecordActivity extends AppCompatActivity {

    private TextInputEditText etTitle;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etContent;
    private Button btnSave;
    private Button btnCancel;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        dbHelper = new DatabaseHelper(this);

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

        // 取消按钮 — 返回主界面
        btnCancel.setOnClickListener(v -> finish());

        // 保存按钮 — 校验并插入数据库
        btnSave.setOnClickListener(v -> saveRecord());
    }

    /**
     * 校验输入并保存记录到数据库
     */
    private void saveRecord() {
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

        // 创建记录并插入数据库
        Record record = new Record(title, content, date, time);
        long result = dbHelper.insertRecord(record);

        if (result != -1) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();  // 返回主界面
        } else {
            Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
