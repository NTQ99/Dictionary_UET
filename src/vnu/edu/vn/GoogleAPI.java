package vnu.edu.vn;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.darkprograms.speech.translator.GoogleTranslate;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;


public class GoogleAPI {
    public static String GoogleTrans(String text) throws IOException {
            return GoogleTranslate.translate("vi", text);
    }
    public static void VoiceSpeak(String text) {
        SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

        //tạo ra một thread bởi vì JLayer đang chạy trên thread hiện tại và sẽ làm ứng dụng lag
        Thread thread = new Thread(() -> {
            try {

                //tạo ra đối tượng JLayer instance
                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
                player.play();

            } catch (IOException | JavaLayerException e) {
                System.out.println(e.getMessage()); //in ra exception s
            }
        });


        thread.setDaemon(false);
        //bắt đầu Thread
        thread.start();
    }
}
