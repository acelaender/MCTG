package dal.repositories;

import dal.UnitOfWork;
import models.User;
import models.UserData;
import models.UserStats;

import java.sql.Blob;
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
            int oid = getOid();
            //

            if(userRows.isEmpty()){
                try(PreparedStatement preparedStatement2 = this.unitOfWork.prepareStatement("""
                    insert into users (int id, string username, string password, int elo, int wins, int losses, int coins)
                    values (?, ?, ?, ?, ?, ?, ?)
                """)){
                    preparedStatement2.setInt(1, oid);
                    preparedStatement2.setString(2, user.getUsername());
                    preparedStatement2.setString(3, user.getPassword());
                    //TODO: Standard ELO---------------------------
                    preparedStatement2.setInt(4, 100);
                    //-------------------------------------
                    preparedStatement2.setInt(5, 0);
                    preparedStatement2.setInt(6, 0);
                    preparedStatement2.setInt(7, 20);

                    resultSet = preparedStatement2.executeQuery();

                } catch (SQLException e){
                    throw new SQLException("Error  at insert Statement", e);
                }
            }
        } catch (SQLException b) {
            throw new SQLException("Error at select Statement", b);
        }
        return true;
    }

    public boolean login(User user) throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<User> userRows = new ArrayList<>();
            while (resultSet.next()){
                User userInRow = new User(
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                userRows.add(userInRow);
            }

            //PASSWORD COMPARISON:
            //TODO: hash pw and compare it

            if(userRows.get(0).getPassword().equals(user.getPassword())){
                return true;
            }else return false;



        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public UserData getUserCredentials(User user) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new UserData(
                    resultSet.getString(2),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public UserStats getUserStats(User user) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new UserStats(
                    resultSet.getString(2),
                    resultSet.getInt(7),
                    resultSet.getInt(8),
                    resultSet.getInt(6)
            );

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public boolean alterUser(User user){
        return false;
    }

    private int getOid() throws SQLException {
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select oid from users order by oid desc
                """)){
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int oid = resultSet.getInt(1);

            return oid;
        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

}
