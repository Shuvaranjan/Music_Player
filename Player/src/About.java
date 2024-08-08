import java.awt.Color;

import javax.swing.JDialog;

public class About extends JDialog {
   public MusicPlayerGUI musicPlayerGUI;
    public About(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
        setSize(350,450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(musicPlayerGUI);
        setModal(true);
        getContentPane().setBackground(new Color(0, 145, 153));
    }

    
}
