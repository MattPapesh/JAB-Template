package app.audio;

import java.util.LinkedList;
import javax.sound.sampled.Clip;

/**
 * AppAudio is responsible for playing audio clips of WAV audio files with the use of the Clip & AudioFile classes. 
 * Using AppAudio, WAV files can be played once, played multiple times consecutively, and stopped at any moment. 
 */
public class AppAudio 
{
    private LinkedList<AudioFile> audio_files = new LinkedList<AudioFile>();
    private static AppAudio app_audio = new AppAudio();

    @Override
    protected void finalize() throws Throwable  {
        audio_files.clear();
    }

    /**
     * Used to begin playing the WAV file passed in until the audio file is either stopped or finished playing. 
     * @param file_name (String) : The specified WAV file to play. 
     */
    public void playAudioFile(String file_name) {
        audio_files.addLast(new AudioFile(file_name)); 
        audio_files.getLast().getAudioClip().start();
    }

    /**
     * Used to begin playing the WAV file passed in indefinitely until the file is manually stopped with the use of the "stop" methods. 
     * @param file_name (String) : The specified WAV file to indefinitely play. 
     */
    public void playAudioFileLoopContinuously(String file_name) {
        if(!containsAudioFile(file_name)) {
            audio_files.addLast(new AudioFile(file_name));
            audio_files.getLast().getAudioClip().loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Used to play a WAV file passed in for a set period of times. Moreover, the audio file will play until it has been played 
     * the designated number of times, or until it is manually stopped with the use of the "stop" methods. 
     * 
     * @param file_name (String) : The specified WAV file to play. 
     * @param count (int) : The specified unsigned amount of times the WAV file should be consecutively played. 
     */
    public void playAudioFileLoop(String file_name, int count) {
        audio_files.addLast(new AudioFile(file_name));
        audio_files.getLast().getAudioClip().loop(Math.max(count, 0));
    }

    /**
     * Used to immediately stop a WAV file from playing while canceling any replays the file was expected to play. Lastly, 
     * if the WAV file passed in is not currently playing, then nothing will happen.  
     * 
     * @param file_name (String) : The specified WAV file to stop playing. 
     */
    public void stopAudioFile(String file_name) {
        for(var audio_file : audio_files) {
            if(audio_file.getFileName() == file_name) {
                audio_file.getAudioClip().stop();
                audio_file.getAudioClip().close();
                audio_files.remove(audio_file);
            }
        }
    }

    /**
     * Used to immediately stop all WAV files from playing while canceling all replays that any of the files were expected to play. Lastly, 
     * if any WAV files passed in are not currently playing, then nothing will happen to those specific files.  
     */
    public void stopAllAudioFiles() {
        for(var audio_file : audio_files) {
            audio_file.getAudioClip().stop();
            audio_file.getAudioClip().close();
        }

        audio_files.clear();
    }

    /**
     * Used to determine if a specific WAV file is currently playing. 
     * @param file_name (String) : The specified WAV file in question.
     * @return Whether or not the WAV file in question is currently playing. 
     */
    private boolean containsAudioFile(String file_name) {
        for(var audio_file : audio_files) {
            if(audio_file.getFileName() == file_name) {
                return true;
            }
        }

        return false;
    }

    public static AppAudio getInstance() {
        return app_audio;
    }
}