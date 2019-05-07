public List getFixedSchedule() throws ScheduleException {
    FixedPeriod prototype = new FixedPeriod();
    prototype.setNotional(payFixed ? -1 * getNotional() : getNotional());
    prototype.setCurrency(getCurrency());
    prototype.setFixedRate(getFixedRate());
    prototype.setBusinessDayConvention(getFixedConvention());
    prototype.setDaycountCalculator(getFixedDaycountCalculator());
    prototype.setPaymentHolidayCalendar(getPaymentBusinessDays());
    List periods = ScheduleGenerator.generateSchedule(getEffectiveDate(), getMaturityDate(), getFixedFrequency(), getStubType(), prototype, MAX_PERIODS);
    return periods;
}
