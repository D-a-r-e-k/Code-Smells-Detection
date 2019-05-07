/** Render a solid colored triangle. */
private void renderFlatTriangle(Vec2 pos1, double zf1, Vec2 pos2, double zf2, Vec2 pos3, double zf3, int width, int height, double clip, int mode, RGBColor color) {
    int x1, y1, z1, x2, y2, z2, x3, y3, z3;
    int dx1, dx2, dy1, dy2, dz1, dz2, mx1, mx2, mz1, mz2;
    int xstart, xend, yend, zstart, zend, y, z, dz, left, right, i, index;
    int clipDist = (int) (clip * 65535.0), r, g, b, red, green, blue, col;
    if (mode == MODE_COPY) {
        col = color.getARGB();
        red = green = blue = 0;
    } else {
        col = 0;
        red = (int) (color.getRed() * 255.0f);
        green = (int) (color.getGreen() * 255.0f);
        blue = (int) (color.getBlue() * 255.0f);
    }
    if (pos1.y <= pos2.y && pos1.y <= pos3.y) {
        x1 = ((int) pos1.x) << 16;
        y1 = ((int) pos1.y);
        z1 = (int) (zf1 * 65535.0);
        if (pos2.y < pos3.y) {
            x2 = ((int) pos2.x) << 16;
            y2 = ((int) pos2.y);
            z2 = (int) (zf2 * 65535.0);
            x3 = ((int) pos3.x) << 16;
            y3 = ((int) pos3.y);
            z3 = (int) (zf3 * 65535.0);
        } else {
            x2 = ((int) pos3.x) << 16;
            y2 = ((int) pos3.y);
            z2 = (int) (zf3 * 65535.0);
            x3 = ((int) pos2.x) << 16;
            y3 = ((int) pos2.y);
            z3 = (int) (zf2 * 65535.0);
        }
    } else if (pos2.y <= pos1.y && pos2.y <= pos3.y) {
        x1 = ((int) pos2.x) << 16;
        y1 = ((int) pos2.y);
        z1 = (int) (zf2 * 65535.0);
        if (pos1.y < pos3.y) {
            x2 = ((int) pos1.x) << 16;
            y2 = ((int) pos1.y);
            z2 = (int) (zf1 * 65535.0);
            x3 = ((int) pos3.x) << 16;
            y3 = ((int) pos3.y);
            z3 = (int) (zf3 * 65535.0);
        } else {
            x2 = ((int) pos3.x) << 16;
            y2 = ((int) pos3.y);
            z2 = (int) (zf3 * 65535.0);
            x3 = ((int) pos1.x) << 16;
            y3 = ((int) pos1.y);
            z3 = (int) (zf1 * 65535.0);
        }
    } else {
        x1 = ((int) pos3.x) << 16;
        y1 = ((int) pos3.y);
        z1 = (int) (zf3 * 65535.0);
        if (pos1.y < pos2.y) {
            x2 = ((int) pos1.x) << 16;
            y2 = ((int) pos1.y);
            z2 = (int) (zf1 * 65535.0);
            x3 = ((int) pos2.x) << 16;
            y3 = ((int) pos2.y);
            z3 = (int) (zf2 * 65535.0);
        } else {
            x2 = ((int) pos2.x) << 16;
            y2 = ((int) pos2.y);
            z2 = (int) (zf2 * 65535.0);
            x3 = ((int) pos1.x) << 16;
            y3 = ((int) pos1.y);
            z3 = (int) (zf1 * 65535.0);
        }
    }
    dx1 = x3 - x1;
    dy1 = y3 - y1;
    dz1 = z3 - z1;
    if (dy1 == 0)
        return;
    dx2 = x2 - x1;
    dy2 = y2 - y1;
    dz2 = z2 - z1;
    mx1 = dx1 / dy1;
    mz1 = dz1 / dy1;
    xstart = xend = x1;
    zstart = zend = z1;
    y = y1;
    if (dy2 != 0) {
        mx2 = dx2 / dy2;
        mz2 = dz2 / dy2;
        if (y2 < 0) {
            xstart += mx1 * dy2;
            xend += mx2 * dy2;
            zstart += mz1 * dy2;
            zend += mz2 * dy2;
            y = y2;
        } else if (y < 0) {
            xstart -= mx1 * y;
            xend -= mx2 * y;
            zstart -= mz1 * y;
            zend -= mz2 * y;
            y = 0;
        }
        yend = (y2 < height ? y2 : height);
        index = y * width;
        while (y < yend) {
            if (xstart < xend) {
                left = xstart >> 16;
                right = xend >> 16;
                z = zstart;
                dz = zend - zstart;
            } else {
                left = xend >> 16;
                right = xstart >> 16;
                z = zend;
                dz = zstart - zend;
            }
            if (left != right) {
                dz /= (right - left);
                if (left < 0) {
                    z -= left * dz;
                    left = 0;
                }
                if (right > width)
                    right = width;
                if (mode == MODE_COPY) {
                    for (i = left; i < right; i++) {
                        if (z < zbuffer[index + i] && z > clipDist) {
                            pixel[index + i] = col;
                            zbuffer[index + i] = z;
                        }
                        z += dz;
                    }
                } else if (mode == MODE_ADD) {
                    for (i = left; i < right; i++) {
                        if (z > clipDist) {
                            r = ((pixel[index + i] & 0xFF0000) >> 16) + red;
                            g = ((pixel[index + i] & 0xFF00) >> 8) + green;
                            b = (pixel[index + i] & 0xFF) + blue;
                            if (r > 255)
                                r = 255;
                            if (g > 255)
                                g = 255;
                            if (b > 255)
                                b = 255;
                            pixel[index + i] = 0xFF000000 + (r << 16) + (g << 8) + b;
                        }
                        z += dz;
                    }
                } else {
                    for (i = left; i < right; i++) {
                        if (z > clipDist) {
                            r = ((pixel[index + i] & 0xFF0000) >> 16) - red;
                            g = ((pixel[index + i] & 0xFF00) >> 8) - green;
                            b = (pixel[index + i] & 0xFF) - blue;
                            if (r < 0)
                                r = 0;
                            if (g < 0)
                                g = 0;
                            if (b < 0)
                                b = 0;
                            pixel[index + i] = 0xFF000000 + (r << 16) + (g << 8) + b;
                        }
                        z += dz;
                    }
                }
            }
            xstart += mx1;
            zstart += mz1;
            xend += mx2;
            zend += mz2;
            index += width;
            y++;
        }
    }
    dx2 = x3 - x2;
    dy2 = y3 - y2;
    dz2 = z3 - z2;
    if (dy2 != 0) {
        mx2 = dx2 / dy2;
        mz2 = dz2 / dy2;
        xend = x2;
        zend = z2;
        if (y < 0) {
            xstart -= mx1 * y;
            xend -= mx2 * y;
            zstart -= mz1 * y;
            zend -= mz2 * y;
            y = 0;
        }
        yend = (y3 < height ? y3 : height);
        index = y * width;
        while (y < yend) {
            if (xstart < xend) {
                left = xstart >> 16;
                right = xend >> 16;
                z = zstart;
                dz = zend - zstart;
            } else {
                left = xend >> 16;
                right = xstart >> 16;
                z = zend;
                dz = zstart - zend;
            }
            if (left != right) {
                dz /= (right - left);
                if (left < 0) {
                    z -= left * dz;
                    left = 0;
                }
                if (right > width)
                    right = width;
                if (mode == MODE_COPY) {
                    for (i = left; i < right; i++) {
                        if (z < zbuffer[index + i] && z > clipDist) {
                            pixel[index + i] = col;
                            zbuffer[index + i] = z;
                        }
                        z += dz;
                    }
                } else if (mode == MODE_ADD) {
                    for (i = left; i < right; i++) {
                        if (z > clipDist) {
                            r = ((pixel[index + i] & 0xFF0000) >> 16) + red;
                            g = ((pixel[index + i] & 0xFF00) >> 8) + green;
                            b = (pixel[index + i] & 0xFF) + blue;
                            if (r > 255)
                                r = 255;
                            if (g > 255)
                                g = 255;
                            if (b > 255)
                                b = 255;
                            pixel[index + i] = 0xFF000000 + (r << 16) + (g << 8) + b;
                        }
                        z += dz;
                    }
                } else {
                    for (i = left; i < right; i++) {
                        if (z > clipDist) {
                            r = ((pixel[index + i] & 0xFF0000) >> 16) - red;
                            g = ((pixel[index + i] & 0xFF00) >> 8) - green;
                            b = (pixel[index + i] & 0xFF) - blue;
                            if (r < 0)
                                r = 0;
                            if (g < 0)
                                g = 0;
                            if (b < 0)
                                b = 0;
                            pixel[index + i] = 0xFF000000 + (r << 16) + (g << 8) + b;
                        }
                        z += dz;
                    }
                }
            }
            xstart += mx1;
            zstart += mz1;
            xend += mx2;
            zend += mz2;
            index += width;
            y++;
        }
    }
}
