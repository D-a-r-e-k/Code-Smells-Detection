public boolean isStandard() {
    final String namespace = _fname.getNamespace();
    return (namespace == null) || (namespace.equals(Constants.EMPTYSTRING));
}
