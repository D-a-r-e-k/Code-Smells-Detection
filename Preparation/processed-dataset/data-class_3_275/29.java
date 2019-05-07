public void setPropertyValue(int index, Object value) {
    if (index == 1) {
        Object values[] = PROPERTIES[1].getAllowedValues();
        for (int i = 0; i < values.length; i++) if (values[i].equals(value))
            setEndsStyle(i);
    } else {
        super.setPropertyValue(index, value);
    }
}
