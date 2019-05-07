public User getSlowPayingUser() {
    User poorPayer = new User("Poor Payer", 10000.00);
    poorPayer.addPhone(64, 9, 3737598);
    poorPayer.addPhone(64, 27, 4556112);
    poorPayer.setAccount(new Account(456778, "poor"));
    return poorPayer;
}
