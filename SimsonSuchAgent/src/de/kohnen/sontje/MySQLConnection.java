/**
 * 
 */
package de.kohnen.sontje;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author dirk
 *
 */
public class MySQLConnection {

	private static Connection conn = null;
	private static String dbHost = "kohnen.selfip.com";
	private static String dbPort = "3306";
	private static String database = "sontje_dev";
	private static String dbUser = "root";
	private static String dbPassword = "i4HgAZ3rbT7n8ir7fGkXvwEVZ";

	private MySQLConnection() {
		try {
			// Datenbanktreiber für ODBC Schnittstellen laden.
			// Für verschiedene ODBC-Datenbanken muss dieser Treiber
			// nur einmal geladen werden.
			Class.forName("com.mysql.jdbc.Driver");
		 
			// Verbindung zur ODBC-Datenbank 'sakila' herstellen.
			// Es wird die JDBC-ODBC-Brücke verwendet.
			conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
					+ dbPort + "/" + database + "?" + "user=" + dbUser + "&"
					+ "password=" + dbPassword);
		} catch (ClassNotFoundException e) {
			System.out.println("Treiber nicht gefunden");
		} catch (SQLException e) {
			System.out.println("Connect nicht moeglich");
		}
	}
		 
	private static Connection getInstance(){
		if(conn == null)
			new MySQLConnection();
		return conn;
	}
	
	public static Vector<Shop> getShops(){
		conn = getInstance();
		Shop shop = null;
		Vector<Shop> v = new Vector<Shop>();
		
		if(conn != null){
			Statement query;
			try {
				query = conn.createStatement();
	 
				String sql = "SELECT * FROM Shop";
				ResultSet result = query.executeQuery(sql);
	 
				while (result.next()) {
					shop = new Shop(result);
					v.add(shop);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return v;
	}
	
	public static boolean isFirstQuery(int id){
		conn = getInstance();
		boolean fq = true;
		if(conn != null){
			Statement query;
			try {
				query = conn.createStatement();
	 
				String sql = "SELECT COUNT(*) AS count FROM ShopArtikelZuordnung WHERE shopID = " + id + ";";
				ResultSet result = query.executeQuery(sql);
				result.next();
				if (result.getInt("count") > 0){
					fq = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fq;
		
	}
	 
	public static HashMap<String, Integer> getArtikelHashMap(){
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		conn = getInstance();

		if(conn != null){
			Statement query;
			try {
				query = conn.createStatement();
	 
				String sql = "SELECT ID,ean FROM Artikel";
				ResultSet result = query.executeQuery(sql);
	 
				while (result.next()) {
					hm.put(result.getString("ean"), result.getInt("ID"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return hm;
	}
	
	public static void updateArtikel(){
		
	}
	
	public static void insertArtikel(Artikel a){
		conn = getInstance();
		Timestamp current = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

		if(conn != null){
			Statement query;
			int result;
			try {
				query = conn.createStatement();
				String sql = String.format("INSERT INTO `Artikel`(`ean`, `artikelNr`, `hersteller`, `herstellerNr`, `simsonNr`, `titel`, `image`, "
						+ "`beschreibung`, `timeCreated`, `timeModified`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')", a.getEan(),
						a.getArtikelNr(), a.getHersteller(), a.getHerstellerNr(), a.getSimsonNr(), a.getTitel(), a.getImageUrl(), a.getBeschreibung(), current,current);
				result = query.executeUpdate(sql);
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
			}
		}
	}
}
