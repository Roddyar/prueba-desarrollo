package com.scb.raar.arbol.archivos;

import com.scb.raar.arbol.estructura.Elemento;
import com.scb.raar.arbol.exception.ArbolException;
import com.scb.raar.arbol.operaciones.ArbolOperaciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <Clave>
 * @param <Clase>
 * @author raruiz
 */
public class OperacionesArchivos<Clave extends Comparable<Clave>, Clase> {
    private final ArbolOperaciones<Clave> arbol;
    private final Repositorio<Clase> archivo;

    public OperacionesArchivos(String path, String nombreArchivo, int separacion) throws ArbolException {
        this.arbol = new ArbolOperaciones(path, nombreArchivo + ".txt");
        this.archivo = new Repositorio(path, nombreArchivo + ".dat", separacion);
    }

    public int agregar(Clave clave, Clase objeto) throws ArbolException {
        Integer existencia = this.arbol.buscar(clave);
        if (existencia == null) {
            int pos = archivo.escribir(objeto);
            arbol.agregar(clave, pos);
            return pos;
        } else {
            return 0;
        }
    }

    public Boolean modificar(Clave clave, Clave claveNueva, Clase objeto) throws ArbolException, IOException {
        if (eliminar(clave)) {
            agregar(claveNueva, objeto);
            return true;
        } else
            return false;
    }

    public Boolean exists(Clave clave) throws ArbolException {
        Integer existencia = this.arbol.buscar(clave);
        return existencia != null;
    }

    public Clase get(Clave clave) throws ArbolException {
        Integer existencia = this.arbol.buscar(clave);
        if (existencia != null) {
            return archivo.leer(existencia);
        } else {
            return null;
        }
    }

    public List<Clase> listar() throws ArbolException {
        List<Elemento> listaPosicion = this.arbol.listar();
        if (listaPosicion == null) {
            return null;
        }
        List<Clase> listaClase = new ArrayList<>();
        for (Elemento elemento : listaPosicion) {
            listaClase.add(archivo.leer(elemento.getPosicion()));
        }
        return listaClase;
    }

    public Boolean eliminar(Clave clave) throws ArbolException, IOException {
        Integer existencia = this.arbol.buscar(clave);
        if (existencia != null) {
            arbol.eliminar(clave);
            archivo.eliminar(existencia);
            return true;
        } else {
            return false;
        }
    }
}
