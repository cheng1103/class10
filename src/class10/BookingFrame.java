package class10;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class BookingFrame extends JFrame {
	public BookingFrame() {
		setTitle("Court Booking");
		setLayout(new BorderLayout());

		JLabel dateLabel = new JLabel("Enter Date (YYYY-MM-DD):");
		JTextField dateField = new JTextField();
		JButton checkButton = new JButton("Check Availability");
		JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		inputPanel.add(dateLabel);
		inputPanel.add(dateField);
		inputPanel.add(checkButton);
     
		JPanel availabilityPanel = new JPanel();
		availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(availabilityPanel);
      
		JButton bookButton = new JButton("Book Selected");
		bookButton.setEnabled(false); 
		JPanel actionPanel = new JPanel(new FlowLayout());
		actionPanel.add(bookButton);
		
		add(inputPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(actionPanel, BorderLayout.SOUTH);	
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
     
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = dateField.getText().trim();
				if (date.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a valid date!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				availabilityPanel.removeAll();
				bookButton.setEnabled(false);

				try (Connection conn = DriverManager.getConnection(
						"jdbc:sqlserver://localhost;encrypt=true;trustServerCertificate=true;databaseName=BadmintonCourtBookingSystem",
						"sa", "123")) {

					String query = "select c.id AS court_id, c.CourtName, t.id as timeslot_id, t.start_time, t.end_time " +
							"from Court c " +
							"join CourtAvailability ca on c.id = ca.court_id " +
							"join TimeSlots t on t.id = ca.timeslot_id " +
							"where ca.availability_date = ? " +
							"and ca.court_id not in ( " +
							"select court_id from Bookings where booking_date = ? AND start_time = t.id" +
							")";

					PreparedStatement ps = conn.prepareStatement(query);
					ps.setString(1, date);
					ps.setString(2, date);

					ResultSet rs = ps.executeQuery();

					boolean hasResults = false;
					while (rs.next()) {
						hasResults = true;
						int courtId = rs.getInt("court_id");
						String courtName = rs.getString("CourtName");
						int timeSlotId = rs.getInt("timeslot_id");
						Time startTime = rs.getTime("start_time");
						Time endTime = rs.getTime("end_time");
						JCheckBox checkBox = new JCheckBox(
								"Court: " + courtName + " | Time: " + startTime + " - " + endTime);
						checkBox.putClientProperty("court_id", courtId);
						checkBox.putClientProperty("timeslot_id", timeSlotId);

						availabilityPanel.add(checkBox);
					}

					if (!hasResults) {
						availabilityPanel.add(new JLabel("No available courts for the selected date."));
                    } 
					else {
						bookButton.setEnabled(true);
                    }

					availabilityPanel.revalidate();
					availabilityPanel.repaint();
                }	catch (Exception ex) {
                	JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		
		
		 
		 }
}