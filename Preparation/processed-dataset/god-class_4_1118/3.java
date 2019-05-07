void processAlter() {
    session.setScripting(true);
    readThis(Tokens.ALTER);
    switch(token.tokenType) {
        case Tokens.TABLE:
            {
                read();
                processAlterTable();
                break;
            }
        case Tokens.DOMAIN:
            {
                read();
                processAlterDomain();
                break;
            }
        default:
            {
                throw unexpectedToken();
            }
    }
}
