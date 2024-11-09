import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClienteRunnable implements Runnable {
    private Thread thread;
    private String nombre;
    private static int contador = 0;
    private static final Random random = new Random();

    public ClienteRunnable() {
        contador++;
        nombre = "hilo" + contador;
        thread = new Thread(this, nombre);
        thread.start();
    }

    @Override
    public void run() {
        Libro libro = null;
        try {
            Socket socket = new Socket("127.0.0.1", 2000);
            System.out.println("Conectado al servidor como " + nombre);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(output);
            ObjectInputStream objInput = new ObjectInputStream(input);
            ObjectOutputStream objOutput = new ObjectOutputStream(output);

            // Automatización de las opciones
            int[] opciones = {1, 2, 3, 4, 5}; // Opciones a ejecutar automáticamente (1: Consultar ISBN, 2: Consultar Título, 3: Consultar Autor, 4: Salir)
            String[] isbns = {"978-3-16-148410-0", "978-0-12-345678-9", "978-1-23-456789-6", "978-4-56-789012-3", "978-9-87-654321-5"};
            String[] titulos = {"La isla de la mujer dormida", "La victoria", "El clan", "La vegetariana", "Hasta que la luna caiga"};
            String[] autores = {"Arturo Perez Reverte", "Paloma Sánchez", "Carmen Mola", "Han Kang", "Sarah A. Parker"};

            for (int opcion : opciones) {
                switch (opcion) {
                    case 1:
                        String ISBN = isbns[random.nextInt(isbns.length)];
                        System.out.println(nombre + " consulta ISBN: " + ISBN);
                        dataOut.writeInt(1);
                        dataOut.flush();
                        dataOut.writeUTF(ISBN);
                        dataOut.flush();
                        libro = (Libro) objInput.readObject();
                        System.out.println(libro != null ? libro : "Libro no encontrado");
                        break;
                    case 2:
                        String titulo = titulos[random.nextInt(titulos.length)];
                        System.out.println(nombre + " consulta título: " + titulo);
                        dataOut.writeInt(2);
                        dataOut.flush();
                        dataOut.writeUTF(titulo);
                        dataOut.flush();
                        libro = (Libro) objInput.readObject();
                        System.out.println(libro != null ? libro : "Libro no encontrado");
                        break;
                    case 3:
                        String autor = autores[random.nextInt(autores.length)];
                        System.out.println(nombre + " consulta autor: " + autor);
                        dataOut.writeInt(3);
                        dataOut.flush();
                        dataOut.writeUTF(autor);
                        dataOut.flush();
                        libro = (Libro) objInput.readObject();
                        System.out.println(libro != null ? libro : "Libro no encontrado");
                        break;
                    case 4:
                        objOutput.writeObject(addLibro());
                        objOutput.flush();
                        break;
                    case 5:
                        dataOut.writeInt(5);
                        System.out.println(nombre + " se desconecta");
                        break;
                }
            }
            input.close();
            output.close();
            socket.close();
            System.out.println("Desconectado del servidor");

        } catch (IOException e) {
            System.out.println("Error en la conexión al servidor: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Clase libro no encontrada: " + e.getMessage());
        }
    }
    private Libro addLibro(){
        String isbn = "978-5-26-448410-0";
        String titulo = "Ready Player One";
        String autor = "Ernest Cline";
        double precio = 43.50;
        return new Libro(isbn, titulo, autor, precio);
    }
}
