package dal.repositories;

import dal.UnitOfWork;
import models.Card;
import models.TradingDeal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradingRepository {

    //TODO create 3 functions in tradings

    private UnitOfWork unitOfWork;

    public TradingRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void createDeal(TradingDeal tradingDeal) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into tradingdeals (id, cardtotrade, minimumdamage) values (?, ?, ?)
                """)){
            preparedStatement.setString(1, tradingDeal.getId());
            preparedStatement.setString(2, tradingDeal.getCardToTrade());
            preparedStatement.setInt(3, tradingDeal.getMinimumDamage());

            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        }catch(SQLException e){
            throw new SQLException("Error at insert Statement", e);
        }
    }

    public boolean dealExists(String cardId) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from tradingdeals where cardtotrade = ?
                """)){
            preparedStatement.setString(1, cardId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        }catch (SQLException e){
            throw new SQLException("Error at delete Statement", e);
        }
    }

    public boolean deleteDeal(String id) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                delete from tradingdeals where id = ?
                """)){
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            return true;

        }catch (SQLException e){
            throw new SQLException("Error at delete Statement", e);
        }
    }

    public boolean makeDeal(String id, String tradedCard, String username) throws SQLException {
        //ID -> DealID tradedCard -> The card being offered username -> username offering to buy card
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from tradingdeals where id = ?""")){
            preparedStatement.setString(1, id);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            TradingDeal tradingDeal =new TradingDeal();
            if(resultSet.next()){
                tradingDeal = new TradingDeal(resultSet.getString("id"), resultSet.getString("cardtotrade"), resultSet.getInt("minimumdamage"));
            }else{
                return false;
            }
            PreparedStatement preparedStatement2 = this.unitOfWork.prepareStatement("""
                    select damage from cards where id = ?""");
                preparedStatement2.setString(1, tradedCard);
                preparedStatement2.executeQuery();
                resultSet = preparedStatement2.getResultSet();
                int damage = 0;
                if(resultSet.next()){
                    damage = resultSet.getInt("damage");
                }else{
                    return false;
                }

            PreparedStatement preparedStatement6 = this.unitOfWork.prepareStatement("""
                    select owner from usercards where card = ( select cardtotrade from tradingdeals where id = ? )""");
                preparedStatement6.setString(1, id); //TODO WHAT IS ID
                preparedStatement6.executeQuery();
                resultSet = preparedStatement6.getResultSet();
                while(resultSet.next()){
                    if(resultSet.getString("owner").equals(username)){
                        return false;
                    }
                }

            if(tradingDeal.getMinimumDamage() <= damage){

                PreparedStatement preparedStatement4 = this.unitOfWork.prepareStatement("""
                        delete from tradingdeals where id = ?""");
                preparedStatement4.setString(1, id);
                preparedStatement4.executeUpdate();

                PreparedStatement preparedStatement5 = this.unitOfWork.prepareStatement("""
                        update usercards set owner = ( select owner from usercards where card = ? ) where card = ? """);
                preparedStatement5.setString(1, tradingDeal.getCardToTrade());
                preparedStatement5.setString(2, tradedCard);
                preparedStatement5.executeUpdate();

                PreparedStatement preparedStatement3 = this.unitOfWork.prepareStatement("""
                        update usercards set owner = ? where card = ?""");
                preparedStatement3.setString(1, username);
                preparedStatement3.setString(2, tradingDeal.getCardToTrade());
                preparedStatement3.executeUpdate();

                unitOfWork.commitTransaction();
                return true;
            }else{
                return false;
            }
        }
    }

    public ArrayList<TradingDeal> getTrades() throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from tradingdeals""")){
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<TradingDeal> tradingDeals = new ArrayList<>();
            while(resultSet.next()){
                tradingDeals.add(new TradingDeal(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3)));
            }
            return tradingDeals;
        }catch (SQLException e){
            throw new SQLException("Error at getTrades", e);
        }
    }

}
