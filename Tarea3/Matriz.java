class Matriz{
  int[][] matriz;
  int filas,columnas;

  Matriz(int filas, int columnas,boolean inicializar){
    this.matriz = new int[filas][columnas];
    this.filas = filas;
    this.columnas = columnas;
  }

  static void inicializar(Matriz A, Matriz B, int nTam){
    int contador = 0;
    for(int i = 0; i < nTam; i++){
      for(int j = 0; j < nTam; j++){
        A.matriz[i][j]= contador;
        B.matriz[i][j]= contador+16;
        contador+=1;
      }
    }
  }

  void imprimir(){
    for(int i = 0; i < filas; i++){
      for(int j = 0; j < columnas; j++){
        System.out.print(matriz[i][j] + " ");
      }
      System.out.println("");
    }
    System.out.println("");
  }


  void transponer(){
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < i; j++) {
        int x = matriz[i][j];
        matriz[i][j] = matriz[j][i];
        matriz[j][i] = x;
      }
    }
  }
  
  Matriz multiplicar(Matriz B){
    Matriz C = new Matriz(filas, B.columnas,false);
    for (int i = 0; i < filas; i++)
      for (int j = 0; j < B.columnas; j++)
        for (int k = 0; k < B.filas; k++)
          C.matriz[i][j] += matriz[i][k] * B.matriz[k][j];
    return C;
  }

  Matriz multiplicarConTranspuesta(Matriz B){
    Matriz C = new Matriz(filas, B.filas,false);
    for (int i = 0; i < filas; i++)
      for (int j = 0; j < B.filas; j++)
        for (int k = 0; k < B.columnas; k++)
          C.matriz[i][j] += matriz[i][k] * B.matriz[j][k];
    return C;
  }

  Matriz segmentar(int inicioColumna, int finalColumna, int inicioFila, int finalFila){
    int tamCol = finalColumna-inicioColumna;
    int tamFil = finalFila-inicioFila;
    Matriz C = new Matriz(tamFil, tamCol, false);
    for (int i = inicioFila, k = 0; i < finalFila; i++, k++) {
      for (int j = inicioColumna, h = 0; j < finalColumna; j++, h++) {
        C.matriz[k][h] = matriz[i][j];
      }
    }
    return C;
  }


  public static void main(String[] args){
    int filas = 4;
    int columnas = filas;
    Matriz m1 = new Matriz(filas,columnas, true);
    Matriz m2 = new Matriz(filas,columnas, true);
    inicializar(m1, m2, filas);
    m2.transponer();

    Matriz segA1 = m1.segmentar(0,columnas, 0, filas/2);
    Matriz segA2 = m1.segmentar(0,columnas, filas/2, filas);
    Matriz segB1 = m2.segmentar(0,columnas, 0, filas/2);
    Matriz segB2 = m2.segmentar(0,columnas, filas/2, filas);

    segA1.multiplicarConTranspuesta(segB1).imprimir();
    segA1.multiplicarConTranspuesta(segB2).imprimir();
    segA2.multiplicarConTranspuesta(segB1).imprimir();
    segA2.multiplicarConTranspuesta(segB2).imprimir();
  }
}