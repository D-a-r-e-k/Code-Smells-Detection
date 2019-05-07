public <T extends Period> HolidayCalendar<T> getHolidayCalendar(String cityIdentifier, Class<T> c) {
    if (cityIdentifier.equals("WE")) {
        return new WeekendHolidayCalendar<T>();
    } else {
        throw new HolidayCalendarException("Unknown holiday city \"" + cityIdentifier + "\", HolidayCalendarFactoryImpl only accepts \"WE\" (Weekend Holiday Calendar).");
    }
}
