/** Add a random displacement to a vector.  The displacements are uniformly distributed
     over the volume of a sphere whose radius is given by size.  number is used for 
     distributing the displacements evenly. */
public void randomizePoint(Vec3 pos, Random random, double size, int number) {
    double x, y, z;
    int d;
    if (size == 0.0)
        return;
    // Pick a random vector within an octant of the unit sphere. 
    do {
        x = random.nextDouble();
        y = random.nextDouble();
        z = random.nextDouble();
    } while (x * x + y * y + z * z > 1.0);
    x *= size;
    y *= size;
    z *= size;
    // Decide which octant of the sphere to use for this ray. 
    d = distrib1[number & 15];
    if (d < 2)
        x *= -1.0;
    if (d == 1 || d == 2)
        y *= -1.0;
    if ((distrib2[number & 15] & 1) == 0)
        z *= -1.0;
    pos.x += x;
    pos.y += y;
    pos.z += z;
}
