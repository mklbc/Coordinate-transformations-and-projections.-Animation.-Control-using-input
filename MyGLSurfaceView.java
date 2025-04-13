package com.example.kulubecioglu_lab3_gles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    private myWorkMode wmRef = null;
    private TextView cameraInfoTextView;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        renderer = new MyGLRenderer();
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void setWorkMode(myWorkMode workMode) {
        this.wmRef = workMode;
        renderer.setWorkMode(workMode);
    }

    public void setCameraInfoTextView(TextView textView) {
        //this.cameraInfoTextView = textView;
    }

    public void updateCameraInfoText() {
        if (wmRef instanceof myNineCubesMode && cameraInfoTextView != null) {
            myNineCubesMode mode = (myNineCubesMode) wmRef;
            String info = String.format("a=%.0f b=%.0f x=%.1f y=%.1f z=%.1f",
                    mode.getAlpha(), mode.getBeta(), mode.getCamX(), mode.getCamY(), mode.getCamZ());
            //cameraInfoTextView.setText(info);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (wmRef == null || wmRef.onTouchNotUsed()) return false;

        int cx = getWidth();
        int cy = getHeight();
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (wmRef.onActionDown(x, y, cx, cy)) updateCameraInfoText();
                break;
            case MotionEvent.ACTION_MOVE:
                if (wmRef.onActionMove(x, y, cx, cy)) updateCameraInfoText();
                break;
        }
        requestRender();
        return true;
    }
}
