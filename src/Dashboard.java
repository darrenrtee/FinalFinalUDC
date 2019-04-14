import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends javax.swing.JFrame {

    private Connection con;
    private User user;
    private Playlist currplaylist;
    private Album curralbum;
    private ArrayList<Playlist> yourplaylist;
    private ArrayList<Album> albums;
    private String artist;
    private ArrayList<Song> yoursongs;
    
    public Dashboard() {
        initComponents();
        aligntable();
        clearall();
        icons();
        currplaylist = null;
        yourplaylist = new ArrayList<>();
        yoursongs = new ArrayList<>();
    }

    public ArrayList<Song> getYoursongs() {
        return yoursongs;
    }

    public void setYoursongs(ArrayList<Song> yoursongs) {
        this.yoursongs = yoursongs;
    }
    
    public void initializePlayer(){
        if(getuser().gettype().equals("Artist")){
            try {
                initializeArtist();
                loadplaylists();
                loadplaylisttable();
                loadalbums();
                loaduploadedsongs();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(getuser().gettype().equals("Registered")){
            initializeUser();
            loadplaylists();
            loadplaylisttable();
        }
        else if(getuser().gettype().equals("Guest")){
            initializeUser();
        }
    }
    
    public void initializeArtist(){
        albums = new ArrayList<>();
        
    }
    public void handleSearchResult(String name,String criteria){
        switch(criteria){
            case "Artist" :
                break;
            case "Listener" :
               break;
            case "Playlist" :
               break;
            case "Album" :
               break;
             case "Song" :
               break;
        }
    }
    public void initializeUser(){
        albumtextlabel.setVisible(false);
        jScrollPane2.setVisible(false);
        albumT.setVisible(false);
        createalbumBtn.setVisible(false);
        jScrollPane4.setVisible(false);
        albumsongT.setVisible(false);
        songtextLabel.setVisible(false);
        addsongtoplay.setVisible(false);
    }
    
    public void loaduploadedsongs(){
        try {
            Statement statement = getcon().createStatement();
            ResultSet result= statement.executeQuery("SELECT * FROM databasedc.song WHERE UserID = '"+getuser().getid()+"'");
          
            DefaultTableModel modelp = (DefaultTableModel) albumsongT.getModel();
            modelp.setRowCount(0);
            
            while(result.next()){
                Director build = new Director();
                if(result.getString("SongGenre").equals("Happy")){
                   build.setSongBuilder(new HappyBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername());
                }
                else if(result.getString("SongGenre").equals("Sad")){
                   build.setSongBuilder(new SadBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername());  
                }
                else if(result.getString("SongGenre").equals("Senti")){
                   build.setSongBuilder(new SentiBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername()); 
                }
                else{
                   build.setSongBuilder(new NoBuilder()); 
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername()); 
                }
                
                yoursongs.add(build.getSong());
                
                modelp.insertRow(albumsongT.getRowCount(), new Object[]{
                    result.getString("SongTitle"),
                    result.getString("SongGenre")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void icons() // NOTHING
    {
        BufferedImage imgprof = null;
        BufferedImage imglogout = null;
        
        try {
            imgprof = ImageIO.read(getClass().getResource("/Icons/viewprof.png"));
            imglogout = ImageIO.read(getClass().getResource("/Icons/logout.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        viewprofbtn.setIcon(new ImageIcon(imgprof.getScaledInstance(58, 51, Image.SCALE_SMOOTH)));
        logoutbtn.setIcon(new ImageIcon(imglogout.getScaledInstance(58, 51, Image.SCALE_SMOOTH)));
    }

    public Album getalbum() {
        return curralbum;
    }

    public void setalbum(Album curralbum) {
        this.curralbum = curralbum;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
    
    public ArrayList<Album> getAlbums() {
        return albums;
    }
    
    public void loadsongs() // DONE
    {
        getplaylist().cleararray();
        try {
            Statement state = getcon().createStatement();
            ResultSet result = state.executeQuery("SELECT * \n" +
                                                  "FROM databasedc.songdetail NATURAL JOIN databasedc.song \n" +
                                                  "WHERE PlaylistID = '"+getplaylist().getid()+"'");
            
            while(result.next())
            {
                Director build = new Director();
                if(result.getString("SongGenre").equals("Happy"))
                {
                   build.setSongBuilder(new HappyBuilder());
                   build.constructSong(result.getInt("SongID"), result.getString("AlbumName"), result.getString("SongTitle"), result.getString("SongArtist"));
                }
                else if(result.getString("SongGenre").equals("Sad"))
                {
                   build.setSongBuilder(new SadBuilder());
                   build.constructSong(result.getInt("SongID"), result.getString("AlbumName"), result.getString("SongTitle"), result.getString("SongArtist"));  
                }
                else if(result.getString("SongGenre").equals("Senti"))
                {
                   build.setSongBuilder(new SentiBuilder()); 
                   build.constructSong(result.getInt("SongID"), result.getString("AlbumName"), result.getString("SongTitle"), result.getString("SongArtist")); 
                }
                else
                {
                   build.setSongBuilder(new NoBuilder());
                   build.constructSong(result.getInt("SongID"), result.getString("AlbumName"), result.getString("SongTitle"), result.getString("SongArtist")); 
                }
                build.getSong().setTimesPlayed(result.getInt("SongPlayed"));
                
                if(result.getString("SongFav").equals("true"))
                    build.getSong().setFavorite();
                
                getplaylist().addSong(build.getSong());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void loadsongstable() // NOTHING
    {
        DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
        model.setRowCount(0);
        
        for(int x=0; x<getplaylist().getSongs().size(); x++)
        {
            if(getplaylist().getSongs().get(x).getGenre() == null){
                model.insertRow(songlistT.getRowCount(), new Object[]{
                    x+1,
                    getplaylist().getSongs().get(x).getTitle().trim(),
                    getplaylist().getSongs().get(x).getArtist().trim(),
                    "-",
                    getplaylist().getSongs().get(x).getAlbum().trim()
                });
            }
            else{
                model.insertRow(songlistT.getRowCount(), new Object[]{
                    x+1,
                    getplaylist().getSongs().get(x).getTitle().trim(),
                    getplaylist().getSongs().get(x).getArtist().trim(),
                    getplaylist().getSongs().get(x).getGenre().getGenre().trim(),
                    getplaylist().getSongs().get(x).getAlbum().trim()
                });
            }
        }
    }
    
    public void loadplaylists() // DONE
    {
        yourplaylist = new ArrayList<>();
        
        try {
            Statement state = getcon().createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM databasedc.playlistdc WHERE UserID = '"+getuser().getid()+"'");
            while(result.next())
            {
                yourplaylist.add(new Playlist(result.getInt("PlaylistID"),result.getString("PlaylistName"),result.getString("PlaylistDate")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void loadplaylisttable() // NOTHING
    {
        DefaultTableModel model = (DefaultTableModel) playlistT.getModel();
        model.setRowCount(0);
        
        for(int x=0; x<yourplaylist.size(); x++)
        {
            model.insertRow(playlistT.getRowCount(), new Object[]{
                yourplaylist.get(x).getName()
            });
        }
    }
    
    public void loadalbumtable() // DONE
    {
        DefaultTableModel model = (DefaultTableModel) albumT.getModel();
        model.setRowCount(0);
        
        Statement state;
        try {
            state = getcon().createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM databasedc.uploadalbum NATURAL JOIN databasedc.albumdc NATURAL JOIN databasedc.useraccount");
            while(result.next())
            {
                model.insertRow(albumT.getRowCount(), new Object[]{
                   result.getString("AlbumName"),
                   result.getString("UserName"),
                   result.getDate("UploadedDate")
                });
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void notifier() // DONE
    {
        try {
            Statement statenotify = getcon().createStatement();
            ResultSet rsnotify = statenotify.executeQuery("SELECT * FROM databasedc.followuser INNER JOIN databasedc.useraccount ON useraccount.UserID = FollowUserID WHERE useraccount.UserID = '"+getuser().getid()+"'");
            String display = "There's new uploads from";
            boolean exists = false;
            while(rsnotify.next())
            {
                if(rsnotify.getString("Notify").equals("true"))
                {
                    exists = true;
                    display.concat(", " + rsnotify.getString("UserName"));
                    Statement statefalse = getcon().createStatement();
                    statefalse.execute("UPDATE databasedc.followuser SET Notify = '"+"false"+"' WHERE followuser.UserID = '"+getuser().getid()+"'");
                }
            }
            if(exists)
               JOptionPane.showMessageDialog(null, display);
            
            Statement playlistNotify = getcon().createStatement();
            ResultSet rsplaylistNotify = playlistNotify.executeQuery("SELECT * FROM databasedc.followplaylist NATURAL JOIN databasedc.useraccount WHERE UserID = '"+getuser().getid()+"'");
            String display2 = "There's new playlists from";
            exists = false;
            while(rsplaylistNotify.next())
            {
                if(rsplaylistNotify.getString("Notify").equals("true"))
                {
                    exists = true;
                    display2.concat(", " + rsplaylistNotify.getString("UserName"));
                    Statement statefalse = getcon().createStatement();
                    statefalse.execute("UPDATE databasedc.followplaylist SET Notify = '"+"false"+"' WHERE followuser.UserID = '"+getuser().getid()+"'");
                }
            }
            if(exists)
               JOptionPane.showMessageDialog(null, display2);
            
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void aligntable() // NOTHING
    {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );
        playlistT.setDefaultRenderer(Object.class, centerRenderer);
        songlistT.setDefaultRenderer(Object.class, centerRenderer);
        albumT.setDefaultRenderer(Object.class, centerRenderer);
    }
    
    public void clearall() // NOTHING
    {
        DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
        playlistname.setText("");
        playlistuser.setText("");
        playlistdate.setText("");
        model.setRowCount(0);
        sortcombo.setSelectedIndex(0);
    }
    
    public void setplaylist(Playlist playlist)
    {
        this.currplaylist = playlist;
    }
    
    public Playlist getplaylist()
    {
        return this.currplaylist;
    }
    
    public void setuser(User user)
    {
        this.user = user;
    }
    
    public User getuser()
    {
        return this.user;
    }
    
    public void setcon(Connection con)
    {
        this.con = con;
    }
    
    public Connection getcon()
    {
        return this.con;
    }
    
    public void setalbumartist(String artist)
    {
        this.artist = artist;
    }
    
    public String getalbumartist()
    {
        return this.artist;
    }
    
    public void viewprofile() // NOTHING
    {
        YourProfile prof = new YourProfile(this, true);
        prof.setuser(getuser());
        prof.setcon(getcon());
        prof.setprofile();
        prof.setVisible(true);
    }
    
    public void openalbum() // DONE (NEW)
    {
        if(albumT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Select An Album", "No Selection!", JOptionPane.WARNING_MESSAGE);
        else
        {
            try {
                Statement stateopenal = getcon().createStatement();
                ResultSet resultopenal = stateopenal.executeQuery("SELECT * FROM databasedc.songalbum NATURAL JOIN databasedc.albumdc NATURAL JOIN databasedc.useraccount WHERE AlbumName = '"+albumT.getValueAt(albumT.getSelectedRow(), 0).toString()+"' AND UserName = '"+albumT.getValueAt(albumT.getSelectedRow(), 1).toString()+"'");
                ArrayList<Integer> albumsongid = new ArrayList<>();
                while(resultopenal.next())
                {
                    albumsongid.add(resultopenal.getInt("SongID"));
                }
                
                setalbumartist(albumT.getValueAt(albumT.getSelectedRow(), 1).toString());
                
                DefaultTableModel model = (DefaultTableModel) albumsongT.getModel();
                model.setRowCount(0);

                for(int x=0; x<albumsongid.size(); x++)
                {
                    Statement statedisplaysong = getcon().createStatement();
                    ResultSet resultdisplaysong = statedisplaysong.executeQuery("SELECT * FROM databasedc.song WHERE SongID = '"+albumsongid.get(x)+"'");
                    while(resultdisplaysong.next())
                    {
                        model.insertRow(albumsongT.getRowCount(), new Object[]{
                            resultdisplaysong.getString("SongTitle")
                        });
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void createplay() // DONE
    {
        if(getuser().gettype().equals("Registered") || getuser().gettype().equals("Artist"))
        {
            CreatePlay create = new CreatePlay(getcon(),getuser(),this.yourplaylist);
            yourplaylist.add(create.getplay());
            loadplaylisttable();
            
        }
        else
        {
            CreatePlay create = new CreatePlay(getcon(),getuser(),this.yourplaylist);
            try {
                if(create.isCreated())
                {
                    Statement statecheck = getcon().createStatement();
                    ResultSet resultcheck = statecheck.executeQuery("SELECT MAX(UserID) FROM databasedc.useraccount");
                    int id=-1;
                    while(resultcheck.next())
                    {
                        id = resultcheck.getInt("MAX(UserID)");
                    }

                    Statement statechecker = getcon().createStatement();
                    ResultSet resultchecker = statechecker.executeQuery("SELECT * FROM databasedc.useraccount WHERE UserID = '"+id+"'");
                    boolean signuped = false;
                    while(resultchecker.next())
                    {
                        if(resultchecker.getString("Type").equals("Registered"))
                            signuped = true;
                    }

                    if(signuped)
                    {
                        JOptionPane.showMessageDialog(null, "Please Re-Login");
                        this.dispose();
                        Login log = new Login();
                        log.setVisible(true);
                    }
                }
                yourplaylist.add(create.getplay());
                loadplaylisttable();
            } catch (SQLException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loadplay() // DONE
    {
        if(playlistT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Select A Playlist", "No Selection!", JOptionPane.WARNING_MESSAGE);
        else
        {
            LoadPlay load = new LoadPlay(getcon(),getuser(),yourplaylist);
            load.setplaylistname(playlistT.getValueAt(playlistT.getSelectedRow(), 0).toString());
            load.loadplay();
            setplaylist(load.getplay());
            playlistname.setText(getplaylist().getName());
            playlistuser.setText(getuser().getusername());
            playlistdate.setText(getplaylist().getdate());

            sortcombo.setSelectedIndex(0);
            loadsongs();
            loadsongstable();
        }  
    }
    
    public void editplay() // DONE
    {
        if(playlistT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Choose A Playlist First!");
        else
        {
            EditPlay editp = new EditPlay(this, true);
            editp.setcon(getcon());
            editp.setprevname(playlistT.getValueAt(playlistT.getSelectedRow(), 0).toString());
            editp.setarrplay(yourplaylist);
            editp.setVisible(true);
           
            if(editp.getedited())
            {
                loadplaylists();
                loadplaylisttable();
                
                if(getplaylist() != null && getplaylist().getName().equals(playlistT.getValueAt(playlistT.getSelectedRow(), 0).toString()))
                {
                    playlistname.setText(editp.getchange());
                    for(int y=0; y<yourplaylist.size(); y++)
                    {
                        if(yourplaylist.get(y).getName().equals(editp.getchange()))
                            setplaylist(yourplaylist.get(y));
                    }
                }  
            } 
        }
    }
    
    public void addsongtoplay() // DONE
    {
        if(albumsongT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Select A Song in the Library");
        else
        {
            if(getplaylist() == null)
                JOptionPane.showMessageDialog(null, "Choose A Playlist First!");
            else
            {
                try {
                    Statement state = getcon().createStatement();
                    if(songlistT.getRowCount() == 0)
                    {
                        Statement statecheck = getcon().createStatement();
                        ResultSet resultcheck = statecheck.executeQuery("SELECT * FROM databasedc.song WHERE SongTitle = '"+albumsongT.getValueAt(albumsongT.getSelectedRow(), 0).toString()+"' && SongArtist = '"+getalbumartist()+"'");
                        int songid=0;
                        while(resultcheck.next())
                        {
                            songid = resultcheck.getInt("SongID");
                        }
                        state.execute("INSERT INTO databasedc.songdetail(PlaylistID,SongID,AlbumName,Genre,SongFav,SongPlayed) VALUES('"+getplaylist().getid()+"','"+songid+"','"+""+"','"+null+"','"+"false"+"','"+0+"')");  
                        JOptionPane.showMessageDialog(null, "Upload Complete!");
                        loadsongs();
                        loadsongstable();
                    }
                    else
                    {
                        boolean exists = false;
                        for(int x=0; x<songlistT.getRowCount(); x++)
                        {
                            if((songlistT.getValueAt(x, 1).equals(albumsongT.getValueAt(albumsongT.getSelectedRow(), 0)) && songlistT.getValueAt(x, 2).equals(getalbumartist())))
                            {
                                exists = true;
                                break;
                            }
                        }
                        if(!exists)
                        {
                             Statement statecheck = getcon().createStatement();
                             ResultSet resultcheck = statecheck.executeQuery("SELECT * FROM databasedc.song WHERE SongTitle = '"+albumsongT.getValueAt(albumsongT.getSelectedRow(), 0).toString()+"' && SongArtist = '"+getalbumartist()+"'");
                             int songid=0;
                             while(resultcheck.next())
                             {
                                 songid = resultcheck.getInt("SongID");
                             }
                             state.execute("INSERT INTO databasedc.songdetail(PlaylistID,SongID,AlbumName,Genre,SongFav,SongPlayed) VALUES('"+getplaylist().getid()+"','"+songid+"','"+""+"','"+null+"','"+"false"+"','"+0+"')");  
                             JOptionPane.showMessageDialog(null, "Upload Complete!");
                             loadsongs();
                             loadsongstable();
                        }
                        else
                            JOptionPane.showMessageDialog(null, "This Song Already Been Added");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
    
    public void deletesong() // DONE
    {
        if(songlistT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Select A Song", "No Selection!", JOptionPane.WARNING_MESSAGE);
        else{
            int dialogResultGuest = JOptionPane.showConfirmDialog(null, "Are You Sure?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(dialogResultGuest == JOptionPane.YES_OPTION){
                for(int x=0; x<getplaylist().getSongs().size(); x++)
                {
                    String artistdb = getplaylist().getSongs().get(x).getArtist();
                    String artisttable = songlistT.getValueAt(songlistT.getSelectedRow(), 2).toString();
                    String titledb = getplaylist().getSongs().get(x).getTitle();
                    String titletable = songlistT.getValueAt(songlistT.getSelectedRow(), 1).toString();
                    if(artistdb.equals(artisttable) && titledb.equals(titletable)){
                        try {
                            Statement state = getcon().createStatement();
                            String deletesong = "DELETE FROM databasedc.songdetail WHERE SongID = '"+getplaylist().getSongs().get(x).getid()+"'";
                            state.execute(deletesong);
                            state.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                }
              JOptionPane.showMessageDialog(null, "Delete Successful!");
              loadsongs();
              loadsongstable();
            }
        }
    }
    
    public void deleteplaylist() // DONE
    {
        if(playlistT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Choose A Playlist First!");
        else
        {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete Playlist", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(dialogResult == JOptionPane.YES_OPTION){
                for(int x=0; x<yourplaylist.size(); x++)
                {
                    if(yourplaylist.get(x).getName().equals(playlistT.getValueAt(playlistT.getSelectedRow(), 0).toString()))
                    {
                        try {
                            Statement state = getcon().createStatement();
                            state.execute("DELETE FROM databasedc.playlistdc WHERE PlaylistName = '"+yourplaylist.get(x).getName()+"'");
                            Statement state2 = getcon().createStatement();
                            state2.execute("DELETE FROM databasedc.songdetail WHERE PlaylistID = '"+yourplaylist.get(x).getid()+"'");
                            
                            if(getplaylist() !=null && getplaylist().getName().equals(playlistT.getValueAt(playlistT.getSelectedRow(), 0).toString()))
                            {
                                clearall();
                                setplaylist(null);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        yourplaylist.remove(x);
                        break;
                    }
                }
                
                JOptionPane.showMessageDialog(null, "Delete Successful!");
                loadplaylisttable();
            }
        }
    }
    
    public void sort() // NOTHING
    {
        if(getplaylist() == null && !sortcombo.getSelectedItem().equals("Sort:")){
            JOptionPane.showMessageDialog(null, "Choose A Playlist First!");
            sortcombo.setSelectedIndex(0);
        }  
        else
        {    
            if(songlistT.getRowCount() != 0)
            {
                if(sortcombo.getSelectedItem().equals("Title"))
                {
                    DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
                    model.setRowCount(0);
                    int x=1;
                    Collections.sort(getplaylist().getSongs(), Song.TitleComparator);
                    for(Song str: getplaylist().getSongs()){
                        if(str.getGenre() == null)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                "-",
                                str.getAlbum().trim()
                            });
                        }
                        else
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                str.getGenre().getGenre(),
                                str.getAlbum().trim()
                            });
                        }
                        x++;
	            }
                }
                else if(sortcombo.getSelectedItem().equals("Album"))
                {
                    DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
                    model.setRowCount(0);
                    int x=1;
                    Collections.sort(getplaylist().getSongs(), Song.AlbumNameComparator);
                    for(Song str: getplaylist().getSongs()){
                        if(str.getGenre() == null)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                "-",
                                str.getAlbum().trim()
                            });
                        }
                        else
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                str.getGenre().getGenre(),
                                str.getAlbum().trim()
                            });
                        }
                        x++;
	            }
                }
                else if(sortcombo.getSelectedItem().equals("Artist"))
                {
                    DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
                    model.setRowCount(0);
                    int x=1;
                    Collections.sort(getplaylist().getSongs(), Song.ArtistComparator);
                    for(Song str: getplaylist().getSongs()){
                        if(str.getGenre() == null)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                "-",
                                str.getAlbum().trim()
                            });
                        }
                        else
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                x,
                                str.getTitle().trim(),
                                str.getArtist().trim(),
                                str.getGenre().getGenre(),
                                str.getAlbum().trim()
                            });
                        }
                        x++;
	            }
                }
                else if(sortcombo.getSelectedItem().equals("Genre"))
                {
                    DefaultTableModel model = (DefaultTableModel) songlistT.getModel();
                    model.setRowCount(0);
                    int y=1;
                    for(int x=0; x<getplaylist().getSongs().size(); x++){
                        if(getplaylist().getSongs().get(x).getGenre() == null)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                y,
                                getplaylist().getSongs().get(x).getTitle().trim(),
                                getplaylist().getSongs().get(x).getArtist().trim(),
                                "-",
                                getplaylist().getSongs().get(x).getAlbum().trim()
                            });
                            y++;
                        }
	            }
                    for(int x=0; x<getplaylist().getSongs().size(); x++){
                        if(getplaylist().getSongs().get(x).getGenre() instanceof Happy)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                y,
                                getplaylist().getSongs().get(x).getTitle().trim(),
                                getplaylist().getSongs().get(x).getArtist().trim(),
                                getplaylist().getSongs().get(x).getGenre().getGenre(),
                                getplaylist().getSongs().get(x).getAlbum().trim()
                            });
                            y++;
                        }
	            }
                    for(int x=0; x<getplaylist().getSongs().size(); x++){
                        if(getplaylist().getSongs().get(x).getGenre() instanceof Sad)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                y,
                                getplaylist().getSongs().get(x).getTitle().trim(),
                                getplaylist().getSongs().get(x).getArtist().trim(),
                                getplaylist().getSongs().get(x).getGenre().getGenre(),
                                getplaylist().getSongs().get(x).getAlbum().trim()
                            });
                            y++;
                        }
	            }
                    for(int x=0; x<getplaylist().getSongs().size(); x++){
                        if(getplaylist().getSongs().get(x).getGenre() instanceof Senti)
                        {
                            model.insertRow(songlistT.getRowCount(), new Object[]{
                                y,
                                getplaylist().getSongs().get(x).getTitle().trim(),
                                getplaylist().getSongs().get(x).getArtist().trim(),
                                getplaylist().getSongs().get(x).getGenre().getGenre(),
                                getplaylist().getSongs().get(x).getAlbum().trim()
                            });
                            y++;
                        }
	            }
                }
            }
        }
    }
    
    public void loadal()
    {
        if(albumT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Choose An Album First!");
        else{
            for(int x=0; x<albums.size(); x++)
            {
                if(albums.get(x).getName().equals(albumT.getValueAt(albumT.getSelectedRow(),0)))
                {
                    setalbum(albums.get(x));
                    break;
                }
            }
            
            specificalbumwindow window = new specificalbumwindow();
            window.setcon(getcon());
            window.setalbum(getalbum());
            window.initialize();
            window.setVisible(true);
        }
    }
    
    public void loadalbums() throws FileNotFoundException{
        try {
            Statement statement = getcon().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM databasedc.albumdc NATURAL JOIN databasedc.useraccount WHERE UserID = '"+getuser().getid()+"'");
            
            DefaultTableModel modelp = (DefaultTableModel) albumT.getModel();
            modelp.setRowCount(0);
            
            
            while(rs.next()){
                File file = new File("SamplePicture.png");
                Blob blob = rs.getBlob("AlbumCover");
                InputStream in = blob.getBinaryStream();
                OutputStream out = new FileOutputStream(file);
                byte[] bytes = blob.getBytes(1,(int)blob.length());
                out.write(bytes);
                out.close();
                
                albums.add(new Album(rs.getInt("AlbumID"), rs.getString("AlbumName"), rs.getString("AlbumDate"), file, rs.getString("FirstName") + " " + rs.getString("LastName")));
                
                modelp.insertRow(albumT.getRowCount(), new Object[]{
                    rs.getString("AlbumName")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadalbumsong() // UPDATES SONGS INSIDE THE ALBUM
    {
        getalbum().cleararray();
        try {
            Statement state = getcon().createStatement();
            ResultSet result = state.executeQuery("SELECT * \n" +
                                                  "FROM databasedc.songalbum NATURAL JOIN databasedc.song \n" +
                                                  "WHERE AlbumID = '"+getalbum().getid()+"'");
            
            while(result.next())
            {
                Director build = new Director();
                if(result.getString("SongGenre").equals("Happy")){
                   build.setSongBuilder(new HappyBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername());
                }
                else if(result.getString("SongGenre").equals("Sad")){
                   build.setSongBuilder(new SadBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername());  
                }
                else if(result.getString("SongGenre").equals("Senti")){
                   build.setSongBuilder(new SentiBuilder());
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername()); 
                }
                else{
                   build.setSongBuilder(new NoBuilder()); 
                   build.constructSong(result.getInt("SongID"), getalbum().getName(), result.getString("SongTitle"), getuser().getusername()); 
                }
                build.getSong().setTimesPlayed(result.getInt("SongPlayed"));
                
                if(result.getString("SongFav").equals("true"))
                    build.getSong().setFavorite();
                
                getalbum().addSong(build.getSong());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        createplay = new javax.swing.JButton();
        editplay = new javax.swing.JButton();
        addsongtoplay = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        loadplay = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        playlistT = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        albumT = new javax.swing.JTable();
        songtextLabel = new javax.swing.JLabel();
        deleteplay = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        songlistT = new javax.swing.JTable();
        playlistuser = new javax.swing.JLabel();
        deletesongplaybtn = new javax.swing.JButton();
        playbtn = new javax.swing.JButton();
        favbtn = new javax.swing.JButton();
        playlistname = new javax.swing.JLabel();
        sortcombo = new javax.swing.JComboBox<>();
        logoutbtn = new javax.swing.JButton();
        playlistdate = new javax.swing.JLabel();
        viewprofbtn = new javax.swing.JButton();
        searchbtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        albumsongT = new javax.swing.JTable();
        albumtextlabel = new javax.swing.JLabel();
        createalbumBtn = new javax.swing.JButton();
        openalbumbtn1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(60, 3));
        setResizable(false);
        setSize(new java.awt.Dimension(1800, 887));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1800, 887));

        createplay.setText("New Playlist");
        createplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createplayActionPerformed(evt);
            }
        });

        editplay.setText("Edit Playlist");
        editplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editplayActionPerformed(evt);
            }
        });

        addsongtoplay.setText("Upload Song");
        addsongtoplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addsongtoplayActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lithos Pro Regular", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("LIBRARY");
        jLabel10.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Ravie", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("PLAYLISTS");

        loadplay.setText("Load Playlist");
        loadplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadplayActionPerformed(evt);
            }
        });

        playlistT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "YourPlaylist"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        playlistT.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        playlistT.getTableHeader().setReorderingAllowed(false);
        playlistT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                playlistTMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(playlistT);
        if (playlistT.getColumnModel().getColumnCount() > 0) {
            playlistT.getColumnModel().getColumn(0).setResizable(false);
        }

        albumT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Album Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        albumT.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        albumT.getTableHeader().setReorderingAllowed(false);
        albumT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                albumTMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(albumT);
        if (albumT.getColumnModel().getColumnCount() > 0) {
            albumT.getColumnModel().getColumn(0).setResizable(false);
        }

        songtextLabel.setFont(new java.awt.Font("Ravie", 1, 24)); // NOI18N
        songtextLabel.setForeground(new java.awt.Color(255, 255, 255));
        songtextLabel.setText("Uploaded Songs");

        deleteplay.setText("Delete Playlist");
        deleteplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteplayActionPerformed(evt);
            }
        });

        songlistT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Title", "Artist", "Genre", "Album"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        songlistT.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        songlistT.getTableHeader().setReorderingAllowed(false);
        songlistT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                songlistTMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(songlistT);
        if (songlistT.getColumnModel().getColumnCount() > 0) {
            songlistT.getColumnModel().getColumn(0).setResizable(false);
            songlistT.getColumnModel().getColumn(0).setPreferredWidth(1);
            songlistT.getColumnModel().getColumn(1).setResizable(false);
            songlistT.getColumnModel().getColumn(1).setPreferredWidth(300);
            songlistT.getColumnModel().getColumn(2).setResizable(false);
            songlistT.getColumnModel().getColumn(3).setResizable(false);
            songlistT.getColumnModel().getColumn(4).setResizable(false);
        }

        playlistuser.setFont(new java.awt.Font("Rockwell", 0, 14)); // NOI18N
        playlistuser.setForeground(new java.awt.Color(255, 255, 255));

        deletesongplaybtn.setText("Remove");
        deletesongplaybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletesongplaybtnActionPerformed(evt);
            }
        });

        playbtn.setText("Play");
        playbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playbtnActionPerformed(evt);
            }
        });

        favbtn.setText("Favorite");
        favbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                favbtnActionPerformed(evt);
            }
        });

        playlistname.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        playlistname.setForeground(new java.awt.Color(255, 255, 255));
        playlistname.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        playlistname.setToolTipText("");

        sortcombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort:", "Title", "Artist", "Genre", "Album" }));
        sortcombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortcomboActionPerformed(evt);
            }
        });

        logoutbtn.setActionCommand("Logout");
        logoutbtn.setPreferredSize(new java.awt.Dimension(60, 60));
        logoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutbtnActionPerformed(evt);
            }
        });

        playlistdate.setForeground(new java.awt.Color(255, 255, 255));

        viewprofbtn.setActionCommand("View Profile");
        viewprofbtn.setPreferredSize(new java.awt.Dimension(60, 60));
        viewprofbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewprofbtnActionPerformed(evt);
            }
        });

        searchbtn.setText("Search");
        searchbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchbtnMouseClicked(evt);
            }
        });
        searchbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbtnActionPerformed(evt);
            }
        });

        albumsongT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title", "Genre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        albumsongT.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(albumsongT);
        if (albumsongT.getColumnModel().getColumnCount() > 0) {
            albumsongT.getColumnModel().getColumn(0).setResizable(false);
            albumsongT.getColumnModel().getColumn(1).setResizable(false);
        }

        albumtextlabel.setFont(new java.awt.Font("Ravie", 1, 24)); // NOI18N
        albumtextlabel.setForeground(new java.awt.Color(255, 255, 255));
        albumtextlabel.setText("ALBUMS");

        createalbumBtn.setText("Create Album");
        createalbumBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createalbumBtnActionPerformed(evt);
            }
        });

        openalbumbtn1.setText("Open Album");
        openalbumbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openalbumbtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(createplay, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loadplay, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(albumtextlabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteplay, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(223, 223, 223)
                                .addComponent(jLabel10))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(playlistname, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewprofbtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(logoutbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(playlistuser, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playlistdate, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(playbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deletesongplaybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(341, 341, 341)
                        .addComponent(favbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sortcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(createalbumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(openalbumbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(songtextLabel)
                            .addComponent(addsongtoplay, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(88, 88, 88)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playlistname, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logoutbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewprofbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(playlistuser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(playlistdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sortcombo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(playbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(deletesongplaybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(favbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(searchbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editplay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loadplay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createplay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addComponent(deleteplay, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(albumtextlabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(songtextLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addsongtoplay, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(openalbumbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(createalbumBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewprofbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewprofbtnActionPerformed
        viewprofile();
    }//GEN-LAST:event_viewprofbtnActionPerformed

    private void logoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutbtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure?", "Log Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(dialogResult == JOptionPane.YES_OPTION){
            if(getuser().gettype().equals("Registered") || getuser().gettype().equals("Artist"))
            {
                this.dispose();
                Login log = new Login();
                log.setVisible(true);
            }
            else{
                int dialogResultGuest = JOptionPane.showConfirmDialog(null, "Do you wanna create an account?", "Create Account", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(dialogResultGuest == JOptionPane.YES_OPTION){
                    Signup sign = new Signup(this,true);
                    sign.setcon(getcon());
                    sign.setVisible(true);
                    
                    if(sign.getsignup())
                    {
                        try {
                            Statement stategetid = getcon().createStatement();
                            ResultSet resultgetid = stategetid.executeQuery("SELECT MAX(UserID) FROM databasedc.useraccount");
                            int maxid = 0;
                            while(resultgetid.next())
                            {
                                maxid = resultgetid.getInt("MAX(UserID)");
                            }
                            
                            Statement stateputplay = getcon().createStatement();
                            ResultSet resultputplay = stateputplay.executeQuery("SELECT * FROM databasedc.playlistdc WHERE UserID = '"+(maxid-1)+"'");
                            Statement stateinsertplay = getcon().createStatement();
                            Statement stateinsertsong = getcon().createStatement();
                            int maxplayid = 0;        
                            while(resultputplay.next())
                            { 
                                maxplayid = resultputplay.getInt("PlaylistID");
                            }
                            stateinsertplay.execute("UPDATE databasedc.playlistdc SET UserID = '"+maxid+"' WHERE UserID = '"+(maxid-1)+"'");
                            stateinsertsong.execute("UPDATE databasedc.songdetail SET PlaylistID = '"+maxplayid+"' WHERE PlaylistID = '"+(maxplayid-1)+"'");
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                this.dispose();
                Login log = new Login();
                log.setVisible(true);
            }
        }
    }//GEN-LAST:event_logoutbtnActionPerformed

    private void songlistTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_songlistTMousePressed
        albumT.clearSelection();
        for(int x=0; x<getplaylist().getSongs().size(); x++)
        {
            if(getplaylist().getSongs().get(x).getTitle().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 1)) && getplaylist().getSongs().get(x).getArtist().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 2)))
            {
                if(getplaylist().getSongs().get(x).getfavorite())
                    favbtn.setText("Unfavorite");
                else
                    favbtn.setText("Favorite");
                break;
            }
        }
    }//GEN-LAST:event_songlistTMousePressed

    private void playbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playbtnActionPerformed
        if(songlistT.getSelectedRow() < 0)
        {
            ArrayList<ShuffleSong> arrayshuf = new ArrayList<>();
            for(int y=0; y<songlistT.getRowCount(); y++)
            {
                arrayshuf.add(new ShuffleSong(y+1,songlistT.getValueAt(y, 1).toString(),songlistT.getValueAt(y, 2).toString()));           
            }
            
            for(int x=0; x<getplaylist().getSongs().size(); x++)
            {
                if(getplaylist().getSongs().get(x).getArtist().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 2).toString()) && getplaylist().getSongs().get(x).getTitle().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 1).toString()))
                {
                    try {
                        Statement stategetaddsongplay = getcon().createStatement();
                        ResultSet resultgetaddsongplay = stategetaddsongplay.executeQuery("SELECT * FROM databasedc.songdetail WHERE SongID = '"+getplaylist().getSongs().get(x).getid()+"' AND PlaylistID = '"+getplaylist().getid()+"'");
                        int timeplay = 0;
                        while(resultgetaddsongplay.next())
                        {
                            timeplay = resultgetaddsongplay.getInt("SongPlayed");
                        }
                        Statement stateupaddsongplay = getcon().createStatement();
                        stateupaddsongplay.execute("UPDATE databasedc.songdetail SET SongPlayed = '"+(timeplay+1)+"' WHERE SongID = '"+getplaylist().getSongs().get(x).getid()+"' AND PlaylistID = '"+getplaylist().getid()+"'");
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    Musicplayer mf = new Musicplayer(this, true);
                    mf.setcon(getcon());
                    mf.setsong(getplaylist().getSongs().get(x));
                    mf.songtoplay();
                    mf.getwav().setshuf(arrayshuf);
                    mf.setVisible(true);
                    mf.stoptimer();
                    mf.getwav().stopMusic();
                    loadsongs();
                    break;
                }
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Please Select A Song First!");
    }//GEN-LAST:event_playbtnActionPerformed

    private void deletesongplaybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletesongplaybtnActionPerformed
        deletesong();
    }//GEN-LAST:event_deletesongplaybtnActionPerformed

    private void favbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_favbtnActionPerformed
        if(songlistT.getSelectedRow() < 0)
            JOptionPane.showMessageDialog(null, "Please Choose A Song To Fav/UnFav");
        else
        {
            if(favbtn.getText().equals("Favorite"))
            {
                for(int x=0; x<getplaylist().getSongs().size(); x++)
                {
                    if(getplaylist().getSongs().get(x).getTitle().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 1)) && getplaylist().getSongs().get(x).getArtist().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 2)))
                    {
                        try {
                            Statement statechafav = getcon().createStatement();
                            statechafav.execute("UPDATE databasedc.songdetail SET SongFav = '"+"true"+"' WHERE SongID = '"+getplaylist().getSongs().get(x).getid()+"' AND PlaylistID = '"+getplaylist().getid()+"'");
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        break;
                    }
                }
                favbtn.setText("Unfavorite");
            }
            else
            {
                for(int x=0; x<getplaylist().getSongs().size(); x++)
                {
                    if(getplaylist().getSongs().get(x).getTitle().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 1)) && getplaylist().getSongs().get(x).getArtist().equals(songlistT.getValueAt(songlistT.getSelectedRow(), 2)))
                    {
                        try {
                            Statement statechafav = getcon().createStatement();
                            statechafav.execute("UPDATE databasedc.songdetail SET SongFav = '"+"false"+"' WHERE SongID = '"+getplaylist().getSongs().get(x).getid()+"' AND PlaylistID = '"+getplaylist().getid()+"'");
                        } catch (SQLException ex) {
                            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        break;
                    }
                }
                favbtn.setText("Favorite");
            }
            
            loadsongs();
        }
    }//GEN-LAST:event_favbtnActionPerformed

    private void sortcomboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortcomboActionPerformed
        sort();
    }//GEN-LAST:event_sortcomboActionPerformed

    private void createplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createplayActionPerformed
        createplay();
    }//GEN-LAST:event_createplayActionPerformed

    private void loadplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadplayActionPerformed
        loadplay();
    }//GEN-LAST:event_loadplayActionPerformed

    private void editplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editplayActionPerformed
        editplay();
    }//GEN-LAST:event_editplayActionPerformed

    private void deleteplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteplayActionPerformed
        deleteplaylist();
    }//GEN-LAST:event_deleteplayActionPerformed

    private void addsongtoplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addsongtoplayActionPerformed
        uploadsong upload = new uploadsong();
        upload.setcon(getcon());
        upload.setuser(getuser());
        upload.setSongs(getYoursongs());
        upload.setOwner(this);
        upload.setVisible(true);
    }//GEN-LAST:event_addsongtoplayActionPerformed

    private void albumTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_albumTMousePressed
        songlistT.clearSelection();
    }//GEN-LAST:event_albumTMousePressed

    private void playlistTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playlistTMousePressed
        albumT.clearSelection();
        songlistT.clearSelection();
    }//GEN-LAST:event_playlistTMousePressed

    private void createalbumBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createalbumBtnActionPerformed
        CreateAlbum create = new CreateAlbum();
        create.setcon(getcon());
        create.setuser(getuser());
        create.setOwner(this);
        create.setVisible(true);
    }//GEN-LAST:event_createalbumBtnActionPerformed

    private void searchbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchbtnMouseClicked
        Search search = new Search(this,true);
        search.setcon(getcon());
        search.setuser(getuser());
        search.setVisible(true);
        search.setOwner(this);
    }//GEN-LAST:event_searchbtnMouseClicked

    private void searchbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchbtnActionPerformed

    private void openalbumbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openalbumbtn1ActionPerformed
        loadal();
    }//GEN-LAST:event_openalbumbtn1ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addsongtoplay;
    private javax.swing.JTable albumT;
    private javax.swing.JTable albumsongT;
    private javax.swing.JLabel albumtextlabel;
    private javax.swing.JButton createalbumBtn;
    private javax.swing.JButton createplay;
    private javax.swing.JButton deleteplay;
    private javax.swing.JButton deletesongplaybtn;
    private javax.swing.JButton editplay;
    private javax.swing.JButton favbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton loadplay;
    private javax.swing.JButton logoutbtn;
    private javax.swing.JButton openalbumbtn1;
    private javax.swing.JButton playbtn;
    private javax.swing.JTable playlistT;
    private javax.swing.JLabel playlistdate;
    private javax.swing.JLabel playlistname;
    private javax.swing.JLabel playlistuser;
    private javax.swing.JButton searchbtn;
    private javax.swing.JTable songlistT;
    private javax.swing.JLabel songtextLabel;
    private javax.swing.JComboBox<String> sortcombo;
    private javax.swing.JButton viewprofbtn;
    // End of variables declaration//GEN-END:variables
}
