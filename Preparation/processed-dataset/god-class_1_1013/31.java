/**
 * Constructs a function that returns a new uniform random number in the open unit interval <code>(0.0,1.0)</code> (excluding 0.0 and 1.0).
 * Currently the engine is {@link cern.jet.random.engine.MersenneTwister}
 * and is seeded with the current time.
 * <p>
 * Note that any random engine derived from {@link cern.jet.random.engine.RandomEngine} and any random distribution derived from {@link cern.jet.random.AbstractDistribution} are function objects, because they implement the proper interfaces.
 * Thus, if you are not happy with the default, just pass your favourite random generator to function evaluating methods.
 */
public static DoubleFunction random() {
    return new cern.jet.random.engine.MersenneTwister(new java.util.Date());
}
