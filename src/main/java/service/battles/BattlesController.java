package service.battles;

import com.fasterxml.jackson.core.JsonProcessingException;
import dal.UnitOfWork;
import dal.repositories.CardRepository;
import dal.repositories.UserRepository;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import httpServer.http.ContentType;
import httpServer.http.HttpStatus;
import httpServer.server.Request;
import httpServer.server.Response;
import models.User;
import service.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class BattlesController extends Controller{
    final ArrayList<Player> queue = new ArrayList<>();
    private final Object lock = new Object();
    private final Map<Player, String> battleResults = new HashMap<>();

    private final int ROUNDMAX = 100;


    public BattlesController() {}


    public Response battle(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        String username = request.getHeaderMap().getHeader("Authorization");

        if (username != null && username.startsWith("Bearer ")) {
            username = username.substring(7);
            username = username.replace("-mtcgToken", "").trim();
        } else {
            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    "{ \"message\": \"You must be logged in to do that!\" }"
            );
        }

        try(unitOfWork){
            ArrayList<Card> deck = new CardRepository(unitOfWork).getDeck(username);
            if(deck.isEmpty()){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\": \"You have no cards in your deck!\" }"
                );
            }

            Player player = new Player(username, deck);

            synchronized (lock){
                if(queue.isEmpty()){
                    queue.add(player);

                    try {
                        lock.wait();
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        return new Response(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ContentType.JSON,
                                "{ \"message\": \"Error while waiting for player to join!\" }"
                                );
                    }

                    String result = battleResults.remove(player);
                    return new Response(
                            HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            result
                    );
                } else {
                    Player opponent = queue.remove(0);

                    String battleResult = play(player, opponent);
                    if(player.getDeck().isEmpty()){
                        new UserRepository(unitOfWork).loose(player.getUsername());
                        new UserRepository(unitOfWork).win(opponent.getUsername());
                    }else if(opponent.getDeck().isEmpty()){
                        new UserRepository(unitOfWork).loose(opponent.getUsername());
                        new UserRepository(unitOfWork).win(opponent.getUsername());
                    }

                    battleResults.put(player, battleResult);
                    battleResults.put(opponent, battleResult);


                    lock.notify();

                    String result = battleResults.remove(player);

                    return new Response(
                            HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            result
                    );
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\": \"Internal Server Error!\" }"

        );

    }

    public String play(Player player1, Player player2){
        player1.setDeck(sortDeck(player1.getDeck()));
        player2.setDeck(sortDeck(player2.getDeck()));

        int count = 1;
        String battleLog = "";

        while (count <= ROUNDMAX && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty()) {
            Random random = new Random();
            int randomInt1 = random.nextInt(0, player1.getDeck().size());
            int randomInt2 = random.nextInt(0, player2.getDeck().size());

            int winner = fight(player1.getDeck().get(randomInt1), player2.getDeck().get(randomInt2));

            if(winner == 1){
                battleLog += "Round " + count + ": The card " + player1.getDeck().get(randomInt1).getName() + " of Player 1 wins!\n";
                Card loserCard = player2.getDeck().remove(randomInt2);
                player1.getDeck().add(loserCard);
            }else if(winner == 0){
                battleLog += "Round " + count + ": Ends in a draw!\n";
            }else {
                battleLog += "Round " + count + ": The card " + player2.getDeck().get(randomInt2).getName() + " of Player 2 wins!\n";
                Card loserCard = player1.getDeck().remove(randomInt1);
                player2.getDeck().add(loserCard);
            }
            count++;
        }

        count--;

        if(player1.getDeck().isEmpty()){
            battleLog += "Player 2 wins the game after " + count + " rounds!\n";
        }else if(player2.getDeck().isEmpty()){
            battleLog += "Player 1 wins the game after " + count + " rounds!\n";
        }else {
            battleLog += "The game ended in a draw!\n";
        }

        return battleLog;

    }

    private ArrayList<Card> sortDeck(ArrayList<Card> deck){
        for(int i = 0; i < deck.size(); i++){
            if(deck.get(i).getName().contains("Spell")){
                deck.get(i).setType(1);
            }else{
                deck.get(i).setType(2);
            }
            if(deck.get(i).getName().contains("Water")){
                deck.get(i).setElement(Element.WATER);
            }else if(deck.get(i).getName().contains("Fire")){
                deck.get(i).setElement(Element.FIRE);
            }else{
                deck.get(i).setElement(Element.NORMAL);
            }
        }
        return deck;
    }

    public int fight(Card card1, Card card2){

        //returns 1 for card 1 won, 2 for card 2 won, 0 for draw

        //Goblins are too afraid of Dragons to attack
        if(card1.getName().contains("Goblin") && card2.getName().contains("Dragon")){
            return 2;
        }else if(card1.getName().contains("Dragon") && card2.getName().contains("Goblin")){
            return 1;
        }

        //Wizzards can control Orks so they dont damage them
        if(card1.getName().contains("Wizzard") && card2.getName().contains("Ork")){
            return 1;
        }else if(card1.getName().contains("Ork") && card2.getName().contains("Wizzard")){
            return 2;
        }

        //The Knight drowns at WaterSpell
        if(card1.getName().contains("Knight") && card2.getName().equals("WaterSpell")){
            return 2;
        }else if(card1.getName().equals("WaterSpell") && card2.getName().contains("Knight")){
            return 1;
        }

        //Kraken is immune against spells
        if(card1.getName().contains("Kraken") && card2.getType() == 1){
            return 1;
        }else if(card1.getType() == 1 && card2.getName().contains("Kraken")){
            return 2;
        }

        //FireElfs can evade Dragons Attacks
        if(card1.getName().contains("FireElf") && card2.getName().contains("Dragon")){
            return 1;
        }else if(card1.getName().contains("Dragon") && card2.getName().contains("FireElf")){
            return 2;
        }

        //Normal v Normal
        if(card1.getType() == 2 && card2.getType() == 2){
            if(card1.getDamage() > card2.getDamage()){
                return 1;
            }else if(card1.getDamage() < card2.getDamage()){
                return 2;
            }else return 0;
        } else {
            //Normal v Spell & Spell v Spell
            if(isStronger(card1.getElement(), card2.getElement()) == 0){
                if(card1.getDamage() > card2.getDamage()){
                    return 1;
                }else if(card1.getDamage() < card2.getDamage()){
                    return 2;
                }else return 0;
            }else if(isStronger(card1.getElement(), card2.getElement()) == 1){
                if(card1.getDamage() * 2 > card2.getDamage()){
                    return 1;
                }else if(card1.getDamage() * 2 < card2.getDamage()){
                    return 2;
                }else return 0;
            }else {
                if(card1.getDamage() > card2.getDamage() * 2){
                    return 1;
                }else if(card1.getDamage() < card2.getDamage() * 2){
                    return 2;
                }else return 0;
            }
        }

    }


    private int isStronger(Element element1, Element element2){
        if(element1 == element2){
            return 0;
        }else if(element1 == Element.WATER){
            if(element2 == Element.FIRE){
                return 1; //Water v Fire
            }else{
                return 2; //Water v Normal
            }
        }else if(element1 == Element.FIRE){
            if(element2 == Element.NORMAL){
                return 1; //Fire v normal
            }else {
                return 2; //Fire v Water
            }
        }
        return 0;
    }

}
