package com.example.broadcast;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

//import java.util.logging.Handler;

public class JobNotificacion extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(3000);
                } catch (InterruptedException e)
                {
                    Log.e("JobNotificacionStatus","Hilo interrunpido, hubo error" , e);
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JobNotificacion.this, "JobnOTIFICACION ejecutado!", Toast.LENGTH_SHORT).show();
                    }
                });

                jobFinished(params, false);
            }
        });

        backgroundThread.start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
