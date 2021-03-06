package me.hexiaranks.utils;

import java.sql.SQLException;
import java.sql.Statement;


public class MySqlUtils {

	private Statement statement;
	private String database;
	
	public MySqlUtils(Statement statement, String database) {
		this.statement = statement;
		this.database = database;
	}
	
	/**
	 * 
	 * @param uuid player uuid
	 * @param key (name, rank, prestige, master, path)
	 * @param value (new string value)
	 */
	public void set(String uuid, String key, String value) {
		try {
			statement.executeUpdate("UPDATE " + database + " set " + key + " = '" + value + "' where uuid='" + uuid + "';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
