package dal.repositories;

import dal.UnitOfWork;
import models.Card;

public class TradingRepository {

    //TODO create 3 functions in tradings

    private UnitOfWork unitOfWork;

    public TradingRepository(UnitOfWork unitOfWork) {}

    public boolean createDeal(Card card){
        //INSERT INTO tradings
        return false;
    }

    public boolean deleteDeal(int oid){
        //DELETE FROM tradings WHERE oid == oid
        return false;
    }

    public boolean makeDeal(Card card){
        return false;
    }


}
