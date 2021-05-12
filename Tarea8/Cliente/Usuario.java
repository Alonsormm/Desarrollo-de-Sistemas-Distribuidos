import java.net.URLDecoder;
import java.util.Scanner;

import com.google.gson.*;

public class Usuario {
  int id_usuario;
  String email;
  String nombre;
  String apellido_paterno;
  String apellido_materno;
  String fecha_nacimiento;
  String telefono;
  String genero;
  byte[] foto;

  // @FormParam necesita un metodo que convierta una String al objeto de tipo
  // Usuario
  public static Usuario valueOf(String s) throws Exception {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64()).create();
    return (Usuario) j.fromJson(URLDecoder.decode(s, "UTF-8"), Usuario.class);
  }

  public String toJson() throws Exception {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64()).create();
    return j.toJson(this, Usuario.class);
  }

  public static Usuario leerDesdeTeclado(){
    Scanner input = new Scanner( System.in );
    System.out.println("Escriba los siguientes campos que se le solicitan");
    Usuario u = new Usuario();
    while(u.email == "" || u.email == null){
      System.out.print("(Obligatorio) Email: ");
      u.email = input.nextLine();
    }
    while(u.nombre == "" || u.nombre == null){
      System.out.print("(Obligatorio) Nombre: ");
      u.nombre = input.nextLine();
    }
    while(u.apellido_paterno == "" || u.apellido_paterno == null){
      System.out.print("(Obligatorio) Apellido paterno: ");
      u.apellido_paterno = input.nextLine();
    }
    while(u.apellido_materno == "" || u.apellido_materno == null){
      System.out.print("(Obligatorio) Apellido materno: ");
      u.apellido_materno = input.nextLine();
    }

    while(true){
      System.out.print("(Obligatorio) Fecha de nacimiento(aaaa-mm-dia): ");
      u.fecha_nacimiento = input.nextLine();
      String[] dia_mes_anio = u.fecha_nacimiento.split("-");
      System.out.println(u.fecha_nacimiento);
      if(dia_mes_anio.length == 3){
        int dia = Integer.parseInt(dia_mes_anio[2]);
        int mes = Integer.parseInt(dia_mes_anio[1]);
        int anio = Integer.parseInt(dia_mes_anio[0]);
        if(dia > 0 && dia < 32 && mes > 0 && mes < 13 && anio > 1900 && anio < 2021) break;
      }
    }
    

    System.out.print("(Opcional) Telefono: ");
    u.telefono = input.nextLine();

    System.out.print("(Opcional) Genero(M/F): ");
      u.genero = input.nextLine();

    if (u.genero == "") u.genero = "M";

    return u;
  }

  public void imprimir() {
    if (nombre != null && nombre != "")
      System.out.println("Nombre: " + nombre);
    if (apellido_paterno != null && apellido_paterno != "")
      System.out.println("Apellido paterno: " + apellido_paterno);
    if (apellido_materno != null && apellido_materno != "")
      System.out.println("Apellido materno: " + apellido_materno);
    if (fecha_nacimiento != null && fecha_nacimiento != "")
      System.out.println("Fecha de nacimiento: " + fecha_nacimiento);
    if (telefono != null && telefono != "")
      System.out.println("Telefono: " + telefono);
    if (genero != null && genero != "")
      System.out.println("Genero: " + genero);
  }
}
