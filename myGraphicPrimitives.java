package com.example.kulubecioglu_lab3_gles;

public class myGraphicPrimitives {

    // Add a quad (dörtgen) - 2 üçgenle oluşturulur
    public static int addQuadXYZRGB(float[] array, int pos,
                                    float x1, float y1, float z1,
                                    float x2, float y2, float z2,
                                    float x3, float y3, float z3,
                                    float x4, float y4, float z4,
                                    float r, float g, float b)
    {

        // Triangle 1: v1, v2, v3
        pos = addTriangleXYZRGB(array, pos,
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3,
                r, g, b);

        // Triangle 2: v1, v3, v4
        pos = addTriangleXYZRGB(array, pos,
                x1, y1, z1,
                x3, y3, z3,
                x4, y4, z4,
                r, g, b);

        return pos;
    }

    // Add a triangle
    public static int addTriangleXYZRGB(float[] array, int pos,
                                        float x1, float y1, float z1,
                                        float x2, float y2, float z2,
                                        float x3, float y3, float z3,
                                        float r, float g, float b) {

        float[] vertices = {
                x1, y1, z1, r, g, b,
                x2, y2, z2, r, g, b,
                x3, y3, z3, r, g, b
        };

        for (float v : vertices) array[pos++] = v;
        return pos;
    }

    // Add a chessboard - nx * ny kare (her biri quad)
    public static int addChessXYZRGB(float[] array, int pos,
                                     float x0, float y0, float z0,
                                     int nx, int ny, float size) {

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                float x = x0 + i * size;
                float y = y0 + j * size;
                float z = z0;

                float r = ((i + j) % 2 == 0) ? 1.0f : 0.3f;
                float g = ((i + j) % 2 == 0) ? 1.0f : 0.3f;
                float b = ((i + j) % 2 == 0) ? 1.0f : 0.3f;

                pos = addQuadXYZRGB(array, pos,
                        x, y, z,
                        x + size, y, z,
                        x + size, y + size, z,
                        x, y + size, z,
                        r, g, b);
            }
        }
        return pos;
    }

    // Add a cube (6 quad)
    public static int addCubeXYZRGB(float[] array, int pos,
                                    float cx, float cy, float cz,
                                    float size, float r, float g, float b) {

        float half = size / 2;

        float x1 = cx - half, x2 = cx + half;
        float y1 = cy - half, y2 = cy + half;
        float z1 = cz - half, z2 = cz + half;

        // Front
        pos = addQuadXYZRGB(array, pos,
                x1, y1, z2,
                x2, y1, z2,
                x2, y2, z2,
                x1, y2, z2,
                r, g, b);

        // Back
        pos = addQuadXYZRGB(array, pos,
                x1, y1, z1,
                x2, y1, z1,
                x2, y2, z1,
                x1, y2, z1,
                r * 0.7f, g * 0.7f, b * 0.7f);

        // Left
        pos = addQuadXYZRGB(array, pos,
                x1, y1, z1,
                x1, y1, z2,
                x1, y2, z2,
                x1, y2, z1,
                r * 0.6f, g * 0.6f, b * 0.6f);

        // Right
        pos = addQuadXYZRGB(array, pos,
                x2, y1, z1,
                x2, y1, z2,
                x2, y2, z2,
                x2, y2, z1,
                r * 0.8f, g * 0.8f, b * 0.8f);

        // Top
        pos = addQuadXYZRGB(array, pos,
                x1, y2, z1,
                x2, y2, z1,
                x2, y2, z2,
                x1, y2, z2,
                r * 0.9f, g * 0.9f, b * 0.9f);

        // Bottom
        pos = addQuadXYZRGB(array, pos,
                x1, y1, z1,
                x2, y1, z1,
                x2, y1, z2,
                x1, y1, z2,
                r * 0.5f, g * 0.5f, b * 0.5f);

        return pos;
    }

    public static int addPyramidXYZRGB(float[] array, int pos,
                                       float cx, float cy, float cz,
                                       float r, float g, float b) {
        float baseSize = 1.0f;
        float height = 1.0f;

        float half = baseSize / 2;

        // Taban köşeleri
        float x1 = cx - half, y1 = cy - half, z1 = cz;
        float x2 = cx + half, y2 = cy - half, z2 = cz;
        float x3 = cx + half, y3 = cy + half, z3 = cz;
        float x4 = cx - half, y4 = cy + half, z4 = cz;

        // Tepe noktası
        float xa = cx, ya = cy, za = cz + height;

        // Daha hoş bir soft sarımsı ton (örnek: altın gibi)
        float newR = 1.0f;
        float newG = 0.84f;
        float newB = 0.3f;

        // 4 yan yüzey (sadece tepeye bağlı üçgenler)
        pos = addTriangleXYZRGB(array, pos, xa, ya, za, x1, y1, z1, x2, y2, z2, newR, newG, newB);
        pos = addTriangleXYZRGB(array, pos, xa, ya, za, x2, y2, z2, x3, y3, z3, newR, newG, newB);
        pos = addTriangleXYZRGB(array, pos, xa, ya, za, x3, y3, z3, x4, y4, z4, newR, newG, newB);
        pos = addTriangleXYZRGB(array, pos, xa, ya, za, x4, y4, z4, x1, y1, z1, newR, newG, newB);

        return pos;
    }


}
