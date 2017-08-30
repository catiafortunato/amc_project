import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;

public class Login extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textField;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			        // Determine the new location of the window
			        int w = frame.getSize().width;
			        int h = frame.getSize().height;
			        int x = (dim.width-w)/2;
			        int y = (dim.height-h)/2;
			        // Move the window
			        frame.setLocation(x, y);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Welcome                                          ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 403, 241);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblClusteringParaModelos = new JLabel("CLUSTERING FOR PHARMACOKINETICS MODELS:");
		lblClusteringParaModelos.setBounds(6, 6, 346, 22);
		lblClusteringParaModelos.setForeground(new Color(0,160,228));
		lblClusteringParaModelos.setFont(new Font("Calibri", Font.BOLD, 16));
		contentPane.add(lblClusteringParaModelos);
		
		JLabel lblWelcomePleaseInsert = new JLabel("Please insert your credentials below:");
		lblWelcomePleaseInsert.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblWelcomePleaseInsert.setBounds(10, 39, 289, 14);
		contentPane.add(lblWelcomePleaseInsert);
		
		JLabel lblUsername = new JLabel("USERNAME:");
		lblUsername.setForeground(Color.GRAY);
		lblUsername.setFont(new Font("Calibri", Font.BOLD, 18));
		lblUsername.setBounds(30, 78, 98, 14);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("PASSWORD:");
		lblPassword.setForeground(Color.GRAY);
		lblPassword.setFont(new Font("Calibri", Font.BOLD, 18));
		lblPassword.setBounds(30, 115, 98, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Calibri", Font.PLAIN, 12));
		passwordField.setBounds(130, 110, 123, 22);
		contentPane.add(passwordField);
		
		textField = new JTextField();
		textField.setFont(new Font("Calibri", Font.PLAIN, 12));
		textField.setBounds(130, 73, 123, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("LOGIN");
		btnNewButton.setFont(new Font("Calibri", Font.PLAIN, 16));
		btnNewButton.setBackground(new Color(0,160,228));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String uname = textField.getText();
				String ps = passwordField.getText();
				if(uname.equals("amc")&& ps.equals("amc2017")){
					Interface nw= new Interface();
					nw.NewScreen();
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(contentPane, "Invalid Username or Password, try again");
				}
				
			}
		});
		btnNewButton.setBounds(261, 152, 109, 39);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(338, 6, 56, 47);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		Image img= new ImageIcon(this.getClass().getResource("IST_A_NEG.png")).getImage();
		Image newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		lblNewLabel.setIcon(new ImageIcon(newimg));
		lblNewLabel_1.setBounds(339, 10, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 63, 364, 82);
		contentPane.add(panel);
		
	
	
	}
}
