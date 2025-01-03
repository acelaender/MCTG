package dal.repositories;

import dal.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CardRepository {
    private UnitofWork unitofWork;

    public CardRepository(UnitOfWork unitOfWork){
        this.unitofWork = unitofWork;
    }

    //TODO create 4 functions in cardrepos

    public boolean createPackage(/*Array of 5 Cards*/){
        //INSERT INTO packages
        //TODO getOid
        return false;
    }

    public void /*TODO return cardarray*/ buyPackage(/*Specific Pack? random Pack?*/){
        //SELECT * FROM packages
    }

    public void getCards() throws SQLException {
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
                select * from usercards where user = ?
                """)){

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public void getDeck() throws SQLException{
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
                select * from usercards where user = ? and indeck = 1
                """)){
            //TODO set id
            int uid = 1;

            preparedStatement.setInt(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public boolean setDeck() throws SQLException{
        try(PreparedStatement preparedStatement = this.unitofWork.prepareStatement("""
                update usercards set indeck = 1 where user = ?
                """)){

        }catch (SQLException e){
            throw new SQLException("Error at update Statement", e);
        }
        return false;
    }
}
