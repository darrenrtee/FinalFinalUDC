import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Search extends javax.swing.JDialog {
    private Dashboard owner;
    private Connection con;
    private User user;
    public void setcon(Connection con)
    {
        this.con = con;
    }
    
    public Connection getcon()
    {
        return this.con;
    }
    public void setuser(User user)
    {
        this.user = user;
    }
    
    public User getuser()
    {
        return this.user;
    }
    
    public void setOwner(Dashboard owner){
        this.owner = owner;
    }
    
    public Dashboard getOwner(){
        return this.owner;
    }
    
    public Search(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchtext = new javax.swing.JTextField();
        searchbox = new javax.swing.JComboBox<>();
        okbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        resulttable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        searchbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Artist", "Listener", "Playlist", "Album", "Song" }));

        okbtn.setText("OK");
        okbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okbtnActionPerformed(evt);
            }
        });

        resulttable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        resulttable.getTableHeader().setReorderingAllowed(false);
        resulttable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resulttableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(resulttable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(okbtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void searching() // NOT DONE
    {
        try{
            String text = searchtext.getText();
        
            if(text.equals(""))
                JOptionPane.showMessageDialog(null, "Please Input Something To Search");
            else{
                if(searchbox.getSelectedItem().equals("None"))
                    JOptionPane.showMessageDialog(null, "Please Choose A Category to Search!");
                else if(searchbox.getSelectedItem().equals("Artist")){
                    DefaultTableModel model = (DefaultTableModel) resulttable.getModel();
                    model.setRowCount(0);
                    
                    Statement searchArtist = getcon().createStatement();
                    ResultSet rsSearchArtist = searchArtist.executeQuery("SELECT * FROM databasedc.useraccount WHERE Type = '"+"Artist"+"'");
                    
                    while(rsSearchArtist.next()){
                        String artistUsername = rsSearchArtist.getString("UserName");
                        
                        if(artistUsername.contains(text)){
                            
                            resulttable.getColumnModel().getColumn(0).setHeaderValue("Artist Name");
                            
                            model.insertRow(resulttable.getRowCount(), new Object[]{
                                artistUsername
                            });
                            
                            resulttable.getTableHeader().repaint();
                        }
                    }
                }
                else if(searchbox.getSelectedItem().equals("Listener")){
                    DefaultTableModel model = (DefaultTableModel) resulttable.getModel();
                    model.setRowCount(0);
                    
                    Statement searchListener = getcon().createStatement();
                    ResultSet rsSearchListener = searchListener.executeQuery("SELECT * FROM databasedc.useraccount WHERE Type = '"+"Registered"+"'");
                    
                    while(rsSearchListener.next()){
                        String listenerUsername = rsSearchListener.getString("UserName");
                        
                        if(listenerUsername.contains(text)){
                            
                            resulttable.getColumnModel().getColumn(0).setHeaderValue("Listener Name");
                            
                            model.insertRow(resulttable.getRowCount(), new Object[]{
                                listenerUsername
                            });
                            
                            resulttable.getTableHeader().repaint();
                        }
                    }
                }
                else if(searchbox.getSelectedItem().equals("Playlist")){
                    DefaultTableModel model = (DefaultTableModel) resulttable.getModel();
                    model.setRowCount(0);
                    
                    Statement searchPlaylist = getcon().createStatement();
                    ResultSet rsSearchPlaylist = searchPlaylist.executeQuery("SELECT * FROM databasedc.playlistdc NATURAL JOIN databasedc.useraccount WHERE UserID != '"+getuser().getid()+"' AND Type != '"+"Guest"+"'");
                    
                    while(rsSearchPlaylist.next()){
                        String playlistName = rsSearchPlaylist.getString("PlaylistName");
                        
                        if(playlistName.contains(text)){
                            
                            resulttable.getColumnModel().getColumn(0).setHeaderValue("Playlist Owner");
                            resulttable.getColumnModel().getColumn(1).setHeaderValue("Playlist Name");
                            
                            model.insertRow(resulttable.getRowCount(), new Object[]{
                                rsSearchPlaylist.getString("UserName"),
                                playlistName
                            });
                            
                            resulttable.getTableHeader().repaint();
                        }
                    }
                }
                else if(searchbox.getSelectedItem().equals("Album")){
                    DefaultTableModel model = (DefaultTableModel) resulttable.getModel();
                    model.setRowCount(0);
                    
                    Statement searchAlbum = getcon().createStatement();
                    ResultSet rsSearchAlbum = searchAlbum.executeQuery("SELECT * FROM databasedc.albumdc NATURAL JOIN databasedc.useraccount WHERE UserID != '"+getuser().getid()+"' AND Type != '"+"Guest"+"'");
                    
                    while(rsSearchAlbum.next()){
                        String albumName = rsSearchAlbum.getString("AlbumName");
                        
                        if(albumName.contains(text)){
                            
                            resulttable.getColumnModel().getColumn(0).setHeaderValue("Artist Name");
                            resulttable.getColumnModel().getColumn(1).setHeaderValue("Album");
                            
                            model.insertRow(resulttable.getRowCount(), new Object[]{
                                rsSearchAlbum.getString("UserName"),
                                albumName
                            });
                            
                            resulttable.getTableHeader().repaint();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void okbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okbtnActionPerformed
        searching();
    }//GEN-LAST:event_okbtnActionPerformed

    private void resulttableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resulttableMouseClicked
       if(evt.getClickCount() == 2){
           
       }
    }//GEN-LAST:event_resulttableMouseClicked

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Search dialog = new Search(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okbtn;
    private javax.swing.JTable resulttable;
    private javax.swing.JComboBox<String> searchbox;
    private javax.swing.JTextField searchtext;
    // End of variables declaration//GEN-END:variables
}
