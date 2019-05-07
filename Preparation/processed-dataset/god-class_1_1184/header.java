void method0() { 
/** There is a call to equals() in the method. */
public static final RefComparisonWarningProperty SAW_CALL_TO_EQUALS = new RefComparisonWarningProperty("SAW_CALL_TO_EQUALS", PriorityAdjustment.AT_MOST_LOW);
/** Method is private (or package-protected). */
public static final RefComparisonWarningProperty PRIVATE_METHOD = new RefComparisonWarningProperty("PRIVATE_METHOD", PriorityAdjustment.LOWER_PRIORITY);
/** Compare inside test case */
public static final RefComparisonWarningProperty COMPARE_IN_TEST_CASE = new RefComparisonWarningProperty("COMPARE_IN_TEST_CASE", PriorityAdjustment.FALSE_POSITIVE);
/** Comparing static strings using equals operator. */
public static final RefComparisonWarningProperty COMPARE_STATIC_STRINGS = new RefComparisonWarningProperty("COMPARE_STATIC_STRINGS", PriorityAdjustment.FALSE_POSITIVE);
/** Comparing a dynamic string using equals operator. */
public static final RefComparisonWarningProperty DYNAMIC_AND_UNKNOWN = new RefComparisonWarningProperty("DYNAMIC_AND_UNKNOWN", PriorityAdjustment.RAISE_PRIORITY);
public static final RefComparisonWarningProperty STRING_PARAMETER_IN_PUBLIC_METHOD = new RefComparisonWarningProperty("STATIC_AND_PARAMETER_IN_PUBLIC_METHOD", PriorityAdjustment.RAISE_PRIORITY);
public static final RefComparisonWarningProperty STRING_PARAMETER = new RefComparisonWarningProperty("STATIC_AND_PARAMETER", PriorityAdjustment.NO_ADJUSTMENT);
/** Comparing static string and an unknown string. */
public static final RefComparisonWarningProperty STATIC_AND_UNKNOWN = new RefComparisonWarningProperty("STATIC_AND_UNKNOWN", PriorityAdjustment.LOWER_PRIORITY);
/** Saw a call to String.intern(). */
public static final RefComparisonWarningProperty SAW_INTERN = new RefComparisonWarningProperty("SAW_INTERN", PriorityAdjustment.LOWER_PRIORITY);
}
