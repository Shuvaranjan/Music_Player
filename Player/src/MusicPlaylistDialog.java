import java.awt.Color;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicPlaylistDialog extends JDialog{
    public MusicPlayerGUI musicPlayerGUI;

    private ArrayList<String> songPaths;

    public MusicPlaylistDialog(MusicPlayerGUI musicPlayerGUI){

        songPaths = new ArrayList<>();
        this.musicPlayerGUI = musicPlayerGUI;
        setTitle("Create Playlist");
        setSize(400,400);
        setResizable(false);
        getContentPane().setBackground(new Color(0, 83, 87));
        setLocationRelativeTo(musicPlayerGUI);
        setLayout(null);
        setModal(true);

        addComponents();
    }
    private void addComponents() {
       JPanel songContainer = new JPanel();
       songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
       songContainer.setBackground(Color.LIGHT_GRAY);
       songContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
       songContainer.setBounds((int) (getWidth() * 0.025), 10, (int) (getWidth() * 0.90), (int) (getWidth() * 0.75));
       add(songContainer);


    //    add song button
    JButton addSongButton = new JButton("Add");
    addSongButton.setBounds(60, (int) (getHeight() * 0.80), 100, 25);
    addSongButton.setFont(new Font("Dialog", Font.BOLD, 14));
    addSongButton.setFocusable(false);
    addSongButton.setBackground(new Color(208, 0, 0));
    addSongButton.setForeground(Color.WHITE);
    addSongButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    addSongButton.setBorder(BorderFactory.createLoweredSoftBevelBorder());
    addSongButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        //    open File Explorer
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
      fileChooser.setCurrentDirectory(new File("Player/src/Audio"));
      int result = fileChooser.showOpenDialog(MusicPlaylistDialog.this);

      File seletedFile  = fileChooser.getSelectedFile();
      if (result == fileChooser.APPROVE_OPTION && seletedFile != null) {
        JLabel filePathLabel = new JLabel(seletedFile.getPath());
        filePathLabel.setFont(new Font("Arial", Font.BOLD, 12));
        filePathLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(0,0,0,150)));
        songPaths.add(filePathLabel.getText());

        // add the container
        songContainer.add(filePathLabel);

        songContainer.revalidate();
      }
      
            
        }
        
    });
    add(addSongButton);

    //    save Playlist button
    JButton savePlaylistButton = new JButton("Save");
    savePlaylistButton.setBounds(215, (int) (getHeight() * 0.80), 100, 25);
    savePlaylistButton.setFont(new Font("Dialog", Font.BOLD, 14));
    savePlaylistButton.setFocusable(false);
    savePlaylistButton.setBackground(new Color(47, 208, 0));
    savePlaylistButton.setForeground(Color.WHITE);
    savePlaylistButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    savePlaylistButton.setBorder(BorderFactory.createLoweredSoftBevelBorder());
    savePlaylistButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
         try {
            JFileChooser fileChooser =new JFileChooser();
            fileChooser.setCurrentDirectory(new File("Player/src/Audio"));
   
            int result = fileChooser.showSaveDialog(MusicPlaylistDialog.this);
   
            if (result == fileChooser.APPROVE_OPTION) {
               File selectedFile = fileChooser.getSelectedFile();
   
               if (!selectedFile.getName().substring(selectedFile.getName().length()-4).equalsIgnoreCase(".txt")) {
                   selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                   
               }
               selectedFile.createNewFile();
   
               FileWriter fileWriter = new FileWriter(selectedFile);
               BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);
               for (String songPath : songPaths) {
                   bufferedWriter.write(songPath + "\n");
                }
            bufferedWriter.close();
            JOptionPane.showMessageDialog(MusicPlaylistDialog.this,"SuccessFully Created Your Playlist.");
            MusicPlaylistDialog.this.dispose();
            }
         } catch (Exception ea) {
           ea.printStackTrace();
         }
        }
    
        
    });
    add(savePlaylistButton);
    }

}
