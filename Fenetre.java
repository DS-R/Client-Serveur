/**
* @author DI SANTOLO Richard
* Programme pour établir une connection client/serveur, ce code gère l'affichage de l'interface.
*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.text.*;


public class Fenetre extends JFrame {    
    protected Action closeAction;
    static final JMenuBar mainMenuBar = new JMenuBar();
    protected JMenu fichierMenu, editionMenu;
    private JButton submitButton, connectButton;
    public JFormattedTextField serveurTF, portTF;
    public JTextArea clientTA, serveurTA;
    public JLabel serveurTFLabel, portTFLabel, clientTALabel, serveurTALabel;
    public JPanel fieldPanel, areaPanel, buttonPanel;
    protected boolean thread = true;
    
    Fenetre(){
        setTitle("CLIENT");        
        setSize(600, 600);//Les dimensions de la fenêtre
        createActions();// Les actions
        addMenus(); // Les menus
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//Centrer la fenetre
        setResizable(false);//Bloque le redimensionnement
        //Text Field
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        try{
            // Serveur 
            MaskFormatter ip = new MaskFormatter("###.###.#.###");//Pour obliger l'utilisateur a mette seulement des chiffres
            serveurTF = new JFormattedTextField(ip);
            serveurTF.setPreferredSize(new Dimension(100, 30));
            serveurTFLabel = new JLabel("Serveur");

            gbc.gridx = 0;
            gbc.gridy = 0;            
            add(serveurTFLabel, gbc);
            
            gbc.weightx = 0;
            gbc.gridx = 1;
            gbc.gridy = 0;
            add(serveurTF, gbc);
            
            //Port    
            MaskFormatter port = new MaskFormatter("####");
            portTF = new JFormattedTextField(port);
            portTF.setPreferredSize(new Dimension(50, 30));
            portTFLabel = new JLabel("Port");
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(portTFLabel,gbc);
    
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(portTF,gbc);       
        }catch(ParseException pe){pe.printStackTrace();}
        //Area Text
        //client
        JPanel areaPanel = new JPanel();
        clientTALabel = new JLabel ("Client");
        clientTA = new JTextArea(15, 15);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(clientTALabel, gbc);
              
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(clientTA, gbc);
        
        //serveur       
        serveurTALabel = new JLabel ("Serveur");
        serveurTA = new JTextArea(15, 15);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(serveurTALabel, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 3;
        add(serveurTA, gbc);        

        //Button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new finishButtonListener());
        
        connectButton = new JButton("Connection");
        connectButton.addActionListener(new connectButtonListener());
            
       gbc.gridx = 0;
       gbc.gridy = 4;
       add(connectButton, gbc);
       
       gbc.gridx = 2;
       gbc.gridy = 4;
       add(submitButton, gbc);
       
   }
    //Barre de menu
    public void addMenus(){
        fichierMenu = new JMenu("Fichier");
        fichierMenu.add(new JMenuItem(closeAction));
        mainMenuBar.add(fichierMenu);
        editionMenu = new JMenu("Edition");
        mainMenuBar.add(editionMenu);
        
        setJMenuBar(mainMenuBar);
    }
    //Création des actions
    public void createActions(){
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        closeAction = new closeActionClass("Quitter", KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
    }
    //CloseAction
    public class closeActionClass extends AbstractAction{
        public closeActionClass(String text, KeyStroke shortcut){
            super(text);
            putValue(ACCELERATOR_KEY, shortcut);
        }
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
    //Boutton Deconnection
    public class finishButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){

        }
    }
    //Bouton connection
    public class connectButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                new MonClient("localhost", 2222);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//FIN connectButtonListener    

    
    public int parseStringInt(){
        int portParse = Integer.parseInt(portTF.getText());
        return portParse;
    }
    public String addressArea(){
    String addressArea = serveurTF.getText();
    return addressArea;
    }
    //Méthode pour gérer l'affichage dans le text area
    public void appendServeurTA(String text){             
        serveurTA.append(text+"\n");            
    }
}//FIN Fenetre