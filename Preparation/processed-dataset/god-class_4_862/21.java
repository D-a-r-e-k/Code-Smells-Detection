public Calendar getAdjustedEndCalendar() {
    return fixingHolidayCalendar.adjust(getEndCalendar(), getBusinessDayConvention());
}
