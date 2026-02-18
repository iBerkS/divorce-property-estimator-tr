package com.divorce.estimator;

import com.divorce.estimator.model.*;
import com.divorce.estimator.service.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Asset> assets = new ArrayList<>();

        assets.add(new Asset("Araba",
                PropertyType.ACQUIRED,
                Spouse.A,
                50000,
                0));

        assets.add(new Asset("Birikim",
                PropertyType.ACQUIRED,
                Spouse.B,
                70000,
                0));

        assets.add(new Asset("Evlilik öncesi arsa",
                PropertyType.PERSONAL,
                Spouse.A,
                200000,
                0));

        Calculator calculator = new Calculator();
        CalculationResult result = calculator.calculate(assets);

        System.out.println("A net edinilmiş: " + result.getANet());
        System.out.println("B net edinilmiş: " + result.getBNet());
        System.out.println("Toplam net: " + result.getTotalNet());
        System.out.println("Kişi başı hak: " + result.getEqualShare());

        if (result.getAReceivable() > 0) {
            System.out.println("B, A'ya " + result.getAReceivable() + " TL öder.");
        } else if (result.getBReceivable() > 0) {
            System.out.println("A, B'ye " + result.getBReceivable() + " TL öder.");
        } else {
            System.out.println("Denge eşit.");
        }
    }
}
