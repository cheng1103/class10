package class10;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegistrationForm extends JFrame{
	//public static void mainshowRegistrationForm() {
	public RegistrationForm() {
		//JFrame frame = new JFrame("Registration Form");
		setLayout(new GridLayout(5, 2, 10, 10));
		
		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField(10);
		JLabel emailLabel = new JLabel("Email:");
		JTextField emailField = new JTextField();
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField();
		JLabel phoneLabel = new JLabel("Phone Number:");
		JTextField phoneField = new JTextField();
		JButton registerButton = new JButton("Register");
		JButton backButton = new JButton("Back to Menu");

		add(usernameLabel);
		add(usernameField);
		add(emailLabel);
		add(emailField);
		add(passwordLabel);
		add(passwordField);
		add(phoneLabel);
		add(phoneField);
		add(new JLabel());
		add(registerButton);
		add(backButton);

		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());
				String phoneNumber = phoneField.getText();

				if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Connection conn = null;
				try {
                  
					DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
					String dbURL = "jdbc:sqlserver://localhost;encrypt=true;trustServerCertificate=true;databaseName=BadmintonCourtBookingSystem";
					String user = "sa";
					String pass = "123";

					conn = DriverManager.getConnection(dbURL, user, pass);

					if (conn != null) {
                        
						String insertQuery = "INSERT INTO [User] (Username, Email, Password, PhoneNumber) VALUES (?, ?, ?, ?)";
						PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
						preparedStatement.setString(1, username);
						preparedStatement.setString(2, email);
						preparedStatement.setString(3, password);
						preparedStatement.setString(4, phoneNumber);

						int row = preparedStatement.executeUpdate();
						if (row > 0) {
							JOptionPane.showMessageDialog(null, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
						} 
						else {
							JOptionPane.showMessageDialog(null, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
						}
						preparedStatement.close();
					}
                } 
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                } 
				finally {
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} 
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
				MainRun.main(null); 
			}
		});
	}
	}
