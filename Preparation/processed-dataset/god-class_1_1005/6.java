public Integer findInt(String s) {
    Integer result;
    if ("".equals(s))
        result = new Integer(0);
    else
        result = new Integer(Math.abs(Integer.valueOf(s).intValue()));
    return result;
}
