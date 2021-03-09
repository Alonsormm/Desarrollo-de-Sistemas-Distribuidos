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

  static class Worker extends Thread {
    Socket conexion;
    Matriz segmA;
    Matriz segmB;
    static Object obj = new Object();

    Worker(Socket conexion, Matriz segmA, Matriz segmB) {
      // Se reciven los segmentos para ser multiplicados por cada nodo
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

        //El servidor envia los segmentos correspondientes al nodo para que se haga la multiplicacion
        salidaObjetos.writeObject(segmA);
        salidaObjetos.writeObject(segmB);
        //Se espera a que el nodo haga la multiplicacion de sus segmentos para leer la matriz resultante
        Matriz resultado = (Matriz)entradaObjecto.readObject();
        synchronized (lock) {
          System.out.println("Llego el valor una matriz del nodo " + (resultado.indice-1));
          if(matrices[resultado.indice-1] != null) {
            throw new Exception("No se puede recivir dos calculos de un mismo nodo");
          }
          //Una vez calculado se guarda en un arreglo de matrices la cual sera unida para crear el resultado final
          //Se guarda en el indice del nodo que ejecuto la operacion para mantener un correcto orden y que no importa que nodo acabe primero
          matrices[resultado.indice-1] = resultado;
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

  static void inicializar(Matriz A, Matriz B, int nTam){
    for(int i = 0; i < nTam; i++){
      for(int j = 0; j < nTam; j++){
        A.matriz[i][j]= 3*i+2*j;
        B.matriz[i][j]= 3*i-2*j;
      }
    }
  }

  static void servidor() throws Exception {
    ServerSocket servidor = new ServerSocket(50000);
    System.out.println("Se inicio el servidor");
    int filas = n;
    int columnas = filas;
    // Se crean dos matrices a ser multiplicadas
    Matriz m1 = new Matriz(filas,columnas,0);
    Matriz m2 = new Matriz(filas,columnas,0);
    inicializar(m1, m2, filas);
    //Se transpone la matriz 2 para usar las filas en vez de las columnas
    m2.transponer();
    //Se segmentan las matrices para mandar solamente porciones de la matriz a los distintos nodos
    Matriz segA1 = m1.segmentar(0,columnas, 0, filas/2);
    Matriz segA2 = m1.segmentar(0,columnas, filas/2, filas);
    Matriz segB1 = m2.segmentar(0,columnas, 0, filas/2);
    Matriz segB2 = m2.segmentar(0,columnas, filas/2, filas);

    Worker[] w = new Worker[4];
    for (int i = 0; i < 4; i++) {
      Socket conexion = servidor.accept();
      Worker w_temp;
      // Dependiendo del nodo se mandara una porcion de la matriz distinta
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

    //Una vez obtenidos todos los resultados las matrices obtenidas de los diferentes nodos se uniran en una sola
    Matriz resultado = Matriz.unirMatrices(matrices[0],matrices[1], matrices[2], matrices[3]);
    //Se calculara el checksum de la matriz resultante
    System.out.println("El resultado del checksum es: " + resultado.checksum());
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

    //Se leen los segmentos de matriz mandados por el servidor
    Matriz seg1 = (Matriz) entradaObjecto.readObject();
    Matriz seg2 = (Matriz) entradaObjecto.readObject();

    //Se realiza la multiplicacion usando las filas en vez de las columnas del segmento 2
    //Para aprovechar la memoria cache del procesador
    Matriz resultado = seg1.multiplicarConTranspuesta(seg2);
    //Se guarda el numero de nodo que hizo la operacion para unir el segmento en la posicion correcta
    resultado.indice = num_nodo;
    //Se manda la matriz resultado de la multiplicacion al servidor
    salidaObjetos.writeObject(resultado);
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