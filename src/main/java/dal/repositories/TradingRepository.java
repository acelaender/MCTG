package dal.repositories;

import dal.UnitOfWork;
import models.Card;
import models.TradingDeal;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TradingRepository {

    //TODO create 3 functions in tradings

    private UnitOfWork unitOfWork;

    public TradingRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void createDeal(TradingDeal tradingDeal) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                insert into tradingdeals (id, cardtotrade, type, minimumdamage) values (?, ?, ?, ?)
                """)){
            preparedStatement.setString(1, tradingDeal.getId());
            preparedStatement.setString(2, tradingDeal.getCardToTrade());
            preparedStatement.setString(3, tradingDeal.getType());
            preparedStatement.setInt(4, tradingDeal.getMinimumDamage());

            preparedStatement.executeQuery();
        }catch(SQLException e){
            throw new SQLException("Error at insert Statement", e);
        }
    }

    public void deleteDeal(int id) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                delete from tradingdeals where id = ?
                """)){
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

        }catch (SQLException e){
            throw new SQLException("Error at delete Statement", e);
        }
    }

    public boolean makeDeal(Card card){
        return false;
    }


}
