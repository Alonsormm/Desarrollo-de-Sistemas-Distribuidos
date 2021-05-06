public class Test {
  float[][] separa_matriz(float [][] A, int inicio) { 
    int N = A[0].length;
    float[][] M = new float[N/2][N];
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  float[][] transpuesta(float [][] matriz) {
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

  float[][] multiplica_matrices(float [][] A, float [][] B){
    int N = A[0].length;
    float[][] C = new float[N/2][N/2];
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N/2; j++)
        for (int k = 0; k < N; k++)
          C[i][j] += A[i][k] * B[j][k];
    return C;
  }

  static double checksum(float [][] matriz){
    int filas = matriz.length;
    int columnas = matriz[0].length;
    double sum = 0;
    for(int i = 0; i < filas; i++){
      for(int j = 0; j < columnas; j++)
        sum += matriz[i][j];
    }
    return sum;
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

  void acomoda_matriz(float[][] C,float[][] A,int renglon,int columna, int N){
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N/2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }

  void inicializar_matrices(float [][] A, float [][] B, int N){
    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++) {
        A[i][j] = i - 3 * j;
        B[i][j] = i + 3 * j;
      }
  }

  public static void main(String[] args) {
    Test t = new Test();
    int N = 4;
    float[][] A = new float[N][N];
    float[][] B = new float[N][N];
    t.inicializar_matrices(A,B,N);
    float[][] A1 = t.separa_matriz(A, 0);
    float[][] A2 = t.separa_matriz(A, N/2);

    float[][] B1 = t.separa_matriz(B, 0);
    float[][] B2 = t.separa_matriz(B, N/2);

    float[][] C1 = t.multiplica_matrices(A1,B1);
    float[][] C2 = t.multiplica_matrices(A1,B2);
    float[][] C3 = t.multiplica_matrices(A2,B1);
    float[][] C4 = t.multiplica_matrices(A2,B2);

    float[][] C = new float[N][N];
    t.acomoda_matriz(C,C1,0,0, N);
    t.acomoda_matriz(C,C2,0,N/2, N);
    t.acomoda_matriz(C,C3,N/2,0, N);
    t.acomoda_matriz(C,C4,N/2,N/2, N);

    System.out.println(t.checksum(C));
  }
}
