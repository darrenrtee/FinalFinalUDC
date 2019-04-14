
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Darren
 */
public class specificalbumwindow extends javax.swing.JFrame {
    private Album album;
    
    private Connection connection;
    public specificalbumwindow() {
        initComponents();
    }
    
    public void initialize(){
         try {
            albumnamelabel.setText(this.album.getName());
            albumartist.setText(this.album.getArtist());
            BufferedImage albumimage = null;
            File image = album.getFile();
            albumimage = ImageIO.read(image);
            albumimagelabel.setIcon(new ImageIcon(albumimage.getScaledInstance(64, 64,Image.SCALE_SMOOTH)));
            DefaultTableModel modelp = (DefaultTableModel) albumsongsTable.getModel();
            modelp.setRowCount(0);
        } catch (IOException ex) {
            Logger.getLogger(specificalbumwindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    public void loadalbumsongstable() // LOADS THE SONGS INSIDE THE ALBUM INTO THE TABLE
    {
        DefaultTableModel modelp = (DefaultTableModel) songsInTheAlbumTable.getModel();
        modelp.setRowCount(0);
        
        for(int x=0; x<getalbum().getSongs().size(); x++)
        {
            modelp.insertRow(songsInTheAlbumTable.getRowCount(), new Object[]{
                getalbum().getSongs().get(x).getTitle(),
                getalbum().getSongs().get(x).getGenre()
            });
        }
    }
    */
    public void setalbum(Album album){
        this.album = album;
    }
    
    public void setcon(Connection connection){
        this.connection = connection;
    }
   
    public Connection getcon(){
        return this.connection;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        albumimagelabel = new javax.swing.JLabel();
        albumartist = new javax.swing.JLabel();
        albumnamelabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        albumsongsTable = new javax.swing.JTable();
        playalbumsongBtn = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        albumimagelabel.setMaximumSize(new java.awt.Dimension(64, 64));
        albumimagelabel.setMinimumSize(new java.awt.Dimension(64, 64));
        albumimagelabel.setPreferredSize(new java.awt.Dimension(64, 64));

        albumartist.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        albumartist.setText("Artist name");

        albumnamelabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        albumnamelabel.setText("Album Name");

        albumsongsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title ", "Genre"
            }
        ));
        jScrollPane1.setViewportView(albumsongsTable);

        playalbumsongBtn.setText("Play");
        playalbumsongBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playalbumsongBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(albumimagelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(albumartist, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(139, 139, 139)
                        .addComponent(playalbumsongBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(143, 143, 143)
                    .addComponent(albumnamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(204, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(albumimagelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(albumartist, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(playalbumsongBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(albumnamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(305, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void playalbumsongBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playalbumsongBtnMouseClicked
        if(albumsongsTable.getSelectedRow() < 0){
            
        }
    }//GEN-LAST:event_playalbumsongBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(specificalbumwindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(specificalbumwindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(specificalbumwindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(specificalbumwindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new specificalbumwindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel albumartist;
    private javax.swing.JLabel albumimagelabel;
    private javax.swing.JLabel albumnamelabel;
    private javax.swing.JTable albumsongsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel playalbumsongBtn;
    // End of variables declaration//GEN-END:variables
}
