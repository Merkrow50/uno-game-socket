package Model;

public class CartaEspecial {

    private final ValorEspecial valorEspecial;
    public enum ValorEspecial {
        MAIS_QUATRO, COMPRAR_QUATRO;

        private static final ValorEspecial[] valorespecial = ValorEspecial.values();

        public static ValorEspecial getValorEspecial(int i) {
            return ValorEspecial.valorespecial[i];
        }
    }

    public CartaEspecial(ValorEspecial valorEspecial) {
        this.valorEspecial = valorEspecial;
    }

    public ValorEspecial getValorEspecial() {
        return valorEspecial;
    }

    @Override
    public String toString() {
        return "" + valorEspecial +
                '}';
    }
}
