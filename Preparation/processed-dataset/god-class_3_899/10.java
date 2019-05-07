static String genReturn(boolean value) {
    String retval = (value ? "true" : "false");
    if (Options.getDebugLookahead() && jj3_expansion != null) {
        String tracecode = "trace_return(\"" + ((NormalProduction) jj3_expansion.parent).getLhs() + "(LOOKAHEAD " + (value ? "FAILED" : "SUCCEEDED") + ")\");";
        if (Options.getErrorReporting()) {
            tracecode = "if (!jj_rescan) " + tracecode;
        }
        return "{ " + tracecode + " return " + retval + "; }";
    } else {
        return "return " + retval + ";";
    }
}
