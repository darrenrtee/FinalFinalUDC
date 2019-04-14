import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private ConnectiontoDB connection = new ConnectiontoDB();
    SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd");
    
    public Login() {
        initComponents();
        connection.createConnection();
        mainicon();
        setghost();
    }
    
    public void clearlog()
    {
        AccountNameField.setText("");
        PasswordField.setText("");
        showpass.setSelected(false);
    }
    
   public void logToPlayer(){
        String username;
        String password;
        if(AccountNameField.getText().equals("") && PasswordField.getPassword().equals(""))
            JOptionPane.showMessageDialog(null,"Username and Password Field Cannot be Empty","No Username and password Found!", JOptionPane.WARNING_MESSAGE);
        else if(AccountNameField.getText().equals(""))
            JOptionPane.showMessageDialog(null,"Username Field Cannot be Empty","No Username Found!", JOptionPane.WARNING_MESSAGE);
        else if(PasswordField.getPassword().equals(""))
            JOptionPane.showMessageDialog(null,"Password Field Cannot be Empty","No Password Found!", JOptionPane.WARNING_MESSAGE);
        else{
            try {
                username = AccountNameField.getText();
                password = new String(PasswordField.getPassword());
                
                Statement stateL = connection.getconnect().createStatement();
                ResultSet resultL = stateL.executeQuery("SELECT * FROM databasedc.useraccount WHERE UserName = '"+username+"' AND PassWord = '"+password+"'");
                while(resultL.next()){
                    int userid = resultL.getInt("UserID");
                    String firstname = resultL.getString("FirstName");
                    String lastname = resultL.getString("LastName");
                    String type = resultL.getString("Type");
                    Date date = resultL.getDate("RegisteredDate");
                    
                    this.dispose();
                    
                    Dashboard dashboard = new Dashboard();
                    
                    if(type.equals("Artist")){
                        dashboard.setuser(new Artist(userid, username, password, firstname, lastname, date));
                        dashboard.setcon(connection.getconnect());
                        dashboard.initializePlayer();
                    }
                    else if(type.equals("Registered")){
                        System.out.println("kek");
                        dashboard.setuser(new Registered(userid, username, password, firstname, lastname, date));
                        dashboard.setcon(connection.getconnect());
                        dashboard.initializePlayer();
                    }
                    
                    
                    dashboard.setVisible(true);
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loginguest()
    {
        try {
            int guestid = 0;
            Date guestdate = null;
            Statement state = connection.getconnect().createStatement();
            Statement stateCheck = connection.getconnect().createStatement();
            String insertguest = "INSERT INTO useraccount(UserName, PassWord, FirstName, LastName, Type, RegisteredDate) VALUES('"+"Guest"+"','"+""+"','"+"Guest"+"','"+"User"+"','"+"Guest"+"','" + datefor.format(new Date()) + "')";   
            state.execute(insertguest);
            state.close();
            
            ResultSet resultCheck2 = stateCheck.executeQuery("SELECT MAX(UserID) FROM databasedc.useraccount");
            if(resultCheck2.next())
                guestid = resultCheck2.getInt("MAX(UserID)");
            
            ResultSet resultCheck = stateCheck.executeQuery("SELECT * FROM databasedc.useraccount WHERE UserID = '"+guestid+"'");
            if(resultCheck.next())
                guestdate = resultCheck.getDate("RegisteredDate");
            
            stateCheck.close();
            JOptionPane.showMessageDialog(null, "Log In Successful!"); 
            this.dispose();
            Dashboard musicplayer = new Dashboard();
            musicplayer.setuser(new Guest(guestid, guestdate));
            musicplayer.setcon(connection.getconnect());
            musicplayer.loadalbumtable();
            musicplayer.setVisible(true);
            
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        Musicplayericon = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        musicusericon = new javax.swing.JLabel();
        AccountNameField = new javax.swing.JTextField();
        musicpassicon = new javax.swing.JLabel();
        PasswordField = new javax.swing.JPasswordField();
        loginbtn = new javax.swing.JButton();
        regbtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        loginguestbtn = new javax.swing.JButton();
        showpass = new javax.swing.JCheckBox();
        exitButton = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(700, 200));
        setUndecorated(true);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Harlow Solid Italic", 2, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Music Player");

        AccountNameField.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        AccountNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                AccountNameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                AccountNameFieldFocusLost(evt);
            }
        });
        AccountNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccountNameFieldActionPerformed(evt);
            }
        });

        PasswordField.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        PasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PasswordFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PasswordFieldFocusLost(evt);
            }
        });

        loginbtn.setText("Log In");
        loginbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbtnActionPerformed(evt);
            }
        });
        loginbtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                loginbtnKeyPressed(evt);
            }
        });

        regbtn.setText("Register");
        regbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regbtnActionPerformed(evt);
            }
        });
        regbtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                regbtnKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("or");

        loginguestbtn.setText("Log In as Guest");
        loginguestbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginguestbtnActionPerformed(evt);
            }
        });
        loginguestbtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                loginguestbtnKeyPressed(evt);
            }
        });

        showpass.setText(" Show Password");
        showpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showpassActionPerformed(evt);
            }
        });

        exitButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        exitButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitButton.setText("X");
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(loginguestbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(89, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Musicplayericon, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(musicpassicon, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(musicusericon, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(showpass)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AccountNameField)
                            .addComponent(PasswordField))
                        .addGap(18, 18, 18))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Musicplayericon, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AccountNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(musicusericon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(musicpassicon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(showpass)
                .addGap(18, 18, 18)
                .addComponent(loginbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginguestbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AccountNameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_AccountNameFieldFocusGained
        if(AccountNameField.getForeground().equals(Color.GRAY)){
            AccountNameField.setText("");
            AccountNameField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_AccountNameFieldFocusGained

    private void AccountNameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_AccountNameFieldFocusLost
        if(AccountNameField.getText().equals("")){
            AccountNameField.setText("Enter Your Username");
            AccountNameField.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_AccountNameFieldFocusLost

    private void PasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordFieldFocusGained
        if(PasswordField.getForeground().equals(Color.GRAY)){
            if(showpass.isSelected())
                PasswordField.setEchoChar((char)0);
            else
                PasswordField.setEchoChar('*');
            
            PasswordField.setText("");
            PasswordField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_PasswordFieldFocusGained

    private void PasswordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordFieldFocusLost
        if(PasswordField.getText().equals("")){
            PasswordField.setEchoChar((char)0);
            PasswordField.setText("Enter Password");
            PasswordField.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_PasswordFieldFocusLost

    private void showpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showpassActionPerformed
        if(!PasswordField.getForeground().equals(Color.GRAY))
        {
            if(showpass.isSelected())
               PasswordField.setEchoChar((char)0);
            else
               PasswordField.setEchoChar('*');
        }
    }//GEN-LAST:event_showpassActionPerformed

    private void regbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regbtnActionPerformed
        Signup sign = new Signup(this, true);
        sign.setcon(connection.getconnect());
        sign.setVisible(true);
    }//GEN-LAST:event_regbtnActionPerformed

    private void loginbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbtnActionPerformed
        logToPlayer();
    }//GEN-LAST:event_loginbtnActionPerformed

    private void loginguestbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginguestbtnActionPerformed
        loginguest();
    }//GEN-LAST:event_loginguestbtnActionPerformed

    private void loginguestbtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginguestbtnKeyPressed
        loginguest();
    }//GEN-LAST:event_loginguestbtnKeyPressed

    private void loginbtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loginbtnKeyPressed
        logToPlayer();
    }//GEN-LAST:event_loginbtnKeyPressed

    private void regbtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_regbtnKeyPressed
        Signup sign = new Signup(this, true);
        sign.setcon(connection.getconnect());
        sign.setVisible(true);
    }//GEN-LAST:event_regbtnKeyPressed

    private void AccountNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccountNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AccountNameFieldActionPerformed

    private void exitButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitButtonMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitButtonMouseClicked

    public void setghost()
    {
        AccountNameField.setText("Enter Your Uesrname");
        AccountNameField.setForeground(Color.GRAY);
        PasswordField.setEchoChar((char)0);
        PasswordField.setText("Enter Your Password");
        PasswordField.setForeground(Color.GRAY);
    }
    
    public void mainicon()
    {
        BufferedImage imgmusic = null;
        BufferedImage imguser = null;
        BufferedImage imgpass = null;
        
        try {
            imgmusic = ImageIO.read(getClass().getResource("/Icons/musicplayer.png"));
            imguser = ImageIO.read(getClass().getResource("/Icons/musicuser.png"));
            imgpass = ImageIO.read(getClass().getResource("/Icons/musicpass.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
                
        Musicplayericon.setIcon(new ImageIcon(imgmusic.getScaledInstance(135, 113, Image.SCALE_SMOOTH)));
        musicusericon.setIcon(new ImageIcon(imguser.getScaledInstance(70, 54, Image.SCALE_SMOOTH)));
        musicpassicon.setIcon(new ImageIcon(imgpass.getScaledInstance(70, 54, Image.SCALE_SMOOTH)));
    }
    
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AccountNameField;
    private javax.swing.JLabel Musicplayericon;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JLabel exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton loginbtn;
    private javax.swing.JButton loginguestbtn;
    private javax.swing.JLabel musicpassicon;
    private javax.swing.JLabel musicusericon;
    private javax.swing.JButton regbtn;
    private javax.swing.JCheckBox showpass;
    // End of variables declaration//GEN-END:variables
}
