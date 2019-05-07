// 
public static int subtractMonths(TimestampData a, TimestampData b, boolean isYear) {
    synchronized (HsqlDateTime.tempCalGMT) {
        boolean negate = false;
        if (b.getSeconds() > a.getSeconds()) {
            negate = true;
            TimestampData temp = a;
            a = b;
            b = temp;
        }
        HsqlDateTime.setTimeInMillis(HsqlDateTime.tempCalGMT, a.getSeconds() * 1000);
        int months = HsqlDateTime.tempCalGMT.get(Calendar.MONTH);
        int years = HsqlDateTime.tempCalGMT.get(Calendar.YEAR);
        HsqlDateTime.setTimeInMillis(HsqlDateTime.tempCalGMT, b.getSeconds() * 1000);
        months -= HsqlDateTime.tempCalGMT.get(Calendar.MONTH);
        years -= HsqlDateTime.tempCalGMT.get(Calendar.YEAR);
        if (isYear) {
            months = years * 12;
        } else {
            if (months < 0) {
                months += 12;
                years--;
            }
            months += years * 12;
            if (negate) {
                months = -months;
            }
        }
        return months;
    }
}
