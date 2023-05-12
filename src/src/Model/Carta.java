package Model;

public class Carta {

    private final Valor valor;
    private final Cor cor;

    public enum Valor {

        ZERO, UM, DOIS, TRES, QUATRO, CINCO, SEIS, SETE, OITO, NOVE, INVERTER, PULAR, COMPRAR_DUAS;

        private static final Valor[] valor = Valor.values();

        public static Valor getValores(int i) {
            return Valor.valor[i];
        }

    }

    public enum Cor {

        AMARELO, VERDE, AZUL, VERMELHO;

        private static final Cor[] cor = Cor.values();

        public static Cor getCores(int i) {
            return Cor.cor[i];
        }
    }

    public Carta(Valor valor, Cor cor) {
        this.valor = valor;
        this.cor = cor;
    }


    public Valor getValor() {
        return valor;
    }

    public Cor getCor() {
        return cor;
    }

    @Override
    public String toString() {
        return "" + valor + " " +
                cor +
                '}';
    }
}
