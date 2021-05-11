package com.deadanddeceased.timeloop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> {
    Context context;
    ArrayList<Timer> timers;

    public TimerAdapter(Context ct, ArrayList<Timer> timers) {
        context = ct;
        this.timers = timers;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.single_timer_layout, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        Timer currTimer = timers.get(position);
        holder.timerNameView.setText(currTimer.getName());
        holder.timerSwitch.setChecked(currTimer.isActive());
        holder.timerSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTimer.toggleActive();
            }
        });
        holder.editTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTimerActivity.class);
                intent.putExtra("name", currTimer.getName());
                intent.putExtra("interval", currTimer.getSecondsTotal());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        holder.deleteTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (timers.get(position).isActive()) {
                    timers.get(position).toggleActive();
                }
                timers.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, timers.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }

    public class TimerViewHolder extends RecyclerView.ViewHolder {
        private TextView timerNameView;
        private Switch timerSwitch;
        private Button editTimerButton;
        private Button deleteTimerButton;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            timerNameView = itemView.findViewById(R.id.timerName);
            timerSwitch = itemView.findViewById(R.id.timerSwitch);
            editTimerButton = itemView.findViewById(R.id.editTimerButton);
            deleteTimerButton = itemView.findViewById(R.id.deleteTimerButton);
        }
    }
}
