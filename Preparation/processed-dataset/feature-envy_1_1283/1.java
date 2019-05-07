public GraphControllerXmlBean read() {
    try {
        XMLBeanReader br = new XMLBeanReader();
        br.load(_graphFile, this.getClass().getClassLoader());
        return (GraphControllerXmlBean) br.iterator().next();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
