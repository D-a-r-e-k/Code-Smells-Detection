/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibraryGeneric.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fitlibrary.collection.CollectionSetUpTraverse;
import fitlibrary.collection.array.ArraySetUpTraverse;
import fitlibrary.collection.array.ArrayTraverse;
import fitlibrary.collection.list.ListTraverse;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.parser.Parser;
import fitlibrary.parser.collection.ListParser;
import fitlibrary.parser.lookup.ParserFactory;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Table;
import fitlibrary.traverse.Evaluator;
import fitlibrary.typed.Typed;
import fitlibraryGeneric.generic.GenericTypeUtility.GenericCases;
import fitlibraryGeneric.typed.GenericTyped;
import fitlibraryGeneric.typed.GenericTypedObject;

public class ListParser2 extends ListParser {
    private GenericTyped componentTyped;
    
    public ListParser2(Evaluator evaluator, GenericTyped typed) {
        super(evaluator,typed);
        this.componentTyped = typed.getComponentTyped();
        if (componentTyped.typeCases() == GenericCases.CLASS_TYPE) {// to handle comma-separated list
        	valueParser = showParser = componentTyped.resultParser(evaluator);
        }
    }
    @Override
	protected List<Object> parseTable(Table table, TestResults testResults) {
    	switch (componentTyped.typeCases()) {
    	case CLASS_TYPE:
       		if (componentTyped.isEffectivelyPrimitive() || componentTyped.isEnum()) {
    			ArraySetUpTraverse setUpTraverse = new ArraySetUpTraverse(componentTyped.asClass(),componentTyped.parser(evaluator));
    			setUpTraverse.setRuntimeContext(evaluator.getRuntimeContext());
    			setUpTraverse.interpretAfterFirstRow(table, testResults);
    			Object array = setUpTraverse.getResults();
    			List<Object> result = new ArrayList<Object>();
    			for (int i = 0; i < Array.getLength(array); i++)
    				result.add(Array.get(array, i));
    			return result;
    		}
       		if (componentTyped.isArray())
        		return parseNested(table, testResults);
    		if (CollectionSetUpTraverse.hasObjectFactoryMethodFor(table,evaluator))
    			return super.parseTable(table,testResults);
    		ListSetUpTraverse2 setUp = new ListSetUpTraverse2(componentTyped.asClass());
    		setUp.interpretWithinScope(table,evaluator,testResults);
    		return setUp.getResults();
    	case PARAMETERIZED_TYPE:
    		return parseNested(table, testResults);
    	default:
    		throw new FitLibraryException("Type not sufficiently bound: "+componentTyped);
    	}
    }
	private List<Object> parseNested(Table table, TestResults testResults) {
		NestingListSetUpTraverse nestedSetUp = new NestingListSetUpTraverse(componentTyped);
		nestedSetUp.interpretWithinScope(table,evaluator,testResults);
		return nestedSetUp.getResults();
	}
    @SuppressWarnings({"fallthrough", "unchecked"})
	@Override
    protected boolean tableMatches(Table table, Object initialResult, TestResults testResults) {
    	Object result = initialResult;
    	List<?> listResult = null;
    	if (result instanceof List)
    		listResult = (List<?>)result;
    	else if (result.getClass().isArray())
    		listResult = Arrays.asList((Object[])result);
    	else
    		throw new FitLibraryException("Unable to treat object as list: "+result.getClass());
    	switch (componentTyped.typeCases()) {
    	case CLASS_TYPE:
    		if (componentTyped.isEffectivelyPrimitive() || componentTyped.isEnum()) {
				Object arrayResult = Array.newInstance(componentTyped.asClass(), listResult.size());
    			for (int i = 0; i < listResult.size(); i++)
    				Array.set(arrayResult,i,listResult.get(i));
				ArrayTraverse arrayTraverse = new ArrayTraverse(new GenericTypedObject(arrayResult, new GenericTyped(arrayResult.getClass())));
    			return arrayTraverse.doesInnerTablePass(table,evaluator,testResults);
    		}
    		if (result.getClass().isArray())
    			result = Arrays.asList((Object[])result);
        	if (!componentTyped.isArray()) {
        		ListTraverse listTraverse = new ListTraverse(null);
        		listTraverse.setActualCollection(listResult);
        		listTraverse.setComponentType(componentTyped.asClass());
        		return listTraverse.doesInnerTablePass(table,evaluator,testResults);
        	}
    	case PARAMETERIZED_TYPE:
    		if (result.getClass().isArray())
    			result = Arrays.asList((Object[])result);
    		NestingListTraverse nestingList = new NestingListTraverse((List<Object>)result,componentTyped);
    		return nestingList.doesTablePass(table,evaluator,testResults);
    	default:
    		throw new FitLibraryException("Type not sufficiently bound: "+componentTyped);
    	}
    }
    public static ParserFactory parserFactory() {
    	return new ParserFactory() {
    		public Parser parser(Evaluator evaluator, Typed typed) {
    			if (typed instanceof GenericTyped) {
    				GenericTyped genericTyped = (GenericTyped)typed;
    				if (genericTyped.isGeneric() || genericTyped.isArray())
    					return new ListParser2(evaluator,genericTyped);
    			}
    			return new ListParser(evaluator,typed);
    		}
    	};
    }
}
