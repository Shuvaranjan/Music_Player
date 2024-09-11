import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Hashtable;

public class MusicPlayerGUI extends JFrame implements ActionListener {
    private JPanel panel, playbackbuttons;
    private JLabel songTitle, songArtistName;
    private JSlider slider;
    // private JSlider soundSlider;
    private JToolBar toolBar;
    private JMenuBar menuBar;
    private JMenu songMenu, playlistMenu, aboutMenu;
    private JMenuItem loadSong, createPlaylist, loadPlaylist, about;

    private MusicPlayer musicPlayer;
    private JFileChooser fileChooser;

    public MusicPlayerGUI() {
        super("Music Player");
        setSize(500, 650);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 83, 87));

        musicPlayer = new MusicPlayer(this);
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("Player/src/Audio"));

        // add filter file chooser to only see .mp3 files
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
        addComponents();
    }

    private void addComponents() {
        panel = new JPanel();
        panel.setBounds(110, 100, 280, 200);
        panel.setBackground(new Color(0, 0, 0, 100));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 55), 2));
        add(panel);

        playbackbuttons = new JPanel();
        playbackbuttons.setBounds(0, 450, 500, 80);
        playbackbuttons.setBackground(new Color(24, 24, 24));
        playbackbuttons.setLayout(null);
        add(playbackbuttons);

        ImageIcon musicIcon = new ImageIcon(ClassLoader.getSystemResource("Images/music-logo.png"));
        this.setIconImage(musicIcon.getImage());

        ImageIcon image1 = new ImageIcon(ClassLoader.getSystemResource("Images/cd.png"));
        Image image = image1.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon img = new ImageIcon(image);
        JLabel pic = new JLabel(img);
        pic.setBounds(335, 20, 250, 250);
        panel.add(pic);

        songTitle = new JLabel("<html><body><u>Song Name</u></body></html>");
        songTitle.setBounds(0, 300, getWidth() - 10, 50);
        songTitle.setHorizontalAlignment(JLabel.CENTER);
        songTitle.setForeground(Color.WHITE);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        add(songTitle);

        songArtistName = new JLabel("Artist");
        songArtistName.setBounds(0, 345, getWidth(), 20);
        songArtistName.setHorizontalAlignment(JLabel.CENTER);
        songArtistName.setForeground(Color.WHITE);
        songArtistName.setFont(new Font("Dialog", Font.PLAIN, 17));
        add(songArtistName);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.setBounds(85, 375, 330, 40);
        slider.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider) e.getSource();

                int frame = source.getValue();

                musicPlayer.setCurrentFrame(frame);
                musicPlayer.setCurrentTimeInMilli(
                        (int) (frame / (1.30 * musicPlayer.getCurrentSong().getFrameRatePerMilliseconds())));

                // resume the song
                musicPlayer.playCurrentSong();

                enablePauseButtonDisablePlayButton();
            }

        });
        slider.setBackground(null);
        slider.setFocusable(false);

        add(slider);

        addPlayBackbuttons();
        toolbar();

    }

    private void addPlayBackbuttons() {
        ImageIcon i = new ImageIcon(ClassLoader.getSystemResource("Images/preveious-button.png"));
        Image i2 = i.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JButton previous = new JButton(i3);
        previous.setBounds(120, 20, 40, 40);
        previous.setBackground(null);
        previous.setFocusable(false);
        previous.setBorderPainted(false);
        previous.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // go to the previous song
                musicPlayer.prevSong();
            }

        });
        previous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playbackbuttons.add(previous);

        ImageIcon pi = new ImageIcon(ClassLoader.getSystemResource("Images/pause.png"));
        Image xi = pi.getImage().getScaledInstance(38, 38, Image.SCALE_DEFAULT);
        ImageIcon zi = new ImageIcon(xi);
        JButton pause = new JButton(zi);
        pause.setBounds(228, 20, 40, 40);
        pause.setBackground(null);
        pause.setVisible(false);
        pause.setFocusable(false);
        pause.setBorderPainted(false);
        pause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enablePlayButtonDisablePauseButton();
                musicPlayer.pauseSong();
            }

        });
        pause.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playbackbuttons.add(pause);

        ImageIcon pai = new ImageIcon(ClassLoader.getSystemResource("Images/play-button.png"));
        Image xai = pai.getImage().getScaledInstance(38, 38, Image.SCALE_DEFAULT);
        ImageIcon zai = new ImageIcon(xai);
        JButton play = new JButton(zai);
        play.setBounds(228, 20, 40, 40);
        play.setBackground(null);
        play.setFocusable(false);
        play.setBorderPainted(false);
        play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                enablePauseButtonDisablePlayButton();

                musicPlayer.playCurrentSong();
            }

        });
        playbackbuttons.add(play);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Images/next-button.png"));
        Image i4 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i5 = new ImageIcon(i4);
        JButton next = new JButton(i5);
        next.setBounds(335, 20, 40, 40);
        next.setBackground(null);
        next.setFocusable(false);
        next.setBorderPainted(false);
        next.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // go to the next song
                musicPlayer.nextSong();
            }

        });
        next.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playbackbuttons.add(next);

    }

    private void toolbar() {
        // Create toolbar
        toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 25);
        toolBar.setFloatable(false);
        toolBar.setBorderPainted(false);
        toolBar.setBackground(new Color(0, 61, 63));
        add(toolBar);

        // create menubar
        menuBar = new JMenuBar();
        // menu.setBackground(new Color(0, 61, 63));
        menuBar.setBackground(null);
        menuBar.setBorderPainted(false);
        toolBar.add(menuBar);

        // add songMenu
        songMenu = new JMenu("Song");
        songMenu.setForeground(Color.WHITE);
        songMenu.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        // menu.setBorder(BorderFactory.createMatteBorder(0,0,2,0,Color.WHITE));
        songMenu.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                songMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                songMenu.setBorder(null);

            }

        });
        menuBar.add(songMenu);

        // add drop-down load Song
        loadSong = new JMenuItem("Load Song");
        loadSong.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)));
        loadSong.setFocusable(false);
        loadSong.setBackground(null);
        loadSong.addActionListener(this);
        songMenu.add(loadSong);

        // add playlistMenu
        playlistMenu = new JMenu("Playlist");
        playlistMenu.setForeground(Color.WHITE);
        playlistMenu.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        playlistMenu.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                playlistMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                playlistMenu.setBorder(null);

            }

        });
        menuBar.add(playlistMenu);

        // add drop-down createPlaylist
        createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(this);
        createPlaylist.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)));
        playlistMenu.add(createPlaylist);

        // add drop-down load Playlist
        loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)));
        loadPlaylist.addActionListener(this);
        playlistMenu.add(loadPlaylist);

        aboutMenu = new JMenu("About");
        aboutMenu.setForeground(Color.WHITE);
        aboutMenu.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        aboutMenu.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                aboutMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                aboutMenu.setBorder(null);

            }

        });
        menuBar.add(aboutMenu);

        about = new JMenuItem("about");
        about.addActionListener(this);
        aboutMenu.add(about);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == about) {
            new About(MusicPlayerGUI.this).setVisible(true);
        } else if (e.getSource() == loadSong) {
            int result = fileChooser.showOpenDialog(MusicPlayerGUI.this);
            File selectedFile = fileChooser.getSelectedFile();
            if (result == fileChooser.APPROVE_OPTION && selectedFile != null) {
                // create a song obj based on selected file
                Song song = new Song(selectedFile.getPath());

                // load song in music player
                musicPlayer.loadSong(song);

                // update song Title and Artist
                updateSongTitleAndArtist(song);

                // enable pause button and disable play button
                enablePauseButtonDisablePlayButton();

                // update playback slider
                updatePlaybackSlider(song);
            }

        } else if (e.getSource() == createPlaylist) {
            // load music playlist dialog
            new MusicPlaylistDialog(MusicPlayerGUI.this).setVisible(true);

        } else if (e.getSource() == loadPlaylist) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Playlist", "txt"));
            fileChooser.setCurrentDirectory(new File("Player/src/Audio"));

            int result = fileChooser.showOpenDialog(MusicPlayerGUI.this);
            File selectedFile = fileChooser.getSelectedFile();

            if (result == fileChooser.APPROVE_OPTION && selectedFile != null) {
                // stop the music
                musicPlayer.stopSong();

                // load playlist
                musicPlayer.loadPlaylist(selectedFile);
            }

        }
    }

    // this wil be used to update our slider from the music player class
    public void setPlaybackSliderValue(int frame) {
        slider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song) {
        songTitle.setText(song.getSongTitle());
        songArtistName.setText(song.getSongArtist());

    }

    public void updatePlaybackSlider(Song song) {
        // update max count for slider
        slider.setMaximum(song.getMp3File().getFrameCount());
        // create the song length label
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        // beginning will be 00:00
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Agency FB", Font.BOLD, 18));
        labelBeginning.setForeground(Color.WHITE);

        // end will be depending on the song
        JLabel labelEnd = new JLabel(song.getSongLength() + " ");
        labelEnd.setFont(new Font("Agency FB", Font.BOLD, 18));
        labelEnd.setForeground(Color.WHITE);

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

    }

    public void enablePauseButtonDisablePlayButton() {
        JButton play = (JButton) playbackbuttons.getComponent(1);
        JButton pause = (JButton) playbackbuttons.getComponent(1);

        // turn off playButton
        play.setVisible(false);
        play.setEnabled(false);

        // turn on pauseButton
        pause.setVisible(true);
        pause.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton() {
        JButton play = (JButton) playbackbuttons.getComponent(1);
        JButton pause = (JButton) playbackbuttons.getComponent(1);

        // turn on playButton
        play.setVisible(true);
        play.setEnabled(true);

        // turn off pauseButton
        pause.setVisible(false);
        pause.setEnabled(false);
    }

}
