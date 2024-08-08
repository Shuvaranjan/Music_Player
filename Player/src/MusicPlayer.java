import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayer extends PlaybackListener {
  // this will be used to update isPaused more synchronized
  private static final Object playSignal = new Object();
  private MusicPlayerGUI musicPlayerGUI;
  // we will need a way to store our song's details, so we will be creating a song
  // class
  private Song currentSong;

  public Song getCurrentSong() {
    return currentSong;
  }

  private ArrayList<Song> playlist;

  private int currentPlaylistIndex;
  private AdvancedPlayer advancedPlayer;
  private boolean isPaused;
  // boolean value is tell me the song is finished
  private boolean songFinished;

  private boolean pressedNext, pressedPrevious;
  // store in ten last frame when the playback is finished (used for pausing and
  // resuming)
  private int currentFrame;

  public void setCurrentFrame(int frame) {
    currentFrame = frame;
  }

  private int currentTimeInMilli;

  public void setCurrentTimeInMilli(int timeInMilli) {
    currentTimeInMilli = timeInMilli;
  }

  // create constructor
  public MusicPlayer(MusicPlayerGUI musicPlayerGUI) {
    this.musicPlayerGUI = musicPlayerGUI;

  }

  public void loadSong(Song song) {
    currentSong = song;
    playlist = null;

      // stop the song if possible
      if (!songFinished) 
      stopSong();

      // play the current song if not null
    if (currentSong != null) {
      
      // reset frame
      currentFrame = 0;

      // reset currentTimeInMilli
      currentTimeInMilli = 0;

      // update GUI
      musicPlayerGUI.setPlaybackSliderValue(0);
      
      playCurrentSong();

    }
  }

  public void loadPlaylist(File playlistFile) {
    playlist = new ArrayList<>();

    try {
      FileReader fileReader = new FileReader(playlistFile);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      String songPath;
      while ((songPath = bufferedReader.readLine()) != null) {
        Song song = new Song(songPath);
        playlist.add(song);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (playlist.size() > 0) {
      // reset playback slider
      musicPlayerGUI.setPlaybackSliderValue(0);
      currentTimeInMilli = 0;

      currentSong = playlist.get(0);

      currentFrame = 0;

      // update GUI
      musicPlayerGUI.enablePauseButtonDisablePlayButton();
      musicPlayerGUI.updateSongTitleAndArtist(currentSong);
      musicPlayerGUI.updatePlaybackSlider(currentSong);

      // start song
      playCurrentSong();

    }
  }

  public void pauseSong() {
    if (advancedPlayer != null) {
      isPaused = true;
      stopSong();

    }
  }

  public void stopSong() {
    if (advancedPlayer != null) {
      advancedPlayer.stop();
      advancedPlayer.close();
      advancedPlayer = null;

    }
  }

  public void nextSong() {

    if (playlist == null)return;
    

    if (currentPlaylistIndex + 1 > playlist.size() - 1)return;

    pressedNext = true;

    if (!songFinished) 
    stopSong();  
    // increase current playlist Index
    currentPlaylistIndex++;

    // update current Song
    currentSong = playlist.get(currentPlaylistIndex);

    // reset frame
    currentFrame = 0;

    // reset currentTimeInMilli
    currentTimeInMilli = 0;

    // update GUI
    musicPlayerGUI.enablePauseButtonDisablePlayButton();
    musicPlayerGUI.updateSongTitleAndArtist(currentSong);
    musicPlayerGUI.updatePlaybackSlider(currentSong);

    // play the song
    playCurrentSong();

  }

  public void prevSong() {
    // no need to go to the next song  if there is no playlist
    if (playlist == null)return;

    // check to see if we can go to the previous song
    if (currentPlaylistIndex - 1 < 0)return;

    pressedPrevious = true;
      // stop the song if possible
      if (!songFinished) 
      stopSong();

    // decrease current playlist Index
    currentPlaylistIndex--;

    // update current Song
    currentSong = playlist.get(currentPlaylistIndex);

    // reset frame
    currentFrame = 0;

    // reset currentTimeInMilli
    currentTimeInMilli = 0;

    // update GUI
    musicPlayerGUI.enablePauseButtonDisablePlayButton();
    musicPlayerGUI.updateSongTitleAndArtist(currentSong);
    musicPlayerGUI.updatePlaybackSlider(currentSong);

    // play the song
    playCurrentSong();

  }

  public void playCurrentSong() {
    if (currentSong == null) {
      JOptionPane.showMessageDialog(null, "Please Select the song before playing!");
      return;
    }
    try {
      FileInputStream fileInputStream = new FileInputStream(currentSong.getFilePath());
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
      advancedPlayer = new AdvancedPlayer(bufferedInputStream);
      advancedPlayer.setPlayBackListener(this);
      // play music
      startMusicThread();

      // start playback slider thread
      startPlaybackSliderThread();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void startMusicThread() {
    new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          if (isPaused) {
            synchronized (playSignal) {
              // update flag
              isPaused = false;
              playSignal.notify();
            }

            // resume music player last frame
            advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
          } else {
            // play music for the beginning
            advancedPlayer.play();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }).start();
  }

  // create a thread that will handle updating the slider
  private void startPlaybackSliderThread() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        if (isPaused) {
          try {
            synchronized (playSignal) {
              playSignal.wait();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }

        }

        // System.out.println("isPaused: " + isPaused);
        while (!isPaused && !pressedNext && !pressedPrevious) {
          try {
            // increment current time in milliseconds
            currentTimeInMilli++;

            // calculate into frame vale
            int calclutedFrame = (int) ((double) currentTimeInMilli * 1.30 * currentSong.getFrameRatePerMilliseconds());

            // update GUI
            musicPlayerGUI.setPlaybackSliderValue(calclutedFrame);
            Thread.sleep(1);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        

      }
    }).start();
  }

  @Override
  public void playbackStarted(PlaybackEvent evt) {
    // this method gets called when the song finished or if the player gets closed
    System.out.println("Playback Started");
    songFinished = false;
    pressedNext = false;
    pressedPrevious = false;
  }

  @Override
  public void playbackFinished(PlaybackEvent evt) {
    // this method gets called in the beginning of the song
    // musicPlayerGUI.enablePlayButtonDisablePauseButton();
    System.out.println("Playback Finished");

    if (isPaused) {
      currentFrame += (int) ((double) evt.getFrame() * currentSong.getFrameRatePerMilliseconds());
      // System.out.println("Stopped: " + currentFrame);

    } else {

      // if the user pressed next or prev we don't need to execute the rest of the code
      if (pressedNext || pressedPrevious) 
      return;

      // when the song ends
      songFinished = true;

      if (playlist == null) {

        // update GUI
        musicPlayerGUI.enablePlayButtonDisablePauseButton();

      } else {
        // last song in the playlist
        if (currentPlaylistIndex == playlist.size() - 1) {
          // update GUI
          musicPlayerGUI.enablePlayButtonDisablePauseButton();

        } else {
          // go to the next song in the playlist
          nextSong();
        }
      }
    }

  }

}
