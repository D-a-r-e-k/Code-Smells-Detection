public EventSetDescriptor[] getEventSetDescriptors() {
    try {
        EventSetDescriptor[] esds = { new EventSetDescriptor(TestSetProducer.class, "testSet", TestSetListener.class, "acceptTestSet") };
        return esds;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}
