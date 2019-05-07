public boolean isWeekend(Calendar d) {
    return d.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || d.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
}
