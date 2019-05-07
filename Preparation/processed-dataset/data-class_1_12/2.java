private void addParse(Parse parse, String newString, String[] tags) throws ParseException {
    Parse newParse = new Parse(newString, tags);
    newParse.more = parse.more;
    newParse.trailer = parse.trailer;
    parse.more = newParse;
    parse.trailer = null;
}
