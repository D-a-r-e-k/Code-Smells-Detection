/** Calculate the lighting model at a point on a surface.  If diffuse, specular, or highlight
     is null, that component will not be calculated. */
private void calcLight(Vec3 pos, Vec3 norm, Vec3 viewdir, Vec3 faceNorm, double roughness, RGBColor diffuse, RGBColor specular, RGBColor highlight, RasterContext context) {
    Vec3 reflectDir = context.tempVec[0], lightDir = context.tempVec[1];
    double viewDot = viewdir.dot(norm), faceDot = viewdir.dot(faceNorm);
    RGBColor outputColor = context.tempColor[0];
    if (diffuse != null)
        diffuse.copy(ambColor);
    if (highlight != null)
        highlight.setRGB(0.0f, 0.0f, 0.0f);
    if (specular != null) {
        if (envMode == Scene.ENVIRON_SOLID)
            specular.copy(envColor);
        else {
            // Find the reflection direction and add in the environment color. 
            reflectDir.set(norm);
            reflectDir.scale(-2.0 * viewDot);
            reflectDir.add(viewdir);
            context.camera.getViewToWorld().transformDirection(reflectDir);
            envMapping.getTextureSpec(reflectDir, context.surfSpec2, 1.0, smoothScale, time, envParamValue);
            if (envMode == Scene.ENVIRON_DIFFUSE)
                specular.copy(context.surfSpec2.diffuse);
            else
                specular.copy(context.surfSpec2.emissive);
        }
    }
    // Prevent artifacts where the triangle is facing toward the viewer, but the local 
    // interpolated normal is facing away. 
    if (viewDot < 0.0 && faceDot > 0.0)
        viewDot = TOL;
    else if (viewDot > 0.0 && faceDot < 0.0)
        viewDot = -TOL;
    // Loop over the lights and add in each one. 
    for (int i = light.length - 1; i >= 0; i--) {
        Light lt = (Light) light[i].getObject();
        Vec3 lightPos = context.lightPosition[i];
        double distToLight, lightDot;
        if (lt instanceof PointLight) {
            lightDir.set(pos);
            lightDir.subtract(lightPos);
            distToLight = lightDir.length();
            lightDir.scale(1.0 / distToLight);
        } else if (lt instanceof SpotLight) {
            lightDir.set(pos);
            lightDir.subtract(lightPos);
            distToLight = lightDir.length();
            lightDir.scale(1.0 / distToLight);
        } else if (lt instanceof DirectionalLight)
            lightDir.set(context.lightDirection[i]);
        lt.getLight(outputColor, light[i].getCoords().toLocal().times(pos));
        if (lt.getType() == Light.TYPE_AMBIENT) {
            if (diffuse != null)
                diffuse.add(outputColor.getRed(), outputColor.getGreen(), outputColor.getBlue());
            continue;
        }
        lightDot = lightDir.dot(norm);
        if ((lightDot >= 0.0 && viewDot <= 0.0) || (lightDot <= 0.0 && viewDot >= 0.0))
            continue;
        if (diffuse != null) {
            float dot = (float) (lightDot < 0.0 ? -lightDot : lightDot);
            diffuse.add(outputColor.getRed() * dot, outputColor.getGreen() * dot, outputColor.getBlue() * dot);
        }
        if (highlight != null) {
            lightDir.add(viewdir);
            lightDir.normalize();
            double dot = lightDir.dot(norm);
            dot = (dot < 0.0 ? -dot : dot);
            outputColor.scale(FastMath.pow(dot, (int) ((1.0 - roughness) * 128.0) + 1));
            highlight.add(outputColor);
        }
    }
}
