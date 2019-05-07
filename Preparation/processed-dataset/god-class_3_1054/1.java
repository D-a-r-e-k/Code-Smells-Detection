public void processDelete_forMember(GenericRequest request) throws BadInputException, DatabaseException {
    // primary key column(s)  
    SecurityUtil.checkHttpReferer(request);
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    DAOFactory.getWatchDAO().delete_inMember(memberID);
}
