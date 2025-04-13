package com.example.kulubecioglu_lab3_gles;

import android.opengl.GLES32;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private myWorkMode wmRef = null;
    private int renderWidth = 1;
    private int renderHeight = 1;

    public void setWorkMode(myWorkMode workMode) {
        this.wmRef = workMode;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);
        GLES32.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES32.glViewport(0, 0, width, height);
        renderWidth = width;
        renderHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        if (wmRef == null) return;

        if (wmRef.getProgramId() == 0) wmRef.myCreateShaderProgram();
        if (wmRef.getProgramId() == 0) return;

        GLES32.glUseProgram(wmRef.getProgramId());
        wmRef.myUseProgramForDrawing(renderWidth, renderHeight);
    }
}
