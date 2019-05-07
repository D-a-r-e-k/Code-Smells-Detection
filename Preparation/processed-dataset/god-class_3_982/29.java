private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
    stream.defaultReadObject();
    try {
        buildExpression(cronExpression);
    } catch (Exception ignore) {
    }
}
