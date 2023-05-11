package Model;


public class Deck {

    private Carta[] cartas;
    private CartaEspecial[] cartaEspeciais;
    private static final int CARTAS_DO_BARALHO = 108;


    public Deck() {
        cartas = new Carta[CARTAS_DO_BARALHO];
    }

    public void gerarDeque(){

        Carta.Cor[] cores = Carta.Cor.values();

        for (int i=0; i<13; i++){
            Carta.Cor cor = cores[i];

        }
    }



}

