protected int getMonthNumber(String s) {
    Integer integer = (Integer) monthMap.get(s);
    if (integer == null) {
        return -1;
    }
    return integer.intValue();
}
