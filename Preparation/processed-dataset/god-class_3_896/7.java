private void storeProperties() {
    try {
        OutputStream out = new FileOutputStream(propertiesFile);
        properties.setProperty("currentFile", sessionFile == null ? "" : sessionFile.getPath());
        properties.setProperty("locationX", Integer.toString(getX()));
        properties.setProperty("locationY", Integer.toString(getY()));
        properties.setProperty("width", Integer.toString(getWidth()));
        properties.setProperty("height", Integer.toString(getHeight()));
        properties.store(out, "User Specific Properties");
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
