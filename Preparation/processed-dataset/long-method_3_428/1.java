/**
	 * Used to generate a List containing an schedule ordered by period dates.
	 * The periods will be adjacent to each other, e.g: d1,d2 d2,d3 d3,d4
	 *
	 * @param startDate
	 * @param endDate
	 * @param frequencyAmount
	 * @param frequencyUnit
	 * @param stubType
	 * @param prototype
	 * @param maxPeriods
	 * @return A List<Period> containing the schedule
	 * @throws ScheduleException
	 */
public static List<Period> generateSchedule(Calendar startDate, Calendar endDate, int frequencyAmount, int frequencyUnit, StubType stubType, Period prototype, int maxPeriods) throws ScheduleException {
    Calendar cleanEndDate = copyAndReset(endDate);
    Calendar cleanStartDate = copyAndReset(startDate);
    int periodCount = 0;
    logger.fine("Generating " + stubType.name() + " " + frequencyAmount + ":" + getCalendarUnitDescriptor(frequencyUnit) + " schedule between " + ISDADateFormat.format(startDate) + " and " + ISDADateFormat.format(endDate));
    if (stubType == StubType.SHORT_LAST || stubType == StubType.NONE) {
        ArrayList<Period> schedule = new ArrayList<Period>();
        Calendar holdDate = copyAndReset(startDate);
        int count = 1;
        while (holdDate.before(cleanEndDate)) {
            Calendar nextDate = copyAndReset(startDate);
            nextDate.add(frequencyUnit, frequencyAmount * count);
            count++;
            Calendar notionalStartDate = null;
            Calendar notionalEndDate = null;
            if (nextDate.after(cleanEndDate)) {
                if (stubType == StubType.SHORT_LAST) {
                    notionalStartDate = holdDate;
                    notionalEndDate = nextDate;
                    nextDate = cleanEndDate;
                } else {
                    throw new ScheduleException("StubType.NONE used when periods do not fit start and end date");
                }
            }
            Period toAdd = (Period) prototype.clone();
            toAdd.setStartCalendar(holdDate);
            toAdd.setEndCalendar(nextDate);
            toAdd.setReferenceStartCalendar(notionalStartDate);
            toAdd.setReferenceEndCalendar(notionalEndDate);
            schedule.add(toAdd);
            holdDate = nextDate;
            periodCount++;
            if (maxPeriods > 0 && periodCount > maxPeriods) {
                throw new ScheduleException("Maximum number of periods (" + maxPeriods + ") exceeded.");
            }
        }
        return schedule;
    } else if (stubType == StubType.SHORT_FIRST) {
        ArrayList<Period> schedule = new ArrayList<Period>();
        Calendar holdDate = copyAndReset(endDate);
        int count = 1;
        while (holdDate.after(cleanStartDate)) {
            Calendar nextDate = copyAndReset(endDate);
            nextDate.add(frequencyUnit, -1 * frequencyAmount * count);
            count++;
            Calendar notionalStartDate = null;
            Calendar notionalEndDate = null;
            if (nextDate.before(cleanStartDate)) {
                notionalStartDate = nextDate;
                notionalEndDate = holdDate;
                nextDate = cleanStartDate;
            }
            Period toAdd = (Period) prototype.clone();
            toAdd.setStartCalendar(nextDate);
            toAdd.setEndCalendar(holdDate);
            toAdd.setReferenceStartCalendar(notionalStartDate);
            toAdd.setReferenceEndCalendar(notionalEndDate);
            schedule.add(0, toAdd);
            holdDate = nextDate;
            periodCount++;
            if (maxPeriods > 0 && periodCount > maxPeriods) {
                throw new ScheduleException("Maximum number of periods (" + maxPeriods + ") exceeded.");
            }
        }
        return schedule;
    } else if (stubType == StubType.LONG_LAST) {
        ArrayList<Period> schedule = new ArrayList<Period>();
        Calendar holdDate = copyAndReset(startDate);
        int count = 1;
        while (holdDate.before(cleanEndDate)) {
            Calendar nextDate = copyAndReset(startDate);
            nextDate.add(frequencyUnit, frequencyAmount * count);
            Calendar nextDate2 = copyAndReset(startDate);
            nextDate2.add(frequencyUnit, frequencyAmount * (count + 1));
            count++;
            Calendar notionalStartDate = null;
            Calendar notionalEndDate = null;
            if (nextDate2.after(cleanEndDate)) {
                notionalStartDate = nextDate;
                notionalEndDate = nextDate2;
                nextDate = cleanEndDate;
            }
            Period toAdd = (Period) prototype.clone();
            toAdd.setStartCalendar(holdDate);
            toAdd.setEndCalendar(nextDate);
            toAdd.setReferenceStartCalendar(notionalStartDate);
            toAdd.setReferenceEndCalendar(notionalEndDate);
            schedule.add(toAdd);
            holdDate = nextDate;
            periodCount++;
            if (maxPeriods > 0 && periodCount > maxPeriods) {
                throw new ScheduleException("Maximum number of periods (" + maxPeriods + ") exceeded.");
            }
        }
        return schedule;
    } else if (stubType == StubType.LONG_FIRST) {
        ArrayList<Period> schedule = new ArrayList<Period>();
        Calendar holdDate = copyAndReset(endDate);
        int count = 1;
        while (holdDate.after(cleanStartDate)) {
            Calendar nextDate = copyAndReset(endDate);
            nextDate.add(frequencyUnit, -1 * frequencyAmount * count);
            Calendar nextDate2 = copyAndReset(endDate);
            nextDate2.add(frequencyUnit, -1 * frequencyAmount * (count + 1));
            count++;
            Calendar notionalStartDate = null;
            Calendar notionalEndDate = null;
            if (nextDate2.before(cleanStartDate)) {
                notionalStartDate = nextDate2;
                notionalEndDate = nextDate;
                nextDate = cleanStartDate;
            }
            Period toAdd = (Period) prototype.clone();
            toAdd.setStartCalendar(nextDate);
            toAdd.setEndCalendar(holdDate);
            toAdd.setReferenceStartCalendar(notionalStartDate);
            toAdd.setReferenceEndCalendar(notionalEndDate);
            schedule.add(0, toAdd);
            holdDate = nextDate;
            periodCount++;
            if (maxPeriods > 0 && periodCount > maxPeriods) {
                throw new ScheduleException("Maximum number of periods (" + maxPeriods + ") exceeded.");
            }
        }
        return schedule;
    } else {
        throw new ScheduleException("Unsupported stub type " + stubType);
    }
}
