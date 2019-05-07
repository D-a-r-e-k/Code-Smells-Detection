/**
	 * Usage: QuickServer [-options]<br/>
	 * Where options include:<br/>
	 *   -about		Opens About Dialogbox<br/>
	 *   -load <xml_config_file> [options]	Loads the server from xml file.
	 * where options include:
	 *    -fullXML2File <new_file_name>
	 */
public static void main(String args[]) {
    try {
        if (args.length >= 1) {
            if (args[0].equals("-about")) {
                org.quickserver.net.server.gui.About.main(null);
            } else if (args[0].equals("-load") && args.length >= 2) {
                QuickServer qs = QuickServer.load(args[1]);
                if (qs != null)
                    handleOptions(args, qs);
            } else {
                System.out.println(printUsage());
            }
        } else {
            System.out.println(printUsage());
            org.quickserver.net.server.gui.About.showAbout();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
