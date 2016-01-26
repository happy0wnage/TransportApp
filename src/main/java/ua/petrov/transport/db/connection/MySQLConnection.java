package ua.petrov.transport.db.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySQLConnection.class);

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public static Connection getWebInstance() {
		Connection con = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/transport_system");
			con = ds.getConnection();
		} catch (SQLException | NamingException e) {
			LOGGER.error(e.getMessage());
		}
		return con;
	}
}
