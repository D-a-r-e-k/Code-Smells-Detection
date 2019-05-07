/******************************************************************************/
public void setLambda(ArrayList lambda) {
    tf_lambdaNodeDistribution.setText(String.valueOf(((Double) lambda.get(0)).doubleValue()));
    tf_lambdaBorderline.setText(String.valueOf(((Double) lambda.get(1)).doubleValue()));
    tf_lambdaEdgeLength.setText(String.valueOf(((Double) lambda.get(2)).doubleValue()));
    tf_lambdaEdgeCrossing.setText(String.valueOf(((Double) lambda.get(3)).doubleValue()));
    tf_lambdaEdgeDistribution.setText(String.valueOf(((Double) lambda.get(4)).doubleValue()));
    tf_lambdaNodeDistance.setText(String.valueOf(((Double) lambda.get(5)).doubleValue()));
}
