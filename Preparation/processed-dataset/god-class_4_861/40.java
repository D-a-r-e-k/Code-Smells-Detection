public List getFloatSchedule() throws ScheduleException {
    FloatPeriod prototype = new FloatPeriod();
    prototype.setNotional(payFixed ? getNotional() : -1 * getNotional());
    prototype.setCurrency(getCurrency());
    prototype.setBusinessDayConvention(getFloatConvention());
    prototype.setDaycountCalculator(getFloatDaycountCalculator());
    prototype.setPaymentHolidayCalendar(getPaymentBusinessDays());
    prototype.setFixingHolidayCalendar(getFixingBusinessDays());
    prototype.setIndex(getFloatIndex());
    prototype.setFrequency(getFloatFrequency());
    prototype.setFixingOffset(getFixingOffset());
    List periods = ScheduleGenerator.generateSchedule(getEffectiveDate(), getMaturityDate(), getFloatFrequency(), getStubType(), prototype, MAX_PERIODS);
    return periods;
}
