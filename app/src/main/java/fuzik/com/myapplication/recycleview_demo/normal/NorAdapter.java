package fuzik.com.myapplication.recycleview_demo.normal;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fuzik.com.myapplication.R;

public class NorAdapter extends RecyclerView.Adapter<NorAdapter.MyViewHolder> {
    List<Integer> list;

    public NorAdapter(List<Integer> list) {
        this.list = list;
    }

    //加入布局的地方,返回holder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item_nor, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    //item处理的地方
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText("第"+list.get(position)+"条");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //item初始化的地方
    //viewhoder
    class MyViewHolder extends RecyclerView.ViewHolder{
       TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_recycleview_nor);
        }
    }
}
