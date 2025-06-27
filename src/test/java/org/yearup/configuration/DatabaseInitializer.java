package org.yearup.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryDao categoryDao) {
        return args -> {
            // Check if categories already exist
            if (categoryDao.getAllCategories().isEmpty()) {
                // Add 15 categories
                categoryDao.create(new Category(0, "Electronics", "Electronics items"));
                categoryDao.create(new Category(0, "Clothing", "Clothing and apparel"));
                categoryDao.create(new Category(0, "Books", "Books and magazines"));
                categoryDao.create(new Category(0, "Sports", "Sports equipment"));
                categoryDao.create(new Category(0, "Home & Garden", "Home and garden items"));
                categoryDao.create(new Category(0, "Toys", "Toys and games"));
                categoryDao.create(new Category(0, "Beauty", "Beauty products"));
                categoryDao.create(new Category(0, "Automotive", "Car accessories"));
                categoryDao.create(new Category(0, "Food & Grocery", "Food items"));
                categoryDao.create(new Category(0, "Health", "Health products"));
                categoryDao.create(new Category(0, "Music", "Music instruments"));
                categoryDao.create(new Category(0, "Movies", "Movies and entertainment"));
                categoryDao.create(new Category(0, "Games", "Video games"));
                categoryDao.create(new Category(0, "Baby", "Baby products"));
                categoryDao.create(new Category(0, "Office", "Office supplies"));

                System.out.println("Database initialized with 15 categories");
            }
        };
    }
}