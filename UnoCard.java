public class UnoCard {

  private final Color color;

  private final Value value;

  public UnoCard(final Color color, final Value value){
    this.color = color;
    this.value = value;
  }

  public Color getColor() {
    return color;
  }

  public Value getValue() {
    return value;
  }

  @Override
  public String toString() {
    return color + "_" + value;
  }
}
