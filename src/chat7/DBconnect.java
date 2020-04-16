package chat7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBconnect {

	static Connection con;
	static PreparedStatement psmt;
	
	public DBconnect() {
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin://@localhost:1521:orcl", "kosmo", "1234");
			System.out.println("오라클 DB 연결성공");
		} catch (ClassNotFoundException e) {
			System.out.println("오라클 드라이버 로딩 실패");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB 연결 실패");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("알 수 없는 예외발생");
		}
	}
	

	public static void execute(String name, String msg) {
		try {
			String query = "INSERT into chating_tb VALUES"
					+ " (seq_chat_num.nextval,?,?,to_char(sysdate, 'yyyy-mm-dd HH:MI:SS'))";
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, name);
			psmt.setString(2, msg);
			psmt.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("DB 연결 실패");
			e.printStackTrace();
		} 
		catch (Exception e) {
			System.out.println("알 수 없는 예외발생");
		}
	}
		
}
