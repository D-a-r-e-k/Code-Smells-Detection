/** Given a reflected or transmitted ray, randomly alter its direction to create gloss and
     translucency effects.  dir is a unit vector in the "ideal" reflected or refracted
     direction, which on exit is overwritten with the new direction.  norm is the local
     surface normal, roughness determines how much the ray direction is altered, and number
     is used for distributing rays evenly. */
public void randomizeDirection(Vec3 dir, Vec3 norm, Random random, double roughness, int number) {
    double x, y, z, scale, dot1, dot2;
    int d;
    if (roughness <= 0.0)
        return;
    // Pick a random vector within an octant of the unit sphere. 
    do {
        x = random.nextDouble();
        y = random.nextDouble();
        z = random.nextDouble();
    } while (x * x + y * y + z * z > 1.0);
    scale = Math.pow(roughness, 1.7) * 0.5;
    x *= scale;
    y *= scale;
    z *= scale;
    // Decide which octant of the sphere to use for this ray. 
    d = distrib1[number & 15];
    if (d < 2)
        x *= -1.0;
    if (d == 1 || d == 2)
        y *= -1.0;
    if ((distrib2[number & 15] & 1) == 0)
        z *= -1.0;
    dot1 = dir.dot(norm);
    dir.x += x;
    dir.y += y;
    dir.z += z;
    dot2 = 2.0 * dir.dot(norm);
    // If the ray is on the wrong side of the surface, flip it back. 
    if (dot1 < 0.0 && dot2 > 0.0) {
        dir.x -= dot2 * norm.x;
        dir.y -= dot2 * norm.y;
        dir.z -= dot2 * norm.z;
    } else if (dot1 > 0.0 && dot2 < 0.0) {
        dir.x += dot2 * norm.x;
        dir.y += dot2 * norm.y;
        dir.z += dot2 * norm.z;
    }
    dir.normalize();
}
