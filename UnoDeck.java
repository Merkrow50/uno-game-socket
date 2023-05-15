import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class UnoDeck {


  private UnoCard[] cards;

  private int cardsInDeck;

  public UnoDeck() {
    cards = new UnoCard[108];
    reset();
  }


  public void reset() {

    Color[] colors = Color.values();

    cardsInDeck = 0;

    for(int i = 0; i < colors.length-1; i++){

      Color color = colors[i];


      cards[cardsInDeck++] = new UnoCard(color, Value.getValue(0));

      for (int j = 1; j < 10; j++){

        cards[cardsInDeck++] = new UnoCard(color, Value.getValue(j));
        cards[cardsInDeck++] = new UnoCard(color, Value.getValue(j));

      }

      Value[] values = new Value[]{Value.Dois, Value.Pular, Value.Reverse};

      for (Value value: values){
        cards[cardsInDeck++] = new UnoCard(color, value);
        cards[cardsInDeck++] = new UnoCard(color, value);
      }
    }


    Value[] values = new Value[]{Value.Curinga, Value.CuringaMaisQuatro};

    for (Value value: values){

      for (int i = 0; i < 4; i++){

        cards[cardsInDeck++] = new UnoCard(Color.Curinga, value);

      }
    }

  }

  public void replaceDeckWith(ArrayList<UnoCard> cards) {
    this.cards = cards.toArray(new UnoCard[cards.size()]);
    this.cardsInDeck = this.cards.length;
  }



  public boolean isEmpty() {
    return cards.length == 0;
  }


  public void shuffle() {

    int n = cards.length;
    Random random = new Random();


    for (int i = 0; i < cards.length; i++){
      int ramdomValue = i + random.nextInt(n - i);
      UnoCard ramdomCard = cards[ramdomValue];
      cards[ramdomValue] = cards[i];
      cards[i] = ramdomCard;
    }

  }

  public UnoCard drawCard() {
    if(isEmpty()){
      throw new IllegalArgumentException("Não foi possivel criar o card pois nao há nenhuma carta no deck");
    }

    return cards[--cardsInDeck];
  }

  public UnoCard[] drawCard(int n){
    if(n < 0) {
      throw new IllegalArgumentException("Deve se definir um numero maior que zero para criar os cards");
    }


    if (n > cardsInDeck) {
      throw new IllegalArgumentException("Não foi possivel adicionar esta quantidade " + n + "pois o deck so possui " + cardsInDeck + " cartas");
    }

    UnoCard[] ret = new UnoCard[n];

    for (int i = 0; i < n; i++){
      ret[i] = cards[--cardsInDeck];
    }

    return ret;
  }

}
