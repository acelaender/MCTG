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
        this.unitofWork = unitofWork;
    }

    //TODO create 4 functions in cardrepos

    public boolean createPackage(/*Array of 5 Cards*/){
        //INSERT INTO packages
        return false;
    }

    public void  buyPackage(/*Specific Pack? random Pack?*/){
        //SELECT * FROM packages
    }

    public ArrayList<Card> getCards(String username) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
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
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
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
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
                update usercards set indeck = 1 where user = ?
                """)){
            preparedStatement.setString(1, username);


        }catch (SQLException e){
            throw new SQLException("Error at update Statement", e);
        }
        return false;
    }
}
