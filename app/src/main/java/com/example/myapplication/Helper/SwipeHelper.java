package com.example.myapplication.Helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeHelper extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetector gestureDetector;

    public SwipeHelper(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return super.onInterceptTouchEvent(rv, e);
    }

    public abstract void onSwipeLeft();

    public abstract void onSwipeRight();
}
