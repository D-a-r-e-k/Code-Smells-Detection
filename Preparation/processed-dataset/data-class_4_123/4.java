public Object visit(final IAttributeCollection attributes, Object ctx) {
    for (int a = 0, aCount = attributes.size(); a < aCount; ++a) {
        // TODO: define a global way to set the mask set of attrs to be visited  
        attributes.get(a).accept(this, ctx);
    }
    return ctx;
}
