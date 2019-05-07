/** Add a random displacement to a vector.  The displacements are uniformly distributed
     over the volume of a sphere whose radius is given by size. */
void randomizePoint(Vec3 pos, double size) {
    if (size == 0.0)
        return;
    // Pick a random vector within the unit sphere. 
    double x, y, z;
    do {
        x = random.nextDouble() - 0.5;
        y = random.nextDouble() - 0.5;
        z = random.nextDouble() - 0.5;
    } while (x * x + y * y + z * z > 0.25);
    pos.x += 2.0 * size * x;
    pos.y += 2.0 * size * y;
    pos.z += 2.0 * size * z;
}
