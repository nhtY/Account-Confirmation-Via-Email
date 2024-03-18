package com.nihat.loginregister;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootApplication
public class LoginRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginRegisterApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(DataSource dataSource) {
		return args -> {
			System.out.println("Hello World");

			Connection conn = null;
			try (Connection connection = dataSource.getConnection()){
				conn = connection;
				System.out.println("Connected to the database");

				DatabaseMetaData metaData = connection.getMetaData();
				System.out.println("Connected to: " + metaData.getDatabaseProductName());
				System.out.println("Driver: " + metaData.getDriverName());

			} catch (Exception e) {
				System.out.println("Error: ");
				System.out.println(e);
			} finally {
				System.out.println("Closing connection");
				if (conn != null) {
					conn.close();
				}
			}
		};
	}
}
