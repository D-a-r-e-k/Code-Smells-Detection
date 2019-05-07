/**
     * Parses the date string and returns a date object:
     *   11/2/98 ->> 11/2/1998
     *   3/15'00 ->> 3/15/2000
     */
private Date parseDate(String line) {
    try {
        StringTokenizer st = new StringTokenizer(line, "D/\'");
        int month = Integer.parseInt(st.nextToken().trim());
        int day = Integer.parseInt(st.nextToken().trim());
        int year = Integer.parseInt(st.nextToken().trim());
        if (year < 100) {
            if (line.indexOf("'") < 0)
                year = year + 1900;
            else
                year = year + 2000;
        }
        calendar.clear();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
