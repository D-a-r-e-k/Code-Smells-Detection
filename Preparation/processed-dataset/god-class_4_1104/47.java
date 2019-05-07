public void setPropertyValue(int index, Object value) {
    if (index == 0) {
        Object values[] = PROPERTIES[0].getAllowedValues();
        for (int i = 0; i < values.length; i++) if (values[i].equals(value))
            setSmoothingMethod(i + 2);
    } else {
        Object values[] = PROPERTIES[1].getAllowedValues();
        for (int i = 0; i < values.length; i++) if (values[i].equals(value))
            setClosed(i == 0 || i == 2, i == 1 || i == 2);
    }
}
