/******************************************************************************/
public ArrayList getLambda() {
    ArrayList lambda = new ArrayList();
    lambda.add(new Double(tf_lambdaNodeDistribution.getText()));
    lambda.add(new Double(tf_lambdaBorderline.getText()));
    lambda.add(new Double(tf_lambdaEdgeLength.getText()));
    lambda.add(new Double(tf_lambdaEdgeCrossing.getText()));
    lambda.add(new Double(tf_lambdaEdgeDistribution.getText()));
    lambda.add(new Double(tf_lambdaNodeDistance.getText()));
    lambda = getAdditionalLambda(lambda);
    return lambda;
}
