private void zapGraphXformer(GraphXformer gxf, Exception e) {
    System.err.println("WARNING: exception in " + ": transformation will not be applied");
    e.printStackTrace();
    gxf = null;
}
