/**
     * @return the strings
     */
public List<String> getStringList() {
    List<String> result = new ArrayList<String>(fields.size());
    for (JTextField tf : fields) {
        result.add(tf.getText());
    }
    return result;
}
