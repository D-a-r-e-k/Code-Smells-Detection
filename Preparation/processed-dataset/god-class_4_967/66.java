/******************************************************************************/
public void setLayoutUpdateLambda(ArrayList lambda) {
    tf_lu_lambdaNodeDistribution.setText(String.valueOf(((Double) lambda.get(0)).doubleValue()));
    tf_lu_lambdaBorderline.setText(String.valueOf(((Double) lambda.get(1)).doubleValue()));
    tf_lu_lambdaEdgeLength.setText(String.valueOf(((Double) lambda.get(2)).doubleValue()));
    tf_lu_lambdaEdgeCrossing.setText(String.valueOf(((Double) lambda.get(3)).doubleValue()));
    tf_lu_lambdaEdgeDistribution.setText(String.valueOf(((Double) lambda.get(4)).doubleValue()));
    tf_lu_lambdaNodeDistance.setText(String.valueOf(((Double) lambda.get(5)).doubleValue()));
}
