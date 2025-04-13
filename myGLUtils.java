
package com.example.kulubecioglu_lab3_gles;

import android.opengl.GLES32;
import android.util.Log;

public class myGLUtils {

    // Shader programı oluştur
    public static int createProgram(String vertexSource, String fragmentSource) {
        Log.d("GLUtils", "createProgram() çağrıldı");

        int vertexShader = loadShader(GLES32.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            Log.e("GLUtils", "Vertex shader oluşturulamadı.");
            return 0;
        } else {
            Log.d("GLUtils", "Vertex shader oluşturuldu: ID=" + vertexShader);
        }

        int fragmentShader = loadShader(GLES32.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            Log.e("GLUtils", "Fragment shader oluşturulamadı.");
            return 0;
        } else {
            Log.d("GLUtils", "Fragment shader oluşturuldu: ID=" + fragmentShader);
        }

        int program = GLES32.glCreateProgram();
        if (program == 0) {
            Log.e("GLUtils", "Program oluşturulamadı.");
            return 0;
        }

        GLES32.glAttachShader(program, vertexShader);
        GLES32.glAttachShader(program, fragmentShader);
        GLES32.glLinkProgram(program);

        int[] linkStatus = new int[1];
        GLES32.glGetProgramiv(program, GLES32.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES32.GL_TRUE) {
            Log.e("GLUtils", "Program link hatası: " + GLES32.glGetProgramInfoLog(program));
            GLES32.glDeleteProgram(program);
            return 0;
        }

        Log.d("GLUtils", "Shader program başarıyla oluşturuldu. ID=" + program);

        GLES32.glDeleteShader(vertexShader);
        GLES32.glDeleteShader(fragmentShader);

        return program;
    }

    // Shader yükle
    public static int loadShader(int type, String shaderSource) {
        String shaderType = (type == GLES32.GL_VERTEX_SHADER) ? "VERTEX" : "FRAGMENT";
        Log.d("GLUtils", shaderType + " shader yükleniyor...");

        int shader = GLES32.glCreateShader(type);
        GLES32.glShaderSource(shader, shaderSource);
        GLES32.glCompileShader(shader);

        int[] compiled = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("GLUtils", shaderType + " shader compile hatası: " + GLES32.glGetShaderInfoLog(shader));
            GLES32.glDeleteShader(shader);
            return 0;
        }

        Log.d("GLUtils", shaderType + " shader başarıyla derlendi. ID=" + shader);
        return shader;
    }

    // Vertex verisini GPU’ya bağla
    public static void bindVertexArrayWithColor(float[] arrayVertex, int glProgram) {
        Log.d("GLUtils", "Vertex buffer bind ediliyor...");
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocateDirect(arrayVertex.length * 4);
        bb.order(java.nio.ByteOrder.nativeOrder());
        java.nio.FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(arrayVertex);
        vertexBuffer.position(0);

        int[] vbo = new int[1];
        GLES32.glGenBuffers(1, vbo, 0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vbo[0]);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, arrayVertex.length * 4, vertexBuffer, GLES32.GL_STATIC_DRAW);

        int positionHandle = GLES32.glGetAttribLocation(glProgram, "vPosition");
        int colorHandle = GLES32.glGetAttribLocation(glProgram, "vColor");

        Log.d("GLUtils", "positionHandle: " + positionHandle + ", colorHandle: " + colorHandle);

        if (positionHandle >= 0) {
            GLES32.glEnableVertexAttribArray(positionHandle);
            GLES32.glVertexAttribPointer(positionHandle, 3, GLES32.GL_FLOAT, false, 6 * 4, 0);
        } else {
            Log.e("GLUtils", "vPosition attrib bulunamadı!");
        }

        if (colorHandle >= 0) {
            GLES32.glEnableVertexAttribArray(colorHandle);
            GLES32.glVertexAttribPointer(colorHandle, 3, GLES32.GL_FLOAT, false, 6 * 4, 3 * 4);
        } else {
            Log.e("GLUtils", "vColor attrib bulunamadı!");
        }

        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        Log.d("GLUtils", "Vertex buffer bind tamamlandı.");
    }
}
