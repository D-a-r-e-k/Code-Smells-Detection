/**
     * This method based on code from the String taglib at Apache Jakarta:
     * http://cvs.apache.org/viewcvs/jakarta-taglibs/string/src/org/apache/taglibs/string/util/StringW.java?rev=1.16&content-type=text/vnd.viewcvs-markup
     * Copyright (c) 1999 The Apache Software Foundation.
     * Author: timster@mac.com
     *
     * @param str
     * @param lower
     * @param upper
     * @param appendToEnd
     * @return
     */
public static String truncateNicely(String str, int lower, int upper, String appendToEnd) {
    // strip markup from the string 
    String str2 = removeHTML(str, false);
    boolean diff = (str2.length() < str.length());
    // quickly adjust the upper if it is set lower than 'lower' 
    if (upper < lower) {
        upper = lower;
    }
    // now determine if the string fits within the upper limit 
    // if it does, go straight to return, do not pass 'go' and collect $200 
    if (str2.length() > upper) {
        // the magic location int 
        int loc;
        // first we determine where the next space appears after lower 
        loc = str2.lastIndexOf(' ', upper);
        // now we'll see if the location is greater than the lower limit 
        if (loc >= lower) {
            // yes it was, so we'll cut it off here 
            str2 = str2.substring(0, loc);
        } else {
            // no it wasnt, so we'll cut it off at the upper limit 
            str2 = str2.substring(0, upper);
            loc = upper;
        }
        // HTML was removed from original str 
        if (diff) {
            // location of last space in truncated string 
            loc = str2.lastIndexOf(' ', loc);
            // get last "word" in truncated string (add 1 to loc to eliminate space 
            String str3 = str2.substring(loc + 1);
            // find this fragment in original str, from 'loc' position 
            loc = str.indexOf(str3, loc) + str3.length();
            // get truncated string from original str, given new 'loc' 
            str2 = str.substring(0, loc);
            // get all the HTML from original str after loc 
            str3 = extractHTML(str.substring(loc));
            // remove any tags which generate visible HTML 
            // This call is unecessary, all HTML has already been stripped 
            //str3 = removeVisibleHTMLTags(str3); 
            // append the appendToEnd String and 
            // add extracted HTML back onto truncated string 
            str = str2 + appendToEnd + str3;
        } else {
            // the string was truncated, so we append the appendToEnd String 
            str = str2 + appendToEnd;
        }
    }
    return str;
}
