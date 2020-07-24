package com.scb.raar.arbol.estructura;

import com.scb.raar.arbol.exception.ArbolException;
import com.scb.raar.arbol.operaciones.ArbolRaiz;

import java.io.Serializable;

/**
 * @author raruiz
 */
public abstract class Nodo<Clave extends Comparable<Clave>> implements Serializable {

    private int padre;
    private TipoNodo tipo;
    private int numElementos = 0;
    private int posicion;

    public int getPadre() {
        return padre;
    }

    public void setPadre(int padre) {
        this.padre = padre;
    }

    public TipoNodo getTipo() {
        return tipo;
    }

    public void setTipo(TipoNodo tipo) {
        this.tipo = tipo;
    }

    public int getNumElementos() {
        return numElementos;
    }

    public void setNumElementos(int numElementos) {
        this.numElementos = numElementos;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Boolean isMenorIgual(Clave elem1, Clave elem2) {
        int comparacion = elem1.compareTo(elem2);
        return comparacion <= 0;
    }

    public Boolean isMayor(Clave elem1, Clave elem2) {
        int comparacion = elem1.compareTo(elem2);
        return comparacion > 0;
    }

    public Boolean isIgual(Clave elem1, Clave elem2) {
        int comparacion = elem1.compareTo(elem2);
        return comparacion == 0;
    }

    public void modificarNumElementos(int numElementos) {
        this.numElementos += numElementos;
    }

    public Boolean isEmpty() {
        return numElementos == 0;
    }

    public Boolean isFull() {
        return numElementos == 2;
    }

    public Boolean isInterno() {
        return this.getTipo() == TipoNodo.INTERNO;
    }

    public Boolean isHoja() {
        return this.getTipo() == TipoNodo.HOJA;
    }

    public Integer validarRamaPerteneceNodo(ArbolRaiz archivo) throws ArbolException {
        if (this.padre == 0) return null;

        NodoInterno parent = (NodoInterno) archivo.leer(this.padre);

        if (parent.getRamaIzq() == this.posicion) return 0;
        else if (parent.getRamaCen() == this.posicion) return 1;
        else if (parent.getRamaDer() == this.posicion) return 2;
        else return 0;
    }

    public void cambiarClave(Clave claveAnterior, Clave nuevaClave, ArbolRaiz archivo) throws ArbolException {

        if (this.padre != 0) {

            NodoInterno parent = (NodoInterno) archivo.leer(this.padre);

            if (parent.getNumElementos() == 1) {

                if (this.isIgual((Clave) parent.getClave(0), claveAnterior)) parent.setClave(0, nuevaClave);

                archivo.Modificar(parent.getPosicion(), parent);

            } else if (parent.getNumElementos() == 2) {

                if (this.isIgual((Clave) parent.getClave(0), claveAnterior)) {
                    parent.setClave(0, nuevaClave);
                    archivo.Modificar(parent.getPosicion(), parent);
                } else if (this.isIgual((Clave) parent.getClave(1), claveAnterior)) {
                    parent.setClave(1, nuevaClave);
                    archivo.Modificar(parent.getPosicion(), parent);
                }
            }

            parent.cambiarClave(claveAnterior, nuevaClave, archivo);
        }
    }
}
