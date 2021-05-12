import java.rmi.Naming;

public class Checador {
  public static void main(String[] args) {
    String url_nodo1 = "//" + args[0] + "/matrices";
    try {
      String[] remoteObjects= Naming.list(url_nodo1);
      System.out.println(remoteObjects.length);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
