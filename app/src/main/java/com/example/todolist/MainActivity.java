package com.example.todolist;

import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
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
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoListAdapter(todoList);
        recyclerView.setAdapter(adapter);
        Button add = findViewById(R.id.add);
        editText = findViewById(R.id.edit);
        NavigationView nav = findViewById(R.id.nav_view);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_peace :{
                     fresh(R.drawable.peace);
                        break;
                    }
                    case R.id.nav_normal :{
                        fresh(R.drawable.normal);
                        break;
                    }
                    case R.id.nav_warning :{
                        fresh(R.drawable.warning);
                        break;
                    }
                    case R.id.nav_back : {
                        initTodoList();
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
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
        for(int i = 0;i<=j;i++){
            if(!"".equals(pref.getString(""+i,""))){
                TodoList todo = new TodoList(pref.getString(""+i,""),pref.getInt((i)+"image",0),
                        pref.getBoolean("check"+i,false));
                todoList.add(todo);
            }
        }
    }
    public void inittTodoList(int id){
        todoList.clear();
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        int j = pref.getInt("cnt",-1);
        for(int i=0;i<=j;i++){
            if(pref.getInt(i+"image",0) == id){
                TodoList todo = new TodoList(pref.getString(i+"",""),pref.getInt((i)+"image",0),
                        false);
                todoList.add(todo);
            }
        }
        if(todoList.isEmpty()){
            Toast.makeText(MainActivity.this,"不存在这种计划",Toast.LENGTH_SHORT).show();
            initTodoList();
        }
        else {
            Toast.makeText(MainActivity.this,"此页面仅供查看,点击“查看所有”返回",Toast.LENGTH_LONG).show();
        }
    }
    public void fresh(int id) {
        inittTodoList(id);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Todo adapter1 = new Todo(todoList);
        recyclerView.setAdapter(adapter1);
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
