private String formatAmount(long amount, Account account) {
    return number.format(((double) amount) / account.getCurrency().getScaleFactor());
}
