package com.example.quest.hmi_demo.navigation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.navigation.model.Step;

import java.util.List;

/**
 * Created by test on 20/2/17.
 */
public class MapDirectionsAdapter extends RecyclerView.Adapter<MapDirectionsAdapter.ViewHolder>{

    private List<Step> steps;
    private int rowLayout;
    private Context context;

    public MapDirectionsAdapter(List<Step> steps, int rowLayout, Context context){
        this.steps = steps;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MapDirectionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(rowLayout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MapDirectionsAdapter.ViewHolder holder, int position) {
        holder.duration.setText(Html.fromHtml("<b>Duration:</b> "+steps.get(position).getDuration().getText()));
        holder.distance.setText(Html.fromHtml("<b>Distance:</b> "+steps.get(position).getDistance().getText()));
        holder.path.setText(Html.fromHtml(steps.get(position).getPath()));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView path,duration,distance;
        public ViewHolder(View v) {
            super(v);
            path = (TextView)v.findViewById(R.id.path);
            distance = (TextView)v.findViewById(R.id.distance);
            duration = (TextView)v.findViewById(R.id.duration);
        }
    }
}
