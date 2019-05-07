@Override
protected List<Object> parseTable(Table table, TestResults testResults) {
    switch(componentTyped.typeCases()) {
        case CLASS_TYPE:
            if (componentTyped.isEffectivelyPrimitive() || componentTyped.isEnum()) {
                ArraySetUpTraverse setUpTraverse = new ArraySetUpTraverse(componentTyped.asClass(), componentTyped.parser(evaluator));
                setUpTraverse.setRuntimeContext(evaluator.getRuntimeContext());
                setUpTraverse.interpretAfterFirstRow(table, testResults);
                Object array = setUpTraverse.getResults();
                List<Object> result = new ArrayList<Object>();
                for (int i = 0; i < Array.getLength(array); i++) result.add(Array.get(array, i));
                return result;
            }
            if (componentTyped.isArray())
                return parseNested(table, testResults);
            if (CollectionSetUpTraverse.hasObjectFactoryMethodFor(table, evaluator))
                return super.parseTable(table, testResults);
            ListSetUpTraverse2 setUp = new ListSetUpTraverse2(componentTyped.asClass());
            setUp.interpretWithinScope(table, evaluator, testResults);
            return setUp.getResults();
        case PARAMETERIZED_TYPE:
            return parseNested(table, testResults);
        default:
            throw new FitLibraryException("Type not sufficiently bound: " + componentTyped);
    }
}
