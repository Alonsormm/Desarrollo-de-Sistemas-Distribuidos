class Tarea2{

  public static void main(String[] args){
    int[] ns = {100,200,300,500,1000};

    for (int i = 0; i < 5; i++){
      System.out.println("MultiplicaMatriz con N = " + ns[i]);
      MultiplicaMatriz m = new MultiplicaMatriz(ns[i]);
    }
    for (int i = 0; i < 5; i++){
      System.out.println("MultiplicaMatriz_2 con N = " + ns[i]);
      MultiplicaMatriz_2 m2 = new MultiplicaMatriz_2(ns[i]);
    }
  }

}