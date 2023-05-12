package Model;


import java.util.Collections;
import java.util.List;

public class Deck {

    private Carta[] cartas;
    private CartaEspecial[] cartaEspeciais;
    private static int CARTAS_DO_BARALHO = 108;


    public Deck() {
        cartas = new Carta[CARTAS_DO_BARALHO];
    }

    public void gerarDeque(){

        Carta.Cor[] cores = Carta.Cor.values();

        for (int i=0; i<=3; i++){
            Carta.Cor cor = cores[i];

            cartas[CARTAS_DO_BARALHO--] = new Carta(Carta.Valor.getValores(i), cor);

            for (int j=0; j<=8; j++){
                cartas[CARTAS_DO_BARALHO--] = new Carta(Carta.Valor.getValores(j), cor);
            }
        }

        Carta.Valor[] valores = Carta.Valor.values();

        for (Carta.Valor v : Carta.Valor.values()){

        }

    } //fim do método gerarDeque

    public void embaralhar () {
        Collections.shuffle(List.of(cartas));
        Collections.shuffle(List.of(cartaEspeciais));
    }



}

