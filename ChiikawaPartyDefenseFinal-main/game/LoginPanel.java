package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JFrame parentFrame;

    public LoginPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(null);
        setBackground(Color.DARK_GRAY);

        JLabel title = new JLabel("Login to Play");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(120, 40, 300, 40);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(80, 120, 100, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(180, 120, 150, 30);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(80, 170, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(180, 170, 150, 30);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 230, 120, 35);
        add(loginButton);

        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBounds(100, 280, 250, 30);
        add(statusLabel);

        // Login button logic
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword());

                if (user.isEmpty() || pass.isEmpty()) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Please fill in all fields!");
                    return;
                }

                // Authenticate with database
                boolean isAuthenticated = HttpClient.authenticateUser(user, pass);
                
                if (isAuthenticated) {
                    statusLabel.setForeground(Color.GREEN);
                    statusLabel.setText("Login successful!");
                    
                    // Store the logged-in username
                    GameFrame.setCurrentUser(user);

                    SwingUtilities.invokeLater(() -> {
                        parentFrame.setContentPane(new game.HomeScreenPanel(parentFrame));
                        parentFrame.revalidate();
                    });

                } else {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Invalid credentials!");
                }
            }
        });
    }
}
