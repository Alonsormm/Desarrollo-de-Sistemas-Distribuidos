import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote
{
  public float[][] separa_matriz(float [][] A, int inicio) throws RemoteException;
  public float[][] multiplica_matrices(float [][] A, float [][] B) throws RemoteException;
  public double checksum(float [][] matriz) throws RemoteException;
  public void acomoda_matriz(float[][] C,float[][] A,int renglon,int columna, int N) throws RemoteException;
}
