/** Given an RGBColor and a transparency value, calculate the ARGB value. */
private int calcARGB(RGBColor color, RGBColor transparency) {
    double t = (transparency.getRed() + transparency.getGreen() + transparency.getBlue()) / 3.0;
    if (!transparentBackground || t <= 0.0)
        return color.getARGB();
    if (t >= 1.0)
        return 0;
    double scale = 255.0 / (1.0 - t);
    int a, r, g, b;
    a = (int) (255.0 * (1.0 - t));
    r = (int) (color.getRed() * scale);
    g = (int) (color.getGreen() * scale);
    b = (int) (color.getBlue() * scale);
    if (r < 0)
        r = 0;
    if (r > 255)
        r = 255;
    if (g < 0)
        g = 0;
    if (g > 255)
        g = 255;
    if (b < 0)
        b = 0;
    if (b > 255)
        b = 255;
    return (a << 24) + (r << 16) + (g << 8) + b;
}
