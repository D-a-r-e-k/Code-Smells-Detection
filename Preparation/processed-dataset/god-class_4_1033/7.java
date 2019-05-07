public void publish(NameValue nameValue) {
    try {
        File file = new File(nameValue.getValue());
        mPublished.add(new NameValue(nameValue.getName(), file.getCanonicalPath()));
    } catch (Exception ex) {
        mPublished.add(nameValue);
    }
}
