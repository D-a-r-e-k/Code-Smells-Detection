/**
     * Apply find/replace regexes to our WM template
     * @param template
     * @return Returns the template with all regexprs applied.
     */
public String convertTemplate(String template) {
    String contents = StringUtils.fileContentsToString(template);
    // Overcome Velocity 0.71 limitation. 
    // HELP: Is this still necessary? 
    if (!contents.endsWith("\n")) {
        contents += "\n";
    }
    // Convert most markup. 
    Perl5Util perl = new Perl5Util();
    for (int i = 0; i < perLineREs.length; i += 2) {
        contents = perl.substitute(makeSubstRE(i), contents);
    }
    // Convert closing curlies. 
    if (perl.match("m/javascript/i", contents)) {
        // ASSUMPTION: JavaScript is indented, WM is not. 
        contents = perl.substitute("s/\n}/\n#end/g", contents);
    } else {
        contents = perl.substitute("s/(\n\\s*)}/$1#end/g", contents);
        contents = perl.substitute("s/#end\\s*\n\\s*#else/#else/g", contents);
    }
    return contents;
}
