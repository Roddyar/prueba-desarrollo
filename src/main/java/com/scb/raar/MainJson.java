package com.scb.raar;

import com.scb.raar.arbol.archivos.OperacionesArchivos;
import com.scb.raar.arbol.entidades.Persona;
import com.scb.raar.arbol.exception.ArbolException;
import com.scb.raar.json.LeerJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author raruiz
 */
public class MainJson {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        LeerJson leerJson = new LeerJson("./src/db/read.json");
        leerJson.readJson();
    }

}
