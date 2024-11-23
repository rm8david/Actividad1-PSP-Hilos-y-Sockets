import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    static List<Libro> libros = new ArrayList<>();

    public static void main(String[] args) {
        libros.add(new Libro("978-3-16-148410-0","La isla de la mujer dormida","Arturo Perez Reverte",21.75));
        libros.add(new Libro("978-0-12-345678-9","La victoria","Paloma Sánchez",23.75));
        libros.add(new Libro("978-1-23-456789-6","El clan","Carmen Mola",20.80));
        libros.add(new Libro("978-4-56-789012-3","La vegetariana","Han Kang",19.85));
        libros.add(new Libro("978-9-87-654321-5","Hasta que la luna caiga","Sarah A. Parker",23.65));

        try (ServerSocket serverSocket = new ServerSocket(2000)) {
            System.out.println("Servidor iniciado, esperando clientes...");
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());
                // Crear un hilo para cada cliente
                new Thread(new ClientHandler(clienteSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error en el socket servidor: " + e.getLocalizedMessage());
        }
    }


    static class ClientHandler implements Runnable {
        private Socket clienteSocket;

        public ClientHandler(Socket socket) {
            this.clienteSocket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream dataIn = new DataInputStream(clienteSocket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(clienteSocket.getOutputStream());
                 ObjectOutputStream objectOut = new ObjectOutputStream(clienteSocket.getOutputStream());
                 ObjectInputStream objectIn = new ObjectInputStream(clienteSocket.getInputStream())
            )
            {
                int opcion = 0;
                do {
                    opcion = dataIn.readInt();
                    switch (opcion) {
                        case 1:
                            String ISBN = dataIn.readUTF();
                            Libro encontrado = buscarISBN(ISBN);
                            objectOut.writeObject(encontrado);
                            objectOut.flush();
                            if (encontrado != null) {
                                System.out.println("El cliente ha buscado el libro con el ISBN: " + encontrado.getIsbn());
                            } else {
                                System.out.println("El libro con el ISBN " + ISBN + " no fue encontrado.");
                            }
                            break;
                        case 2:
                            String titulo = dataIn.readUTF();
                            Libro encontrado2 = buscarTitulo(titulo);
                            objectOut.writeObject(encontrado2);
                            objectOut.flush();
                            System.out.println("El cliente ha buscado el título: " + titulo);
                            break;
                        case 3:
                            String autor = dataIn.readUTF();
                            Libro encontrado3 = buscarAutor(autor);
                            objectOut.writeObject(encontrado3);
                            objectOut.flush();
                            System.out.println("El cliente ha buscado el autor: " + autor);
                            break;
                        case 4:
                            Libro addLibro = (Libro)objectIn.readObject();
                            libros.add(addLibro);
                            System.out.println("Se ha añadido el libro: "+addLibro.toString());
                    }
                } while (opcion != 5);
                System.out.println("Cliente desconectado");
            } catch (IOException | ClassNotFoundException e) {
                if(e.getMessage()!= null){
                    System.out.println("Error al manejar el cliente: " + e.getMessage());
                }else{
                    System.out.println("Cliente desconectado ");
                }
            }
        }

        private Libro buscarAutor(String libroAutor) {
            for (Libro l : libros) {
                if (l.getAutor().toLowerCase().contains(libroAutor.toLowerCase())) {
                    return l;
                }
            }
            return null;
        }

        private static synchronized void añadirLibro(Libro libro) {
            libros.add(libro);
        }

        private static Libro buscarTitulo(String titulo) {
            for (Libro l : libros) {
                if (l.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                    return l;
                }
            }
            return null;
        }

        private static Libro buscarISBN(String isbn) {
            for (Libro l : libros) {
                if (l.getIsbn().equals(isbn)) {
                    return l;
                }
            }
            return null;
        }
    }
}