import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.net.ServerSocket;

class Pi {
  static Object lock = new Object();
  static double pi = 0;

  static class Worker extends Thread {
    Socket conexion;
    static Object obj = new Object();

    Worker(Socket conexion) {
      this.conexion = conexion;
    }

    public void run() {
      try {
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        double x = entrada.readDouble();
        synchronized (lock) {
          pi += x;
          System.out.println("Llego el valor de " + x);
        }
        salida.close();
        entrada.close();
        conexion.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }

    }
  }

  static void servidor() throws Exception {
    ServerSocket servidor = new ServerSocket(50000);
    Worker[] w = new Worker[4];
    for (int i = 0; i < 4; i++) {
      Socket conexion = servidor.accept();
      Worker w_temp = new Worker(conexion);
      w_temp.start();
      w[i] = w_temp;
    }
    for (int i = 0; i < 4; i++)
      w[i].join();

    System.out.println(pi);
    servidor.close();
  }

  static void cliente(int num_nodo) throws Exception {
    Socket conexion = null;
    while (true) {
      try {
        conexion = new Socket("localhost", 50000);
        break;
      } catch (Exception e) {
        Thread.sleep(100);
      }
    }

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());

    double suma = 0;
    for (int i = 0; i < 10000000; i++)
      suma += 4.0 / (8 * i + 2 * (num_nodo - 2) + 3);
    suma = num_nodo % 2 == 0 ? -suma : suma;
    System.out.println("Se calculo el valor " + suma + " en el nodo " + num_nodo);
    salida.writeDouble(suma);
    salida.close();
    entrada.close();
    conexion.close();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Uso:");
      System.err.println("java PI <nodo>");
      System.exit(0);
    }
    int nodo = Integer.valueOf(args[0]);
    if (nodo == 0) {
      try {
        servidor();
      } catch (Exception e) {
        System.err.println(e);
      }
    } else {
      try {
        cliente(nodo);
      } catch (Exception e) {
        System.err.println(e);
      }
    }
  }
}