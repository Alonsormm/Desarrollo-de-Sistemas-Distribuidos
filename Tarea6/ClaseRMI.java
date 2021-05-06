import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI
{

  public ClaseRMI() throws RemoteException
  {
    super( );
  }

  
  public float[][] separa_matriz(float [][] A, int inicio) throws RemoteException { 
    int N = A[0].length;
    float[][] M = new float[N/2][N];
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  public float[][] multiplica_matrices(float [][] A, float [][] B) throws RemoteException {
    int N = A[0].length;
    float[][] C = new float[N/2][N/2];
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N/2; j++)
        for (int k = 0; k < N; k++)
          C[i][j] += A[i][k] * B[j][k];
    return C;
  }

  public double checksum(float [][] matriz) throws RemoteException {
    int filas = matriz.length;
    int columnas = matriz[0].length;
    double sum = 0;
    for(int i = 0; i < filas; i++){
      for(int j = 0; j < columnas; j++)
        sum += matriz[i][j];
    }
    return sum;
  }

  public void acomoda_matriz(float[][] C,float[][] A,int renglon,int columna, int N) throws RemoteException {
    for (int i = 0; i < N/2; i++)
      for (int j = 0; j < N/2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }
}
