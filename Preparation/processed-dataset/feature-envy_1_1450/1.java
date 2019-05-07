/**
	 * Format the time in milliseconds into a string in the format dd:hh:mm:ss
	 * @param aTime an elapsed time in ms
	 * @return the formated time string
	 */
public static String formatTime(long aTime) {
    long remainder = 0;
    long days = 0;
    long hours = 0;
    long minutes = 0;
    long seconds = 0;
    days = aTime / 86400000;
    remainder = aTime - (days * 86400000);
    hours = remainder / 3600000;
    remainder = remainder - (hours * 3600000);
    minutes = remainder / 60000;
    remainder = remainder - (minutes * 60000);
    seconds = remainder / 1000;
    DecimalFormat df = new DecimalFormat("00");
    return df.format(days) + ":" + df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds);
}
