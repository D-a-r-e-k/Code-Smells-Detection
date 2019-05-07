public boolean accept(File f) {
    if (f == null)
        return false;
    if (f.isDirectory())
        return true;
    return f.getName().toLowerCase().endsWith(".sta");
}
