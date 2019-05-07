/* (non-Javadoc)
	 * @see org.jfin.date.Period#clone()
	 */
@Override
public FloatPeriod clone() {
    FloatPeriod period = new FloatPeriod();
    period.setStartCalendar(getStartCalendar());
    period.setEndCalendar(getEndCalendar());
    period.setReferenceStartCalendar(getReferenceStartCalendar());
    period.setReferenceEndCalendar(getReferenceEndCalendar());
    period.notional = notional;
    period.currency = currency;
    period.index = index;
    period.businessDayConvention = businessDayConvention;
    period.daycountCalculator = daycountCalculator;
    period.paymentHolidayCalendar = paymentHolidayCalendar;
    period.fixingHolidayCalendar = fixingHolidayCalendar;
    period.frequency = frequency;
    period.fixingOffset = fixingOffset;
    return period;
}
