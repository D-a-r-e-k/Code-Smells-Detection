public Calendar getFixingDate() {
    return fixingHolidayCalendar.advanceBusinessDays(getAdjustedStartCalendar(), getFixingOffset());
}
