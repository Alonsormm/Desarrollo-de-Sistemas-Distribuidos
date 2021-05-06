import java.rmi.Naming;

public class ServidorRMI
{
  public static void main(String[] args) throws Exception
  {
    String url = "//localhost/matrices";
    ClaseRMI obj = new ClaseRMI();
    // registra la instancia en el rmiregistry
    Naming.rebind(url,obj);
  }
}
