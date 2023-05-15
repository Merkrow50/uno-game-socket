public enum Value {

  Zero, Um, Dois, Tres, Quatro, Cinco, Seis, Sete, Oito, Nove, Pular, Reverse, MaisDois, Curinga, CuringaMaisQuatro;

  private static final Value[] values = Value.values();

  public static Value getValue(int i){
    return Value.values[i];
  }

}
