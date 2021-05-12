import java.io.*;
import java.net.*;
// javac -cp $CATALINA_HOME/lib/gson-2.3.1.jar:. *.java

public class Cliente {

  String url_string;

  Cliente(String url_base) { 
    url_string = url_base;
  }

  Usuario consultarUsuario(int id_usuario) throws Exception {
    URL url = new URL(url_string + "/consulta_usuario");
    Usuario u = new Usuario();
    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
    conexion.setDoOutput(true);
    conexion.setRequestMethod("POST");
    conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    String parametros = "id_usuario=" + URLEncoder.encode(Integer.toString(id_usuario), "UTF-8");
    OutputStream os = conexion.getOutputStream();
    os.write(parametros.getBytes());
    os.flush();
    if (conexion.getResponseCode() == 200) {
      // no hubo error
      BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
      String respuesta;
      while ((respuesta = br.readLine()) != null) {
        u = Usuario.valueOf(respuesta);
      }
    } else {
      BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));

      String respuesta;
      while ((respuesta = br.readLine()) != null)
        System.out.println(respuesta);
    }
    return u;
  }

  int altaUsuario(Usuario u) throws Exception {
    URL url = new URL(url_string + "/alta_usuario");
    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
    conexion.setDoOutput(true);
    conexion.setRequestMethod("POST");
    conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    int id = 0;
    String parametros = "usuario=" + URLEncoder.encode(u.toJson(), "UTF-8");
    OutputStream os = conexion.getOutputStream();
    os.write(parametros.getBytes("UTF-8"));
    os.flush();
    if (conexion.getResponseCode() == 200) {
      // no hubo error
      BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
      String respuesta;
      while ((respuesta = br.readLine()) != null) {
        id = Integer.parseInt(respuesta);
      }
    } else {
      BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));

      String respuesta;
      while ((respuesta = br.readLine()) != null)
        System.out.println(respuesta);
    }
    return id;
  }

  void borraUsuario(int id) throws Exception {
    URL url = new URL(url_string + "/borra_usuario");
    HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
    conexion.setDoOutput(true);
    conexion.setRequestMethod("POST");
    conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    String parametros = "id_usuario=" + id;
    OutputStream os = conexion.getOutputStream();
    os.write(parametros.getBytes("UTF-8"));
    os.flush();
    if (conexion.getResponseCode() == 200) {
      // no hubo error
      System.out.println("El usuario ha sido borrado");
    } else {
      BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));

      String respuesta;
      while ((respuesta = br.readLine()) != null)
        System.out.println(respuesta);
    }
  }
}
