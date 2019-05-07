/** Given a reflected or transmitted ray, randomly alter its direction to create gloss and
     translucency effects.  dir is a unit vector in the "ideal" reflected or refracted
     direction, which on exit is overwritten with the new direction.  norm is the local
     surface normal, and roughness determines how much the ray direction is altered. */
void randomizeDirection(Vec3 dir, Vec3 norm, double roughness) {
    if (roughness == 0.0)
        return;
    // Pick a random vector within the unit sphere. 
    double x, y, z, scale, dot1, dot2;
    do {
        x = random.nextDouble() - 0.5;
        y = random.nextDouble() - 0.5;
        z = random.nextDouble() - 0.5;
    } while (x * x + y * y + z * z > 0.25);
    scale = Math.pow(roughness, 1.7) * 0.5;
    dot1 = dir.dot(norm);
    dir.x += 2.0 * scale * x;
    dir.y += 2.0 * scale * y;
    dir.z += 2.0 * scale * z;
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
