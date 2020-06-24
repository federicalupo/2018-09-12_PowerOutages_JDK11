package it.polito.tdp.poweroutages.db;

import java.sql.Connection;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = DBConnect.getConnection();
			connection.close();
			System.out.println("Test PASSED");

		} catch (Exception e) {
			System.err.println("Test FAILED");
		}

		PowerOutagesDAO dao = new PowerOutagesDAO();

		//System.out.println(dao.loadAllNercs());
	}

}
