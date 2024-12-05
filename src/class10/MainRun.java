package class10 ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainRun{
	public static void main(String[] args) {
 
		JFrame frame = new JFrame("Main Menu");
		frame.setLayout(new GridLayout(3, 1, 10, 10));

		JButton registerButton = new JButton("Register");
		JButton loginButton = new JButton("Login");
		JButton exitButton = new JButton("Exit");
		
		frame.add(registerButton);
		frame.add(loginButton);
		frame.add(exitButton);

		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
     
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new RegistrationForm();
			}
		});

		loginButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				frame.dispose(); 
				new LoginFrame();
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });
	}
}
