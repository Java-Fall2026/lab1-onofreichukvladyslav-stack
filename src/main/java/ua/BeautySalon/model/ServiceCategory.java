package ua.BeautySalon.model;

public enum ServiceCategory {
    HAIRCUT(1.0),
    MANICURE(1.2),
    MASSAGE(1.5),
    FACIAL(1.8);

    private final double priceFactor;

    ServiceCategory(double priceFactor){
        this.priceFactor = priceFactor;
    }

    public double getPriceFactor() {
        return priceFactor;
    }
}
