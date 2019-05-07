@SuppressWarnings("unchecked")
public GraphMetadata parse(XMLEventReader xmlEventReader, StartElement start) throws GraphIOException {
    try {
        // Create the new graph. 
        GraphMetadata graphMetadata = new GraphMetadata();
        // Parse the attributes. 
        Iterator iterator = start.getAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            String name = attribute.getName().getLocalPart();
            String value = attribute.getValue();
            if (graphMetadata.getId() == null && GraphMLConstants.ID_NAME.equals(name)) {
                graphMetadata.setId(value);
            } else if (graphMetadata.getEdgeDefault() == null && GraphMLConstants.EDGEDEFAULT_NAME.equals(name)) {
                graphMetadata.setEdgeDefault(GraphMLConstants.DIRECTED_NAME.equals(value) ? EdgeDefault.DIRECTED : EdgeDefault.UNDIRECTED);
            } else {
                graphMetadata.setProperty(name, value);
            }
        }
        // Make sure the graphdefault has been set. 
        if (graphMetadata.getEdgeDefault() == null) {
            throw new GraphIOException("Element 'graph' is missing attribute 'edgedefault'");
        }
        Map<String, V> idToVertexMap = new HashMap<String, V>();
        Collection<EdgeMetadata> edgeMetadata = new LinkedList<EdgeMetadata>();
        Collection<HyperEdgeMetadata> hyperEdgeMetadata = new LinkedList<HyperEdgeMetadata>();
        while (xmlEventReader.hasNext()) {
            XMLEvent event = xmlEventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement element = (StartElement) event;
                String name = element.getName().getLocalPart();
                if (GraphMLConstants.DESC_NAME.equals(name)) {
                    // Parse the description and set it in the graph. 
                    String desc = (String) getParser(name).parse(xmlEventReader, element);
                    graphMetadata.setDescription(desc);
                } else if (GraphMLConstants.DATA_NAME.equals(name)) {
                    // Parse the data element and store the property in the graph. 
                    DataMetadata data = (DataMetadata) getParser(name).parse(xmlEventReader, element);
                    graphMetadata.addData(data);
                } else if (GraphMLConstants.NODE_NAME.equals(name)) {
                    // Parse the node metadata 
                    NodeMetadata metadata = (NodeMetadata) getParser(name).parse(xmlEventReader, element);
                    // Create the vertex object and store it in the metadata 
                    V vertex = getParserContext().createVertex(metadata);
                    metadata.setVertex(vertex);
                    idToVertexMap.put(metadata.getId(), vertex);
                    // Add it to the graph 
                    graphMetadata.addNodeMetadata(vertex, metadata);
                } else if (GraphMLConstants.EDGE_NAME.equals(name)) {
                    // Parse the edge metadata 
                    EdgeMetadata metadata = (EdgeMetadata) getParser(name).parse(xmlEventReader, element);
                    // Set the directed property if not overridden. 
                    if (metadata.isDirected() == null) {
                        metadata.setDirected(graphMetadata.getEdgeDefault() == EdgeDefault.DIRECTED);
                    }
                    // Create the edge object and store it in the metadata 
                    E edge = getParserContext().createEdge(metadata);
                    edgeMetadata.add(metadata);
                    metadata.setEdge(edge);
                    // Add it to the graph. 
                    graphMetadata.addEdgeMetadata(edge, metadata);
                } else if (GraphMLConstants.HYPEREDGE_NAME.equals(name)) {
                    // Parse the edge metadata 
                    HyperEdgeMetadata metadata = (HyperEdgeMetadata) getParser(name).parse(xmlEventReader, element);
                    // Create the edge object and store it in the metadata 
                    E edge = getParserContext().createHyperEdge(metadata);
                    hyperEdgeMetadata.add(metadata);
                    metadata.setEdge(edge);
                    // Add it to the graph 
                    graphMetadata.addHyperEdgeMetadata(edge, metadata);
                } else {
                    // Treat anything else as unknown 
                    getUnknownParser().parse(xmlEventReader, element);
                }
            }
            if (event.isEndElement()) {
                EndElement end = (EndElement) event;
                verifyMatch(start, end);
                break;
            }
        }
        // Apply the keys to this object. 
        applyKeys(graphMetadata);
        // Create the graph object and store it in the metadata 
        G graph = getParserContext().createGraph(graphMetadata);
        graphMetadata.setGraph(graph);
        // Add all of the vertices to the graph object. 
        addVerticesToGraph(graph, idToVertexMap.values());
        // Add the edges to the graph object. 
        addEdgesToGraph(graph, edgeMetadata, idToVertexMap);
        addHyperEdgesToGraph(graph, hyperEdgeMetadata, idToVertexMap);
        return graphMetadata;
    } catch (Exception e) {
        ExceptionConverter.convert(e);
    }
    return null;
}
