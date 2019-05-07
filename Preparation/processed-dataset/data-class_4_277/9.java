/** Add a Photon to the map. */
private void addPhoton(Vec3 pos, Vec3 dir, RGBColor color) {
    Photon p = new Photon(pos, dir, color);
    synchronized (this) {
        photonList.add(p);
    }
    if (direction[p.direction & 0xFFFF] == null) {
        int i = (p.direction >> 8) & 0xFF;
        int j = p.direction & 0xFF;
        double phi = i * Math.PI / 128, theta = j * Math.PI / 256;
        double sphi = Math.sin(phi), cphi = Math.cos(phi);
        double stheta = Math.sin(theta), ctheta = Math.cos(theta);
        direction[p.direction & 0xFFFF] = new Vec3(cphi * stheta, ctheta, sphi * stheta);
    }
}
