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
                SELECT * FROM users WHERE username = ?
            """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                try(PreparedStatement preparedStatement2 = this.unitOfWork.prepareStatement("""
                    INSERT INTO users (username, password, elo, wins, losses, coins, admin)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """)){
                    preparedStatement2.setString(1, user.getUsername());  //USERNAME
                    preparedStatement2.setString(2, user.getPassword());  //PASSWORD
                    //TODO: Standard ELO---------------------------
                    preparedStatement2.setInt(3, 100);                 //ELO
                    //-------------------------------------
                    preparedStatement2.setInt(4, 0);                   //WINS
                    preparedStatement2.setInt(5, 0);                   //LOSSES
                    preparedStatement2.setInt(6, 20);                  //COINS
                    preparedStatement2.setInt(7, 0);                   //ADMIN

                    preparedStatement2.executeUpdate();

                    this.unitOfWork.commitTransaction();


                } catch (SQLException e){
                    throw new SQLException("Error  at insert Statement", e);
                }
            }else{
                return false;
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
                        resultSet.getString(1),
                        resultSet.getString(2)
                );
                userRows.add(userInRow);
            }

            //PASSWORD COMPARISON:
            //TODO: hash pw and compare it

            if(userRows.get(0).getPassword().equals(user.getPassword()) && userRows.size() == 1){
                return true;
            }else return false;



        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public UserData getUserCredentials(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new UserData(
                    resultSet.getString(1),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public UserStats getUserStats(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users where username = ?
                """)){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new UserStats(
                    resultSet.getString(1),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(5)
            );

        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public boolean setUserData(UserData userData) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set bio = ?, image = ? where username = ?""")){
            preparedStatement.setString(1, userData.getBio());
            preparedStatement.setString(2, userData.getImage());
            preparedStatement.setString(3, userData.getUsername());

            preparedStatement.executeUpdate();
            this.unitOfWork.commitTransaction();
            return true;
        }catch (SQLException e){
            throw new SQLException("Error at update UserData Statement", e);
        }
    }

    public ArrayList<UserStats> getScoreBoard() throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users order by elo desc
                """)){
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<UserStats> userStatsRows = new ArrayList<>();

            while (resultSet.next()){
                UserStats statsInRow = new UserStats(
                        resultSet.getString(1),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getInt(5)
                );
                userStatsRows.add(statsInRow);
            }
            return userStatsRows;
        }catch(SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public boolean subractCoins(String username) throws SQLException{
        try(PreparedStatement preparedStatement1 = this.unitOfWork.prepareStatement("""
                select coins from users where username = ?""")){
            preparedStatement1.setString(1, username);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt(1) < 5){
                    return false;
                }
            }
        }catch (SQLException e){
            throw new SQLException("Error at select coins Statement", e);
        }
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set coins = coins - 5 where username = ? """)){
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e){
            throw new SQLException("Error at update Statement", e);
        }
    }

    public boolean isAdmin(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select admin from users where username = ?""")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next() && resultSet.getInt("admin") == 1){
                return true;
            }else return false;
        }catch (SQLException e){
            throw new SQLException("Error at select Statement", e);
        }
    }

    public void loose(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set elo = elo - 5, losses = losses - 1 where username = ?""")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            this.unitOfWork.commitTransaction();
        }catch (SQLException e){
            throw new SQLException("Error at update loss Statement", e);
        }
    }

    public void win(String username) throws SQLException{
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                update users set elo = elo + 3, wins = wins + 1 where username = ?""")) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            this.unitOfWork.commitTransaction();
        }catch (SQLException e){
            throw new SQLException("Error at update loss Statement", e);
        }
    }

}
