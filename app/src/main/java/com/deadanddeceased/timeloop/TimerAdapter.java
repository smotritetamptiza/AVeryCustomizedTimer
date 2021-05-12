package com.deadanddeceased.timeloop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
                if (currTimer.isActive()) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    String name = currTimer.getName();
                    intent.putExtra("name", name);
                    intent.putExtra("id", currTimer.getId());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)currTimer.getId(), intent, 0);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + currTimer.getSecondsTotal() * 1000,
                            currTimer.getSecondsTotal() * 1000, pendingIntent);
                } else {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)currTimer.getId(), intent, 0);
                    alarmManager.cancel(pendingIntent);
                }
            }
        });
        /*
        holder.editTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, "Активный таймер будет перезапущен", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, EditTimerActivity.class);
                intent.putExtra("name", currTimer.getName());
                intent.putExtra("interval", currTimer.getSecondsTotal());
                intent.putExtra("id", currTimer.getId());
                intent.putExtra("wasActive", currTimer.isActive());
                if (currTimer.isActive()) {
                    timers.get(position).toggleActive();
                }
                context.startActivity(intent);
            }
        });*/
        holder.deleteTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (timers.get(position).isActive()) {
                    timers.get(position).toggleActive();
                }
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)currTimer.getId(), intent, 0);
                alarmManager.cancel(pendingIntent);
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
        //private ImageView editTimerButton;
        private ImageView deleteTimerButton;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            timerNameView = itemView.findViewById(R.id.timerName);
            timerSwitch = itemView.findViewById(R.id.timerSwitch);
            //editTimerButton = itemView.findViewById(R.id.editTimerButton);
            deleteTimerButton = itemView.findViewById(R.id.deleteTimerButton);
        }
    }
}
