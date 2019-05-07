private void reportSharingError(String namespace, String name) {
    final String qName = (namespace == null) ? "," + name : namespace + "," + name;
    reportSchemaError("sch-props-correct.2", new Object[] { qName }, null);
}
