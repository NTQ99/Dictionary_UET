package vnu.edu.vn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    /*
    * @Tạo map word chứa dữ liệu từ điển
    * @Tạo Map hint chứa từ gợi ý
    * @với mỗi key tạo ra một list từ gợi ý*/
    static Map<String, String> mapWord = new HashMap<>();

    static String searchWord;

    static String ExplainOfSearchWord;

    static Map<String, List<String>> mapHint = new HashMap<>();

    static final String tipSearch = "Từ bạn cần tìm chưa được cập nhật hoặc không tồn tại. Ấn 'Search' để dịch với Google Translate.";

}
