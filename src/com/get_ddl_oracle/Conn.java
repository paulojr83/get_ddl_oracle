package com.get_ddl_oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
	public static Connection getConnection() throws SQLException {
		try {
			Class.forName( "oracle.jdbc.driver.OracleDriver" );

			return DriverManager.getConnection(
					"jdbc:oracle:thin:@10.0.0.1:1521:ora0", "user",
					"password" );
		} catch( ClassNotFoundException e ) {
			throw new SQLException( e.getMessage() );
		}

	}
}
