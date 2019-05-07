void method0() { 
/** Redundant null comparison is of a checked null value. */
public static final NullDerefProperty CHECKED_VALUE = new NullDerefProperty("CHECKED_VALUE", PriorityAdjustment.RAISE_PRIORITY);
/** Redundant null comparison is of a checked null value. */
public static final NullDerefProperty LONG_RANGE_NULL_SOURCE = new NullDerefProperty("LONG_RANGE_NULL_SOURCE", PriorityAdjustment.AT_MOST_MEDIUM);
/** Redundant nullcheck of previously dereferenced value. */
public static final NullDerefProperty WOULD_HAVE_BEEN_A_KABOOM = new NullDerefProperty("WOULD_HAVE_BEEN_A_KABOOM", PriorityAdjustment.RAISE_PRIORITY);
/** Redundant nullcheck created dead code. */
public static final NullDerefProperty CREATED_DEAD_CODE = new NullDerefProperty("CREATED_DEAD_CODE", PriorityAdjustment.RAISE_PRIORITY);
public static final NullDerefProperty DEREFS_ARE_CLONED = new NullDerefProperty("DEREFS_ARE_CLONED", PriorityAdjustment.AT_MOST_MEDIUM);
public static final NullDerefProperty CLOSING_NULL = new NullDerefProperty("CLOSING_NULL", PriorityAdjustment.PEGGED_HIGH);
public static final NullDerefProperty DEREFS_ARE_INLINED_FINALLY_BLOCKS = new NullDerefProperty("DEREFS_ARE_INLINED_FINALLY_BLOCKS", PriorityAdjustment.AT_MOST_MEDIUM);
public static final NullDerefProperty DEREFS_IN_CATCH_BLOCKS = new NullDerefProperty("DEREFS_IN_CATCH_BLOCKS", PriorityAdjustment.RAISE_PRIORITY);
}
