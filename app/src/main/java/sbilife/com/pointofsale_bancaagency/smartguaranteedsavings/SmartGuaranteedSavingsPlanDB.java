package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

public class SmartGuaranteedSavingsPlanDB {

    ///#######
    public double[] getSAMFRates() {

        return new double[]{7.13, 7.13, 7.13, 7.13, 7.13, 7.13, 7.13, 7.13,
                7.13, 7.12, 7.12, 7.12, 7.10, 7.10, 7.09, 7.09, 7.07, 7.07,
                7.05, 7.03, 7.02, 7.00, 6.98, 6.95, 6.93, 6.90, 6.86, 6.83,
                6.79, 6.74, 6.70, 6.64, 6.58};
    }

    public double[] getTermToMaturity() {

        return new double[]{89.29, 79.73, 71.21, 63.62, 56.85, 50.82, 45.44,
                40.65, 36.38, 32.58, 29.19, 26.18, 23.50, 21.11, 0};
    }

    public double[] getGSVFactors() {

        return new double[]{0, 0.30, 0.30, 0.50, 0.50, 0.50, 0.50, 0.55,
                0.55, 0.55, 0.60, 0.60, 0.60, 0.60, 0.60};
    }

}
