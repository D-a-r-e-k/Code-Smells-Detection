public Calendar getAdjustedStartCalendar() {
    return fixingHolidayCalendar.adjust(getStartCalendar(), getBusinessDayConvention());
}
