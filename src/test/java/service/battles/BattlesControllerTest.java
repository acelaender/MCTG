package service.battles;

import models.Card;
import models.Element;
import models.Player;
import org.junit.Test;

import java.util.ArrayList;

public class BattlesControllerTest {



    void setUp(){

    }

    @Test
    public void testFight_MonsterVMonster(){
        Card card1 = new Card("abcd", "Goblin", 2, Element.NORMAL, 10);
        Card card2 = new Card("cdef", "Knight", 2, Element.NORMAL, 10);
        int result = new BattlesController().fight(card1, card2);
        assert result == 0;
    }

    @Test
    public void testFight_MonsterVSpell(){
        Card card1 = new Card("abcd", "Goblin", 2, Element.WATER, 10);
        Card card2 = new Card("cdef", "Knight", 1, Element.FIRE, 10);
        int result = new BattlesController().fight(card1, card2);
        assert result == 1;
    }

    @Test
    public void testFight_specialFight(){
        Card card1 = new Card("abcd", "WaterSpell", 1, Element.WATER, 10);
        Card card2 = new Card("cdef", "Knight", 2, Element.NORMAL, 20);
        int result = new BattlesController().fight(card1, card2);
        assert result == 1;
    }

    @Test
    public void testFight_specialFightDragon(){
        Card card1 = new Card("abcd", "Dragon", 1, Element.FIRE, 50);
        Card card2 = new Card("cdef", "FireElf", 2, Element.FIRE, 20);
        int result = new BattlesController().fight(card1, card2);
        assert result == 2;
    }

    @Test
    public void testFight_SpellvSpell(){
        Card card1 = new Card("abcd", "WaterSpell", 1, Element.FIRE, 20);
        Card card2 = new Card("cdef", "NormalSpell", 1, Element.NORMAL, 20);
        int result = new BattlesController().fight(card1, card2);
        assert result == 1;
    }

    @Test
    public void testPlay_Player1StrongerthanPlayer2(){
        ArrayList<Card> deck1 = new ArrayList<>();
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));

        ArrayList<Card> deck2 = new ArrayList<>();
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 5));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 5));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 5));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 5));


        Player p1 = new Player("p1", deck1);
        Player p2 = new Player("p2", deck2);

        String result = new BattlesController().play(p1, p2);
        System.out.println(result);
    }

    @Test
    public void testPlay_Draw(){
        ArrayList<Card> deck1 = new ArrayList<>();
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));

        ArrayList<Card> deck2 = new ArrayList<>();
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));


        Player p1 = new Player("p1", deck1);
        Player p2 = new Player("p2", deck2);

        String result = new BattlesController().play(p1, p2);
        System.out.println(result);
    }

    @Test
    public void testPlay_random(){
        ArrayList<Card> deck1 = new ArrayList<>();
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck1.add(new Card("abcd", "FireElf", 2, Element.WATER, 10));
        deck1.add(new Card("abcd", "Dragon", 2, Element.FIRE, 10));
        deck1.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));

        ArrayList<Card> deck2 = new ArrayList<>();
        deck2.add(new Card("abcd", "Goblin", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "Knight", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "WaterSpell", 2, Element.NORMAL, 10));
        deck2.add(new Card("abcd", "FireSpell", 2, Element.NORMAL, 10));


        Player p1 = new Player("p1", deck1);
        Player p2 = new Player("p2", deck2);

        String result = new BattlesController().play(p1, p2);
        System.out.println(result);
    }
}
