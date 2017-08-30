import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.beans.PropertyChangeEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.awt.Dialog.ModalExclusionType;

public class Interface {

	private JFrame frmClusteringForPharmacokinetics;
	private JTextField Mixture;
	private JTextField exportFile;
	private JTextField FinalDirec;
	private JTextField csvfield;
	private JTextField parametersField;


	/* -> A funcao intguard garante que o numero de misturas e um inteiro positivo */

	boolean intguard(String s){
		try{
			Integer.parseInt(s);
			if(Integer.parseInt(s)>0)
				return true;
			else{return false;}
		}
		catch(NumberFormatException nfe){
			return false;
		}
	}

	int M =0;
	private JTextField txtWaiting;

	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frmClusteringForPharmacokinetics.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/* -> A funcao randommix cria uma mistura aleatoria */

	public Mistura randommix(int M){
		LinkedList <thetaj> Theta = new LinkedList<thetaj>();
		Random generator = new Random();
		double normw=0;		
		for (int i=0; i<M; i++){
			double w = generator.nextDouble();
			double sigma = 1+(5-1)*generator.nextDouble();
			double a = 5+(200-5)*generator.nextDouble();
			double b1 = generator.nextDouble()*5;
			if (b1==0 || b1==5){
				b1 = generator.nextDouble()*5;
			}
			double b2 = b1+(5-b1)*generator.nextDouble();
			if ( b2==b1 || b2==5){
				b2 = b1+(5-b1)*generator.nextDouble();
			}

			thetaj t = new thetaj(w, sigma, a, -a, b1, b2);
			Theta.add(t);

			normw = normw + w;
		}
		for(int j=0; j<M; j++){
			Theta.get(j).w/= normw;
		}
		Mistura mix = new Mistura (M, Theta);
		return mix;
	}


	/* -> A função readmix lê um ficheiro .theta com uma mistura*/

	public Mistura readmix(String filename){
		Scanner s;
		LinkedList<thetaj> Theta = new LinkedList<>();
		try{
			s = new Scanner (new File(filename));
			s.useLocale(Locale.ENGLISH);
			while(s.hasNext()){
				double w = s.nextDouble();
				double sigma = s.nextDouble();
				double a1 = s.nextDouble();
				double a2 = s.nextDouble();
				double b1 = s.nextDouble();
				double b2 = s.nextDouble();
				thetaj t = new thetaj(w, sigma, a1, a2, b1, b2);
				Theta.add(t);
			}
			s.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		Mistura mix = new Mistura(Theta.size(), Theta);
		return (mix);
	}


	/* -> A função write exporta um ficheiro .txt com a mistura inicial e os resultados da simulacao */

	public void write (Mistura mix, Mistura inicial) throws IOException{
		try{
			PrintWriter out= new PrintWriter (FinalDirec.getText()+exportFile.getText()+".txt", "UTF-8");
			out.println("Here is your initial Theta:" + " "+ inicial.Theta);
			out.println("\n");
			out.println("Here is your final Theta:"+ " " + mix.Theta );
			out.println("\n");
			out.println("\n");
			for(int i=0 ; i<mix.M ; i++){
				out.println("theta "+(i+1)+ ": ");
				out.println("Value for w: " + " " + mix.Theta.get(i).w +"");
				out.println("Value for sigma:" + " " + mix.Theta.get(i).sigma+" ");
				out.println("Value for a1:" + " " + mix.Theta.get(i).a1+" ");
				out.println("Value for a2:" + " " + mix.Theta.get(i).a2+" ");
				out.println("Value for b1:"+ " "  + mix.Theta.get(i).b1+" ");
				out.println("Value for b2:"+ " "  + mix.Theta.get(i).b2+" ");
				out.println("\n");
			}
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/* -> A função writetheta exporta um ficheiro .theta com os resultados da simulacao */

	public void writetheta (Mistura mix) throws IOException{
		try{
			PrintWriter out= new PrintWriter (FinalDirec.getText()+exportFile.getText()+".theta");
			for(int i=0 ; i<mix.M ; i++){
				out.println(mix.Theta.get(i).w + " ");
				out.println(mix.Theta.get(i).sigma + " ");
				out.println(mix.Theta.get(i).a1 + " ");
				out.println(mix.Theta.get(i).a2 + " ");
				out.println(mix.Theta.get(i).b1 + " ");
				out.println(mix.Theta.get(i).b2 + " ");
				out.println('\n');

			}
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmClusteringForPharmacokinetics = new JFrame();
		frmClusteringForPharmacokinetics.setTitle("Clustering for pharmacokinetics models");
		frmClusteringForPharmacokinetics.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frmClusteringForPharmacokinetics.setResizable(false);
		frmClusteringForPharmacokinetics.getContentPane().setBackground(new Color(255, 255, 255));
		frmClusteringForPharmacokinetics.setBounds(100, 100, 472, 317);
		frmClusteringForPharmacokinetics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClusteringForPharmacokinetics.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 425, 1);
		frmClusteringForPharmacokinetics.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(10, 11, 46, 14);
		panel.add(label);

		JLabel lblClusteringParaModelos = new JLabel("CLUSTERING FOR PHARMACOKINETICS MODELS:");
		lblClusteringParaModelos.setBounds(18, 12, 400, 22);
		lblClusteringParaModelos.setForeground(new Color(0,160,228));
		lblClusteringParaModelos.setFont(new Font("Calibri", Font.BOLD, 19));
		frmClusteringForPharmacokinetics.getContentPane().add(lblClusteringParaModelos);

		JLabel lblInsiraOModo = new JLabel("Please import the file with the data:");
		lblInsiraOModo.setBounds(20, 42, 342, 16);
		lblInsiraOModo.setFont(new Font("Calibri", Font.PLAIN, 12));
		frmClusteringForPharmacokinetics.getContentPane().add(lblInsiraOModo);

		JButton csvField = new JButton(".csv File");
		csvField.setFont(new Font("Calibri", Font.PLAIN, 12));
		csvField.setBackground(Color.WHITE);
		csvField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileFilter(new FileNameExtensionFilter(".csv","CSV"));
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename = f.getAbsolutePath();
				csvfield.setText(filename.replace("\\", "/")+"/"); 

			}
		});
		csvField.setBounds(20, 62, 92, 21);
		frmClusteringForPharmacokinetics.getContentPane().add(csvField);

		csvfield = new JTextField();
		csvfield.setFont(new Font("Calibri", Font.PLAIN, 11));
		csvfield.setColumns(10);
		csvfield.setBounds(122, 63, 89, 20);
		frmClusteringForPharmacokinetics.getContentPane().add(csvfield);

		JLabel lblInsiraONmero = new JLabel("Please insert the way of getting your mixtures:");
		lblInsiraONmero.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblInsiraONmero.setBounds(20, 100, 415, 16);
		frmClusteringForPharmacokinetics.getContentPane().add(lblInsiraONmero);

		JLabel NrMixtures = new JLabel("Number of mixtures:");
		NrMixtures.setFont(new Font("Calibri", Font.BOLD, 13));
		NrMixtures.setForeground(new Color(0,160,228));
		NrMixtures.setBounds(22, 148, 124, 14);
		frmClusteringForPharmacokinetics.getContentPane().add(NrMixtures);


		JLabel lblNowChooseThe = new JLabel("Now choose the directory and the name of the files to export:");
		lblNowChooseThe.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblNowChooseThe.setBounds(20, 190, 415, 16);
		frmClusteringForPharmacokinetics.getContentPane().add(lblNowChooseThe);

		JLabel Name = new JLabel("Name of exported files:");
		Name.setForeground(new Color(0,160,228));
		Name.setFont(new Font("Calibri", Font.BOLD, 13));
		Name.setBounds(21, 240, 139, 14);
		frmClusteringForPharmacokinetics.getContentPane().add(Name);

		Mixture = new JTextField();
		Mixture.setFont(new Font("Calibri", Font.PLAIN, 11));
		Mixture.setColumns(10);
		Mixture.setBounds(144, 148, 86, 20);
		frmClusteringForPharmacokinetics.getContentPane().add(Mixture);

		exportFile = new JTextField();
		exportFile.setFont(new Font("Calibri", Font.PLAIN, 11));
		exportFile.setColumns(10);
		exportFile.setBounds(160, 240, 89, 20);
		frmClusteringForPharmacokinetics.getContentPane().add(exportFile);

		JLabel lblNewLabel = new JLabel("");
		Image img= new ImageIcon(this.getClass().getResource("IST_A_NEG.png")).getImage();
		Image newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);   
		lblNewLabel.setIcon(new ImageIcon(newimg));
		lblNewLabel.setBounds(408, 5, 50, 55);
		frmClusteringForPharmacokinetics.getContentPane().add(lblNewLabel);

		FinalDirec = new JTextField();
		FinalDirec.setFont(new Font("Calibri", Font.PLAIN, 11));
		FinalDirec.setColumns(10);
		FinalDirec.setBounds(122, 210, 158, 20);
		frmClusteringForPharmacokinetics.getContentPane().add(FinalDirec);
		
		JLabel lblStatus = new JLabel("STATUS:");
		lblStatus.setForeground(new Color(128, 128, 128));
		lblStatus.setFont(new Font("Calibri", Font.BOLD, 14));
		lblStatus.setBounds(324, 215, 60, 14);
		frmClusteringForPharmacokinetics.getContentPane().add(lblStatus);
		JLabel statusnew = new JLabel("");
		statusnew.setFont(new Font("Calibri", Font.PLAIN, 13));
		statusnew.setBounds(378, 215, 60, 14);
		frmClusteringForPharmacokinetics.getContentPane().add(statusnew);
		statusnew.setText("WAITING...");
		
		

		JButton Directory = new JButton("Directory");
		Directory.setFont(new Font("Calibri", Font.PLAIN, 12));
		Directory.setBackground(Color.WHITE);
		Directory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename = f.getAbsolutePath();
				FinalDirec.setText(filename.replace("\\", "/")+"/"); 
			}
		});

		JCheckBox SetParameters = new JCheckBox("Set Parameters");
		SetParameters.setBackground(Color.WHITE);

		SetParameters.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					parametersField.setEnabled(true);
					parametersField.setText("");
					Mixture.setEnabled(false);
					Mixture.setText("");
					SetParameters.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFileChooser chooser = new JFileChooser();
							chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							chooser.setFileFilter(new FileNameExtensionFilter(".theta","THETA"));
							chooser.showOpenDialog(null);
							File f = chooser.getSelectedFile();
							String filename = f.getAbsolutePath();
							parametersField.setText(filename.replace("\\", "/")+"/"); 

						}});
					
				}
				else if(e.getStateChange() == ItemEvent.DESELECTED){
					parametersField.setEnabled(false);
					parametersField.setText("");
					Mixture.setEnabled(true);
					Mixture.setText("");
					SetParameters.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFileChooser chooser = new JFileChooser();
							chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							chooser.setFileFilter(new FileNameExtensionFilter(".theta","THETA"));
							File f = chooser.getSelectedFile();
							String filename = f.getAbsolutePath();
							parametersField.setText(filename.replace("\\", "/")+"/"); 
							
						}});
				}
			}
		});
		SetParameters.setBounds(20, 120, 124, 23);
		frmClusteringForPharmacokinetics.getContentPane().add(SetParameters);

		parametersField = new JTextField();
		parametersField.setEnabled(false);
		parametersField.setFont(new Font("Calibri", Font.PLAIN, 11));
		parametersField.setColumns(10);
		parametersField.setBounds(220, 120, 92, 20);
		frmClusteringForPharmacokinetics.getContentPane().add(parametersField);

		Directory.setBounds(20, 210, 92, 21);
		frmClusteringForPharmacokinetics.getContentPane().add(Directory);{

			JButton Submit = new JButton("SUBMIT");
			Submit.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					statusnew.setText("RUNNING...");
				}
			});
			
			Submit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
	
					/*-> Guardas para garantir que o utilizador preenche corretamente a caixa de dialogo */

					if(csvfield.getText().equals("")||FinalDirec.getText().equals("")||exportFile.getText().equals("")){
						JOptionPane.showMessageDialog(null,"Please, fill out any blank spaces left");
					}else{		
						
						/* -> Caso em que o utilizador prefere importar um ficheiro .theta com uma aproximacao inicial */
						
						if(!parametersField.getText().equals("")){
						
														
							// criacao da amostra inicial e da mistura de acordo com o ficheiro .THETA
							
							Amostra amt = new Amostra ();
			
							amt.read(csvfield.getText());
							
							Mistura mix = readmix(parametersField.getText());
							
							//Aplicacao do algoritmo
							mix.EM(amt);

							try {
								//Exportacao das misturas iniciais e das otimizadas e do ficheiro .THETA com os resultados
								write(mix, readmix(parametersField.getText()));
								writetheta(mix);
								JOptionPane.showMessageDialog(null, "DONE! Two files were created in the chosen directory. \r\n (One .txt file and one .theta file with the results)");
								statusnew.setText("DONE!");

							} catch (IOException e) {
								e.printStackTrace();
							}

						}else{
							
						/* -> Caso em que o utilizador prefere utilizar uma mistura inicial aleatoria */	
							
							if(Mixture.getText().equals("")||!intguard(Mixture.getText()))
								JOptionPane.showMessageDialog(null,"The number of classes must be a positive integer");
							else{
								
								// criacao da amostra inicial e da mistura aleatoria com M misturas de gaussianas

								M = Integer.parseInt(Mixture.getText()); 

								Amostra amt = new Amostra();
								
								amt.read(csvfield.getText());	
								
								Mistura mix = randommix(M);
								
								LinkedList<thetaj> aux = new LinkedList<>();
								
								aux.addAll(mix.Theta);
								
								Mistura initial = new Mistura(M, aux);
								
								//aplicacao do algoritmo
								mix.EM(amt);

								try {
									//Exportacao das misturas iniciais e das otimizadas e do ficheiro .THETA com os resultados

									write(mix, initial);
									writetheta(mix);
									JOptionPane.showMessageDialog(null, "DONE! Two files were created in the chosen directory. \r\n (One .txt file and one .theta file with the results)");
									statusnew.setText("DONE!");

								} catch (IOException e) {
									e.printStackTrace();
								}

							}
						}
					}
				}
			});

			Submit.setFont(new Font("Calibri", Font.PLAIN, 16));
			Submit.setForeground(new Color(0, 0, 0));

			Submit.setBackground(new Color(0,160,228));
			Submit.setBounds(310, 234, 142, 41);
			frmClusteringForPharmacokinetics.getContentPane().add(Submit);

			JLabel lbltxtFile = new JLabel(".theta File:");
			lbltxtFile.setForeground(new Color(0, 160, 228));
			lbltxtFile.setFont(new Font("Calibri", Font.BOLD, 13));
			lbltxtFile.setBounds(155, 123, 60, 14);
			frmClusteringForPharmacokinetics.getContentPane().add(lbltxtFile);

			JSeparator separator = new JSeparator();
			separator.setForeground(Color.GRAY);
			separator.setBounds(20, 92, 415, 1);
			frmClusteringForPharmacokinetics.getContentPane().add(separator);

			JSeparator separator_1 = new JSeparator();
			separator_1.setForeground(Color.GRAY);
			separator_1.setBounds(20, 180, 415, 1);
			frmClusteringForPharmacokinetics.getContentPane().add(separator_1);
			
			
			

			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((dimension.getWidth() - frmClusteringForPharmacokinetics.getWidth()) / 2);
			int y = (int) ((dimension.getHeight() - frmClusteringForPharmacokinetics.getHeight()) / 2);
			frmClusteringForPharmacokinetics.setLocation(x, y);

		}
	}	
}