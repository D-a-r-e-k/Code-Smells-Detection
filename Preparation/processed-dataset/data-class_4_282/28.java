/** Render a triangle with hybrid Gouraud/Phong shading. */
private void renderTriangleHybrid(Vec2 pos1, float zf1, Vec3 vert1, Vec3 normf1, double uf1, double vf1, RGBColor diffuse1, Vec2 pos2, float zf2, Vec3 vert2, Vec3 normf2, double uf2, double vf2, RGBColor diffuse2, Vec2 pos3, float zf3, Vec3 vert3, Vec3 normf3, double uf3, double vf3, RGBColor diffuse3, RenderingTriangle tri, Vec3 viewdir, Vec3 faceNorm, double clip, double viewdot, boolean isBackface, ObjectMaterialInfo material, RasterContext context) {
    double x1, x2, x3, y1, y2, y3;
    double dx1, dx2, dy1, dy2, mx1, mx2;
    double xstart, xend;
    float z1, z2, z3, dz1, dz2, mz1, mz2, zstart, zend, z, zl, dz;
    double u1, u2, u3, v1, v2, v3, du1, du2, dv1, dv2, mu1, mu2, mv1, mv2;
    double ustart, uend, vstart, vend, u, v, ul, vl, wl, du, dv;
    RGBColor dif1, dif2, dif3, specular = context.tempColor[1], highlight = context.tempColor[2];
    Vec3 norm1, norm2, norm3;
    float ddifred1, ddifred2, ddifgreen1, ddifgreen2, ddifblue1, ddifblue2;
    float mdifred1, mdifred2, mdifgreen1, mdifgreen2, mdifblue1, mdifblue2;
    double dnormx1, dnormx2, dnormy1, dnormy2, dnormz1, dnormz2;
    double mnormx1, mnormx2, mnormy1, mnormy2, mnormz1, mnormz2;
    float difredstart, difredend, difgreenstart, difgreenend, difbluestart, difblueend;
    double normxstart, normxend, normystart, normyend, normzstart, normzend;
    float difred, difgreen, difblue, ddifred, ddifgreen, ddifblue;
    double normx, normy, normz, dnormx, dnormy, dnormz;
    float denom;
    int left, right, i, index, yend, y, lastAddColor = 0, lastMultColor = 0;
    boolean doSubsample = (subsample > 1), repeat;
    TextureSpec surfSpec = context.surfSpec;
    // Order the three vertices by y coordinate. 
    if (pos1.y <= pos2.y && pos1.y <= pos3.y) {
        x1 = pos1.x;
        y1 = pos1.y;
        z1 = zf1;
        u1 = uf1;
        v1 = vf1;
        dif1 = diffuse1;
        norm1 = normf1;
        if (pos2.y < pos3.y) {
            x2 = pos2.x;
            y2 = pos2.y;
            z2 = zf2;
            u2 = uf2;
            v2 = vf2;
            dif2 = diffuse2;
            norm2 = normf2;
            x3 = pos3.x;
            y3 = pos3.y;
            z3 = zf3;
            u3 = uf3;
            v3 = vf3;
            dif3 = diffuse3;
            norm3 = normf3;
        } else {
            x2 = pos3.x;
            y2 = pos3.y;
            z2 = zf3;
            u2 = uf3;
            v2 = vf3;
            dif2 = diffuse3;
            norm2 = normf3;
            x3 = pos2.x;
            y3 = pos2.y;
            z3 = zf2;
            u3 = uf2;
            v3 = vf2;
            dif3 = diffuse2;
            norm3 = normf2;
        }
    } else if (pos2.y <= pos1.y && pos2.y <= pos3.y) {
        x1 = pos2.x;
        y1 = pos2.y;
        z1 = zf2;
        u1 = uf2;
        v1 = vf2;
        dif1 = diffuse2;
        norm1 = normf2;
        if (pos1.y < pos3.y) {
            x2 = pos1.x;
            y2 = pos1.y;
            z2 = zf1;
            u2 = uf1;
            v2 = vf1;
            dif2 = diffuse1;
            norm2 = normf1;
            x3 = pos3.x;
            y3 = pos3.y;
            z3 = zf3;
            u3 = uf3;
            v3 = vf3;
            dif3 = diffuse3;
            norm3 = normf3;
        } else {
            x2 = pos3.x;
            y2 = pos3.y;
            z2 = zf3;
            u2 = uf3;
            v2 = vf3;
            dif2 = diffuse3;
            norm2 = normf3;
            x3 = pos1.x;
            y3 = pos1.y;
            z3 = zf1;
            u3 = uf1;
            v3 = vf1;
            dif3 = diffuse1;
            norm3 = normf1;
        }
    } else {
        x1 = pos3.x;
        y1 = pos3.y;
        z1 = zf3;
        u1 = uf3;
        v1 = vf3;
        dif1 = diffuse3;
        norm1 = normf3;
        if (pos1.y < pos2.y) {
            x2 = pos1.x;
            y2 = pos1.y;
            z2 = zf1;
            u2 = uf1;
            v2 = vf1;
            dif2 = diffuse1;
            norm2 = normf1;
            x3 = pos2.x;
            y3 = pos2.y;
            z3 = zf2;
            u3 = uf2;
            v3 = vf2;
            dif3 = diffuse2;
            norm3 = normf2;
        } else {
            x2 = pos2.x;
            y2 = pos2.y;
            z2 = zf2;
            u2 = uf2;
            v2 = vf2;
            dif2 = diffuse2;
            norm2 = normf2;
            x3 = pos1.x;
            y3 = pos1.y;
            z3 = zf1;
            u3 = uf1;
            v3 = vf1;
            dif3 = diffuse1;
            norm3 = normf1;
        }
    }
    // Round the coordinates to the nearest pixel to avoid errors during rasterization. 
    x1 = FastMath.round(x1);
    y1 = FastMath.round(y1);
    x2 = FastMath.round(x2);
    y2 = FastMath.round(y2);
    x3 = FastMath.round(x3);
    y3 = FastMath.round(y3);
    // Calculate intermediate variables. 
    z1 = 1.0f / z1;
    u1 *= z1;
    v1 *= z1;
    z2 = 1.0f / z2;
    u2 *= z2;
    v2 *= z2;
    z3 = 1.0f / z3;
    u3 *= z3;
    v3 *= z3;
    dx1 = x3 - x1;
    dy1 = y3 - y1;
    dz1 = z3 - z1;
    if (dy1 == 0)
        return;
    du1 = u3 - u1;
    dv1 = v3 - v1;
    ddifred1 = dif3.getRed() - dif1.getRed();
    ddifgreen1 = dif3.getGreen() - dif1.getGreen();
    ddifblue1 = dif3.getBlue() - dif1.getBlue();
    dnormx1 = norm3.x - norm1.x;
    dnormy1 = norm3.y - norm1.y;
    dnormz1 = norm3.z - norm1.z;
    dx2 = x2 - x1;
    dy2 = y2 - y1;
    dz2 = z2 - z1;
    du2 = u2 - u1;
    dv2 = v2 - v1;
    ddifred2 = dif2.getRed() - dif1.getRed();
    ddifgreen2 = dif2.getGreen() - dif1.getGreen();
    ddifblue2 = dif2.getBlue() - dif1.getBlue();
    dnormx2 = norm2.x - norm1.x;
    dnormy2 = norm2.y - norm1.y;
    dnormz2 = norm2.z - norm1.z;
    denom = (float) (1.0 / dy1);
    mx1 = dx1 * denom;
    mz1 = dz1 * denom;
    mu1 = du1 * denom;
    mv1 = dv1 * denom;
    mdifred1 = ddifred1 * denom;
    mdifgreen1 = ddifgreen1 * denom;
    mdifblue1 = ddifblue1 * denom;
    mnormx1 = dnormx1 * denom;
    mnormy1 = dnormy1 * denom;
    mnormz1 = dnormz1 * denom;
    xstart = xend = x1;
    zstart = zend = z1;
    ustart = uend = u1;
    vstart = vend = v1;
    difredstart = difredend = dif1.getRed();
    difgreenstart = difgreenend = dif1.getGreen();
    difbluestart = difblueend = dif1.getBlue();
    normxstart = normxend = norm1.x;
    normystart = normyend = norm1.y;
    normzstart = normzend = norm1.z;
    y = FastMath.round(y1);
    if (dy2 > 0.0) {
        denom = (float) (1.0 / dy2);
        mx2 = dx2 * denom;
        mz2 = dz2 * denom;
        mu2 = du2 * denom;
        mv2 = dv2 * denom;
        mdifred2 = ddifred2 * denom;
        mdifgreen2 = ddifgreen2 * denom;
        mdifblue2 = ddifblue2 * denom;
        mnormx2 = dnormx2 * denom;
        mnormy2 = dnormy2 * denom;
        mnormz2 = dnormz2 * denom;
        if (y2 < 0) {
            xstart += mx1 * dy2;
            xend += mx2 * dy2;
            zstart += mz1 * dy2;
            zend += mz2 * dy2;
            ustart += mu1 * dy2;
            uend += mu2 * dy2;
            vstart += mv1 * dy2;
            vend += mv2 * dy2;
            difredstart += mdifred1 * dy2;
            difredend += mdifred2 * dy2;
            difgreenstart += mdifgreen1 * dy2;
            difgreenend += mdifgreen2 * dy2;
            difbluestart += mdifblue1 * dy2;
            difblueend += mdifblue2 * dy2;
            normxstart += mnormx1 * dy2;
            normxend += mnormx2 * dy2;
            normystart += mnormy1 * dy2;
            normyend += mnormy2 * dy2;
            normzstart += mnormz1 * dy2;
            normzend += mnormz2 * dy2;
            y = FastMath.round(y2);
        } else if (y < 0) {
            xstart -= mx1 * y;
            xend -= mx2 * y;
            zstart -= mz1 * y;
            zend -= mz2 * y;
            ustart -= mu1 * y;
            uend -= mu2 * y;
            vstart -= mv1 * y;
            vend -= mv2 * y;
            difredstart -= mdifred1 * y;
            difredend -= mdifred2 * y;
            difgreenstart -= mdifgreen1 * y;
            difgreenend -= mdifgreen2 * y;
            difbluestart -= mdifblue1 * y;
            difblueend -= mdifblue2 * y;
            normxstart -= mnormx1 * y;
            normxend -= mnormx2 * y;
            normystart -= mnormy1 * y;
            normyend -= mnormy2 * y;
            normzstart -= mnormz1 * y;
            normzend -= mnormz2 * y;
            y = 0;
        }
        yend = FastMath.round(y2);
        if (yend > height)
            yend = height;
        index = y * width;
        // Rasterize the top half of the triangle, 
        while (y < yend) {
            if (xstart < xend) {
                left = FastMath.round(xstart);
                right = FastMath.round(xend);
                z = zstart;
                dz = zend - zstart;
                u = ustart;
                du = uend - ustart;
                v = vstart;
                dv = vend - vstart;
                difred = difredstart;
                ddifred = difredend - difredstart;
                difgreen = difgreenstart;
                ddifgreen = difgreenend - difgreenstart;
                difblue = difbluestart;
                ddifblue = difblueend - difbluestart;
                normx = normxstart;
                dnormx = normxend - normxstart;
                normy = normystart;
                dnormy = normyend - normystart;
                normz = normzstart;
                dnormz = normzend - normzstart;
            } else {
                left = FastMath.round(xend);
                right = FastMath.round(xstart);
                z = zend;
                dz = zstart - zend;
                u = uend;
                du = ustart - uend;
                v = vend;
                dv = vstart - vend;
                difred = difredend;
                ddifred = difredstart - difredend;
                difgreen = difgreenend;
                ddifgreen = difgreenstart - difgreenend;
                difblue = difblueend;
                ddifblue = difbluestart - difblueend;
                normx = normxend;
                dnormx = normxstart - normxend;
                normy = normyend;
                dnormy = normystart - normyend;
                normz = normzend;
                dnormz = normzstart - normzend;
            }
            if (left != right) {
                if (xend == xstart)
                    denom = 1.0f;
                else if (xend > xstart)
                    denom = (float) (1.0 / (xend - xstart));
                else
                    denom = (float) (1.0 / (xstart - xend));
                dz *= denom;
                du *= denom;
                dv *= denom;
                ddifred *= denom;
                ddifgreen *= denom;
                ddifblue *= denom;
                dnormx *= denom;
                dnormy *= denom;
                dnormz *= denom;
                if (left < 0) {
                    z -= dz * left;
                    u -= du * left;
                    v -= dv * left;
                    difred -= ddifred * left;
                    difgreen -= ddifgreen * left;
                    difblue -= ddifblue * left;
                    normx -= dnormx * left;
                    normy -= dnormy * left;
                    normz -= dnormz * left;
                    left = 0;
                }
                if (right > width)
                    right = width;
                repeat = false;
                for (i = left; i < right; i++) {
                    zl = 1.0f / z;
                    if (zl < fragment[index + i].getOpaqueDepth() && zl > clip) {
                        if (!repeat || (i % subsample == 0)) {
                            ul = u * zl;
                            vl = v * zl;
                            wl = 1.0 - ul - vl;
                            tri.getTextureSpec(surfSpec, viewdot, ul, vl, wl, smoothScale * z, time);
                            if (surfSpec.hilight.getRed() == 0.0f && surfSpec.hilight.getGreen() == 0.0f && surfSpec.hilight.getBlue() == 0.0f && surfSpec.specular.getRed() == 0.0f && surfSpec.specular.getGreen() == 0.0f && surfSpec.specular.getBlue() == 0.0f)
                                context.tempColor[0].setRGB(surfSpec.diffuse.getRed() * difred + surfSpec.emissive.getRed(), surfSpec.diffuse.getGreen() * difgreen + surfSpec.emissive.getGreen(), surfSpec.diffuse.getBlue() * difblue + surfSpec.emissive.getBlue());
                            else {
                                if (positionNeeded)
                                    context.tempVec[2].set(ul * vert1.x + vl * vert2.x + wl * vert3.x, ul * vert1.y + vl * vert2.y + wl * vert3.y, ul * vert1.z + vl * vert2.z + wl * vert3.z);
                                context.tempVec[3].set(normx, normy, normz);
                                context.tempVec[3].normalize();
                                calcLight(context.tempVec[2], context.tempVec[3], viewdir, faceNorm, surfSpec.roughness, null, specular, highlight, context);
                                context.tempColor[0].setRGB(surfSpec.diffuse.getRed() * difred + surfSpec.hilight.getRed() * highlight.getRed() + surfSpec.specular.getRed() * specular.getRed() + surfSpec.emissive.getRed(), surfSpec.diffuse.getGreen() * difgreen + surfSpec.hilight.getGreen() * highlight.getGreen() + surfSpec.specular.getGreen() * specular.getGreen() + surfSpec.emissive.getGreen(), surfSpec.diffuse.getBlue() * difblue + surfSpec.hilight.getBlue() * highlight.getBlue() + surfSpec.specular.getBlue() * specular.getBlue() + surfSpec.emissive.getBlue());
                            }
                            lastAddColor = context.tempColor[0].getERGB();
                            lastMultColor = surfSpec.transparent.getERGB();
                        }
                        context.fragment[i] = createFragment(lastAddColor, lastMultColor, zl, material, isBackface);
                        repeat = doSubsample;
                    } else {
                        context.fragment[i] = null;
                        repeat = false;
                    }
                    z += dz;
                    u += du;
                    v += dv;
                    difred += ddifred;
                    difgreen += ddifgreen;
                    difblue += ddifblue;
                    normx += dnormx;
                    normy += dnormy;
                    normz += dnormz;
                }
                recordRow(y, left, right, context);
            }
            xstart += mx1;
            zstart += mz1;
            ustart += mu1;
            vstart += mv1;
            difredstart += mdifred1;
            difgreenstart += mdifgreen1;
            difbluestart += mdifblue1;
            normxstart += mnormx1;
            normystart += mnormy1;
            normzstart += mnormz1;
            xend += mx2;
            zend += mz2;
            uend += mu2;
            vend += mv2;
            difredend += mdifred2;
            difgreenend += mdifgreen2;
            difblueend += mdifblue2;
            normxend += mnormx2;
            normyend += mnormy2;
            normzend += mnormz2;
            index += width;
            y++;
        }
    }
    // Calculate intermediate variables for the bottom half of the triangle. 
    dx2 = x3 - x2;
    dy2 = y3 - y2;
    dz2 = z3 - z2;
    du2 = u3 - u2;
    dv2 = v3 - v2;
    ddifred2 = dif3.getRed() - dif2.getRed();
    ddifgreen2 = dif3.getGreen() - dif2.getGreen();
    ddifblue2 = dif3.getBlue() - dif2.getBlue();
    dnormx2 = norm3.x - norm2.x;
    dnormy2 = norm3.y - norm2.y;
    dnormz2 = norm3.z - norm2.z;
    if (dy2 > 0.0) {
        denom = (float) (1.0 / dy2);
        mx2 = dx2 * denom;
        mz2 = dz2 * denom;
        mu2 = du2 * denom;
        mv2 = dv2 * denom;
        mdifred2 = ddifred2 * denom;
        mdifgreen2 = ddifgreen2 * denom;
        mdifblue2 = ddifblue2 * denom;
        mnormx2 = dnormx2 * denom;
        mnormy2 = dnormy2 * denom;
        mnormz2 = dnormz2 * denom;
        xend = x2;
        zend = z2;
        uend = u2;
        vend = v2;
        difredend = dif2.getRed();
        difgreenend = dif2.getGreen();
        difblueend = dif2.getBlue();
        normxend = norm2.x;
        normyend = norm2.y;
        normzend = norm2.z;
        if (y < 0) {
            xstart -= mx1 * y;
            xend -= mx2 * y;
            zstart -= mz1 * y;
            zend -= mz2 * y;
            ustart -= mu1 * y;
            uend -= mu2 * y;
            vstart -= mv1 * y;
            vend -= mv2 * y;
            difredstart -= mdifred1 * y;
            difredend -= mdifred2 * y;
            difgreenstart -= mdifgreen1 * y;
            difgreenend -= mdifgreen2 * y;
            difbluestart -= mdifblue1 * y;
            difblueend -= mdifblue2 * y;
            normxstart -= mnormx1 * y;
            normxend -= mnormx2 * y;
            normystart -= mnormy1 * y;
            normyend -= mnormy2 * y;
            normzstart -= mnormz1 * y;
            normzend -= mnormz2 * y;
            y = 0;
        }
        yend = FastMath.round(y3 < height ? y3 : height);
        index = y * width;
        // Rasterize the bottom half of the triangle, 
        while (y < yend) {
            if (xstart < xend) {
                left = FastMath.round(xstart);
                right = FastMath.round(xend);
                z = zstart;
                dz = zend - zstart;
                u = ustart;
                du = uend - ustart;
                v = vstart;
                dv = vend - vstart;
                difred = difredstart;
                ddifred = difredend - difredstart;
                difgreen = difgreenstart;
                ddifgreen = difgreenend - difgreenstart;
                difblue = difbluestart;
                ddifblue = difblueend - difbluestart;
                normx = normxstart;
                dnormx = normxend - normxstart;
                normy = normystart;
                dnormy = normyend - normystart;
                normz = normzstart;
                dnormz = normzend - normzstart;
            } else {
                left = FastMath.round(xend);
                right = FastMath.round(xstart);
                z = zend;
                dz = zstart - zend;
                u = uend;
                du = ustart - uend;
                v = vend;
                dv = vstart - vend;
                difred = difredend;
                ddifred = difredstart - difredend;
                difgreen = difgreenend;
                ddifgreen = difgreenstart - difgreenend;
                difblue = difblueend;
                ddifblue = difbluestart - difblueend;
                normx = normxend;
                dnormx = normxstart - normxend;
                normy = normyend;
                dnormy = normystart - normyend;
                normz = normzend;
                dnormz = normzstart - normzend;
            }
            if (left != right) {
                if (xend == xstart)
                    denom = 1.0f;
                else if (xend > xstart)
                    denom = (float) (1.0 / (xend - xstart));
                else
                    denom = (float) (1.0 / (xstart - xend));
                dz *= denom;
                du *= denom;
                dv *= denom;
                ddifred *= denom;
                ddifgreen *= denom;
                ddifblue *= denom;
                dnormx *= denom;
                dnormy *= denom;
                dnormz *= denom;
                if (left < 0) {
                    z -= dz * left;
                    u -= du * left;
                    v -= dv * left;
                    difred -= ddifred * left;
                    difgreen -= ddifgreen * left;
                    difblue -= ddifblue * left;
                    normx -= dnormx * left;
                    normy -= dnormy * left;
                    normz -= dnormz * left;
                    left = 0;
                }
                if (right > width)
                    right = width;
                repeat = false;
                for (i = left; i < right; i++) {
                    zl = 1.0f / z;
                    if (zl < fragment[index + i].getOpaqueDepth() && zl > clip) {
                        if (!repeat || (i % subsample == 0)) {
                            ul = u * zl;
                            vl = v * zl;
                            wl = 1.0 - ul - vl;
                            tri.getTextureSpec(surfSpec, viewdot, ul, vl, wl, smoothScale * z, time);
                            if (surfSpec.hilight.getRed() == 0.0f && surfSpec.hilight.getGreen() == 0.0f && surfSpec.hilight.getBlue() == 0.0f && surfSpec.specular.getRed() == 0.0f && surfSpec.specular.getGreen() == 0.0f && surfSpec.specular.getBlue() == 0.0f)
                                context.tempColor[0].setRGB(surfSpec.diffuse.getRed() * difred + surfSpec.emissive.getRed(), surfSpec.diffuse.getGreen() * difgreen + surfSpec.emissive.getGreen(), surfSpec.diffuse.getBlue() * difblue + surfSpec.emissive.getBlue());
                            else {
                                if (positionNeeded)
                                    context.tempVec[2].set(ul * vert1.x + vl * vert2.x + wl * vert3.x, ul * vert1.y + vl * vert2.y + wl * vert3.y, ul * vert1.z + vl * vert2.z + wl * vert3.z);
                                context.tempVec[3].set(normx, normy, normz);
                                context.tempVec[3].normalize();
                                calcLight(context.tempVec[2], context.tempVec[3], viewdir, faceNorm, surfSpec.roughness, null, specular, highlight, context);
                                context.tempColor[0].setRGB(surfSpec.diffuse.getRed() * difred + surfSpec.hilight.getRed() * highlight.getRed() + surfSpec.specular.getRed() * specular.getRed() + surfSpec.emissive.getRed(), surfSpec.diffuse.getGreen() * difgreen + surfSpec.hilight.getGreen() * highlight.getGreen() + surfSpec.specular.getGreen() * specular.getGreen() + surfSpec.emissive.getGreen(), surfSpec.diffuse.getBlue() * difblue + surfSpec.hilight.getBlue() * highlight.getBlue() + surfSpec.specular.getBlue() * specular.getBlue() + surfSpec.emissive.getBlue());
                            }
                            lastAddColor = context.tempColor[0].getERGB();
                            lastMultColor = surfSpec.transparent.getERGB();
                        }
                        context.fragment[i] = createFragment(lastAddColor, lastMultColor, zl, material, isBackface);
                        repeat = doSubsample;
                    } else {
                        context.fragment[i] = null;
                        repeat = false;
                    }
                    z += dz;
                    u += du;
                    v += dv;
                    difred += ddifred;
                    difgreen += ddifgreen;
                    difblue += ddifblue;
                    normx += dnormx;
                    normy += dnormy;
                    normz += dnormz;
                }
                recordRow(y, left, right, context);
            }
            xstart += mx1;
            zstart += mz1;
            ustart += mu1;
            vstart += mv1;
            difredstart += mdifred1;
            difgreenstart += mdifgreen1;
            difbluestart += mdifblue1;
            normxstart += mnormx1;
            normystart += mnormy1;
            normzstart += mnormz1;
            xend += mx2;
            zend += mz2;
            uend += mu2;
            vend += mv2;
            difredend += mdifred2;
            difgreenend += mdifgreen2;
            difblueend += mdifblue2;
            normxend += mnormx2;
            normyend += mnormy2;
            normzend += mnormz2;
            index += width;
            y++;
        }
    }
}
