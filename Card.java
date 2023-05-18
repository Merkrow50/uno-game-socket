public class Card {
    public enum Type {
        NUMERO, PULAR, INVERTER, COMPRAR_DOIS, CURINGA, CURINGA_COMPRAR_QUATRO
    }

    public enum Color {
        VERMELHO, AMARELO, VERDE, AZUL, CURINGA
    }

    private Type type;
    private Color color;
    private int number;

    public Card(Type type, Color color) {
        this(type, color, -1);
    }

    public Card(Type type, Color color, int number) {
        this.type = type;
        this.color = color;
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        String typeName = type.toString();
        if (type == Type.NUMERO) {
            return color.toString() + " " + typeName + " " + number;
        } else {
            return color.toString() + " " + typeName;
        }
    }
}
