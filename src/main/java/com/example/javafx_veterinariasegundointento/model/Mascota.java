package com.example.javafx_veterinariasegundointento.model;

public abstract class Mascota {
    private String nombre;
    private int edad;
    private double costoBase;

    public Mascota(String nombre, int edad, double peso, double costoBase) {
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
        this.costoBase = costoBase;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public double getCostoBase() { return costoBase; }
    public void setCostoBase(double costoBase) { this.costoBase = costoBase; }

    public abstract double calcularCostoFinal();
    private double peso;



    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
}

