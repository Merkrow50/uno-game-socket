public enum Color {

  Vermelho, Amarelo, Azul, Verde, Curinga;

  private static final Color[] colors = Color.values();

  public static Color getColor(int i){
    return colors[i];
  }

}
