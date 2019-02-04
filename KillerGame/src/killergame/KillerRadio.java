/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killergame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import static javafx.scene.media.MediaPlayer.INDEFINITE;

public class KillerRadio implements Runnable {
    private static MediaPlayer player; 

    public static MediaPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MediaPlayer player) {
        KillerRadio.player = player;
    }
    
    public static void pause() {
        KillerRadio.player.pause();
    }
    
    public static void restart() {
        KillerRadio.player.play();
    }

    public static void playAudio(String name) {
        Media media = new Media(Paths.get(name).toUri().toString());
        KillerRadio.player = new MediaPlayer(media);
        player.setAutoPlay(true);
        player.setCycleCount(INDEFINITE);

        player.play();
    }

    @Override
    public void run() {
        KillerRadio.player.getOnError().run();
    }
}
