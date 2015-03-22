import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;

import com.mysql.jdbc.Statement;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LoginGUI extends JFrame implements ActionListener  {
	
	static String id;
	private Task task;
	private JFrame frame;
	private JLabel logo;
	private JPanel panel, controls, buttons;
    private JButton submit, close, minimize, register;
    private JTextField username;
    private JPasswordField password;
    
    
    public void create() {
    	
    	frame = new JFrame();
    	panel    = new JPanel();
    	controls = new JPanel();
		buttons  = new JPanel();
    	register = new JButton("Don't have an Account?");

    	username = new JTextField("Email");
    	password = new JPasswordField("Password");
    	
    	submit   = new JButton("LOGIN");
    	close    = new JButton(exitAction); 
		minimize = new JButton(minimizeAction);
		
		panel.setLayout(new MigLayout("insets 0"));
		controls.setLayout(new MigLayout("insets 0"));

    }
    
    
    public void style() {
    	
    	
    	ArrayList<JButton> allButtons = new ArrayList<>();
    	ArrayList<String>  allIcons   = new ArrayList<>();
    	int iconIndex = 0;
    	allIcons.add("exitIcon.png");
    	allIcons.add("miniIcon.png");    	
    	allButtons.add(close);
    	allButtons.add(minimize);

    	for(JButton buttons: allButtons) {
    	
	    	try {
				
				Image      img        = ImageIO.read(new File("assets/img/"+allIcons.get(iconIndex)));
				Image      imgResized = img.getScaledInstance(16, 16,Image.SCALE_SMOOTH);
				ImageIcon  imageIcon = new ImageIcon(imgResized);
				buttons.setIcon(imageIcon);
			
			} catch (IOException ex) { }
			
			Border emptyBorder = BorderFactory.createEmptyBorder(10,0,10,10);
			buttons.setBorder(emptyBorder);
			buttons.setBorderPainted(false);
			buttons.setContentAreaFilled(false);
			buttons.setFocusPainted(false);
			iconIndex++;
			
    	}

    	
		try {
            Image image = ImageIO.read(new File("assets/img/logo.png"));
			Image  resized  = image.getScaledInstance(124, 124,Image.SCALE_SMOOTH);
            ImageIcon logoImage = new ImageIcon(resized);
    		logo = new JLabel(logoImage);
    		
        } catch (IOException k) { k.printStackTrace(); }
		
		
		Color aColor = new Color(0xacacac);
		Font font = new Font("Open Sans", Font.PLAIN, 15);
		
		username.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		username.setForeground(aColor);
		username.setHorizontalAlignment(SwingConstants.CENTER);
		username.setFont(font);
		username.addFocusListener(new ActiveFields());
		
		password.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		password.setForeground(aColor);
		password.setHorizontalAlignment(SwingConstants.CENTER);
		password.setFont(font);
		password.addFocusListener(new ActiveFields());
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		submit.setOpaque(true);
		submit.setBorder(emptyBorder);
		submit.setBackground(new Color(0xe95e59));
		submit.setForeground(Color.WHITE);
		submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Font rfont = new Font("Open Sans", Font.PLAIN, 13);
    	register.setOpaque(false);
    	register.setContentAreaFilled(false);
		register.setFont(rfont);
		
    	register.setBorder(emptyBorder);
    	register.setForeground(Color.BLACK);
    	register.setCursor(new Cursor(Cursor.HAND_CURSOR));

    	panel.setBackground(Color.WHITE);
		buttons.setBackground(new Color(0,0,0,0));
		controls.setBackground(new Color(0,0,0,0));
    	
    }
    
    
    public void add() {
    			
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);
		cr.setSnapSize(new Dimension(10, 10));

		ComponentDraggable cd = new ComponentDraggable();
		cd.setDraggable(controls, frame);
		
		submit.addActionListener(this);
		
		buttons.add(minimize);
		buttons.add(close);
		controls.add(buttons, "push, top, right");
		
		String constraints = "w 70%, hmin 60px, wrap, top, gap 15% 15% 0 25px";
		panel.add(controls, "growx, wrap, top");
		panel.add(logo, "growx, wrap, gap 0 0 10 30");
		panel.add(username, constraints);
		panel.add(password, constraints);
		panel.add(submit, "w 70%, hmin 55px, hmax 55px, wrap, top, gap 15% 15% 10px 00px");
		panel.add(register, ", growx, wrap, top, gap 15% 15% 25px 10px");
		
		frame.setUndecorated(true);
		frame.setContentPane(new ShadowPane());
		frame.add(panel);
        frame.setBackground(new Color(0,0,0,0)); 

		frame.requestFocusInWindow();
		frame.getRootPane().setDefaultButton(submit);
		frame.setSize(380, 555);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
    	
    }
    

	public LoginGUI() {		
		
		create();
		style();
		add();
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class ActiveFields implements FocusListener{
		
		public void focusGained(FocusEvent e) {

			JTextComponent source = (JTextComponent) e.getSource();
	        
			source.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED));
			source.setForeground(Color.BLACK);
			
			if(source.getText().equals("Email"))    source.setText("");
			if(source.getText().equals("Password")) source.setText("");
			
		}

		public void focusLost(FocusEvent e) {

			JTextComponent source = (JTextComponent) e.getSource();
			
			source.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
			source.setForeground(new Color(0xacacac));

			if(source.getText().trim().isEmpty()) {
				if(source== username) source.setText("Email");
				if(source== password) source.setText("Password");
			}
			
		} 

	}


	private final Action exitAction = new AbstractAction("") {

		public void actionPerformed(ActionEvent e) {
			System.exit(0);
	    }
		
	};
	
	private final Action minimizeAction = new AbstractAction("") {

		public void actionPerformed(ActionEvent e) {
			frame.setState(JFrame.ICONIFIED);
		}
		
	};
	   
	@SuppressWarnings("unused")
	private final Action maximizeAction = new AbstractAction("") {
	
		public void actionPerformed(ActionEvent e) {
			//frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		}
	};
	
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	class Task extends SwingWorker<Boolean, Void> {
       
        public Boolean doInBackground() throws SQLException {

        	try {
				Thread.sleep(3800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
        	Connection conn = null;
        	ResultSet resultSet = null;
        	String passwordString = null ;
    		
        	try {
    			
    			Class.forName("com.mysql.jdbc.Driver");
    			
    			passwordString = new String(password.getPassword());
    	        
    			conn = DriverManager.getConnection("jdbc:mysql://localhost/programmingassistant", "root", "");
    	    	String query = "SELECT * FROM learner WHERE Email='" + username.getText() + "'AND Password='" + passwordString + "'";
    	    	Statement statement = (Statement) conn.createStatement(); 
    	    	resultSet = statement.executeQuery(query);

    		} catch (ClassNotFoundException e) { e.printStackTrace(); }
    		
    		
    		
    		
    		 Emailvalidator emailValidator = new Emailvalidator();
    		 
    		 if(!emailValidator.validate(username.getText())) {
		       
    		
    			 System.out.println("Invalid Email ID");
		        return false;
		        
    		 } else if (!resultSet.isBeforeFirst()) {
    			 
    			 return false;

    		 } else {
    		
    			 while(resultSet.next())  id = resultSet.getString("LearnerID");

    			 return true;
    		 
    		 }
    		
    		
    		
    		
//    	    		
//    		if (!resultSet.isBeforeFirst()) return false;
//    		else return true;
//        
    	
        }

        public void done() {
        	
        	try {				
        		
        		boolean status = get();
        		if(status == true) {
        			System.out.println("Access Granted");

        			new MainGUI().run();
        			frame.dispose();
        		}
				else 	{
					JLabel msgLabel = new JLabel("Your Email or Password are Incorrect", JLabel.CENTER); 

					JOptionPane.showMessageDialog (null, msgLabel, "", JOptionPane.PLAIN_MESSAGE);
			
			        
				}

   			 
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	submit.setIcon(null);
        	submit.setEnabled(true);
    		submit.setBackground(new Color(0xe95e59));
    		submit.setText("LOGIN");
    		
        	setCursor(null);
        	System.out.println("done");
        	
        	
        	
        }
    }
	

	
	
	public void actionPerformed(ActionEvent evt) {
		
		try {
	           
			File file = new File("assets\\img\\loader-animation.gif");
			URL url = file.toURI().toURL();
			Icon icon = new ImageIcon(url);

    		submit.setText("");
    		submit.setBackground(Color.WHITE);
    		submit.setEnabled(false);
    		submit.setDisabledIcon(icon);
			submit.setIcon(icon);
			frame.revalidate();
			
        } catch (IOException k) { k.printStackTrace(); }
		
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new Task();
        task.execute();
    
	}
	
	
	
	
	
	
	
	
	
	public class ShadowPane extends JPanel {

		public ShadowPane() {
			setLayout(new BorderLayout());
	        setOpaque(false);
	        setBackground(Color.BLACK);
	        setBorder(new EmptyBorder(12, 12, 2, 2));
	    }

	    @Override
	    public Dimension getPreferredSize() {
	    	return new Dimension(200, 200);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	    	Graphics2D g2d = (Graphics2D) g.create();
	    	g2d.setComposite(AlphaComposite.SrcOver.derive(0.2f));
	    	g2d.fillRect(10, 10, getWidth(), getHeight());
	    	g2d.dispose();
	    }
	  
	}
	
	
	
	public class Emailvalidator {

		private Pattern pattern;
		private Matcher matcher;

		private static final String EMAIL_PATTERN = 
		    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		public Emailvalidator() {
		    pattern = Pattern.compile(EMAIL_PATTERN);
		}

		/**
		 * Validate hex with regular expression
		 * 
		 * @param hex
		 *            hex for validation
		 * @return true valid hex, false invalid hex
		 */
		public boolean validate(final String hex) {

		    matcher = pattern.matcher(hex);
		    return matcher.matches();

		}
	}
	
	
}
