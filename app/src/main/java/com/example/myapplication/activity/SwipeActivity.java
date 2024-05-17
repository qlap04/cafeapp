package com.example.myapplication.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.ProductAdapter1;

public class SwipeActivity extends ItemTouchHelper.SimpleCallback {
    private final ProductAdapter1 adapter;
    private final ColorDrawable background;

    public SwipeActivity(ProductAdapter1 adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT) {
        }
        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        if (dX > 0) {
        } else if (dX < 0) {
        }

        // Override onTouchEvent to handle swipe length
        itemView.setOnTouchListener(new View.OnTouchListener() {
            float dx = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float delta = x - dx;
                        if (delta < -200) { // set your desired threshold for swipe length
                            return true; // consume the event to prevent RecyclerView from scrolling
                        }
                        break;
                }
                return false; // don't consume the event
            }
        });

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
