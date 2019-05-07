private long getGlobalMaxBandwidth(int aMillisBeforeNow) {
    try {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, (aMillisBeforeNow * -1));
        String time = BandwidthRestriction.TIMEONLY_FORMAT.format(now.getTime());
        Date date = BandwidthRestriction.FULLDATE_FORMAT.parse(BandwidthRestriction.DEFAULT_DATE + time);
        String day = BandwidthRestriction.FULLDAY_FORMAT.format(now.getTime()).toUpperCase();
        BandwidthRestriction br = harvestCoordinatorDao.getBandwidthRestriction(day, date);
        if (br != null) {
            return br.getBandwidth();
        }
    } catch (ParseException e) {
        if (log.isErrorEnabled()) {
            log.error("Failed to parse the date for the bandwith restriction : " + e.getMessage(), e);
        }
    }
    return 0;
}
