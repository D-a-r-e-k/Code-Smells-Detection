private void update() {
    au = (float) Math.tan(Math.toRadians(fov * 0.5f));
    av = au / aspect;
}
