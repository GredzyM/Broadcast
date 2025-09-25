package com.example.broadcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BroadcastDinamicoActivity extends AppCompatActivity {

    private static final int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_broadcast_dinamico);
    }

    private BroadcastReceiver airplaneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            String mensaje;
            if (isAirplaneModeOn)
            {
                mensaje = "Nodo avión activado";
            } else
            {
                mensaje = "Nodo avión desactivado";
            }
            Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show();
            JobInfo jobInfo = getJobInfo(BroadcastDinamicoActivity.this);
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (scheduler != null)
            {
                scheduler.cancel(JOB_ID);

                int result = scheduler.schedule(jobInfo);
                if (result == JobScheduler.RESULT_SUCCESS) {
                    Toast.makeText(context, "Job programado al cambiar modo avión", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fallo al programar el job en el servicio", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private static JobInfo getJobInfo(BroadcastDinamicoActivity broadcastDinamicoActivity) {
        ComponentName componentName = new ComponentName(broadcastDinamicoActivity, JobNotificacion.class);
        return new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(2000)
                .setOverrideDeadline(5000)
                .setPersisted(false)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneReceiver, filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneReceiver);
    }
}
