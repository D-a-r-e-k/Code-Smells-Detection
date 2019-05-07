public boolean accept(File f) {
    if (f.isDirectory()) {
        return true;
    }
    return matches(getExtension(f));
}
