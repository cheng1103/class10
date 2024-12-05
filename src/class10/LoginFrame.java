package class10;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginFrame extends JFrame{
	//public void showLoginForm() {
	public LoginFrame() {
		//JFrame frame = new JFrame("Login Form");
		setLayout(new GridLayout(3, 2, 10, 10));
		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField(10);
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField();
		JButton loginButton = new JButton("Login");
		JButton backButton = new JButton("Back to Menu");
	
		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(backButton);
		add(loginButton);
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		loginButton.addActionListener(new ActionListener() {
            
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
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
						String query = "SELECT * FROM [User] WHERE Username = ? AND Password = ?";
						PreparedStatement preparedStatement = conn.prepareStatement(query);
						preparedStatement.setString(1, username);
						preparedStatement.setString(2, password);

						ResultSet resultSet = preparedStatement.executeQuery();

						if (resultSet.next()) {
							JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
						} 
						else {
							JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
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
