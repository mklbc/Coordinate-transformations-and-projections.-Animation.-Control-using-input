package com.example.kulubecioglu_lab3_gles;

public class myShadersLibrary {

    // Vertex Shader (konum + renk + matris dönüşümleri)
    public static final String vertexShaderCode5 =
            "#version 300 es\n" +
                    "layout(location = 0) in vec4 vPosition;\n" +
                    "layout(location = 1) in vec4 vColor;\n" +
                    "uniform mat4 uViewMatrix;\n" +
                    "uniform mat4 uProjMatrix;\n" +
                    "uniform mat4 uModelMatrix;\n" +
                    "out vec4 outColor;\n" +
                    "void main() {\n" +
                    "    gl_Position = uProjMatrix * uViewMatrix * uModelMatrix * vPosition;\n" +
                    "    outColor = vColor;\n" +
                    "}";

    // Fragment Shader (renk çıktısı)
    public static final String fragmentShaderCode3 =
            "#version 300 es\n" +
                    "precision mediump float;\n" +
                    "in vec4 outColor;\n" +
                    "out vec4 fragColor;\n" +
                    "void main() {\n" +
                    "    fragColor = outColor;\n" +
                    "}";
}
