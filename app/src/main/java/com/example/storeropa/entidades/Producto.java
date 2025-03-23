package com.example.storeropa.entidades;

public class Producto {

    private int id;
    private String tipo;
    private String genero;
    private String Talla;
    private String precio;

    public Producto() {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String talla) {
        Talla = talla;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
