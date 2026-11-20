package com.example.javafx_veterinariasegundointento.model;

public class Perro extends Mascota {
    private String raza;
    private boolean vacunado;

    public Perro(String nombre, int edad, double costoBase,double peso, String raza, boolean vacunado) {
        super(nombre, edad,peso, costoBase);
        this.raza = raza;
        this.vacunado = vacunado;
    }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public boolean isVacunado() { return vacunado; }
    public void setVacunado(boolean vacunado) { this.vacunado = vacunado; }

    @Override
    public double calcularCostoFinal() {
        return getCostoBase() + (vacunado ? 5000 : 15000);
    }
}
