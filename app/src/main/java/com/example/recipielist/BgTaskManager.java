package com.example.recipielist;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;

public class BgTaskManager {
    public static final int TASK_COMPLETE = 123;
    private static Handler handler;
    private static BgTaskManager instance;

    public static BgTaskManager getInstance(){
        if(instance == null){
            instance = new BgTaskManager();
        }
        return instance;
    }
    private BgTaskManager(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                BgTask bgTask = (BgTask)msg.obj;
                LottieAnimationView animationView = bgTask.getAnimationView();

                switch (msg.what){
                    case TASK_COMPLETE :
                        animationView.setVisibility(View.INVISIBLE);
                        break;
                    default : super.handleMessage(msg);
                }
            }
        };
    }

    public void handleState(BgTask photoTask, int state) {
        switch (state) {

            // The task finished
            case TASK_COMPLETE:
                /*
                 * Creates a message for the Handler
                 * with the state and the task object
                 */
                Message completeMessage =
                        handler.obtainMessage(state, photoTask);
                completeMessage.sendToTarget();
                break;
        }

    }
}
