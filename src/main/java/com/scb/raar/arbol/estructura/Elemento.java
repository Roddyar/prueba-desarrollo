package com.scb.raar.arbol.estructura;

import java.io.Serializable;

/**
 * @author raruiz
 */
public class Elemento<Clave> implements Serializable {

    private Clave clave;
    private int posicion;

    public Elemento(Clave clave, int posicion) {
        this.clave = clave;
        this.posicion = posicion;
    }

    public Clave getClave() {
        return clave;
    }

    public void setClave(Clave clave) {
        this.clave = clave;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

}
