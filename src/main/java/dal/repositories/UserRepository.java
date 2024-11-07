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

    public registerUser(User user){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                select * from users
                where username = ?
            """)){
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();
            while (resultSet.next()){
                User user = new User(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        );
                userRows.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("Error at select Statement");
        }
    }
}
