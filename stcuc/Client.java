import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Client implements ActionListener {

    JPanel p1;
    JButton b1;
    boolean typing;

    static JFrame f1 = new JFrame();
    static Box vertical = Box.createVerticalBox();
    static JTextField t1;
    static AESAlgo aes;
    static String key;
    static int flag = 0;
    static JPanel a1;
    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;

    Client() {

        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 60);
        f1.add(p1);
        
        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("stcuc-key.png"));
        Image img2 = img1.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        ImageIcon img3 = new ImageIcon(img2);
        JLabel jl1 = new JLabel(img3);
        jl1.setBounds(30, 5, 40, 50);
        p1.add(jl1);

        ImageIcon img4 = new ImageIcon(ClassLoader.getSystemResource("3Dots1.png"));
        Image img5 = img4.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon img6 = new ImageIcon(img5);
        JLabel jl2 = new JLabel(img6);
        jl2.setBounds(395, 5, 50, 50);
        p1.add(jl2);

        ImageIcon img7 = new ImageIcon(ClassLoader.getSystemResource("arrow-88.png"));
        Image img8 = img7.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon img9 = new ImageIcon(img8);
        JLabel jl3 = new JLabel(img9);
        jl3.setBounds(5, 5, 20, 50);
        p1.add(jl3);

        jl3.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent ae) {
                    try {
                        dout.writeUTF("-1");
                    } catch(IOException ioe) {

                    }
                    System.exit(0);
                }
        });
        JLabel l2 = new JLabel("Server");
        l2.setFont(new Font("SAN_SERIF", Font.BOLD, 17));
        l2.setForeground(Color.white);
        l2.setBounds(77, 17, 80, 17);
        p1.add(l2);

        JLabel l3 = new JLabel("Active Now");
        l3.setFont(new Font("SAN_SERIF", Font.PLAIN, 10));
        l3.setForeground(Color.white);
        l3.setBounds(77, 40, 120, 10);
        p1.add(l3);
        
        Timer t = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(!typing) {
                    l3.setText("Active now");
                }
            }
        });
       t.setInitialDelay(2500);

        a1 = new JPanel();
        //a1.setBounds(5, 65, 440, 540);
        a1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        //f1.add(a1);
    
        JScrollPane sp = new JScrollPane(a1);
        sp.setBounds(5, 65, 440, 540);
        sp.setBorder(BorderFactory.createEmptyBorder());
            
        ScrollBarUI ui = new BasicScrollBarUI() {
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.white);
                this.thumbColor = new Color(7, 94, 84);
                return button;
            }
    
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createIncreaseButton(orientation);
                button.setBackground(new Color(7, 94, 84));
                button.setForeground(Color.white);
                return button;
            }
        };
    
        sp.getVerticalScrollBar().setUI(ui);
        f1.add(sp);

        t1 = new JTextField();
        t1.setBounds(5, 610, 315, 35);
        t1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        t1.setText("Enter 16 Bit Key");
        f1.add(t1);

        t1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                l3.setText("typing...");
                t.stop();
                typing = true;
            }

            public void keyReleased(KeyEvent ke) {
                typing = false;
                if(!t.isRunning()) {
                    t.start();
                }
            }
        });

        b1 = new JButton("Send");
        b1.setBounds(325, 610, 120, 35);
        b1.setBackground(new Color(7, 94, 84));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        b1.addActionListener(this);
        
        f1.add(b1);
        

        f1.getContentPane().setBackground(Color.white);
        f1.setLayout(null);
        f1.setSize(450, 650);
        f1.setLocation(900, 160);
        f1.setUndecorated(true);
        f1.setVisible(true);       // always write at end, because it applies the changes done by us on frame
    }

    public void actionPerformed(ActionEvent ae) {
        String out = t1.getText(), cipherText;
        cipherText = out;
        a1.setLayout(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());
        if(cipherText.equals("-1")) {
            
        } else if(flag == 0) {

            key = out;
        } else if (cipherText.equals("-cKey")) {

            flag = 0;
        } else {

            //a1.setText(a1.getText()+"\nMe: "+out);
            if(out.equals("-voice_msg")) {
                out = SpeechToText.toText();
            }
            
            try {
                sendTextToFile(out);
            } catch (FileNotFoundException fe) {

            }

            aes = new AESAlgo(key);
            try {
                cipherText = aes.encrypt(out);
            } catch(Exception e) {

            }
            
            JPanel p2 = formatLabel(out, 0);
            right.add(p2, BorderLayout.AFTER_LINE_ENDS);
            vertical.add(right);
               
        }

        try {
            dout.writeUTF(cipherText);
        } catch(IOException ioe) {
        
        }

        vertical.add(Box.createVerticalStrut(15));
        a1.add(vertical, BorderLayout.PAGE_START);

        if(out.equals("-cKey"))
            t1.setText("Enter 16 Bit Key");
        else {
            t1.setText("");
            flag = 1;
        }
    }

    public void sendTextToFile(String message) throws FileNotFoundException {
        
        try {
            File file = new File("chat.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            pr.println("Client: "+message);
            pr.close();
            br.close();
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out, int fg) {
        JPanel p3 = new JPanel();
        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>");
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        if(fg == 0)
            l1.setBackground(new Color(37, 211, 102));
        else if(fg == 1)
            l1.setBackground(new Color(169, 169, 169));
        
        l1.setOpaque(true);
        l1.setBorder(new EmptyBorder(15, 15, 15, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));

        p3.add(l1);
        p3.add(l2);
        return p3;
    }

    public static void main(String[] args) {
        new Client();
        f1.setVisible(true);
        String input_msg = "";
        String decipherText;

        try {
            s = new Socket("127.0.0.1", 6001);
        } catch(IOException iex) {

        }
        while(true) {
            try {
                
                din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                input_msg = din.readUTF();

                if(input_msg.equals("-1")) {
                    dout.writeUTF("-1");
                    System.exit(0);
                }
                if(input_msg.equals("-cKey")) {
                    flag = 0;
                    t1.setText("Enter 16 Bit Key");
                }

                if(flag == 1) {

                        aes = new AESAlgo(key);
                        decipherText = aes.decrypt(input_msg);
                        JPanel p2 = formatLabel(decipherText, 1);
                        JPanel left = new JPanel(new BorderLayout());
                        left.add(p2, BorderLayout.LINE_START);
                        vertical.add(left);
                        f1.validate();
                        //a1.setText(a1.getText()+"\nServer: "+input_msg);
                        //a1.setText(a1.getText()+"\nServer: "+decipherText);
                    
                }
            } catch(Exception ex) {

            }
        }

    }
}