/**
     * Determine if none of the parameters have a name, and if that
     * is the case, it means that the parameter values should be sent
     * as the entity body
     *
     * @return true if none of the parameters have a name specified
     */
public boolean getSendParameterValuesAsPostBody() {
    if (getPostBodyRaw()) {
        return true;
    } else {
        boolean noArgumentsHasName = true;
        PropertyIterator args = getArguments().iterator();
        while (args.hasNext()) {
            HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
            if (arg.getName() != null && arg.getName().length() > 0) {
                noArgumentsHasName = false;
                break;
            }
        }
        return noArgumentsHasName;
    }
}
