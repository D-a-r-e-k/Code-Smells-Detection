public Object getPropertyValue(int index) {
    if (index == 1)
        return PROPERTIES[index].getAllowedValues()[endsStyle];
    return super.getPropertyValue(index);
}
