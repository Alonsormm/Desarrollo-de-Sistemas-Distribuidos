import java.io.*;

class Matriz implements Serializable{
  double[][] matriz;
  int filas,columnas;
  int indice;

  Matriz(int filas, int columnas, int indice){
    this.matriz = new double[filas][columnas];
    this.filas = filas;
    this.columnas = columnas;
    this.indice = indice;
  }


  void transponer(){
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < i; j++) {
        double x = matriz[i][j];
        matriz[i][j] = matriz[j][i];
        matriz[j][i] = x;
      }
    }
  }
  
  Matriz multiplicar(Matriz B){
    Matriz C = new Matriz(filas, B.columnas,0);
    for (int i = 0; i < filas; i++)
      for (int j = 0; j < B.columnas; j++)
        for (int k = 0; k < B.filas; k++)
          C.matriz[i][j] += matriz[i][k] * B.matriz[k][j];
    return C;
  }

  Matriz multiplicarConTranspuesta(Matriz B){
    Matriz C = new Matriz(filas, B.filas, 0);
    for (int i = 0; i < filas; i++)
      for (int j = 0; j < B.filas; j++)
        for (int k = 0; k < B.columnas; k++)
          C.matriz[i][j] += matriz[i][k] * B.matriz[j][k];
    return C;
  }

  Matriz segmentar(int inicioColumna, int finalColumna, int inicioFila, int finalFila){
    int tamCol = finalColumna-inicioColumna;
    int tamFil = finalFila-inicioFila;
    Matriz C = new Matriz(tamFil, tamCol, 0);
    for (int i = inicioFila, k = 0; i < finalFila; i++, k++) {
      for (int j = inicioColumna, h = 0; j < finalColumna; j++, h++) {
        C.matriz[k][h] = matriz[i][j];
      }
    }
    return C;
  }

  static Matriz unirMatrices(Matriz m1,Matriz m2,Matriz m3,Matriz m4){
    int numFilas = m1.filas + m2.filas;
    int numColumnas = m1.columnas + m3.columnas;
    Matriz resultado = new Matriz(numFilas, numColumnas, 0);
    for(int i = 0; i < m1.filas ; i++){
      for(int j = 0; j < m1.columnas; j++){
        resultado.matriz[i][j] = m1.matriz[i][j];
      }
    }
    for(int i = 0, h = 0; i < m1.filas ; i++, h++){
      for(int j = 0, k = m1.columnas; j < m1.columnas; j++, k++){
        resultado.matriz[h][k] = m2.matriz[i][j];
      }
    }
    for(int i = 0, h = m1.filas; i < m1.filas ; i++, h++){
      for(int j = 0, k = 0; j < m1.columnas; j++, k++){
        resultado.matriz[h][k] = m3.matriz[i][j];
      }
    }
    for(int i = 0, h = m1.filas; i < m1.filas ; i++, h++){
      for(int j = 0, k = m1.columnas; j < m1.columnas; j++, k++){
        resultado.matriz[h][k] = m4.matriz[i][j];
      }
    }
    return resultado;
  }

  double checksum(){
    double suma = 0;
    for(int i = 0; i < filas; i++)
      for(int j = 0; j < columnas; j++)
        suma+=matriz[i][j];
    return suma;
  }
}