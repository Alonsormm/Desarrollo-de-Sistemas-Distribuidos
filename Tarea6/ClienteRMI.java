import java.rmi.Naming;

public class ClienteRMI
{

  static float[][] transpuesta(float [][] matriz) {
    int tamFil = matriz.length;
    int tamCol = matriz[0].length;
    float [][] c = new float [tamFil][tamCol];
    for (int i = 0; i < tamFil; i++) {
      for (int j = 0; j < i; j++) {
        c[i][j] = matriz[j][i];
        c[j][i] = matriz[i][j];
      }
    }
    return c;
  }

  static void inicializar_matrices(float [][] A, float [][] B, int N){
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++) {
        A[i][j] = i - 3 * j;
        B[i][j] = i + 3 * j;
      }
  }

  static void imprimir_matriz(float [][] matriz){
    int filas = matriz.length;
    int columnas = matriz[0].length;
    for(int i = 0; i < filas; i++){
      for(int j = 0; j < columnas; j++)
        System.out.print(matriz[i][j] + ", ");
        System.out.println("");
    }
  }

  public static void main(String args[]) throws Exception
  {
    if (args.length != 3) {
      System.err.println("Uso:");
      System.err.println("java ClientRMI <ip_nodo_1> <ip_nodo_2> <Tam_Matriz(N)>");
      System.exit(0);
    }
    String url_nodo1 = "//" + args[0] + "/matrices";
    String url_nodo2 = "//" + args[1] + "/matrices";
    InterfaceRMI nodo_1 = (InterfaceRMI)Naming.lookup(url_nodo1);
    InterfaceRMI nodo_2 = (InterfaceRMI)Naming.lookup(url_nodo2);
    int N = Integer.parseInt(args[2]);
    float[][] A = new float[N][N];
    float[][] B = new float[N][N];
    inicializar_matrices(A,B,N);

    if(N <= 8) {
      System.out.println("Matriz A: ");
      imprimir_matriz(A);
      System.out.println("Matriz B: ");
      imprimir_matriz(B);
    }

    float[][] B_transpuesta = transpuesta(B);
    
    float[][] A1 = nodo_1.separa_matriz(A, 0);
    float[][] A2 = nodo_1.separa_matriz(A, N/2);
    float[][] B1 = nodo_2.separa_matriz(B, 0);
    float[][] B2 = nodo_2.separa_matriz(B, N/2);

    float[][] C1 = nodo_1.multiplica_matrices(A1,B1);
    float[][] C2 = nodo_1.multiplica_matrices(A1,B2);
    float[][] C3 = nodo_2.multiplica_matrices(A2,B1);
    float[][] C4 = nodo_2.multiplica_matrices(A2,B2);

    float[][] C = new float[N][N];
    C = nodo_1.acomoda_matriz(C,C1,0,0, N);
    C = nodo_1.acomoda_matriz(C,C2,0,N/2, N);
    C = nodo_2.acomoda_matriz(C,C3,N/2,0, N);
    C = nodo_2.acomoda_matriz(C,C4,N/2,N/2, N);

    if(N <= 8) {
      System.out.println("Resultado AxC: ");
      imprimir_matriz(C);
    }
    System.out.println("Checksum: " + nodo_1.checksum(C));
  }
}
