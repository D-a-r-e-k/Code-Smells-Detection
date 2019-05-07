/**
   * Main method, used for testing this class.
   */
public static void main(String[] args) {
    // Get random number generator initialized by system 
    Random r = new Random();
    // Create density estimator 
    UnivariateKernelEstimator e = new UnivariateKernelEstimator();
    // Output the density estimator 
    System.out.println(e);
    // Monte Carlo integration 
    double sum = 0;
    for (int i = 0; i < 1000; i++) {
        sum += Math.exp(e.logDensity(r.nextDouble() * 10.0 - 5.0));
    }
    System.out.println("Approximate integral: " + 10.0 * sum / 1000);
    // Add Gaussian values into it 
    for (int i = 0; i < 1000; i++) {
        e.addValue(0.1 * r.nextGaussian() - 3, 1);
        e.addValue(r.nextGaussian() * 0.25, 3);
    }
    // Monte Carlo integration 
    sum = 0;
    int points = 10000;
    for (int i = 0; i < points; i++) {
        double value = r.nextDouble() * 10.0 - 5.0;
        sum += Math.exp(e.logDensity(value));
    }
    System.out.println("Approximate integral: " + 10.0 * sum / points);
    // Check interval estimates 
    double[][] Intervals = e.predictIntervals(0.9);
    System.out.println("Printing kernel intervals ---------------------");
    for (int k = 0; k < Intervals.length; k++) {
        System.out.println("Left: " + Intervals[k][0] + "\t Right: " + Intervals[k][1]);
    }
    System.out.println("Finished kernel printing intervals ---------------------");
    double Covered = 0;
    for (int i = 0; i < 1000; i++) {
        double val = -1;
        if (r.nextDouble() < 0.25) {
            val = 0.1 * r.nextGaussian() - 3.0;
        } else {
            val = r.nextGaussian() * 0.25;
        }
        for (int k = 0; k < Intervals.length; k++) {
            if (val >= Intervals[k][0] && val <= Intervals[k][1]) {
                Covered++;
                break;
            }
        }
    }
    System.out.println("Coverage at 0.9 level for kernel intervals: " + Covered / 1000);
    // Compare performance to normal estimator on normally distributed data 
    UnivariateKernelEstimator eKernel = new UnivariateKernelEstimator();
    UnivariateNormalEstimator eNormal = new UnivariateNormalEstimator();
    for (int j = 1; j < 5; j++) {
        double numTrain = Math.pow(10, j);
        System.out.println("Number of training cases: " + numTrain);
        // Add training cases 
        for (int i = 0; i < numTrain; i++) {
            double val = r.nextGaussian() * 1.5 + 0.5;
            eKernel.addValue(val, 1);
            eNormal.addValue(val, 1);
        }
        // Monte Carlo integration 
        sum = 0;
        points = 10000;
        for (int i = 0; i < points; i++) {
            double value = r.nextDouble() * 20.0 - 10.0;
            sum += Math.exp(eKernel.logDensity(value));
        }
        System.out.println("Approximate integral for kernel estimator: " + 20.0 * sum / points);
        // Evaluate estimators 
        double loglikelihoodKernel = 0, loglikelihoodNormal = 0;
        for (int i = 0; i < 1000; i++) {
            double val = r.nextGaussian() * 1.5 + 0.5;
            loglikelihoodKernel += eKernel.logDensity(val);
            loglikelihoodNormal += eNormal.logDensity(val);
        }
        System.out.println("Loglikelihood for kernel estimator: " + loglikelihoodKernel / 1000);
        System.out.println("Loglikelihood for normal estimator: " + loglikelihoodNormal / 1000);
        // Check interval estimates 
        double[][] kernelIntervals = eKernel.predictIntervals(0.95);
        double[][] normalIntervals = eNormal.predictIntervals(0.95);
        System.out.println("Printing kernel intervals ---------------------");
        for (int k = 0; k < kernelIntervals.length; k++) {
            System.out.println("Left: " + kernelIntervals[k][0] + "\t Right: " + kernelIntervals[k][1]);
        }
        System.out.println("Finished kernel printing intervals ---------------------");
        System.out.println("Printing normal intervals ---------------------");
        for (int k = 0; k < normalIntervals.length; k++) {
            System.out.println("Left: " + normalIntervals[k][0] + "\t Right: " + normalIntervals[k][1]);
        }
        System.out.println("Finished normal printing intervals ---------------------");
        double kernelCovered = 0;
        double normalCovered = 0;
        for (int i = 0; i < 1000; i++) {
            double val = r.nextGaussian() * 1.5 + 0.5;
            for (int k = 0; k < kernelIntervals.length; k++) {
                if (val >= kernelIntervals[k][0] && val <= kernelIntervals[k][1]) {
                    kernelCovered++;
                    break;
                }
            }
            for (int k = 0; k < normalIntervals.length; k++) {
                if (val >= normalIntervals[k][0] && val <= normalIntervals[k][1]) {
                    normalCovered++;
                    break;
                }
            }
        }
        System.out.println("Coverage at 0.95 level for kernel intervals: " + kernelCovered / 1000);
        System.out.println("Coverage at 0.95 level for normal intervals: " + normalCovered / 1000);
        kernelIntervals = eKernel.predictIntervals(0.8);
        normalIntervals = eNormal.predictIntervals(0.8);
        kernelCovered = 0;
        normalCovered = 0;
        for (int i = 0; i < 1000; i++) {
            double val = r.nextGaussian() * 1.5 + 0.5;
            for (int k = 0; k < kernelIntervals.length; k++) {
                if (val >= kernelIntervals[k][0] && val <= kernelIntervals[k][1]) {
                    kernelCovered++;
                    break;
                }
            }
            for (int k = 0; k < normalIntervals.length; k++) {
                if (val >= normalIntervals[k][0] && val <= normalIntervals[k][1]) {
                    normalCovered++;
                    break;
                }
            }
        }
        System.out.println("Coverage at 0.8 level for kernel intervals: " + kernelCovered / 1000);
        System.out.println("Coverage at 0.8 level for normal intervals: " + normalCovered / 1000);
    }
}
