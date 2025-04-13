package com.example.kulubecioglu_lab3_gles;

import android.opengl.GLES32;
import android.opengl.Matrix;

public abstract class myWorkMode {
    protected int VAO_id;

    protected int glProgram = 0;
    protected float[] arrayVertex;
    protected int numVertices = 0;

    // Kamera değişkenleri
    protected float alphaViewAngle = 0;
    protected float betaViewAngle = 0;
    protected float viewDistance = 5.0f;

    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];
    protected float[] modelMatrix = new float[16];

    // Dokunmatik kontrol değişkenleri
    protected float xTouchDown, yTouchDown;
    protected float alphaAnglePrev, betaAnglePrev;

    public abstract void myCreateScene();

    public abstract void myCreateShaderProgram();

    public void myUseProgramForDrawing(int width, int height) {
        float aspect = (float) width / height;

        // View matrix
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0);
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1);
        Matrix.translateM(viewMatrix, 0, 0, 0, -viewDistance);

        // Projection matrix
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 100);

        // Model matrix
        Matrix.setIdentityM(modelMatrix, 0);

        // Uniformları gönder
        int viewHandle = GLES32.glGetUniformLocation(glProgram, "uViewMatrix");
        int projHandle = GLES32.glGetUniformLocation(glProgram, "uProjMatrix");
        int modelHandle = GLES32.glGetUniformLocation(glProgram, "uModelMatrix");

        GLES32.glUniformMatrix4fv(viewHandle, 1, false, viewMatrix, 0);
        GLES32.glUniformMatrix4fv(projHandle, 1, false, projectionMatrix, 0);
        GLES32.glUniformMatrix4fv(modelHandle, 1, false, modelMatrix, 0);

        // Çizim
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertices);
    }

    public int getProgramId() {
        return glProgram;
    }

    public boolean onTouchNotUsed() {
        return false;
    }

    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        alphaAnglePrev = alphaViewAngle;
        betaAnglePrev = betaViewAngle;
        return true;
    }

    public boolean onActionMove(float
                                        x, float y, int cx, int cy) {
        alphaViewAngle = alphaAnglePrev + 0.2f * (xTouchDown - x);
        betaViewAngle += 0.2f * (yTouchDown - y);

        if (betaViewAngle < 0) betaViewAngle = 0;
        if (betaViewAngle > 180) betaViewAngle = 180;

        yTouchDown = y;
        return true;
    }
}