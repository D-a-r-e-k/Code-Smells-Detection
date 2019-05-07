/******************************************************************************/
public ArrayList getLayoutUpdateLambda() {
    ArrayList lambda = new ArrayList();
    lambda.add(new Double(tf_lu_lambdaNodeDistribution.getText()));
    lambda.add(new Double(tf_lu_lambdaBorderline.getText()));
    lambda.add(new Double(tf_lu_lambdaEdgeLength.getText()));
    lambda.add(new Double(tf_lu_lambdaEdgeCrossing.getText()));
    lambda.add(new Double(tf_lu_lambdaEdgeDistribution.getText()));
    lambda.add(new Double(tf_lu_lambdaNodeDistance.getText()));
    lambda = getLayoutUpdateAdditionalLambda(lambda);
    return lambda;
}
