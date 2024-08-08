// this class is ude to describe a song

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.mpatric.mp3agic.Mp3File;

public class Song {
private String songTitle;
private String songArtist;
private String songLength;
private String filePath;
private Mp3File mp3File;
private double frameRatePerMilliseconds;

// create a parametarized constructor
public Song(String filePath){
    this.filePath = filePath;
    try {
        mp3File = new Mp3File(filePath);
        frameRatePerMilliseconds = (double) mp3File.getFrameCount() / mp3File.getLengthInMilliseconds();
        songLength = convertToSongLengthFormat();
        // use the Jaudiotagger library to create on audiofile object to read Mp3 files information
        AudioFile audioFile = AudioFileIO.read(new File(filePath));

        Tag tag = audioFile.getTag();
        if (tag != null) {
            songTitle = tag.getFirst(FieldKey.TITLE);
            songArtist = tag.getFirst(FieldKey.ARTIST);
            
        }else{

            // could't read through mp3 files meta data
            songTitle = "unknown";
            songArtist = "unknown";
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

}

private String convertToSongLengthFormat(){
    long minutes = mp3File.getLengthInSeconds() / 60;
    long seconds = mp3File.getLengthInSeconds() % 60;
    String formattedTime = String.format("%02d:%02d", minutes, seconds);
    return formattedTime;
}

// generate getters
public String getSongTitle() {
    return songTitle;
}

public String getSongArtist() {
    return songArtist;
}

public String getSongLength() {
    return songLength;
}

public String getFilePath() {
    return filePath;
}

public Mp3File getMp3File(){
    return mp3File;
}
 
public double getFrameRatePerMilliseconds(){
    return frameRatePerMilliseconds;
}



}
