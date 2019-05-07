public String Output() throws ParseException {
    Parse parse = new Parse(OriginalHTML, new String[] { "td" });
    Fixture testbed = new Fixture();
    if (Annotation.equals("right"))
        testbed.right(parse);
    if (Annotation.equals("wrong"))
        testbed.wrong(parse, Text);
    if (Annotation.equals("error"))
        testbed.error(parse, Text);
    if (Annotation.equals("info"))
        testbed.info(parse, Text);
    if (Annotation.equals("ignore"))
        testbed.ignore(parse);
    return GenerateOutput(parse);
}
