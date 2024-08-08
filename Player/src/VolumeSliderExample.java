import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VolumeSliderExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Volume Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // min, max, initial value
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int volume = volumeSlider.getValue();
                System.out.println("Current volume: " + volume);
                // Here you would update your audio player's volume
            }
        });

        frame.add(volumeSlider);
        frame.pack();
        frame.setVisible(true);
    }
}