/**
     * Process an XML file using Velocity
     */
private void process(String xmlFile, Document projectDocument) throws BuildException {
    File outFile = null;
    File inFile = null;
    Writer writer = null;
    try {
        // the current input file relative to the baseDir 
        inFile = new File(baseDir, xmlFile);
        // the output file relative to basedir 
        outFile = new File(destDir, xmlFile.substring(0, xmlFile.lastIndexOf('.')) + extension);
        // only process files that have changed 
        if (lastModifiedCheck == false || (inFile.lastModified() > outFile.lastModified() || styleSheetLastModified > outFile.lastModified() || projectFileLastModified > outFile.lastModified() || userContextsModifed(outFile.lastModified()))) {
            ensureDirectoryFor(outFile);
            //-- command line status 
            log("Input:  " + xmlFile, Project.MSG_INFO);
            // Build the JDOM Document 
            Document root = builder.build(inFile);
            // Shove things into the Context 
            VelocityContext context = new VelocityContext();
            /*
                 *  get the property TEMPLATE_ENCODING
                 *  we know it's a string...
                 */
            String encoding = (String) ve.getProperty(RuntimeConstants.OUTPUT_ENCODING);
            if (encoding == null || encoding.length() == 0 || encoding.equals("8859-1") || encoding.equals("8859_1")) {
                encoding = "ISO-8859-1";
            }
            Format f = Format.getRawFormat();
            f.setEncoding(encoding);
            OutputWrapper ow = new OutputWrapper(f);
            context.put("root", root.getRootElement());
            context.put("xmlout", ow);
            context.put("relativePath", getRelativePath(xmlFile));
            context.put("treeWalk", new TreeWalker());
            context.put("xpath", new XPathTool());
            context.put("escape", new Escape());
            context.put("date", new java.util.Date());
            /**
                 * only put this into the context if it exists.
                 */
            if (projectDocument != null) {
                context.put("project", projectDocument.getRootElement());
            }
            /**
                 *  Add the user subcontexts to the to context
                 */
            for (Iterator iter = contexts.iterator(); iter.hasNext(); ) {
                Context subContext = (Context) iter.next();
                if (subContext == null) {
                    throw new BuildException("Found an undefined SubContext!");
                }
                if (subContext.getContextDocument() == null) {
                    throw new BuildException("Could not build a subContext for " + subContext.getName());
                }
                context.put(subContext.getName(), subContext.getContextDocument().getRootElement());
            }
            /**
                 * Process the VSL template with the context and write out
                 * the result as the outFile.
                 */
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), encoding));
            /**
                 * get the template to process
                 */
            Template template = ve.getTemplate(style);
            template.merge(context, writer);
            log("Output: " + outFile, Project.MSG_INFO);
        }
    } catch (JDOMException e) {
        outFile.delete();
        if (e.getCause() != null) {
            Throwable rootCause = e.getCause();
            if (rootCause instanceof SAXParseException) {
                System.out.println("");
                System.out.println("Error: " + rootCause.getMessage());
                System.out.println("       Line: " + ((SAXParseException) rootCause).getLineNumber() + " Column: " + ((SAXParseException) rootCause).getColumnNumber());
                System.out.println("");
            } else {
                rootCause.printStackTrace();
            }
        } else {
            e.printStackTrace();
        }
    } catch (Throwable e) {
        if (outFile != null) {
            outFile.delete();
        }
        e.printStackTrace();
    } finally {
        if (writer != null) {
            try {
                writer.flush();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
}
