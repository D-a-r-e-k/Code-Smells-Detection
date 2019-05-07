/**
     * Makes a Perl 5 regular expression for use by ORO.
     */
private final String makeSubstRE(int i) {
    return ("s/" + perLineREs[i] + '/' + perLineREs[i + 1] + "/g");
}
