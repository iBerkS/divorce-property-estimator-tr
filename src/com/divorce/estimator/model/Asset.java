package com.divorce.estimator.model;

public class Asset {

    private String name;
    private PropertyType propertyType;
    private Spouse owner;
    private double value;
    private double debt;


    public Asset(String name,
                 PropertyType propertyType,
                 Spouse owner,
                 double value,
                 double debt) {

        this.name = name;
        this.propertyType = propertyType;
        this.owner = owner;
        this.value = value;
        this.debt = debt;
    }


    public String getName(){return name;}


    public PropertyType getPropertyType(){return propertyType;}


    public Spouse getOwner(){return owner;}


    public double getValue(){return value;}


    public double getDebt() {return debt;}


    public double getNetValue(){return value - debt;}

}
