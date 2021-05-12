import java.util.Scanner;

public class Menu {

  static Cliente c;

  static void borrarUsuarioMenu() {
    System.out.println("Escriba el id del usuario a borrar: ");
    Scanner input = new Scanner(System.in);
    int id = input.nextInt();
    try {
      c.borraUsuario(id);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  static void consultarUsuarioMenu() {
    System.out.println("Escriba el id del usuario a consultar: ");
    Scanner input = new Scanner(System.in);
    int id = input.nextInt();
    try {
      Usuario u = c.consultarUsuario(id);
      if (u.id_usuario == 0)
        return;
      u.imprimir();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  static void altaUsuarioMenu() {
    Usuario u = Usuario.leerDesdeTeclado();
    try {
      int id = c.altaUsuario(u);
      if (id == 0)
        return;
      System.out.println("El id del usuario registrado: " + id);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  static void menu() {
    String opcion = "";
    while (opcion.compareTo("d") != 0) {
      System.out.print("MENU\n \n a. Alta usuario\n b. Consulta usuario\n c. Borra usuario\n d. Salir\n \n Opción: ");
      opcion = System.console().readLine();
      if (opcion.compareTo("a") == 0) {
        altaUsuarioMenu();
      }
      if (opcion.compareTo("b") == 0) {
        consultarUsuarioMenu();
      }
      if (opcion.compareTo("c") == 0) {
        borrarUsuarioMenu();
      }
    }
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Se debe pasar como parametro la ip del servidor del servicio");
      System.exit(1);
    }
    String url_servicio = args[0];
    c = new Cliente(url_servicio);
    menu();
  }
}
