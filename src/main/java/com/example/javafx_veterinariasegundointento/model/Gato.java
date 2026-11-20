package com.example.javafx_veterinariasegundointento.model;

public class Gato extends Mascota {
    private boolean indoor;

    public Gato(String nombre, int edad, double costoBase,double peso, boolean indoor) {
        super(nombre, edad, peso, costoBase);
        this.indoor = indoor;
    }

    public boolean isIndoor() { return indoor; }
    public void setIndoor(boolean indoor) { this.indoor = indoor; }

    @Override
    public double calcularCostoFinal() {
        return getCostoBase() + (indoor ? 4000 : 10000);
    }
}
