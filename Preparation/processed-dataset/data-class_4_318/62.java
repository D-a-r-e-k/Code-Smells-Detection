private String expectedStr(Vector expected) {
    StringBuffer ret = new StringBuffer("{");
    int size = expected.size();
    for (int i = 0; i < size; i++) {
        if (i > 0)
            ret.append(", ");
        ret.append(expected.elementAt(i).toString());
    }
    ret.append('}');
    return ret.toString();
}
