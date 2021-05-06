import java.rmi.Naming;

public class ServidorRMI
{
  public static void main(String[] args) throws Exception
  {
    System.out.println(0);
    String url = "//localhost/matrices";
    ClaseRMI obj = new ClaseRMI();
    System.out.println(2);
    // registra la instancia en el rmiregistry
    Naming.rebind(url,obj);
    System.out.println(3);
  }
}
