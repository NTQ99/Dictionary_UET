package vnu.edu.vn;

import java.sql.*;

public class MySQLConnUtils {

    // Kết nối vào MySQL.
    public static Connection getMySQLConnection() throws SQLException,
            ClassNotFoundException {
        String hostName = "localhost";

        String dbName = "edictdata";
        String userName = "root";
        String password = "123456";

        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName,
                                                String userName, String password) throws SQLException, ClassNotFoundException {
        //nguồn : VietTuts
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?verifyServerCertificate=false&useSSL=true";

        Connection conn = DriverManager.getConnection(connectionURL, userName, password);
        return conn;
    }

    // Thêm từ vào sql
    public static void insertToSql(String TargetWord, String ExplainWord) throws SQLException, ClassNotFoundException {
        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnection();

        // Tạo đối tượng prepareStatement.
        PreparedStatement preStmt = connection.prepareStatement("Insert into tbl_edict(word, detail) values (?,?)");
        preStmt.setString(1, TargetWord);
        preStmt.setString(2, ExplainWord);

        // Thực thi câu lệnh.
        // execute() sử dụng cho các loại lệnh Insert,Update,Delete.
        preStmt.execute();
    }

    // Sửa từ trong sql
    public static void updateFromSql(String TargetWord, String ExplainWord) throws SQLException, ClassNotFoundException {
        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnection();

        // Tạo đối tượng prepareStatement.
        PreparedStatement preStmt = connection.prepareStatement("update tbl_edict set detail = ? where word = ?");
        preStmt.setString(1, ExplainWord);
        preStmt.setString(2, TargetWord);

        // Thực thi câu lệnh.
        // execute() sử dụng cho các loại lệnh Insert,Update,Delete.
        preStmt.execute();
    }

    // Xóa từ trong sql
    public static void deleteFromSql(String TargetWord) throws SQLException, ClassNotFoundException {
        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnection();

        // Tạo đối tượng prepareStatement.
        PreparedStatement preStmt = connection.prepareStatement("delete from tbl_edict where word = ?");
        preStmt.setString(1, TargetWord);

        // Thực thi câu lệnh.
        // execute() sử dụng cho các loại lệnh Insert,Update,Delete.
        preStmt.execute();
    }
}