package com.scb.raar.arbol.operaciones;

import com.scb.raar.arbol.estructura.Elemento;
import com.scb.raar.arbol.estructura.Nodo;
import com.scb.raar.arbol.estructura.NodoHoja;
import com.scb.raar.arbol.estructura.NodoInterno;
import com.scb.raar.arbol.exception.ArbolException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raruiz
 */
public class ArbolOperaciones<Clave extends Comparable<Clave>> {
    private int raiz;
    private final ArbolRaiz<Nodo> archivo;

    public ArbolOperaciones(String path, String ruta) {
        this.archivo = new ArbolRaiz(path, ruta, 750);
        try {
            this.raiz = this.archivo.getRaiz();
        } catch (Exception ex) {
            this.raiz = 0;
        }
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

    public Boolean isMayorIgual(Clave elem1, Clave elem2) {
        int comparacion = elem1.compareTo(elem2);
        return comparacion >= 0;
    }

    public Boolean isMenor(Clave elem1, Clave elem2) {
        int comparacion = elem1.compareTo(elem2);
        return comparacion < 0;
    }

    public void agregar(Clave clave, int posicion) throws ArbolException {
        if (raiz == 0) {
            Elemento nuevo = new Elemento(clave, posicion);
            NodoHoja nuevaRaiz = new NodoHoja<Clave>(nuevo);
            nuevaRaiz.setPosicion(1);
            this.archivo.setRaiz(1);
            this.archivo.escribir(nuevaRaiz);
            this.raiz = 1;
        } else {
            Nodo buscador = (Nodo) this.archivo.leer(this.raiz);
            NodoInterno nodoInt;
            while (buscador.isInterno()) {
                nodoInt = (NodoInterno) buscador;
                if (nodoInt.getNumElementos() == 1) {
                    if (isMenorIgual(clave, (Clave) nodoInt.getClave(0))) {
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    } else if (isMayor(clave, (Clave) nodoInt.getClave(0))) {
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }
                } else {
                    if (isMenorIgual(clave, (Clave) nodoInt.getClave(0))) {
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    } else if (isMayor(clave, (Clave) nodoInt.getClave(1))) {
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaDer());
                    } else {
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }
                }
            }
            NodoHoja nodoHoja = (NodoHoja) buscador;
            int verificar = nodoHoja.addElemento(clave, posicion, this.archivo);
            if (verificar != 0) {
                this.raiz = verificar;
            }
        }
    }

    public Integer buscar(Clave clave) throws ArbolException {
        if (this.raiz == 0) {
            return null;

        }
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz);
        NodoHoja nodoHoja;

        if (buscador.isInterno()) {
            buscador = isInterno(buscador, clave);
        }

        nodoHoja = (NodoHoja) buscador;

        if (isIgual((Clave) nodoHoja.getElementos()[0].getClave(), clave)) {
            return nodoHoja.getElementos()[0].getPosicion();
        } else if (nodoHoja.getElementos()[1] != null && isIgual((Clave) nodoHoja.getElementos()[1].getClave(), clave)) {
            return nodoHoja.getElementos()[1].getPosicion();
        } else {
            return null;
        }
    }

    public Boolean eliminar(Clave clave) throws ArbolException, IOException {

        Nodo buscador = (Nodo) this.archivo.leer(this.raiz);

        NodoHoja nodoHoja;

        if (buscador.isHoja()) {
            nodoHoja = (NodoHoja) buscador;
            if (nodoHoja.getNumElementos() == 2) {
                if (isIgual(clave, (Clave) nodoHoja.getElementos()[0].getClave())) {
                    nodoHoja.setElemento(0, nodoHoja.getElementos()[1]);
                    nodoHoja.setElemento(1, null);

                } else {
                    nodoHoja.setElemento(1, null);
                }
                nodoHoja.setNumElementos(1);
                archivo.Modificar(nodoHoja.getPosicion(), nodoHoja);
                return true;
            } else if (nodoHoja.getNumElementos() == 1 && isIgual((Clave) nodoHoja.getElementos()[0].getClave(), clave)) {
                this.raiz = 0;
                archivo.borrar_archivo();
                return true;
            }
        }

        else if (buscador.isInterno()) {
            buscador = isInterno(buscador, clave);
        }

        nodoHoja = (NodoHoja) buscador;

        nodoHoja.eliminar(clave, this.archivo);


        this.raiz = archivo.getRaiz();
        return true;
    }

    public List<Elemento> listar() throws ArbolException {
        if (this.raiz == 0) {
            return null;
        }
        List<Elemento> lista = new LinkedList<>();
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz);
        while (buscador.isInterno()) {
            NodoInterno buscadorInt = (NodoInterno) buscador;
            buscador = (Nodo) archivo.leer(buscadorInt.getRamaIzq());
        }
        NodoHoja hoja = (NodoHoja) buscador;
        while (hoja.getNext() != 0) {
            if (hoja.getElementos()[0] != null) {
                lista.add(hoja.getElementos()[0]);
            }
            if (hoja.getElementos()[1] != null) {
                lista.add(hoja.getElementos()[1]);
            }
            hoja = (NodoHoja) archivo.leer(hoja.getNext());
        }
        if (hoja.getElementos()[0] != null) {
            lista.add(hoja.getElementos()[0]);
        }
        if (hoja.getElementos()[1] != null) {
            lista.add(hoja.getElementos()[1]);
        }
        return lista;
    }

    private Nodo isInterno(Nodo buscador, Clave clave) throws ArbolException {

        NodoInterno nodoInt;

        while (buscador.isInterno()) {

            nodoInt = (NodoInterno) buscador;

            if (nodoInt.getNumElementos() == 1) {
                if (isMenor(clave, (Clave) nodoInt.getClave(0))) {
                    buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                } else if (isMayorIgual(clave, (Clave) nodoInt.getClave(0))) {
                    buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                }
            }

            else {
                if (isMenor(clave, (Clave) nodoInt.getClave(0))) {
                    buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                } else if (isMayorIgual(clave, (Clave) nodoInt.getClave(1))) {
                    buscador = (Nodo) this.archivo.leer(nodoInt.getRamaDer());
                } else {
                    buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                }
            }
        }
        return buscador;
    }

}
