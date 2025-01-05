package dal.repositories;

import dal.UnitOfWork;
import models.Card;
import models.Element;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CardRepository {
    private UnitOfWork unitOfWork;

    public CardRepository(UnitOfWork unitOfWork){
        this.unitOfWork = unitOfWork;
    }

    //TODO create 4 functions in cardrepos

    public boolean createPackage(ArrayList<Card> cards) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into packages ( card1, card2, card3, card4, card5 ) values ( ?, ?, ?, ?, ? )
                """)){
            preparedStatement.setString(1, cards.get(0).getId());
            preparedStatement.setString(2, cards.get(1).getId());
            preparedStatement.setString(3, cards.get(2).getId());
            preparedStatement.setString(4, cards.get(3).getId());
            preparedStatement.setString(5, cards.get(4).getId());

            preparedStatement.executeQuery();
            return true;

        }catch (SQLException e){
            throw new SQLException("Internal Server Error", e);
        }
    }

    public void  buyPackage(String username) throws SQLException{
        //SELECT * FROM packages
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select id from cards where id in ( select card1, card2, card3, card4, card5 from packages where id = ? )
                """)){
            //TODO Select random package
            int TESTINT = 1;
            preparedStatement.setInt(1, TESTINT);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> cardIds = new ArrayList<>();
            while(resultSet.next()){
                cardIds.add(resultSet.getString("id"));
                //TODO insert into usercards
            }
        }catch (SQLException e) {
            throw new SQLException("Internal Server Error", e);
        }
    }

    public ArrayList<Card> getCards(String username) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from usercards where user = ?
                """)){
            preparedStatement.setString(1, username);

            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();
            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next()){
                Card cardInRow = new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        Element.valueOf(resultSet.getString(3)), //TODO better Element Constraints
                        resultSet.getInt(4)
                );
                cards.add(cardInRow);
            }

            return cards;

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public ArrayList<Card> getDeck(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from cards where id in (select card from usercards where user = ? and indeck = 1 )
                """)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Card> cards = new ArrayList<>();
            while(resultSet.next()){
                Card cardInRow = new Card(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        Element.valueOf(resultSet.getString(3)),
                        resultSet.getInt(4)
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
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update usercards set indeck = 1 where user = ?
                """)){
            preparedStatement.setString(1, username);


        }catch (SQLException e){
            throw new SQLException("Error at update Statement", e);
        }
        return false;
    }


    //TODO make getoid function

}
