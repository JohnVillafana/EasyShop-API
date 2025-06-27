package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();

        String sql = """
            SELECT sc.product_id, sc.quantity, p.* 
            FROM shopping_cart sc
            JOIN products p ON sc.product_id = p.product_id
            WHERE sc.user_id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapRowToProduct(resultSet);
                    int quantity = resultSet.getInt("quantity");

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);

                    shoppingCart.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting shopping cart for user: " + userId, e);
        }

        return shoppingCart;
    }

    @Override
    public void addProductToCart(int userId, int productId) {
        // First check if product already exists in cart
        String checkSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {

            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, productId);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Product exists, update quantity
                    int currentQuantity = resultSet.getInt("quantity");
                    updateProductInCart(userId, productId, currentQuantity + 1);
                } else {
                    // Product doesn't exist, insert new
                    String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        insertStatement.setInt(1, userId);
                        insertStatement.setInt(2, productId);
                        insertStatement.setInt(3, 1);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding product to cart", e);
        }
    }

    @Override
    public void updateProductInCart(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            removeProductFromCart(userId, productId);
            return;
        }

        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product in cart", e);
        }
    }

    @Override
    public void removeProductFromCart(int userId, int productId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing product from cart", e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    // Helper method to map ResultSet to Product
    private Product mapRowToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setProductId(resultSet.getInt("product_id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setCategoryId(resultSet.getInt("category_id"));
        product.setDescription(resultSet.getString("description"));
        product.setColor(resultSet.getString("color"));
        product.setStock(resultSet.getInt("stock"));
        product.setFeatured(resultSet.getBoolean("featured"));
        product.setImageUrl(resultSet.getString("image_url"));
        return product;
    }
}