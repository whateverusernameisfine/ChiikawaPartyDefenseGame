//package game;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class GalleryPanel extends JPanel {
//    public GalleryPanel(JFrame frame) {
//        setLayout(null);
//        setBackground(Color.BLACK);
//
//        JLabel comingSoon = new JLabel("Gallery - Coming Soon!");
//        comingSoon.setFont(new Font("Arial", Font.BOLD, 36));
//        comingSoon.setForeground(Color.WHITE);
//        comingSoon.setBounds(500, 300, 600, 100);
//        add(comingSoon);
//
//        JButton backButton = new JButton("Back");
//        backButton.setBounds(30, 30, 100, 40);
//        add(backButton);
//
//        backButton.addActionListener(e -> {
//            frame.setContentPane(new HomeScreenPanel(frame));
//            frame.revalidate();
//        });
//    }
//}
