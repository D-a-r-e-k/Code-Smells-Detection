public boolean update(ParameterList pl, SunflowAPI api) {
    // get parameters  
    fov = pl.getFloat("fov", fov);
    aspect = pl.getFloat("aspect", aspect);
    update();
    return true;
}
