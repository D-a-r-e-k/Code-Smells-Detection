public boolean accept(File file) {
    if (file.getName().toLowerCase().endsWith(".jar"))
        return true;
    else
        return false;
}
