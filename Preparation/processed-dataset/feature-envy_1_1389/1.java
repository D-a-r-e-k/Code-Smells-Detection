// 
// Geo Format: 
// [Nodes] 
// id,x,y 
// [Edges] 
// id,source_id,target_id,length 
// 
// TODO: Add prototype edge and vertex as argument. these will be cloned to obtain new cells. 
public static void decode(JGraph graph, DefaultGraphCell cellPrototype, DefaultGraphCell portPrototype, DefaultGraphCell edgePrototype, InputStream fstream, int width, int height) throws Exception {
    // Convert our input stream to a 
    // BufferedReader 
    BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
    // Continue to read lines while 
    // there are still some left to read 
    // Map from keys to vertices 
    Hashtable map = new Hashtable();
    // Link to Existing Vertices! 
    Object[] items = JGraphUtilities.getVertices(graph.getModel(), DefaultGraphModel.getAll(graph.getModel()));
    if (items != null) {
        for (int i = 0; i < items.length; i++) if (items[i] != null && items[i].toString() != null) {
            map.put(items[i].toString(), items[i]);
        }
    }
    // Vertices and Edges to insert 
    ArrayList insert = new ArrayList();
    ConnectionSet cs = new ConnectionSet();
    Hashtable nested = new Hashtable();
    boolean nodes = true;
    double maxx = 0;
    double maxy = 0;
    double minx = Double.MAX_VALUE;
    double miny = Double.MAX_VALUE;
    while (in.ready()) {
        String s = in.readLine();
        if (s.startsWith("[")) {
            nodes = !s.equalsIgnoreCase("[edges]");
        } else {
            StringTokenizer st = new StringTokenizer(s, ",");
            if (nodes) {
                if (st.hasMoreTokens()) {
                    String name = null;
                    Double x = null, y = null;
                    if (st.hasMoreTokens())
                        name = new String(st.nextToken());
                    if (st.hasMoreTokens())
                        x = new Double(st.nextToken());
                    if (st.hasMoreTokens())
                        y = new Double(st.nextToken());
                    if ((name != null) && (x != null) && (y != null)) {
                        Object vertex = getVertexForKey(map, name, cellPrototype, portPrototype);
                        if (!graph.getModel().contains(vertex) && !insert.contains(vertex)) {
                            insert.add(vertex);
                        }
                        Map attrs = new Hashtable();
                        Rectangle2D bounds = new Rectangle2D.Double(x.doubleValue(), y.doubleValue(), 0, 0);
                        maxx = Math.max(maxx, bounds.getX());
                        maxy = Math.max(maxy, bounds.getY());
                        minx = Math.min(minx, bounds.getX());
                        miny = Math.min(minx, bounds.getY());
                        GraphConstants.setBounds(attrs, bounds);
                        GraphConstants.setResize(attrs, true);
                        // autosize on insert 
                        GraphConstants.setBorderColor(map, Color.black);
                        nested.put(vertex, attrs);
                    }
                }
            } else {
                // edges 
                if (st.hasMoreTokens()) {
                    String name = null, src = null, tgt = null;
                    Double len = null;
                    if (st.hasMoreTokens())
                        name = new String(st.nextToken());
                    if (st.hasMoreTokens())
                        src = new String(st.nextToken());
                    if (st.hasMoreTokens())
                        tgt = new String(st.nextToken());
                    if (st.hasMoreTokens())
                        len = new Double(st.nextToken());
                    if ((name != null) && (src != null) && (tgt != null) && (len != null)) {
                        String srckey = src.trim();
                        // Get or create source vertex 
                        Object source = getVertexForKey(map, srckey, cellPrototype, portPrototype);
                        if (!graph.getModel().contains(source) && !insert.contains(source))
                            insert.add(source);
                        String tgtkey = tgt.trim();
                        // Get or create source vertex 
                        Object target = getVertexForKey(map, tgtkey, cellPrototype, portPrototype);
                        if (!graph.getModel().contains(target) && !insert.contains(target))
                            insert.add(target);
                        DefaultGraphCell edge = (DefaultGraphCell) edgePrototype.clone();
                        edge.setUserObject(name);
                        //+": "+len); 
                        Object sourcePort = graph.getModel().getChild(source, 0);
                        Object targetPort = graph.getModel().getChild(target, 0);
                        if (sourcePort != null && targetPort != null) {
                            cs.connect(edge, sourcePort, targetPort);
                            insert.add(0, edge);
                        }
                    }
                }
            }
        }
    }
    in.close();
    // Rescale and translate 
    if (maxx > 0 && maxy > 0) {
        double scalex = ((width > 0) ? width / (maxx - minx) : 1);
        double scaley = ((height > 0) ? height / (maxy - miny) : 1);
        Iterator it = nested.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue() instanceof Map) {
                Map t = (Map) entry.getValue();
                Rectangle2D b = GraphConstants.getBounds(t);
                if (b != null) {
                    // scale in-place 
                    b.setFrame((b.getX() - minx) * scalex, (b.getY() - miny) * scaley, b.getWidth(), b.getHeight());
                }
            }
        }
    }
    graph.getModel().insert(insert.toArray(), nested, cs, null, null);
}
