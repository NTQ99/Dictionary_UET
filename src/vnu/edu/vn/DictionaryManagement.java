package vnu.edu.vn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import static vnu.edu.vn.Dictionary.*;

public class DictionaryManagement {
//hàm thêm từ từ file
    public void insertFromFile(){
        BufferedReader br = null;
        File file = new File("data/data.txt");

        try {
            br = new BufferedReader(new FileReader(file));

            String Line, Target, Explain;
            br.skip(1);
            while ((Line = br.readLine()) != null) {
                // cắt dòng thành 2 chuỗi bằng dấu tab
                String[] word = Line.split("\t");
                Target = word[0].trim();
                Explain = word[1].trim();
                mapWord.put(Target, Explain);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//hàm thêm từ từ csdl
    public void insertFromMySQL() throws ClassNotFoundException, SQLException {
        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnection();

        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();

        String sql = "Select idx, word, detail from tbl_edict";

        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        ResultSet rs = statement.executeQuery(sql);

        // Duyệt trên kết quả trả về.
        while (rs.next()) {// Di chuyển con trỏ xuống bản ghi kế tiếp.
            String Target = rs.getString(2);
            String Explain = rs.getString(3);

            mapWord.put(Target.replace("<C><F><I><N><Q>", "").replace("</Q></N></I></F></C>", ""),
                    Explain.replace("<C><F><I><N><Q>", "").replace("</Q></N></I></F></C>", ""));
        }
        // Đóng kết nối
        connection.close();
    }

    public void dictionarySearcher(){
        Set<String> set = mapWord.keySet();

        for(String TargetWord : set)
            for (int l = 0; l <= TargetWord.length(); l ++) {
                String s = TargetWord.substring(0, l);
                if (!mapHint.containsKey(s)) mapHint.put(s, new ArrayList<>());
                mapHint.get(s).add(TargetWord);
            }
    }

    //Ham tra tu trong tu dien
    public void dictionaryLookup() {
        if (mapWord.get(searchWord) != null) {
            ExplainOfSearchWord = mapWord.get(searchWord);
        }
    }

    // Thêm từ vào mảng
    public void insertWord(String TargetWord, String ExplainWord) {
        try {
            MySQLConnUtils.insertToSql(TargetWord, ExplainWord);
            mapWord.put(TargetWord, ExplainWord);

            for (int l = 0; l <= TargetWord.length(); l ++) {
                String s = TargetWord.substring(0, l);
                if (!mapHint.containsKey(s)) mapHint.put(s, new ArrayList<>());
                mapHint.get(s).add(TargetWord);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Sửa từ trong mảng
    public void updateWord(String TargetWord, String ExplainWord) {
        try {
            MySQLConnUtils.updateFromSql(TargetWord, ExplainWord);

            mapWord.remove(TargetWord);
            mapWord.put(TargetWord, ExplainWord);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Xóa từ trong mảng
    public void deleteWord(String TargetWord) {
        try {
            MySQLConnUtils.deleteFromSql(TargetWord);
            mapWord.remove(TargetWord);
            if (mapHint.get(TargetWord).size() == 1) mapHint.remove(TargetWord);

            Set<String> set = mapHint.keySet();
            for (String s: set)
                if (mapHint.get(s).contains(TargetWord)) mapHint.get(s).remove(TargetWord);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
