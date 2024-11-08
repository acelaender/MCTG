package dal.repositories;

import dal.UnitOfWork;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    private UnitOfWork unitOfWork;;

    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public boolean registerUser(User user) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users
                where username = ?
            """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();
            while (resultSet.next()){
                User userInRow = new User(
                        resultSet.getString(2),
                        resultSet.getString(3)
                        );
                userRows.add(userInRow);
            }
            if(userRows.isEmpty()){
                try(PreparedStatement preparedStatement2 = this.unitOfWork.prepareStatement("""
                    //TODO SQLCODE
                """)){
                    //TODO Execute statement and insert parameters
                } catch (SQLException e){
                    throw new SQLException("Error  at insert Statement", e);
                    return false;
                }
            }
        } catch (SQLException b) {
            throw new SQLException("Error at select Statement", b);
            return false;
        }
        return true;
    }

    public boolean login(User user) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();
            while (resultSet.next()){
                User userInRow = new User(
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                userRows.add(userInRow);
            }

            if(userRows.get(0).getPassword().equals(user.getPassword())){
                return true;
            }else return false;

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
            return false;
        }
    }

    public User getUserCredentials(User user){
        return null;
    }

    public User getUserStats(User user){
        return null;
    }

    public boolean alterUser(User user){
        return false;
    }

}
