package com.divorce.estimator.service;

public class CalculationResult {

    private final double aNet;
    private final double bNet;
    private final double totalNet;
    private final double equalShare;
    private final double aReceivable;
    private final double bReceivable;

    public CalculationResult(double aNet,
                             double bNet,
                             double totalNet,
                             double equalShare,
                             double aReceivable,
                             double bReceivable) {
        this.aNet = aNet;
        this.bNet = bNet;
        this.totalNet = totalNet;
        this.equalShare = equalShare;
        this.aReceivable = aReceivable;
        this.bReceivable = bReceivable;
    }

    public double getANet() { return aNet; }
    public double getBNet() { return bNet; }
    public double getTotalNet() { return totalNet; }
    public double getEqualShare() { return equalShare; }
    public double getAReceivable() { return aReceivable; }
    public double getBReceivable() { return bReceivable; }
}
