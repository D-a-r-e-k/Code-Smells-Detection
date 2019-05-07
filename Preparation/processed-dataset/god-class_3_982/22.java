protected int getDayOfWeekNumber(String s) {
    Integer integer = (Integer) dayMap.get(s);
    if (integer == null) {
        return -1;
    }
    return integer.intValue();
}
