/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.sysman.qatool.uifwk.utils.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

/**
 * @author cawei
 */
public class DatabaseUtil
{
	private static final Logger LOGGER = LogManager.getLogger(DatabaseUtil.class);
	private static String CLOUD1_HOSTNAME = Utils.getProperty("ODS_HOSTNAME");

	private static String CLOUD1_PORT = Utils.getProperty("ODS_PORT");

	private static String CLOUD1_SERVICE = Utils.getProperty("ODS_SID");
	private static String CLOUD1_USERNAME = "SYSEMS_T_1004";
	private static String CLOUD1_PWD = Utils.getProperty("ODS_SYSTEM_PWD");

	private static String CLOUD2_HOSTNAME = DatabaseUtil.propsReader("ODS_HOSTNAME");

	private static String CLOUD2_PORT = DatabaseUtil.propsReader("ODS_PORT");

	private static String CLOUD2_SERVICE = DatabaseUtil.propsReader("ODS_SID");

	private static String CLOUD2_USERNAME = "SYSEMS_T_1004";

	private static String CLOUD2_PWD = DatabaseUtil.propsReader("ODS_SYSTEM_PWD");

	public static void Cloud1DeleteData()
	{
		Connection conn = DatabaseUtil.ConnectCloud1();//this c is null

		PreparedStatement pstmt = null;
		String sql = "delete from ems_dashboard where dashboard_Id>4000000;delete from ems_zdt_comparator;delete from ems_zdt_sync;";
		System.out.println(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

			pstmt.close();

			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static void Cloud1ExecuteSQL(String filename) throws SQLException
	{
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(new File(filename));

			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			String[] inst = sb.toString().split(";");

			Connection c = DatabaseUtil.ConnectCloud1();
			Statement st = c.createStatement();

			for (int i = 0; i < inst.length; i++) {
				if (!inst[i].trim().equals("")) {
					st.executeUpdate(inst[i]);
					System.out.println(">>" + inst[i]);
				}
			}

		}
		catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
		}

	}

	public static void Cloud1InsertData()
	{
		Connection conn = DatabaseUtil.ConnectCloud1();//this c is null

		PreparedStatement pstmt = null;
		String sql = "insert into ems_dashboard values(?,'Test dashboard',0,CONCAT('Test dashboard description',?),'18-JUL-17 05.09.38.583000000 AM','18-JUL-17 05.09.38.583000000 AM','Oracle','Oracle',0,3,1,'screenshot',0,?,1,0,1,0,null,1)";
		System.out.println(sql);
		try {
			for (int i = 0; i < 100; i++) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, 5000000 + i);
				pstmt.setLong(2, 5000000 + i);
				pstmt.setLong(3, 673850132);
				int j = pstmt.executeUpdate();
				System.out.println("The number of iteration"+i);
				Assert.assertNotEquals(j, 0);
				pstmt.close();
			}
			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static void Cloud2DeleteData()
	{
		Connection conn = DatabaseUtil.ConnectCloud1();//this c is null

		PreparedStatement pstmt = null;
		String sql = "delete from ems_dashboard where dashboard_Id>4000000;delete from ems_zdt_comparator;delete from ems_zdt_sync;";
		System.out.println(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

			pstmt.close();

			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static void Cloud2ExecuteSQL(String filename) throws SQLException
	{
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(new File(filename));

			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			String[] inst = sb.toString().split(";");

			Connection c = DatabaseUtil.ConnectCloud2();
			Statement st = c.createStatement();

			for (int i = 0; i < inst.length; i++) {
				if (!inst[i].trim().equals("")) {
					st.executeUpdate(inst[i]);
					System.out.println(">>" + inst[i]);
				}
			}

		}
		catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
		}

	}

	public static void Cloud2InsertData()
	{
		Connection conn = DatabaseUtil.ConnectCloud1();//this c is null

		PreparedStatement pstmt = null;
		String sql = "insert into ems_dashboard values(?,'Test dashboard',0,CONCAT('Test dashboard description',?),'18-JUL-17 05.09.38.583000000 AM','18-JUL-17 05.09.38.583000000 AM','Oracle','Oracle',0,3,1,'screenshot',0,?,1,0,1,0,null,1)";
		System.out.println(sql);
		try {
			for (int i = 0; i < 100; i++) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, 5000000 + i);
				pstmt.setLong(2, 5000000 + i);
				pstmt.setLong(3, 673850132);
				int j = pstmt.executeUpdate();

				Assert.assertNotEquals(j, 0);
				pstmt.close();
			}
			conn.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static Connection ConnectCloud1()
	{
		Connection connection = DatabaseUtil.establishConnection(CLOUD1_HOSTNAME, CLOUD1_PORT, CLOUD1_SERVICE, CLOUD1_USERNAME,
				CLOUD1_PWD);

		return connection;

	}

	public static Connection ConnectCloud2()
	{
		Connection connection = DatabaseUtil.establishConnection(CLOUD2_HOSTNAME, CLOUD2_PORT, CLOUD2_SERVICE, CLOUD2_USERNAME,
				CLOUD2_PWD);

		return connection;

	}

	public static Connection establishConnection(String HOSTNAME, String PORT, String SERVICE, String USERNAME, String PASSWORD)
	{

		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		}
		catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();

		}

		System.out.println("Oracle JDBC Driver Registered!");

		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT + ":" + SERVICE, USERNAME,
					PASSWORD);

		}
		catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		}
		else {
			System.out.println("Failed to make connection!");
		}
		return connection;
	}

	public static String propsReader(String PROPERTY)
	{
		String T_WORK = System.getenv("T_WORK");
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(T_WORK + "/emaas.properties.log");
		}
		catch (FileNotFoundException e) {
			System.out.println("Failed to find emaas.properties.log!");
			e.printStackTrace();
		}

		try {
			prop.load(input);
		}
		catch (IOException e) {
			System.out.println("Failed to load emaas.properties.log!");
			e.printStackTrace();
		}

		return prop.getProperty("PROPERTY");
	}

}
