package com.scb.raar.arbol.archivos;

import com.scb.raar.arbol.exception.ArbolException;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @author raruiz
 */
public class Repositorio<Objeto> {

    private final File archivo;
    private final int separacion;

    public Repositorio(String path, String ruta, int separacion) {
        this.archivo = new File(path, ruta);
        this.separacion = separacion;
    }

    public int escribir(Objeto objeto) throws ArbolException {
        ObjectOutputStream ous = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ous = new ObjectOutputStream(bos);
            ous.writeObject(objeto);
            ous.close();

            byte[] cadenaByte = bos.toByteArray();

            if (cadenaByte.length >= this.separacion)
                throw new ArbolException("Error de escritura: Se ha ingresado demasiada información");

            byte[] tamCadena = ByteBuffer.allocate(4).putInt(cadenaByte.length).array();
            long posicionEscritura;

            try (RandomAccessFile tmp = new RandomAccessFile(archivo, "rw")) {
                long tamArchivo = tmp.length();
                posicionEscritura = (long) Math.ceil(tamArchivo / (float) this.separacion) * this.separacion;
                tmp.seek(posicionEscritura);
                tmp.write(tamCadena);
                tmp.write(cadenaByte);
            }
            return (int) (posicionEscritura / this.separacion);

        } catch (IOException ex) {
            throw new ArbolException("Error al leer el archivo");
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException ex) {
                throw new ArbolException("Error al cerrar el archivo");
            }
        }
    }

    public Objeto leer(int pos) throws ArbolException {
        try (RandomAccessFile tmp = new RandomAccessFile(archivo, "r")) {
            int tam = validarTamanio(tmp, pos);
            if (pos * this.separacion >= tmp.length()) {
                throw new ArbolException("Error de Indice");
            }
            byte[] cadena = new byte[tam];
            tmp.readFully(cadena);
            ByteArrayInputStream bis = new ByteArrayInputStream(cadena);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Objeto) ois.readObject();
        } catch (FileNotFoundException ex) {
            throw new ArbolException("No se ha encontrado el archivo");
        } catch (IOException ex) {
            throw new ArbolException("Error al leer el archivo");
        } catch (ClassNotFoundException ex) {
            throw new ArbolException("Error al leer la información " + ex.toString());
        }

    }

    public void eliminar(int pos) throws ArbolException {
        try (RandomAccessFile tmp = new RandomAccessFile(this.archivo, "rwd")) {
            long inicio = pos * this.separacion;
            if (inicio >= tmp.length()) {
                throw new ArbolException("Error de Indice");
            }
            int tam = validarTamanio(tmp, pos);
            tmp.seek(inicio);
            for (long j = 0; j < 350; j++) {
                tmp.write((byte) 0);
            }

        } catch (FileNotFoundException ex) {
            throw new ArbolException("El archivo no ha sido encontrado");
        } catch (IOException ex) {
            throw new ArbolException("Error al leer el archivo");
        }

    }

    public void Modificar(int pos, Objeto clase) throws ArbolException {
        ObjectOutputStream ous = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ous = new ObjectOutputStream(bos);
            ous.writeObject(clase);
            ous.close();
            byte[] cadenaByte = bos.toByteArray();
            if (cadenaByte.length >= this.separacion) {
                throw new ArbolException("Error de modificación: Se ha ingresado demasiada información");
            }
            byte[] tamCadena = ByteBuffer.allocate(4).putInt(cadenaByte.length).array();
            try (RandomAccessFile tmp = new RandomAccessFile(archivo, "rwd")) {
                long inicio = pos * this.separacion;
                if (inicio >= tmp.length()) {
                    throw new ArbolException("Se ha ingresado un indice erróneo");
                }
                tmp.seek(pos * this.separacion);
                tmp.write(tamCadena);
                tmp.write(cadenaByte);
            }
        } catch (IOException ex) {
            throw new ArbolException("Error al escribir la información");
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException ex) {
                throw new ArbolException("Error al cerrar el archivo");
            }
        }
    }

    public File obtener_archivo() {
        return archivo;
    }

    public void borrar_archivo() throws IOException {
        RandomAccessFile tmp = new RandomAccessFile(archivo, "rwd");
        tmp.getChannel().truncate(0);
    }

    public int validarTamanio(RandomAccessFile tmp, int pos) throws ArbolException {
        try {
            tmp.seek(pos * this.separacion);
            byte[] byteTam = new byte[4];
            tmp.read(byteTam);
            int tam = (byteTam[0] << 24) & 0xff000000 |
                    (byteTam[1] << 16) & 0x00ff0000 |
                    (byteTam[2] << 8) & 0x0000ff00 |
                    (byteTam[3]) & 0x000000ff;
            if (tam == 0) {
                throw new ArbolException("No existe un elemento en la posicion solicitada");
            }
            return tam;
        } catch (IOException ex) {
            throw new ArbolException("Error al leer el archivo");
        }
    }

}
