package dbTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PointCharge {
	// Oracle 데이터베이스 연결 정보
	private static final String DB_URL = "jdbc:oracle:thin:@192.168.0.33:1521:XE"; // Oracle 서버 주소와 포트
	private static final String USER = "c##salmon"; // 데이터베이스 사용자 이름
	private static final String PASSWORD = "1234"; // 데이터베이스 비밀번호
	private static final Logger logger = Logger.getLogger(Login.class.getName());
	
	public int chargePoint(int chargeAmount) {
        int currentPoint = getPointFromDatabase(); // 현재 포인트 조회
        if (currentPoint == -1) {
            return -1; // 데이터베이스 오류 발생 시
        }
        int newPoint = currentPoint + chargeAmount; // 충전 후 포인트
        if (updatePointInDatabase(newPoint)) {
            return newPoint; // 충전 성공 시 새로운 포인트 반환
        } else {
            return -1; // 데이터베이스 오류 발생 시
        }
    }

	public int getPointFromDatabase() {
		Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int point = -1;
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String query = "SELECT points FROM users WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            // 사용자명과 비밀번호에 따라 포인트를 가져옴
            statement.setString(1, "username");
            statement.setString(2, "password");
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                point = resultSet.getInt("points");
                System.out.println("현재 잔액 : "+point);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "포인트 조회 중 오류 발생", e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        
		return point;
	}
	
	private boolean updatePointInDatabase(int newPoint) {
		Connection connection = null;
        PreparedStatement statement = null;
        boolean success = false;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            String query = "UPDATE users SET points = ? WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(query);
            // 사용자명과 비밀번호에 따라 포인트 업데이트
            statement.setString(1, "username");
            statement.setString(2, "password");
            statement.setInt(3, newPoint);
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "포인트 업데이트 중 오류 발생", e);
        } finally {
            closeResources(null, statement, connection);
        }
		return success;
	}

	private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
		try {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "리소스 해제 중 오류 발생", e);
        }
		
	}
	
	
}