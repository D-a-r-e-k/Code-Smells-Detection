private String formatDate(Date date) {
    if (date == null)
        return null;
    calendar.setTime(date);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int year = calendar.get(Calendar.YEAR);
    if ((year >= 1900) && (year < 2000))
        return "D" + month + "/" + day + "/" + (year - 1900);
    if ((year >= 2000) && (year < 2010))
        return "D" + month + "/" + day + "\'0" + (year - 2000);
    else if ((year >= 2010) && (year < 2100))
        return "D" + month + "/" + day + "\'" + (year - 2000);
    else
        return null;
}
