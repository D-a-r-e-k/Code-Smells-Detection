void checkDatabaseUpdateAuthorisation() {
    session.checkAdmin();
    session.checkDDLWrite();
}
