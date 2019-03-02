package com.example.todolist;

import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int imageId;
    private int i;
    private EditText editText;
    private List<TodoList> todoList = new ArrayList<>();
    private TodoListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.GREEN);
        setSupportActionBar(toolbar);
        //初始化适配器
        initTodoList();
        //recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoListAdapter(todoList);
        recyclerView.setAdapter(adapter);
        Button add = findViewById(R.id.add);
        editText = findViewById(R.id.edit);
//        FloatingActionButton fab = findViewById(R.id.fab_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String inputText = editText.getText().toString();
                if("".equals(inputText)){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_LONG).show();
                }
                else {
                    //数据存储
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("提醒");
                    dialog.setMessage("选择计划优先级（紧急程度）");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("紧急", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imageId = R.drawable.warning;
                            TodoList todo = new TodoList(inputText,imageId,false);
                            todoList.add(todo);
                            adapter.notifyItemInserted(todoList.size()-1);
                            select(inputText,imageId);
                        }
                    });
                    dialog.setNeutralButton("一般", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imageId = R.drawable.normal;
                            TodoList todo = new TodoList(inputText,imageId,false);
                            todoList.add(todo);
                            adapter.notifyItemInserted(todoList.size()-1);
                            select(inputText,imageId);
                        }
                    });
                    dialog.setNegativeButton("不紧急", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imageId = R.drawable.peace;
                            TodoList todo = new TodoList(inputText,imageId,false);
                            todoList.add(todo);
                            adapter.notifyItemInserted(todoList.size()-1);
                            select(inputText,imageId);
                        }
                    });
                    dialog.show();
                }
            }
        });

    }
    private void initTodoList(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        todoList.clear();
        int j = pref.getInt("cnt",-1);
        for(;j>=0;j--){
            if(!"".equals(pref.getString(""+j,""))){
                TodoList todo = new TodoList(pref.getString(""+j,""),pref.getInt((j)+"image",0),
                        pref.getBoolean("check"+j,false));
                todoList.add(todo);
            }
        }

    }
    private void select(String str,int id){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        i=pref.getInt("cnt",-1)+1;
        String s = ""+i;
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(s,str);
        editor.putInt(s+"image",id);
        editor.putBoolean("check"+s,false);
        editor.putInt("cnt",i);
        editor.apply();
        editText.setText("");
    }
}
