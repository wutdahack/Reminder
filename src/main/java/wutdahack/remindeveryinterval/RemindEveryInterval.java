package wutdahack.remindeveryinterval;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class RemindEveryInterval {

    public static void main(String[] args) {
        JFrame window = new JFrame();

        JLabel notifTextLabel = new JLabel("notification text");
        notifTextLabel.setSize(300, 50);
        notifTextLabel.setLocation(0, 0);
        window.getContentPane().add(notifTextLabel);

        JLabel timerTextLabel = new JLabel("timer delay in minutes");
        timerTextLabel.setSize(300, 50);
        timerTextLabel.setLocation(300, 0);
        window.getContentPane().add(timerTextLabel);

        JTextField notifTextField = new JTextField();
        notifTextField.setSize(300, 100);
        notifTextField.setLocation(0, 50);
        window.getContentPane().add(notifTextField);

        JTextField timerTimeTextField = new JTextField();
        timerTimeTextField.setSize(300, 100);
        timerTimeTextField.setLocation(300, 50);
        window.getContentPane().add(timerTimeTextField);

        JButton button = new JButton("create timer!");
        button.addActionListener(actionEvent -> {createTimer((Integer.parseInt(timerTimeTextField.getText()) * 60000), notifTextField.getText()).start(); button.setEnabled(false);});
        button.setSize(300, 100);
        button.setLocation(600, 50);
        window.getContentPane().add(button);

        Image img = Toolkit.getDefaultToolkit().createImage(RemindEveryInterval.class.getResource("/Timer.png"));
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setSize(300, 300);
        window.getContentPane().add(imgLabel);

        window.setTitle("a helpful reminder");
        window.setIconImage(img);
        window.setVisible(true);
        window.setSize(1280, 720);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static void displayNotif(String notifText) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image iconImg = Toolkit.getDefaultToolkit().createImage(RemindEveryInterval.class.getResource("/Timer.png"));
        TrayIcon icon = new TrayIcon(iconImg);
        icon.setImageAutoSize(true);
        tray.add(icon);
        icon.displayMessage("a helpful reminder", notifText, TrayIcon.MessageType.INFO);
    }

    private static void playNotifSound() throws URISyntaxException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(RemindEveryInterval.class.getResource("/ui 3 sound effect.wav").toURI()));
        Clip audioClip = AudioSystem.getClip();
        audioClip.open(inputStream);
        FloatControl volume = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(-15.0F);
        audioClip.start();
    }

    private static Timer createTimer(int timeInMinutes, String notifText) {

        Timer timer = new Timer(timeInMinutes,
                actionEvent -> {
                    try {
                        playNotifSound();
                        displayNotif(notifText);
                    } catch (AWTException | UnsupportedAudioFileException | IOException | LineUnavailableException |
                             URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        return timer;
    }


}
