package game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class Audio extends Thread{
    /*the audio class is necessary as a thread because there should only be one instance of the
      audio manager class.*/

  private boolean stopped = false;
  private boolean repeater;
  private static int CASE_START_CLIP = 0;
  private int choice;
  private File F; //audioFile F
  private AudioInputStream AST;
  private AudioFormat AF;
  private SourceDataLine SDL;
  private boolean done;

     public Audio(File F, boolean loop){
        
         this.F = F;
         this.repeater = loop;
         this.initGameTune();
         this.start();
     }


     @Override
     public void run(){
         
        this.done = false;
        int TClength = 0;
        int total = 0;
        int bytesRead = 0;
        byte[] audioBuffer = new byte[100];
       try{TClength = AST.available();}
       catch(IOException IOE1){System.out.println(IOE1.toString());}
        this.SDL.start();


       while((total < TClength) && !stopped){
            
           /*the next block of code reads data into the audioBuffer array.
            audioBuffer is a byteArray and the read function returns the number of bytes
            read. Finally we write to the SourceDataLine in order to replenish the buffer
            */

           try{bytesRead = AST.read(audioBuffer, 0, 100);}
           catch(IOException IOE2){System.out.println(IOE2.toString());}

           total += bytesRead; //keeps a record of how much data has been read from audio
          // System.out.println("bytesread: "+bytesRead+" total: "+total);
           SDL.write(audioBuffer, 0, bytesRead); //replenishes the buffer with new data.
          
           if(this.repeater == true){
           if(total >= TClength){
              SDL.drain();
              SDL.stop();
              SDL.close();
              initGameTune();
              SDL.start();
              total=0;
          }
       }
       }
        //AudioManager.removeRunning(this);
        this.done = true;
    }
     public void initGameTune(){

        //1)try to get an audio stream from the selected file.
       try{ AST = AudioSystem.getAudioInputStream(F);}
       catch(UnsupportedAudioFileException UAFE){System.out.println(UAFE.toString());}
       catch(IOException IOE){System.out.println(IOE.toString());}

       //2)get audioFormat from audio file stream.
       AF = AST.getFormat();

       //3)try to create a sourcedataline from the audio format.
       try{SDL = AudioSystem.getSourceDataLine(AF);}
       catch(LineUnavailableException LUE){System.out.println(LUE.toString());}

       /*4) reserve the sourcedataline above... just because you got a line from the
        code in 3) above, doesnt stop another program from reserving the data after
        you got it. to prevent this from happening, we need to exclusively reserve it
        this is why it is necessary to open the line with the code below*/

       /*to open the line, we need to use the open method in the dataline object.
       there is an open method which uses the audioFormat variable. so the method below
        is (SourceDataLine).(AudioFormat) -> 3).open(2))*/
      try{SDL.open(AF);}
      catch(LineUnavailableException LUE2){System.out.println(LUE2.toString()); }
    }
     public void stopPlay(){
         this.stopped = true;
     }

     public boolean getAudioStatus(){
         return this.done;
     }

    }




public class AudioManager{


    private Audio A;
    private File F;
    private static HashMap AudioSet = new  HashMap<String, File>(); // this is a collection of audio files (wav)
    private static HashMap RunningTunes = new  HashMap<String,Audio>(); //this is a collection of running tunes.
    

    public AudioManager(File F, String name){
        AudioSet.put(name,F);
    }
    static void removeRunning(Audio A){
         RunningTunes.remove(A);
    }
    public boolean Insert(File F, String name){
        if(F.equals((File)AudioSet.get(name))){
            return false;
        }
        else{
            AudioSet.put(name, F);
            return true;
        }
    }
   


    public void stopAudio(String name){
        Audio temp = (Audio)RunningTunes.get(name);
        temp.stopPlay();
    }

    public Audio playAudio(String name, boolean loop){
        File F = (File)AudioSet.get(name);
        Audio A = new Audio(F, loop);

         if(RunningTunes.get(name) == null){ //search for the audio in list, insert if not there
           RunningTunes.put(name, A);
         }
         else{
            RunningTunes.remove(name);//remove from the list of running tunes(may be old)
            RunningTunes.put(name, A);//places a new running audio tune in the running table.
         }
        return A;
    }
        
    }


