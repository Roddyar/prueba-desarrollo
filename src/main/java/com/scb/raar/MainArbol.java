package com.scb.raar;

import com.scb.raar.arbol.archivos.OperacionesArchivos;
import com.scb.raar.arbol.entidades.Persona;
import com.scb.raar.arbol.exception.ArbolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author raruiz
 */
public class MainArbol {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int validar = 0, opcion;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            OperacionesArchivos<String, Persona> archivos = new OperacionesArchivos("src/db/", "personas", 1000);
            while (validar == 0) {
                printMenu();
                String identificacion, nombres, apellidos;
                opcion = INT(br.readLine());
                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el número de cédula de la persona: ");
                        identificacion = br.readLine();
                        System.out.println("Ingrese los nombres");
                        nombres = br.readLine();
                        System.out.println("Ingrese los apellidos");
                        apellidos = br.readLine();
                        int val = archivos.agregar(identificacion, new Persona(identificacion, nombres, apellidos));
                        System.out.println("Persona agregada en la posición: " + val);
                        break;
                    case 2:
                        System.out.println("Ingrese el número de cédula de la persona a modificar ");
                        String cedulaModificar = br.readLine();
                        if (archivos.exists(cedulaModificar)) {
                            System.out.println("Ingrese el nuevo número de cédula de la persona");
                            identificacion = br.readLine();
                            System.out.println("Ingrese los nuevos nombres");
                            nombres = br.readLine();
                            System.out.println("Ingrese los nuevos apellidos");
                            apellidos = br.readLine();
                            if (archivos.modificar(cedulaModificar, identificacion, new Persona(identificacion, nombres, apellidos)))
                                System.out.println("Persona modificada!");
                            else
                                System.out.println("No se pudo modificar la persona, favor comunicarse con el departamento de soporte");
                        } else {
                            System.out.println("No existe persona con la cédula: " + cedulaModificar);
                        }
                        break;
                    case 3:
                        System.out.println("Ingrese el número de cédula de la persona a consultar");
                        identificacion = br.readLine();
                        if (archivos.exists(identificacion)) {
                            Persona leerPersona = archivos.get(identificacion);
                            System.out.println("Persona: " + leerPersona.toString());
                        } else {
                            System.out.println("No existe persona con la cédula: " + identificacion);
                        }
                        break;
                    case 4:
                        List<Persona> lista = archivos.listar();
                        System.out.println("\nLista");
                        if (lista == null || lista.isEmpty())
                            System.out.println("No hay personas guardadas");
                        else
                            lista.forEach((persona) -> System.out.println(persona.toString()));
                        break;
                    case 5:
                        System.out.println("Ingrese el número de cédula de la persona a eliminar");
                        identificacion = br.readLine();
                        if (archivos.eliminar(identificacion))
                            System.out.println("Persona eliminada!");
                        else
                            System.out.println("No existe persona con la cédula: " + identificacion);
                        break;
                    case 6:
                        validar = 1;
                        break;
                    default:
                        System.out.println("Sólo son permitidas las opciones del menú");
                }

            }

        } catch (ArbolException ex) {
            System.out.println("Ha ocurrido  un error: " + ex.getMessage());
        }
    }

    public static int INT(String numero) {
        if (isNumeric(numero))
            return Integer.parseInt(numero);
        else
            return 10;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void printMenu() {
        System.out.println("\nPRUEBA RODDY ARANA RUIZ");
        System.out.println("USO ARBOL B+ CON ARCHIVOS DE TEXTO EN DISCO");
        System.out.println("1.- Registrar una persona");
        System.out.println("2.- Modificar una persona");
        System.out.println("3.- Consultar persona por cédula");
        System.out.println("4.- Listar todas las personas");
        System.out.println("5.- Eliminar una persona");
        System.out.println("6.- Salir");
    }

}
