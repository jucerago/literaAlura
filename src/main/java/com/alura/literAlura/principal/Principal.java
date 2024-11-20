package com.alura.literAlura.principal;

import com.alura.literAlura.model.*;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import com.alura.literAlura.service.ConsumoApi;
import com.alura.literAlura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.stream.IIOByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> search_history=new ArrayList<>();
    @Autowired
    private LibroRepository repositorio;
    @Autowired
    private AutorRepository autorRepositorio;
    private List<Libro> libros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private Scanner teclado = new Scanner(System.in);


    public void muestraElMEnu() throws Exception {
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    Elija la opcion escribiendo el numero correspondiente:
                    1- Buscar libro por titulo
                    2- Listar libros de la base de datos
                    3- Listar autores de la base de datos
                    4- Listar autores vivos por año seleccionado
                    5- Listar libros por idioma
                    0- Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    mostrarAutoresPorAno();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;

                default:
                    System.out.println("Opcion invalida");

            }

        }

    }



    //Busqueda de Libro por nombre
    private void buscarLibro() throws Exception {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, RespuestaConsulta.class);

//        Optional<DatosLibros> libroBuscado = Optional.ofNullable(datosBusqueda.resultados().stream()
//                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
//                .toList().get(0));
//        if (libroBuscado.isPresent()){
//            System.out.println("El libro fue encontrado");
//            System.out.println(libroBuscado.get());
//            Libro libro = new Libro(libroBuscado.get());
//            search_history.add(libro);
//            repositorio.save(libro);
//        }else {
//            System.out.println("El libro no fue encontrado");
//        }

        try{
            if(datosBusqueda.count()!=0){
                DatosLibros data = datosBusqueda.resultados().stream()
                        .filter(b->b.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                        .toList().get(0);
                Libro libro = new Libro(data);

                System.out.println(libro);
                System.out.println("*".repeat(100));
                search_history.add(libro);
                List<Autor> stored_autores=new ArrayList<>();

                for(Autor autor:libro.getAutores()){
                    var _autor= autorRepositorio.find_by_name(autor.get_autor_name());
                    if(_autor!=null)stored_autores.add(_autor);
                }
                if(!stored_autores.isEmpty()){
                    Map<String,Boolean> stored_map=stored_autores.stream()
                            .collect(Collectors.toMap(Autor::get_autor_name, obj->true));
                    List<Autor> refactor_autores = new ArrayList<>();
                    for(Autor _autor:libro.getAutores()){
                        if(!stored_map.get(_autor.get_autor_name())){
                            refactor_autores.add(_autor);
                        }
                    }
                    libro.setAutores(refactor_autores);
                    libro.getAutores().forEach(a->autorRepositorio.save(a));
                    libro.add_autores(stored_autores);
                }
                else{
                    libro.getAutores().forEach(a->autorRepositorio.save(a));
                }
                var is_book_present = repositorio.is_book_present(libro.getTitulo());
                if(!is_book_present)
                    repositorio.save(libro);
                else System.out.println("El libro ya se encuentra en la base de datos");

            }
            else {
                System.out.println("*".repeat(100));
                System.out.println("Libro no encontrado");

            }
        }catch (Exception e){
            System.out.println("El libro ya se encuentra en la base de datos");

        }
    }

    //Listar libro de la base de datos
    private void mostrarLibrosBuscados() {
        libros = repositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    //Listar autores de la base de datos
    private void mostrarAutoresBuscados(){
        autores = autorRepositorio.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autor::get_autor_name))
                .forEach(System.out::println);
    }

    //Listar autores vivos por año seleccionado
    private void mostrarAutoresPorAno(){
        System.out.println("Ingrese el año deseado");
        var fechaFiltro = teclado.nextLine();

        try{
            List<Autor> autores = autorRepositorio.find_by_date(Integer.valueOf(fechaFiltro));
            if(!autores.isEmpty())
                   autores.forEach(System.out::println);
               else{
                   System.out.println("*".repeat(100));
                   System.out.println("No hay autores vivos en el año "+fechaFiltro);
               }
        }catch (NumberFormatException e){
               System.out.println("Año invalido");

        }
    }

    //Listar libros por idioma
    private void mostrarLibrosPorIdioma(){
        System.out.println("*".repeat(100));
        System.out.println("Lista de libros por Idiomas");
        List<Idioma> idiomas = repositorio.find_idiomas();
        for(int i=0;i<idiomas.size();i++){
            System.out.println((i+1)+". "+idiomas.get(i));
        }
        if(idiomas.isEmpty()) System.out.println("No hay idiomas en la base de datos");
        System.out.println("*".repeat(100));
        System.out.print("Selecciona un Idioma: ");
        var seleccionarIdioma = teclado.nextLine();
        try {
            var cantLibros = repositorio.find_count_by_idioma(idiomas.get(Integer.valueOf(seleccionarIdioma)-1));
            System.out.println("Hay "+cantLibros+" libros en "+
                    idiomas.get(Integer.valueOf(seleccionarIdioma)-1)+" guardados.");
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            System.out.println("Seleccion invalida.");
        }
    }


}
