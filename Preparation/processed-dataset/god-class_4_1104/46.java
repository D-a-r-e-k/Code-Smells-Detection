public Object getPropertyValue(int index) {
    if (index == 0)
        return PROPERTIES[0].getAllowedValues()[smoothingMethod - 2];
    Object values[] = PROPERTIES[1].getAllowedValues();
    if (uclosed && !vclosed)
        return values[0];
    if (!uclosed && vclosed)
        return values[1];
    if (uclosed && vclosed)
        return values[2];
    return values[3];
}
