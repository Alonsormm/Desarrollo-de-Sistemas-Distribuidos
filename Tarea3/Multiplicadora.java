import java.net.Socket;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataInputStream;
import java.lang.Thread;
import java.net.ServerSocket;

class Multiplicadora {
  static Object lock = new Object();
  static int n = 1000;
  static Matriz[] matrices = new Matriz[4];
  static int indice = 0;

  static class Worker extends Thread {
    Socket conexion;
    Matriz segmA;
    Matriz segmB;
    static Object obj = new Object();

    Worker(Socket conexion, Matriz segmA, Matriz segmB) {
      this.segmA = segmA;
      this.segmB = segmB;
      this.conexion = conexion;
    }

    public void run() {
      try {
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        ObjectOutputStream salidaObjetos = new ObjectOutputStream(salida);
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        ObjectInputStream entradaObjecto = new ObjectInputStream(entrada);
      
        salidaObjetos.writeObject(segmA);
        salidaObjetos.writeObject(segmB);
        Matriz resultado = (Matriz)entradaObjecto.readObject();
        synchronized (lock) {
          System.out.println("Llego el valor una matriz");
          matrices[indice] = resultado;
          indice+=1;
        }
        salida.close();
        salidaObjetos.close();
        entradaObjecto.close();
        entrada.close();
        conexion.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }

    }
  }

  static void servidor() throws Exception {
    ServerSocket servidor = new ServerSocket(50000);
    System.out.println("Se inicio el servidor");
    int filas = n;
    int columnas = filas;
    Matriz m1 = new Matriz(filas,columnas);
    Matriz m2 = new Matriz(filas,columnas);
    Matriz.inicializar(m1, m2, filas);
    m2.transponer();
    Matriz segA1 = m1.segmentar(0,columnas, 0, filas/2);
    Matriz segA2 = m1.segmentar(0,columnas, filas/2, filas);
    Matriz segB1 = m2.segmentar(0,columnas, 0, filas/2);
    Matriz segB2 = m2.segmentar(0,columnas, filas/2, filas);

    Worker[] w = new Worker[4];
    for (int i = 0; i < 4; i++) {
      Socket conexion = servidor.accept();
      Worker w_temp;
      if(i == 0)
        w_temp = new Worker(conexion,segA1, segB1);
      else if(i == 1)
        w_temp = new Worker(conexion,segA1, segB2);
      else if(i == 2)
        w_temp = new Worker(conexion,segA2, segB1);
      else
        w_temp = new Worker(conexion,segA2, segB2);
      w_temp.start();
      w[i] = w_temp;
    }
    for (int i = 0; i < 4; i++)
      w[i].join();

    Matriz resultado = Matriz.unirMatrices(matrices[0],matrices[1], matrices[2], matrices[3]);
    resultado.imprimir();
    //System.out.println(pi);
    servidor.close();
  }

  static void cliente(int num_nodo) throws Exception {
    Socket conexion = null;
    System.out.println("Se inicio el client " + num_nodo);
    while (true) {
      try {
        conexion = new Socket("localhost", 50000);
        System.out.println("Se conecto el client " + num_nodo);
        break;
      } catch (Exception e) {
        Thread.sleep(100);
      }
    }

    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
    ObjectOutputStream salidaObjetos = new ObjectOutputStream(salida);
    DataInputStream entrada = new DataInputStream(conexion.getInputStream());
    ObjectInputStream entradaObjecto = new ObjectInputStream(entrada);

    Matriz seg1 = (Matriz) entradaObjecto.readObject();
    Matriz seg2 = (Matriz) entradaObjecto.readObject();
    // System.out.println("Se calculo el valor " + suma + " en el nodo " + num_nodo);
    salidaObjetos.writeObject(seg1.multiplicarConTranspuesta(seg2));
    salida.close();
    salidaObjetos.close();
    entradaObjecto.close();
    entrada.close();
    conexion.close();
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Uso:");
      System.err.println("java Multiplicadora <nodo>");
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