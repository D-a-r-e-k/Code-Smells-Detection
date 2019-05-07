void method0() { 
/** for serialization */
private static final long serialVersionUID = 7268777944939129714L;
/**
   * The clusterer
   */
protected Clusterer m_clusterer;
//  protected Instances m_trainingSet; 
/**
   * Training or Test Instances
   */
//  protected Instances m_testSet; 
protected DataSetEvent m_testSet;
/**
   * The set number for the test set
   */
protected int m_setNumber;
/**
   * Indicates if m_testSet is a training or a test set. 0 for test, >0 for training
   */
protected int m_testOrTrain;
/**
   * The last set number for this series
   */
protected int m_maxSetNumber;
public static int TEST = 0;
public static int TRAINING = 1;
}
