package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {

    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, profile.getUserId());
            statement.setString(2, profile.getFirstName());
            statement.setString(3, profile.getLastName());
            statement.setString(4, profile.getPhone());
            statement.setString(5, profile.getEmail());
            statement.setString(6, profile.getAddress());
            statement.setString(7, profile.getCity());
            statement.setString(8, profile.getState());
            statement.setString(9, profile.getZip());

            statement.executeUpdate();

            // Return the created profile
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating profile", e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting profile for user: " + userId, e);
        }

        return null;
    }

    @Override
    public void update(int userId, Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, phone = ?, email = ?, " +
                "address = ?, city = ?, state = ?, zip = ? WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getEmail());
            statement.setString(5, profile.getAddress());
            statement.setString(6, profile.getCity());
            statement.setString(7, profile.getState());
            statement.setString(8, profile.getZip());
            statement.setInt(9, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile", e);
        }
    }

    private Profile mapRow(ResultSet resultSet) throws SQLException {
        Profile profile = new Profile();
        profile.setUserId(resultSet.getInt("user_id"));
        profile.setFirstName(resultSet.getString("first_name"));
        profile.setLastName(resultSet.getString("last_name"));
        profile.setPhone(resultSet.getString("phone"));
        profile.setEmail(resultSet.getString("email"));
        profile.setAddress(resultSet.getString("address"));
        profile.setCity(resultSet.getString("city"));
        profile.setState(resultSet.getString("state"));
        profile.setZip(resultSet.getString("zip"));
        return profile;
    }
}
