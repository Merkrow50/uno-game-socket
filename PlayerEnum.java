public enum PlayerEnum {
    A("Player A", 1),
    B("Player B", 2),
    C("Player C", 3),
    D("Player D", 4),
    E("Player E", 5),
    F("Player F", 6),
    G("Player G", 7),
    H("Player H", 8),
    I("Player I", 9),
    J("Player J", 10);

    public final String rotulo;
    public final int valor;

    PlayerEnum(String rotulo, int valor) {
        this.rotulo = rotulo;
        this.valor = valor;
    }
}
