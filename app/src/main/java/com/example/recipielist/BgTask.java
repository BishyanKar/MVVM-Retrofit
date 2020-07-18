package com.example.recipielist;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipielist.requests.RecipeApiClient;

public class BgTask {
    private LottieAnimationView animationView;
    private BgTaskManager bgTaskManager;
    public BgTask(LottieAnimationView animationView){
        bgTaskManager = BgTaskManager.getInstance();
        this.animationView = animationView;
    }
    public void handleDecodeState(int state) {
        int outState;
        // Converts the decode state to the overall state.
        switch(state) {
            case RecipeApiClient.DECODE_STATE_COMPLETED:
                outState = BgTaskManager.TASK_COMPLETE;
                break;
            default: outState = -1;
        }
        // Calls the generalized state method
        handleState(outState);
    }
    // Passes the state to Manager
    void handleState(int state) {
        /*
         * Passes a handle to this task and the
         * current state to the class that created
         * the thread pools
         */
        bgTaskManager.handleState(this, state);
    }

    public void setAnimationView(LottieAnimationView animationView) {
        this.animationView = animationView;
    }

    public LottieAnimationView getAnimationView() {
        return animationView;
    }
}
