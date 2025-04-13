package com.example.kulubecioglu_lab3_gles;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.os.SystemClock;

public class myPyramidRotationMode extends myWorkMode {

    private float alphaViewAngle = 45; // sahneye çapraz bakış
    private float betaViewAngle = 30;  // hafif yukarıdan bakış
    private float viewDistance = 12.0f; // biraz daha geriden başlat

    protected float xTouchDown, yTouchDown;
    protected float alphaAnglePrev, betaAnglePrev;

    private float modelRotationOffset = 0f;
    private float modelRotationOffsetManual = 0f;
    private float cameraMoveOffset = 0f;

    private OnCameraUpdateListener cameraUpdateListener;

    public interface OnCameraUpdateListener {
        void onCameraUpdate(String info);
    }

    public void setCameraUpdateListener(OnCameraUpdateListener listener) {
        this.cameraUpdateListener = listener;
    }

    private void notifyCameraInfo() {
        if (cameraUpdateListener != null) {
            float camX = (float)(Math.sin(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
            float camY = (float)(Math.cos(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
            float camZ = (float)(Math.sin(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
            String info = String.format("a=%.0f b=%.0f x=%.1f y=%.1f z=%.1f", alphaViewAngle, betaViewAngle, camX, camY, camZ);
            cameraUpdateListener.onCameraUpdate(info);
        }
    }

    public myPyramidRotationMode() {
        super();
        myCreateScene();
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
    public void myCreateScene() {
        int size = 2000;
        arrayVertex = new float[size];
        int pos = 0;

        // Zemin (chessboard)
        pos = myGraphicPrimitives.addChessXYZRGB(arrayVertex, pos, -2.0f, -2.0f, 0.0f, 4, 4, 1.0f);

        // Piramit
        pos = myGraphicPrimitives.addPyramidXYZRGB(arrayVertex, pos,
                0f, 0f, 0.6f,
                1f, 1f, 0f);

        numVertices = pos / 6;
    }

    @Override
    public void myUseProgramForDrawing(int width, int height) {
        float aspect = (float) width / height;

        float eyeX = (float)(Math.sin(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
        float eyeY = (float)(Math.cos(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
        float eyeZ = (float)(Math.sin(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);

        float centerX = 0f, centerY = 0f, centerZ = 0f;

        Matrix.setLookAtM(viewMatrix, 0,
                eyeX, eyeY, eyeZ,
                centerX, centerY, centerZ,
                0f, 0f, 1f);

        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 30);

        int viewHandle = GLES32.glGetUniformLocation(glProgram, "uViewMatrix");
        int projHandle = GLES32.glGetUniformLocation(glProgram, "uProjMatrix");
        int modelHandle = GLES32.glGetUniformLocation(glProgram, "uModelMatrix");

        // Zemin çizimi (modelRotationOffsetManual dönüşüyle)
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, modelRotationOffsetManual, 0, 0, 1);
        GLES32.glUniformMatrix4fv(modelHandle, 1, false, modelMatrix, 0);
        GLES32.glUniformMatrix4fv(viewHandle, 1, false, viewMatrix, 0);
        GLES32.glUniformMatrix4fv(projHandle, 1, false, projectionMatrix, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertices - 12);

        // Piramit çizimi (otomatik dönüş + manuel dönüş)
        Matrix.setIdentityM(modelMatrix, 0);
        modelRotationOffset += 7.2f;
        Matrix.rotateM(modelMatrix, 0, modelRotationOffset + modelRotationOffsetManual, 0, 0, 1);
        GLES32.glUniformMatrix4fv(modelHandle, 1, false, modelMatrix, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, numVertices - 12, 12);

        notifyCameraInfo();
    }

    @Override
    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        alphaAnglePrev = modelRotationOffsetManual;
        betaAnglePrev = cameraMoveOffset;
        return true;
    }

    @Override
    public boolean onActionMove(float x, float y, int cx, int cy) {
        float dx = x - xTouchDown;
        float dy = y - yTouchDown;

        modelRotationOffsetManual = alphaAnglePrev + dx * 0.02f;
        cameraMoveOffset = betaAnglePrev - dy * 0.005f;

        return true;
    }

    public float getAlpha() {
        return alphaViewAngle;
    }

    public float getBeta() {
        return betaViewAngle;
    }

    public float getCamX() {
        return (float)(Math.sin(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
    }

    public float getCamY() {
        return (float)(Math.cos(Math.toRadians(alphaViewAngle)) * Math.cos(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
    }

    public float getCamZ() {
        return (float)(Math.sin(Math.toRadians(betaViewAngle))) * (viewDistance + cameraMoveOffset);
    }
}