/** Add all of the components for the Javadoc panel of the preferences window. */
private void _setupJavadocPanel(ConfigPanel panel) {
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.JAVADOC_API_REF_VERSION, "Java Version for \"Open Java API Javadoc\"", this, "Version of the Java API documentation to be used."));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.JAVADOC_ACCESS_LEVEL, "Access Level", this, "<html>Fields and methods with access modifiers at this level<br>" + "or higher will be included in the generated Javadoc.</html>"));
    addOptionComponent(panel, new ForcedChoiceOptionComponent(OptionConstants.JAVADOC_LINK_VERSION, "Java Version for Javadoc Links", this, "Version of Java for generating links to online Javadoc documentation."));
    addOptionComponent(panel, new StringOptionComponent(OptionConstants.JAVADOC_1_5_LINK, "Javadoc 1.5 URL", this, "URL for the Java 1.5 API, for generating links to library classes."));
    addOptionComponent(panel, new StringOptionComponent(OptionConstants.JAVADOC_1_6_LINK, "Javadoc 1.6 URL", this, "URL for the Java 1.6 API, for generating links to library classes."));
    addOptionComponent(panel, new StringOptionComponent(OptionConstants.JUNIT_LINK, "JUnit URL", this, "URL for the JUnit API, for \"Open Java API Javadoc\"."));
    VectorStringOptionComponent additionalJavadoc = new VectorStringOptionComponent(OptionConstants.JAVADOC_ADDITIONAL_LINKS, "Additional Javadoc URLs", this, "<html>Additional URLs with Javadoc, for \"Open Java API Javadoc\"<br>" + "and auto-completion.</html>") {

        protected boolean verify(String s) {
            // verify that the allclasses-frame.html file exists at that URL. do not actually parse it now 
            boolean result = true;
            try {
                java.net.URL url = new java.net.URL(s + "/allclasses-frame.html");
                java.io.InputStream urls = url.openStream();
                java.io.InputStreamReader is = null;
                java.io.BufferedReader br = null;
                try {
                    is = new java.io.InputStreamReader(urls);
                    br = new java.io.BufferedReader(is);
                    String line = br.readLine();
                    if (line == null) {
                        result = false;
                    }
                } finally {
                    if (br != null) {
                        br.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (urls != null) {
                        urls.close();
                    }
                }
            } catch (java.io.IOException ioe) {
                result = false;
            }
            if (!result) {
                JOptionPane.showMessageDialog(ConfigFrame.this, "Could not find the Javadoc at the URL\n" + s, "Error Adding Javadoc", JOptionPane.ERROR_MESSAGE);
            }
            return result;
        }
    };
    addOptionComponent(panel, additionalJavadoc);
    addOptionComponent(panel, new DirectoryOptionComponent(OptionConstants.JAVADOC_DESTINATION, "Default Destination Directory", this, "Optional default directory for saving Javadoc documentation.", _dirChooser));
    addOptionComponent(panel, javadocCustomParams = new StringOptionComponent(OptionConstants.JAVADOC_CUSTOM_PARAMS, "Custom Javadoc Parameters", this, "Any extra flags or parameters to pass to Javadoc."));
    // Note: JAVADOC_FROM_ROOTS is intended to set the -subpackages flag, but I don't think that's something 
    // we should support -- in general, we only support performing operations on the files that are open. 
    // (dlsmith r4189) 
    //    addOptionComponent(panel,  
    //                       new BooleanOptionComponent(OptionConstants.JAVADOC_FROM_ROOTS, 
    //                                                  "Generate Javadoc From Source Roots", this, 
    //                                                  "<html>Whether \"Javadoc All\" should generate Javadoc for all packages<br>" + 
    //                                                  "in an open document's source tree, rather than just the document's<br>" + 
    //                                                  "own package and sub-packages.</html>")); 
    panel.displayComponents();
}
