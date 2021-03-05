class MultiplicaMatriz {
  int N;
  int[][] A = new int[N][N];
  int[][] B = new int[N][N];
  int[][] C = new int[N][N];

  MultiplicaMatriz(int N) {
    this.N = N;
    A = new int[this.N][this.N];
    B = new int[this.N][this.N];
    C = new int[this.N][this.N];
    long t1 = System.currentTimeMillis();

    // inicializa las matrices A y B

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++) {
        A[i][j] = 2 * i - j;
        B[i][j] = i + 2 * j;
        C[i][j] = 0;
      }

    // multiplica la matriz A y la matriz B, el resultado queda en la matriz C

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        for (int k = 0; k < N; k++)
          C[i][j] += A[i][k] * B[k][j];

    long t2 = System.currentTimeMillis();
    System.out.println("Tiempo: " + (t2 - t1) + "ms");
  }
}