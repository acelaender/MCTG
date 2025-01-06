package dal.repositories;

import dal.UnitOfWork;
import models.Card;
import models.Element;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class CardRepository {
    private UnitOfWork unitOfWork;

    public CardRepository(UnitOfWork unitOfWork){
        this.unitOfWork = unitOfWork;
    }

    //TODO create 1 functions in cardrepos

    //TODO WORKS
    public boolean createPackage(ArrayList<Card> cards) throws SQLException {

        for (int i = 0; i < cards.size(); i++) {
            try(PreparedStatement preparedStatement1 = this.unitOfWork.prepareStatement("""
                insert into cards ( id, name, damage ) values (?, ?, ?)""")){
                preparedStatement1.setString(1, cards.get(i).getId());
                preparedStatement1.setString(2, cards.get(i).getName());
                preparedStatement1.setInt(3, cards.get(i).getDamage());
                preparedStatement1.executeUpdate();
            }catch (SQLException e){
                throw new SQLException("Internal Server Error at creating cards", e);
            }
        }


        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into packages ( card1, card2, card3, card4, card5 ) values ( ?, ?, ?, ?, ? )
                """)){
            preparedStatement.setString(1, cards.get(0).getId());
            preparedStatement.setString(2, cards.get(1).getId());
            preparedStatement.setString(3, cards.get(2).getId());
            preparedStatement.setString(4, cards.get(3).getId());
            preparedStatement.setString(5, cards.get(4).getId());

            preparedStatement.executeUpdate();



        }catch (SQLException e){
            throw new SQLException("Internal Server Error at creating Package", e);
        }

        this.unitOfWork.commitTransaction();
        return true;

    }

    //TODO WORKS
    public boolean buyPackage(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select id from cards where id in ( SELECT card1 FROM packages WHERE id = ?
                                                       UNION
                                                       SELECT card2 FROM packages WHERE id = ?
                                                       UNION
                                                       SELECT card3 FROM packages WHERE id = ?
                                                       UNION
                                                       SELECT card4 FROM packages WHERE id = ?
                                                       UNION
                                                       SELECT card5 FROM packages WHERE id = ? )
                """)){
            ArrayList<Integer> ids = getOids();
            if(ids.size() == 0) return false;

            Random random = new Random();
            int randomId = random.nextInt(0, ids.size());

            randomId = ids.get(randomId);

            preparedStatement.setInt(1, randomId);
            preparedStatement.setInt(2, randomId);
            preparedStatement.setInt(3, randomId);
            preparedStatement.setInt(4, randomId);
            preparedStatement.setInt(5, randomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> cardIds = new ArrayList<>();
            while(resultSet.next()){
                cardIds.add(resultSet.getString("id"));
            }
            if(cardIds.size() < 5 || cardIds.size() > 5){
                throw new SQLException("Internal Server Error at getting CardIds");
            }else {
                for (int i = 0; i < cardIds.size(); i++) {
                    try(PreparedStatement preparedStatement1 = this.unitOfWork.prepareStatement("""
                        insert into usercards ( card, owner, indeck ) values (?, ?, 0)""")) {
                        preparedStatement1.setString(1, cardIds.get(i));
                        preparedStatement1.setString(2, username);

                        preparedStatement1.executeUpdate();

                    }
                }
                PreparedStatement preparedStatement2 = this.unitOfWork.prepareStatement("""
                        delete from packages where id = ?""");
                preparedStatement2.setInt(1, randomId);
                preparedStatement2.executeUpdate();
                this.unitOfWork.commitTransaction();
                return true;
            }
        }catch (SQLException e) {
            throw new SQLException("Internal Server Error", e);
        }
    }

    //TODO WORKS
    public ArrayList<Card> getCards(String username) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from usercards where owner = ?
                """)){
            preparedStatement.setString(1, username);

            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next()){
                Card cardInRow = new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(4)
                );
                cards.add(cardInRow);
            }

            return cards;

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    //TODO WORKS
    public ArrayList<Card> getDeck(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from cards where id in (select card from usercards where owner = ? and indeck = 1 )
                """)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next()){
                Card cardInRow = new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)
                );
                cards.add(cardInRow);
            }

            return cards;
        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }


    //TODO finish function
    public boolean setDeck(String username, ArrayList<Card> cards) throws SQLException{
        for (int i = 0; i < cards.size(); i++) {
            try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                    select owner from usercards where card = ?""")){
                preparedStatement.setString(1, cards.get(i).getId());
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();
                if(resultSet.next()){
                    if(!resultSet.getString("owner").equals(username)){
                        System.out.println(resultSet.getString("owner") + " " + username);
                        return false;
                    }
                }
            }
        }

        PreparedStatement preparedStatement1 = this.unitOfWork.prepareStatement("""
                update usercards set indeck = 0 where indeck = 1 and owner = ?""");
        preparedStatement1.setString(1, username);
        preparedStatement1.executeUpdate();


        for (int i = 0; i < cards.size(); i++) {
            try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update usercards set indeck = 1 where card = ?
                """)){
                preparedStatement.setString(1, cards.get(i).getId());
                preparedStatement.executeUpdate();
            }catch (SQLException e){
                unitOfWork.rollbackTransaction();
                throw new SQLException("Error at update Statement", e);
            }
        }
        unitOfWork.commitTransaction();
        return true;
    }


    private ArrayList<Integer> getOids() throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select id from packages order by id desc""")) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            ArrayList<Integer> oids = new ArrayList<>();
            while (resultSet.next()) {
                oids.add(resultSet.getInt("id"));
            }
            return oids;
        }catch (SQLException e){
            throw new SQLException("Internal Server Error", e);
        }
    }

}
