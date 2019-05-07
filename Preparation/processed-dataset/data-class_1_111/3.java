public Ray getRay(float x, float y, int imageWidth, int imageHeight, double lensX, double lensY, double time) {
    float du = -au + ((2.0f * au * x) / (imageWidth - 1.0f));
    float dv = -av + ((2.0f * av * y) / (imageHeight - 1.0f));
    return new Ray(0, 0, 0, du, dv, -1);
}
