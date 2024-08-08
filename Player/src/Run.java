import javax.swing.SwingUtilities;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new MusicPlayerGUI().setVisible(true);
                // Song song = new Song("Player\\src\\Audio\\bollywood_Mohabbatein 2000 - Aankhein Khuli.mp3");
                // System.out.println(song.getSongTitle());
                // System.out.println(song.getSongArtist());
                // new About(null).setVisible(true);
            }
        });
    }
    
}
