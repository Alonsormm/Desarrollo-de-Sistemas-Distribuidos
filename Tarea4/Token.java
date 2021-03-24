import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Token
{
  static DataInputStream entrada;
  static DataOutputStream salida;
  static boolean primera_vez = true;
  static String ip;
  static int nodo;
  static int token;
  static int contador = 0;

  static class Worker extends Thread
  {
    public void run()
    {
      try {
        ServerSocket servidor = new ServerSocket(50000);
        Socket conexion = servidor.accept();
        entrada = new DataInputStream(conexion.getInputStream());
      } catch (Exception e) {
        System.out.println("Fallo");
        System.out.println(e);
      }
    }
  }

  public static void main(String[] args) throws Exception
  {
    if (args.length != 2)
    {
      System.err.println("Se debe pasar como parametros el numero del nodo y la IP del siguiente nodo");
      System.exit(1);
    }

    nodo = Integer.valueOf(args[0]);  // el primer parametro es el numero de nodo
    ip = args[1];  // el segundo parametro es la IP del siguiente nodo en el anillo

    Worker w = new Worker();
    w.start();
    Socket conexion = null;
    while (true) {
      try {
        conexion = new Socket(ip, 50000);
        break;
      } catch (Exception e) {
        Thread.sleep(100);
      }
    }
    salida = new DataOutputStream(conexion.getOutputStream());
    w.join();
    while(true) {
      if(nodo == 0 && contador == 1000){
        break;
      }
      else if (nodo == 0){
        if(primera_vez){
          primera_vez = false;
          token = 1;
        }
        else{
          token = entrada.readInt();
          contador += 1;
          System.out.println("Nodo: " + nodo + " Contador: " + contador + " Token: " + token);
        }
      }
      else {
        token = entrada.readInt();
        contador += 1;
        System.out.println("Nodo: " + nodo + " Contador: " + contador + " Token: " + token);
      }
    }
  }
}
