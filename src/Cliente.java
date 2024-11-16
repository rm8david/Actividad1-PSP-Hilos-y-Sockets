import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Libro libro = null;
        int opcion = 0;
        Scanner sc = new Scanner(System.in);

        try {
            Socket socket = new Socket("127.0.0.1", 2000);
            System.out.println("Conectado al servidor");
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objOutput = new ObjectOutputStream(output);
            ObjectInputStream objInput = new ObjectInputStream(input);
            DataOutputStream dataOut = new DataOutputStream(output);

            do {
                try {
                    System.out.println("Menú de la librería");
                    System.out.println("1. Consultar libro por ISBN");
                    System.out.println("2. Consultar libro por título");
                    System.out.println("3. Buscar por autor");
                    System.out.println("4. Añadir un nuevo libro");
                    System.out.println("5. Salir");
                    System.out.print("Por favor, elija una opción: ");

                    if (sc.hasNextInt()) {
                        opcion = sc.nextInt();
                        sc.nextLine(); // Consumir el salto de línea restante
                    } else {
                        System.out.println("Por favor, ingrese un número válido.");
                        sc.nextLine(); // Consumir la entrada inválida
                        continue; // Repetir el bucle para solicitar la opción nuevamente
                    }

                    switch (opcion) {
                        case 1:
                            System.out.println("Por favor indique el ISBN:");
                            String ISBN = sc.nextLine();
                            dataOut.writeInt(1);
                            dataOut.flush();
                            dataOut.writeUTF(ISBN);
                            dataOut.flush();
                            libro = (Libro) objInput.readObject();
                            System.out.println(libro);
                            break;
                        case 2:
                            System.out.println("Por favor indique el título o parte de el mismo:");
                            String titulo = sc.nextLine();
                            dataOut.writeInt(2);
                            dataOut.flush();
                            dataOut.writeUTF(titulo);
                            dataOut.flush();
                            libro = (Libro) objInput.readObject();
                            System.out.println(libro);
                            break;
                        case 3:
                            System.out.println("Por favor indique el autor o parte de el mismo:");
                            String autor = sc.nextLine();
                            dataOut.writeInt(3);
                            dataOut.flush();
                            dataOut.writeUTF(autor);
                            dataOut.flush();
                            libro = (Libro) objInput.readObject();
                            System.out.println(libro);
                            break;
                        case 4:
                            dataOut.writeInt(4);
                            dataOut.flush();
                            libro = datosLibroNuevo();
                            objOutput.writeObject(libro);
                            objOutput.flush();
                            break;
                        case 5:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            System.out.println("Por favor, ingrese una opción válida.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida, por favor ingrese un número.");
                    sc.nextLine(); // Consumir la entrada inválida
                }
            } while (opcion != 5);

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
    private static Libro datosLibroNuevo(){

        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ISBN del libro: ");
        String ISBN = sc.nextLine();
        System.out.println("Ingrese el titulo del libro: ");
        String titulo = sc.nextLine();
        System.out.println("Ingrese el autor: ");
        String autor = sc.nextLine();
        System.out.println("Ingrese el precio del libro: ");
        double precio = 0;
        while (true) {
            try {
                String precioEntrada = sc.nextLine();
                precio = Double.parseDouble(precioEntrada); // Intentar convertir el precio ingresado a un double
                break; // Salir del bucle si la conversión fue exitosa
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un precio válido (use punto como separador decimal).");
            }
        }
        return new Libro(ISBN, titulo, autor, precio);
    }
}
