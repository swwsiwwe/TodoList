//为了不使RecyclerView的checkbox产生混乱,于是新创一个adapter,只用来展示数据。
package com.example.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class Todo extends RecyclerView.Adapter<Todo.ViewHolder1>  {
    private int i;
    private Context mContext;
    private List<TodoList> mTodoList;
    static class ViewHolder1 extends RecyclerView.ViewHolder{
        ImageButton delete;
        CardView cardview;
        ImageView todoListImage;
        TextView todoList;
        private ViewHolder1(View view){
            super(view);
            cardview = (CardView)view;
            todoListImage = view.findViewById(R.id.important);
            todoList = view.findViewById(R.id.todolist);
        }
    }
    public Todo(List<TodoList> todolists){
        mTodoList = todolists;
    }

    @NonNull
    @Override
    public Todo.ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent,int viewType ) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.todolist_item,parent,false);
        Todo.ViewHolder1 holder = new Todo.ViewHolder1(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Todo.ViewHolder1 holder, int position) {
        TodoList todoList = mTodoList.get(position);
        holder.todoList.setText(todoList.getText());
        Glide.with(mContext).load(todoList.getId()).into(holder.todoListImage);

    }
    @Override
    public int getItemCount() {
        return mTodoList.size();
    }
}
