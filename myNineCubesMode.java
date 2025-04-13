package com.example.kulubecioglu_lab3_gles;

import android.opengl.GLES32;
import android.opengl.Matrix;
public class myNineCubesMode extends myWorkMode {
    private float camX = 0f, camY = -20f, camZ = 4f;
    private float alphaViewAngle = 0;
    private float betaViewAngle = 0;
    protected float xTouchDown, yTouchDown;
    protected float camXPrev, camYPrev, camZPrev;
    private float alphaAnglePrev, betaAnglePrev;

    private float moveSpeed = 0.01f;
    private OnCameraUpdateListener cameraUpdateListener;
    public myNineCubesMode() {
        super();
        myCreateScene();
    }
    public interface OnCameraUpdateListener {
        void onCameraUpdate(String info);
    }
    public void setCameraUpdateListener(OnCameraUpdateListener listener) {
        this.cameraUpdateListener = listener;
    }

    private void notifyCameraInfo() {
        if (cameraUpdateListener != null) {
            String info = String.format("a=%.0f b=%.0f x=%.1f y=%.1f z=%.1f",
                    alphaViewAngle, betaViewAngle, camX, camY, camZ);
            cameraUpdateListener.onCameraUpdate(info);
        }
    }

    @Override
    public void myCreateScene() {
        int size = 20000;
        arrayVertex = new float[size];
        int pos = 0;

        pos = myGraphicPrimitives.addChessXYZRGB(arrayVertex, pos, -4.0f, -4.0f, 0f, 8, 8, 1f);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = 0; k < 3; k++) {
                    pos = myGraphicPrimitives.addCubeXYZRGB(arrayVertex, pos,
                            i * 2.0f, j * 2.0f, k * 2.0f + 1.0f,
                            1.0f, 0.4f, 0.8f, 1.0f);
                }
            }
        }

        numVertices = pos / 6;
    }

    @Override
    public void myCreateShaderProgram() {
        glProgram = myGLUtils.createProgram(
                myShadersLibrary.vertexShaderCode5,
                myShadersLibrary.fragmentShaderCode3
        );
        myGLUtils.bindVertexArrayWithColor(arrayVertex, glProgram);
    }

    @Override
    public void myUseProgramForDrawing(int width, int height) {
        float aspect = (float) width / height;

        float lookX = camX + (float)(Math.sin(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle)));
        float lookY = camY + (float)(Math.cos(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle)));
        float lookZ = camZ + (float)(Math.sin(Math.toRadians(betaViewAngle)));

        Matrix.setLookAtM(viewMatrix, 0,
                camX, camY, camZ,
                lookX, lookY, lookZ,
                0f, 0f, 1f);

        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 100);

        int viewHandle = GLES32.glGetUniformLocation(glProgram, "uViewMatrix");
        int projHandle = GLES32.glGetUniformLocation(glProgram, "uProjMatrix");
        int modelHandle = GLES32.glGetUniformLocation(glProgram, "uModelMatrix");

        Matrix.setIdentityM(modelMatrix, 0);
        GLES32.glUniformMatrix4fv(viewHandle, 1, false, viewMatrix, 0);
        GLES32.glUniformMatrix4fv(projHandle, 1, false, projectionMatrix, 0);
        GLES32.glUniformMatrix4fv(modelHandle, 1, false, modelMatrix, 0);

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertices);
        notifyCameraInfo();
    }

    @Override
    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        camXPrev = camX;
        camYPrev = camY;
        camZPrev = camZ;
        alphaAnglePrev = alphaViewAngle;
        betaAnglePrev = betaViewAngle;
        return true;
    }

    @Override
    public boolean onActionMove(float x, float y, int cx, int cy) {
        float dx = x - xTouchDown;
        float dy = y - yTouchDown;

        // Mouse ileri-geri hareket → kamera ileri-geri
        float forwardX = (float)(Math.sin(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle)));
        float forwardY = (float)(Math.cos(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle)));
        float forwardZ = (float)(Math.sin(Math.toRadians(betaViewAngle)));

        camX = camXPrev + dy * moveSpeed * forwardX;
        camY = camYPrev + dy * moveSpeed * forwardY;
        camZ = camZPrev + dy * moveSpeed * forwardZ;

        // Mouse sağ-sol hareket → bakış yönünü değiştir
        alphaViewAngle = alphaAnglePrev + dx * 0.01f; //hız!

        notifyCameraInfo();
        return true;
    }

    public void adjustAlpha(float delta) {
        alphaViewAngle += delta;
        notifyCameraInfo();
    }

    public void adjustBeta(float delta) {
        betaViewAngle += delta;
        if (betaViewAngle > 89) betaViewAngle = 89;
        if (betaViewAngle < -89) betaViewAngle = -89;
        notifyCameraInfo();
    }

    public float getAlpha() { return alphaViewAngle; }
    public float getBeta() { return betaViewAngle; }
    public float getCamX() { return camX; }
    public float getCamY() { return camY; }
    public float getCamZ() { return camZ; }
}