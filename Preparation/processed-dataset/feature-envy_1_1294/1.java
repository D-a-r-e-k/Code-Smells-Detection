/**
   * @param templateName the name of the template. E.g. 
   *        "/templates/Token.template".
   * @param options the processing options in force, such
   *        as "STATIC=yes" 
   */
public JavaFileGenerator(String templateName, Map options) {
    this.templateName = templateName;
    this.options = options;
}
