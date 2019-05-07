public HolidayCalendar getHolidayCalendar(String cityIdentifier) throws HolidayCalendarException {
    return getHolidayCalendar(cityIdentifier, Period.class);
}
