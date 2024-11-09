import java.io.Serializable;

public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;
    private String isbn;
    private String titulo;
    private String autor;
    private double precio;
    public Libro(String isbn, String titulo, String autor, double precio) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
    }
    public Libro(){}

    @Override
    public String toString() {
        return "Libro{" +
                "autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", precio=" + precio +
                '}';
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
