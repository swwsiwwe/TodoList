package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private int i;
    private Context mContext;
    private List<TodoList> mTodoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        ImageButton delete;
        CardView cardview;
        ImageView todoListImage;
        TextView todoList;
        private ViewHolder(View view){
            super(view);
            cardview = (CardView)view;
            todoListImage = view.findViewById(R.id.important);
            todoList = view.findViewById(R.id.todolist);
            delete = view.findViewById(R.id.delete);
            checkBox = view.findViewById(R.id.remember);
        }
    }
    public TodoListAdapter(List<TodoList> todolists){
        mTodoList = todolists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.todolist_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                notifyItemRemoved(position);
                mTodoList.remove(position);
                notifyItemRangeChanged(position,mTodoList.size()-position);
                SharedPreferences pref =mContext.getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = mContext.getSharedPreferences("data",MODE_PRIVATE).edit();
                i=pref.getInt("cnt",-1);
                int p = i-position;
                for(;p<i;p++){
                    editor.putString(""+(p),pref.getString((p+1)+"",""));
                    editor.putInt((p)+"image",pref.getInt((p+1)+"image",0));
                    editor.putBoolean("check"+(p),pref.getBoolean("check"+(p+1),false));
                }
                editor.remove(p+"");
                editor.remove(p+"image");
                editor.remove("check"+p);
                editor.putInt("cnt",i-1);
                editor.apply();
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref =mContext.getSharedPreferences("data",MODE_PRIVATE);
                int position = holder.getAdapterPosition();
                i=pref.getInt("cnt",-1);
                int p=i-position;
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putBoolean("check"+p,isChecked);
                    editor.apply();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoList todoList = mTodoList.get(position);
        holder.todoList.setText(todoList.getText());
        Glide.with(mContext).load(todoList.getId()).into(holder.todoListImage);
        holder.checkBox.setChecked(todoList.getCheck());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }
}
