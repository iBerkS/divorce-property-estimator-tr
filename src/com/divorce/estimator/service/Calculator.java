package com.divorce.estimator.service;

import com.divorce.estimator.model.Asset;
import com.divorce.estimator.model.PropertyType;
import com.divorce.estimator.model.Spouse;

import java.util.List;

public class Calculator {

    public CalculationResult calculate(List<Asset> assets) {

        double aNet = 0.0;
        double bNet = 0.0;

        for (Asset asset : assets) {

            // Kişisel mallar paylaşım dışı
            if (asset.getPropertyType() == PropertyType.PERSONAL) {
                continue;
            }

            double net = asset.getNetValue();

            // Negatif neti 0 kabul edelim (1.0.0 basitlik için)
            if (net < 0) {
                net = 0;
            }

            if (asset.getOwner() == Spouse.A) {
                aNet += net;
            } else {
                bNet += net;
            }
        }

        double total = aNet + bNet;
        double share = total / 2.0;

        double aReceivable = share - aNet;
        double bReceivable = share - bNet;

        return new CalculationResult(
                aNet,
                bNet,
                total,
                share,
                aReceivable,
                bReceivable
        );
    }
}
