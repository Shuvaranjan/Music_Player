import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public class About extends JDialog {
   public MusicPlayerGUI musicPlayerGUI;
    public About(MusicPlayerGUI musicPlayerGUI){
        setTitle("About");
        this.musicPlayerGUI = musicPlayerGUI;
        setSize(350,450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(musicPlayerGUI);
        setModal(true);
        getContentPane().setBackground(new Color(0, 145, 153));

        addComponents();
    }

    private void addComponents(){
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Images/picture.png"));
        Image i2 = i1.getImage().getScaledInstance(70,100,Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel photo = new JLabel(i3);
        photo.setBounds(250,20,70,100);
        // photo.setHorizontalAlignment(JLabel.RIGHT);
        photo.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(255,255,255,50),new Color(255,255,255,80)));
        
        add(photo);
    }
    
}
