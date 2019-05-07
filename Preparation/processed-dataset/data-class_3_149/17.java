protected void addToSet(int val, int end, int incr, int type) throws ParseException {
    TreeSet set = getSet(type);
    if (type == SECOND || type == MINUTE) {
        if ((val < 0 || val > 59 || end > 59) && (val != ALL_SPEC_INT)) {
            throw new ParseException("Minute and Second values must be between 0 and 59", -1);
        }
    } else if (type == HOUR) {
        if ((val < 0 || val > 23 || end > 23) && (val != ALL_SPEC_INT)) {
            throw new ParseException("Hour values must be between 0 and 23", -1);
        }
    } else if (type == DAY_OF_MONTH) {
        if ((val < 1 || val > 31 || end > 31) && (val != ALL_SPEC_INT) && (val != NO_SPEC_INT)) {
            throw new ParseException("Day of month values must be between 1 and 31", -1);
        }
    } else if (type == MONTH) {
        if ((val < 1 || val > 12 || end > 12) && (val != ALL_SPEC_INT)) {
            throw new ParseException("Month values must be between 1 and 12", -1);
        }
    } else if (type == DAY_OF_WEEK) {
        if ((val == 0 || val > 7 || end > 7) && (val != ALL_SPEC_INT) && (val != NO_SPEC_INT)) {
            throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
        }
    }
    if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
        if (val != -1) {
            set.add(new Integer(val));
        } else {
            set.add(NO_SPEC);
        }
        return;
    }
    int startAt = val;
    int stopAt = end;
    if (val == ALL_SPEC_INT && incr <= 0) {
        incr = 1;
        set.add(ALL_SPEC);
    }
    if (type == SECOND || type == MINUTE) {
        if (stopAt == -1) {
            stopAt = 59;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 0;
        }
    } else if (type == HOUR) {
        if (stopAt == -1) {
            stopAt = 23;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 0;
        }
    } else if (type == DAY_OF_MONTH) {
        if (stopAt == -1) {
            stopAt = 31;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 1;
        }
    } else if (type == MONTH) {
        if (stopAt == -1) {
            stopAt = 12;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 1;
        }
    } else if (type == DAY_OF_WEEK) {
        if (stopAt == -1) {
            stopAt = 7;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 1;
        }
    } else if (type == YEAR) {
        if (stopAt == -1) {
            stopAt = CronTrigger.YEAR_TO_GIVEUP_SCHEDULING_AT;
        }
        if (startAt == -1 || startAt == ALL_SPEC_INT) {
            startAt = 1970;
        }
    }
    // if the end of the range is before the start, then we need to overflow into  
    // the next day, month etc. This is done by adding the maximum amount for that  
    // type, and using modulus max to determine the value being added. 
    int max = -1;
    if (stopAt < startAt) {
        switch(type) {
            case SECOND:
                max = 60;
                break;
            case MINUTE:
                max = 60;
                break;
            case HOUR:
                max = 24;
                break;
            case MONTH:
                max = 12;
                break;
            case DAY_OF_WEEK:
                max = 7;
                break;
            case DAY_OF_MONTH:
                max = 31;
                break;
            case YEAR:
                throw new IllegalArgumentException("Start year must be less than stop year");
            default:
                throw new IllegalArgumentException("Unexpected type encountered");
        }
        stopAt += max;
    }
    for (int i = startAt; i <= stopAt; i += incr) {
        if (max == -1) {
            // ie: there's no max to overflow over 
            set.add(new Integer(i));
        } else {
            // take the modulus to get the real value 
            int i2 = i % max;
            // 1-indexed ranges should not include 0, and should include their max 
            if (i2 == 0 && (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH)) {
                i2 = max;
            }
            set.add(new Integer(i2));
        }
    }
}
