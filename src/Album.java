import java.io.File;
import java.util.ArrayList;

public class Album 
{
    private ArrayList<Song> album;
    private int albumid;
    private String name;
    private String date;
    private File file;
    private String artist;
    
    public Album(int albumtid ,String name, String date,File file,String artist){
        album = new ArrayList();
        this.album = album;
        this.name = name;
        this.date = date;
        this.file = file;
        this.artist = artist;
    }
    
    public void cleararray()
    {
        album = new ArrayList();
    }
    
    public int getid()
    {
        return this.albumid;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void changeName(String name){
        this.name = name;
    }
    
    public String getdate()
    {
        return this.date;
    }
    
    public void addSong(Song song){
        this.album.add(song);
    }
    
    public ArrayList<Song> getSongs(){
        return this.album;
    }
    
    public File getFile(){
        return this.file;
    }
    
    public void setFile(File file){
        this.file = file;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    
}
