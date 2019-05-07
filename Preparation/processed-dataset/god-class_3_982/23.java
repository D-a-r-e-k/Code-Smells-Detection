//////////////////////////////////////////////////////////////////////////// 
// 
// Computation Functions 
// 
//////////////////////////////////////////////////////////////////////////// 
protected Date getTimeAfter(Date afterTime) {
    // Computation is based on Gregorian year only. 
    Calendar cl = new java.util.GregorianCalendar(getTimeZone());
    // move ahead one second, since we're computing the time *after* the 
    // given time 
    afterTime = new Date(afterTime.getTime() + 1000);
    // CronTrigger does not deal with milliseconds 
    cl.setTime(afterTime);
    cl.set(Calendar.MILLISECOND, 0);
    boolean gotOne = false;
    // loop until we've computed the next time, or we've past the endTime 
    while (!gotOne) {
        //if (endTime != null && cl.getTime().after(endTime)) return null; 
        if (cl.get(Calendar.YEAR) > 2999) {
            // prevent endless loop... 
            return null;
        }
        SortedSet st = null;
        int t = 0;
        int sec = cl.get(Calendar.SECOND);
        int min = cl.get(Calendar.MINUTE);
        // get second................................................. 
        st = seconds.tailSet(new Integer(sec));
        if (st != null && st.size() != 0) {
            sec = ((Integer) st.first()).intValue();
        } else {
            sec = ((Integer) seconds.first()).intValue();
            min++;
            cl.set(Calendar.MINUTE, min);
        }
        cl.set(Calendar.SECOND, sec);
        min = cl.get(Calendar.MINUTE);
        int hr = cl.get(Calendar.HOUR_OF_DAY);
        t = -1;
        // get minute................................................. 
        st = minutes.tailSet(new Integer(min));
        if (st != null && st.size() != 0) {
            t = min;
            min = ((Integer) st.first()).intValue();
        } else {
            min = ((Integer) minutes.first()).intValue();
            hr++;
        }
        if (min != t) {
            cl.set(Calendar.SECOND, 0);
            cl.set(Calendar.MINUTE, min);
            setCalendarHour(cl, hr);
            continue;
        }
        cl.set(Calendar.MINUTE, min);
        hr = cl.get(Calendar.HOUR_OF_DAY);
        int day = cl.get(Calendar.DAY_OF_MONTH);
        t = -1;
        // get hour................................................... 
        st = hours.tailSet(new Integer(hr));
        if (st != null && st.size() != 0) {
            t = hr;
            hr = ((Integer) st.first()).intValue();
        } else {
            hr = ((Integer) hours.first()).intValue();
            day++;
        }
        if (hr != t) {
            cl.set(Calendar.SECOND, 0);
            cl.set(Calendar.MINUTE, 0);
            cl.set(Calendar.DAY_OF_MONTH, day);
            setCalendarHour(cl, hr);
            continue;
        }
        cl.set(Calendar.HOUR_OF_DAY, hr);
        day = cl.get(Calendar.DAY_OF_MONTH);
        int mon = cl.get(Calendar.MONTH) + 1;
        // '+ 1' because calendar is 0-based for this field, and we are 
        // 1-based 
        t = -1;
        int tmon = mon;
        // get day................................................... 
        boolean dayOfMSpec = !daysOfMonth.contains(NO_SPEC);
        boolean dayOfWSpec = !daysOfWeek.contains(NO_SPEC);
        if (dayOfMSpec && !dayOfWSpec) {
            // get day by day of month rule 
            st = daysOfMonth.tailSet(new Integer(day));
            if (lastdayOfMonth) {
                if (!nearestWeekday) {
                    t = day;
                    day = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                } else {
                    t = day;
                    day = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                    java.util.Calendar tcal = java.util.Calendar.getInstance(getTimeZone());
                    tcal.set(Calendar.SECOND, 0);
                    tcal.set(Calendar.MINUTE, 0);
                    tcal.set(Calendar.HOUR_OF_DAY, 0);
                    tcal.set(Calendar.DAY_OF_MONTH, day);
                    tcal.set(Calendar.MONTH, mon - 1);
                    tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
                    int ldom = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                    int dow = tcal.get(Calendar.DAY_OF_WEEK);
                    if (dow == Calendar.SATURDAY && day == 1) {
                        day += 2;
                    } else if (dow == Calendar.SATURDAY) {
                        day -= 1;
                    } else if (dow == Calendar.SUNDAY && day == ldom) {
                        day -= 2;
                    } else if (dow == Calendar.SUNDAY) {
                        day += 1;
                    }
                    tcal.set(Calendar.SECOND, sec);
                    tcal.set(Calendar.MINUTE, min);
                    tcal.set(Calendar.HOUR_OF_DAY, hr);
                    tcal.set(Calendar.DAY_OF_MONTH, day);
                    tcal.set(Calendar.MONTH, mon - 1);
                    Date nTime = tcal.getTime();
                    if (nTime.before(afterTime)) {
                        day = 1;
                        mon++;
                    }
                }
            } else if (nearestWeekday) {
                t = day;
                day = ((Integer) daysOfMonth.first()).intValue();
                java.util.Calendar tcal = java.util.Calendar.getInstance(getTimeZone());
                tcal.set(Calendar.SECOND, 0);
                tcal.set(Calendar.MINUTE, 0);
                tcal.set(Calendar.HOUR_OF_DAY, 0);
                tcal.set(Calendar.DAY_OF_MONTH, day);
                tcal.set(Calendar.MONTH, mon - 1);
                tcal.set(Calendar.YEAR, cl.get(Calendar.YEAR));
                int ldom = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                int dow = tcal.get(Calendar.DAY_OF_WEEK);
                if (dow == Calendar.SATURDAY && day == 1) {
                    day += 2;
                } else if (dow == Calendar.SATURDAY) {
                    day -= 1;
                } else if (dow == Calendar.SUNDAY && day == ldom) {
                    day -= 2;
                } else if (dow == Calendar.SUNDAY) {
                    day += 1;
                }
                tcal.set(Calendar.SECOND, sec);
                tcal.set(Calendar.MINUTE, min);
                tcal.set(Calendar.HOUR_OF_DAY, hr);
                tcal.set(Calendar.DAY_OF_MONTH, day);
                tcal.set(Calendar.MONTH, mon - 1);
                Date nTime = tcal.getTime();
                if (nTime.before(afterTime)) {
                    day = ((Integer) daysOfMonth.first()).intValue();
                    mon++;
                }
            } else if (st != null && st.size() != 0) {
                t = day;
                day = ((Integer) st.first()).intValue();
                // make sure we don't over-run a short month, such as february 
                int lastDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                if (day > lastDay) {
                    day = ((Integer) daysOfMonth.first()).intValue();
                    mon++;
                }
            } else {
                day = ((Integer) daysOfMonth.first()).intValue();
                mon++;
            }
            if (day != t || mon != tmon) {
                cl.set(Calendar.SECOND, 0);
                cl.set(Calendar.MINUTE, 0);
                cl.set(Calendar.HOUR_OF_DAY, 0);
                cl.set(Calendar.DAY_OF_MONTH, day);
                cl.set(Calendar.MONTH, mon - 1);
                // '- 1' because calendar is 0-based for this field, and we 
                // are 1-based 
                continue;
            }
        } else if (dayOfWSpec && !dayOfMSpec) {
            // get day by day of week rule 
            if (lastdayOfWeek) {
                // are we looking for the last XXX day of 
                // the month? 
                int dow = ((Integer) daysOfWeek.first()).intValue();
                // desired 
                // d-o-w 
                int cDow = cl.get(Calendar.DAY_OF_WEEK);
                // current d-o-w 
                int daysToAdd = 0;
                if (cDow < dow) {
                    daysToAdd = dow - cDow;
                }
                if (cDow > dow) {
                    daysToAdd = dow + (7 - cDow);
                }
                int lDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                if (day + daysToAdd > lDay) {
                    // did we already miss the 
                    // last one? 
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, 1);
                    cl.set(Calendar.MONTH, mon);
                    // no '- 1' here because we are promoting the month 
                    continue;
                }
                // find date of last occurance of this day in this month... 
                while ((day + daysToAdd + 7) <= lDay) {
                    daysToAdd += 7;
                }
                day += daysToAdd;
                if (daysToAdd > 0) {
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, day);
                    cl.set(Calendar.MONTH, mon - 1);
                    // '- 1' here because we are not promoting the month 
                    continue;
                }
            } else if (nthdayOfWeek != 0) {
                // are we looking for the Nth XXX day in the month? 
                int dow = ((Integer) daysOfWeek.first()).intValue();
                // desired 
                // d-o-w 
                int cDow = cl.get(Calendar.DAY_OF_WEEK);
                // current d-o-w 
                int daysToAdd = 0;
                if (cDow < dow) {
                    daysToAdd = dow - cDow;
                } else if (cDow > dow) {
                    daysToAdd = dow + (7 - cDow);
                }
                boolean dayShifted = false;
                if (daysToAdd > 0) {
                    dayShifted = true;
                }
                day += daysToAdd;
                int weekOfMonth = day / 7;
                if (day % 7 > 0) {
                    weekOfMonth++;
                }
                daysToAdd = (nthdayOfWeek - weekOfMonth) * 7;
                day += daysToAdd;
                if (daysToAdd < 0 || day > getLastDayOfMonth(mon, cl.get(Calendar.YEAR))) {
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, 1);
                    cl.set(Calendar.MONTH, mon);
                    // no '- 1' here because we are promoting the month 
                    continue;
                } else if (daysToAdd > 0 || dayShifted) {
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, day);
                    cl.set(Calendar.MONTH, mon - 1);
                    // '- 1' here because we are NOT promoting the month 
                    continue;
                }
            } else {
                int cDow = cl.get(Calendar.DAY_OF_WEEK);
                // current d-o-w 
                int dow = ((Integer) daysOfWeek.first()).intValue();
                // desired 
                // d-o-w 
                st = daysOfWeek.tailSet(new Integer(cDow));
                if (st != null && st.size() > 0) {
                    dow = ((Integer) st.first()).intValue();
                }
                int daysToAdd = 0;
                if (cDow < dow) {
                    daysToAdd = dow - cDow;
                }
                if (cDow > dow) {
                    daysToAdd = dow + (7 - cDow);
                }
                int lDay = getLastDayOfMonth(mon, cl.get(Calendar.YEAR));
                if (day + daysToAdd > lDay) {
                    // will we pass the end of 
                    // the month? 
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, 1);
                    cl.set(Calendar.MONTH, mon);
                    // no '- 1' here because we are promoting the month 
                    continue;
                } else if (daysToAdd > 0) {
                    // are we swithing days? 
                    cl.set(Calendar.SECOND, 0);
                    cl.set(Calendar.MINUTE, 0);
                    cl.set(Calendar.HOUR_OF_DAY, 0);
                    cl.set(Calendar.DAY_OF_MONTH, day + daysToAdd);
                    cl.set(Calendar.MONTH, mon - 1);
                    // '- 1' because calendar is 0-based for this field, 
                    // and we are 1-based 
                    continue;
                }
            }
        } else {
            // dayOfWSpec && !dayOfMSpec 
            throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
        }
        cl.set(Calendar.DAY_OF_MONTH, day);
        mon = cl.get(Calendar.MONTH) + 1;
        // '+ 1' because calendar is 0-based for this field, and we are 
        // 1-based 
        int year = cl.get(Calendar.YEAR);
        t = -1;
        // test for expressions that never generate a valid fire date, 
        // but keep looping... 
        if (year > CronTrigger.YEAR_TO_GIVEUP_SCHEDULING_AT) {
            return null;
        }
        // get month................................................... 
        st = months.tailSet(new Integer(mon));
        if (st != null && st.size() != 0) {
            t = mon;
            mon = ((Integer) st.first()).intValue();
        } else {
            mon = ((Integer) months.first()).intValue();
            year++;
        }
        if (mon != t) {
            cl.set(Calendar.SECOND, 0);
            cl.set(Calendar.MINUTE, 0);
            cl.set(Calendar.HOUR_OF_DAY, 0);
            cl.set(Calendar.DAY_OF_MONTH, 1);
            cl.set(Calendar.MONTH, mon - 1);
            // '- 1' because calendar is 0-based for this field, and we are 
            // 1-based 
            cl.set(Calendar.YEAR, year);
            continue;
        }
        cl.set(Calendar.MONTH, mon - 1);
        // '- 1' because calendar is 0-based for this field, and we are 
        // 1-based 
        year = cl.get(Calendar.YEAR);
        t = -1;
        // get year................................................... 
        st = years.tailSet(new Integer(year));
        if (st != null && st.size() != 0) {
            t = year;
            year = ((Integer) st.first()).intValue();
        } else {
            return null;
        }
        if (year != t) {
            cl.set(Calendar.SECOND, 0);
            cl.set(Calendar.MINUTE, 0);
            cl.set(Calendar.HOUR_OF_DAY, 0);
            cl.set(Calendar.DAY_OF_MONTH, 1);
            cl.set(Calendar.MONTH, 0);
            // '- 1' because calendar is 0-based for this field, and we are 
            // 1-based 
            cl.set(Calendar.YEAR, year);
            continue;
        }
        cl.set(Calendar.YEAR, year);
        gotOne = true;
    }
    // while( !done ) 
    return cl.getTime();
}
