import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.miginfocom.swing.MigLayout;



public class TestGUI implements ActionListener, ItemListener {
	
	private JFrame frame;
	
	private JPanel panel;

	String dbLesson;
	static int retryCount;
	JButton answers;
	JButton submit;
	JPanel content;
	JButton retry;
	ArrayList<JPanel> questionPanel = new ArrayList<>();
	ArrayList<String> questionNames = new ArrayList<>();
	ArrayList<String> questionAnswers = new ArrayList<>();
	ArrayList<ButtonGroup> radioGroup  = new ArrayList<>();
	ArrayList<JComponent>  radioChoice = new ArrayList<>();
	
	
	JButton back;
	JLabel percentage;
	JLabel mark;
	
	
	
	
	CardLayout card = new CardLayout();
	
	public TestGUI(String currentLesson) {
		this.dbLesson = currentLesson;
	}
	
	public void run() throws SQLException {
		
		newElements();
		styleElements();
		addElements();
		
	}
	
	
	public void newElements() throws SQLException {
		
		frame = new JFrame();
		panel = new JPanel();


    	panel.setLayout(new MigLayout("insets 0 0, align center"));
    
    	content = new JPanel();
    	content.setLayout(card);
    	content.setBackground(Color.GREEN);
    	
    	ImageIcon logoImage = null;
    	try {
			Image image = ImageIO.read(new File("assets/img/FF4D00-0.png"));
			Image  resized  = image.getScaledInstance(300, 1,Image.SCALE_SMOOTH);
			logoImage = new ImageIcon(resized);
		
		} catch (IOException k) { k.printStackTrace(); }
    	
    	
    	
    	
    	
    	Connection conn = null;
	    String getConn = "jdbc:mysql://localhost/programmingassistant";
	    
		try {	
			Class.forName("com.mysql.jdbc.Driver");	        
			conn = DriverManager.getConnection(getConn, "root", "");  
		} catch (ClassNotFoundException e) {}
    		
	    String query = "SELECT * FROM questions where lessonID ='" + dbLesson +"' ORDER BY rand()";
	    Statement statement = (Statement) conn.createStatement(); 
	    ResultSet results = statement.executeQuery(query);

    	JPanel questionWrap = new JPanel();
    	questionWrap.setLayout(new MigLayout("insets 10"));
    	questionWrap.setBackground(new Color(0xf8f8f8));
    	
    	int questionCount = 0;
    	int radioCount = 0;
    	
    	while(results.next()) {
    		
    		//------------
    		questionNames.add(results.getString("questionName"));
    		questionAnswers.add(results.getString("questionAnswer"));
    		
    		//------------
    		
    		questionPanel.add(new JPanel());
    		JPanel currentPanel = questionPanel.get(questionCount);
    		
    		//------------
    		
    		currentPanel.setBackground(new Color(0,0,0,0));
    		currentPanel.setBackground(new Color(0xececec));
    		currentPanel.setLayout(new MigLayout("insets 20 0 20 0, wrap 1, align center"));
        	
    		//------------
    		
    		radioGroup.add(new ButtonGroup());
    		ButtonGroup currentGroup = radioGroup.get(questionCount);

    		//------------
    		
    		query = "SELECT * FROM options WHERE questionID='" + results.getString("questionID") + "' order by rand()";
        	statement = (Statement) conn.createStatement(); 
        	ResultSet optionRS = statement.executeQuery(query);

    		//------------
        	
          	JLabel questionTitle = new JLabel("<html><center>"+ results.getString("questionName") +"</center></html>", SwingConstants.CENTER);
        	questionTitle.setFont(new Font("Lucida Grande",Font.BOLD, 15));
        	currentPanel.add(questionTitle, "wmax 90%, align center, gap 0 10 20 20");

    		//------------
        	
        	while(optionRS.next()) {
        		
            	radioChoice.add(new JRadioButton(optionRS.getString("optionsName")));
            	JRadioButton currentRadio = (JRadioButton) radioChoice.get(radioCount);
		    	currentRadio.setActionCommand(optionRS.getString("optionsName"));
		    	currentGroup.add(currentRadio);
		    	
		    	currentRadio.setVerticalTextPosition(JRadioButton.BOTTOM);
		    	currentRadio.setHorizontalTextPosition(JRadioButton.CENTER);
		    	currentRadio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            	currentRadio.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xcccccc)));
            	currentRadio.setBorderPainted(true);
            	currentRadio.addItemListener(this);
            	currentRadio.setBackground(new Color(0xFFFFFF));
            	currentRadio.setIcon(logoImage);		   
		    	
            	currentPanel.add(currentRadio, "align center, hmin 55, gapy 0 10");
            	radioCount++;

        	}
    		
        	questionWrap.add(currentPanel, "push, growx, h 160px, top, gapy 0 20, wrap");
        	questionCount++;
        	
    	}
    	
    	
    	
    	
    	JScrollPane scroll1 = new JScrollPane(questionWrap);    	
    	scroll1.setBackground(new Color(0,0,0,0));
    	scroll1.setBorder(null);
    	
    	JPanel scores = new JPanel();
    	scores.setLayout(new MigLayout(""));
    	//scores.setBackground(Color.RED);

    	JPanel wrap = new JPanel();
    	wrap.setLayout(new MigLayout());
    //	wrap.setBackground(Color.RED);

    	percentage = new JLabel("0%");
    	percentage.setFont(new Font("Segoe UI",Font.BOLD, 65));

    	mark = new JLabel("You scored: 0/10");
    	
     	mark.setHorizontalAlignment(SwingConstants.CENTER);
    	mark.setHorizontalTextPosition(SwingConstants.CENTER);
    	percentage.setHorizontalAlignment(SwingConstants.CENTER);
    	percentage.setHorizontalTextPosition(SwingConstants.CENTER);
    	
		retry = new JButton("RETRY");
		back = new JButton("BACK");
		back.addActionListener(this);

		
    	wrap.add(percentage, "push, growx, wrap");
    	wrap.add(mark, "push, growx, wrap, gapy 0 30px");
		wrap.add(retry,   "push, w 150, hmin 40px, gap 10 10 10 0, wrap");
		wrap.add(back,   "push, w 150, hmin 40px, gap 10 10 10 10");

    	
    	
    	
    	
    	scores.add(wrap, "push, align center");
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	content.add(scores, "scores");
    	content.add(scroll1, "scroll1");
    	card.show(content, "scores");
    	panel.add(content, "push, grow, wrap");
		
		
	}
	
	
	public void addElements() {
		
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);
		cr.setSnapSize(new Dimension(10, 10));

		ComponentDraggable cd = new ComponentDraggable();
		cd.setDraggable(panel, frame);
		
		submit = new JButton("SUBMIT");

		answers = new JButton("ANSWERS");
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		submit.setOpaque(true);
		submit.setBorder(emptyBorder);
		submit.setBackground(new Color(0xe95e59));
		submit.setForeground(Color.WHITE);
		submit.setCursor(new Cursor(Cursor.HAND_CURSOR));

		retry.setOpaque(true);
		retry.setBorder(emptyBorder);
		retry.setBackground(new Color(0xe95e59));
		retry.setForeground(Color.WHITE);
		retry.setCursor(new Cursor(Cursor.HAND_CURSOR));

		answers.setOpaque(true);
		answers.setBorder(emptyBorder);
		answers.setBackground(new Color(0xe95e59));
		answers.setForeground(Color.WHITE);
		answers.setCursor(new Cursor(Cursor.HAND_CURSOR));
		answers.setVisible(false);
		retry.addActionListener(this);
		answers.addActionListener(this);
		submit.addActionListener(this);
		
		
		panel.add(submit,  "growx, hmin 40px, gap 20 10 10 10, split 3");
		panel.add(answers,   "growx, hmin 40px, gap 10 20 10 10");

	

		frame.setUndecorated(true);
		frame.setContentPane(new ShadowPane());
        frame.setBackground(new Color(0,0,0,0)); 
		frame.add(panel);
		frame.setSize(400, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	
	public void styleElements() {
		
		// Add Logo Icon
	
    	ArrayList<String> controlsIcons = new ArrayList<>();
    	controlsIcons.add("assets/img/exitIcon.png");
    	controlsIcons.add("assets/img/miniIcon.png");
    	controlsIcons.add("assets/img/maxiIcon.png");
    	controlsIcons.add("assets/img/settingsIcon.png");
    	
    	int controlsCount = 0;

		
		
    	panel.setBackground(new Color(0xf8f8f8));
    
	}

	
	
	
	public class ShadowPane extends JPanel {

		public ShadowPane() {
			setLayout(new BorderLayout());
	        setOpaque(false);
	        setBackground(Color.BLACK);
	        setBorder(new EmptyBorder(12, 12, 2, 2));
	    }

	    public Dimension getPreferredSize() {
	    	return new Dimension(200, 200);
	    }

	    protected void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	    	Graphics2D g2d = (Graphics2D) g.create();
	    	g2d.setComposite(AlphaComposite.SrcOver.derive(0.2f));
	    	g2d.fillRect(10, 10, getWidth(), getHeight());
	    	g2d.dispose();
	    }
	  
	}


	public void itemStateChanged(ItemEvent e) {
		
		JComponent source = ((JComponent) e.getSource());
		
		if (e.getStateChange() == ItemEvent.SELECTED) {	
			
			Color black = new Color(0x000000);
			Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, black);
			source.setBorder(border);
		
		} else if (e.getStateChange() == ItemEvent.DESELECTED) {
		        
			Color gray = new Color(0xcccccc);
			Border border = BorderFactory.createMatteBorder(0, 0, 2, 0, gray);
			source.setBorder(border);			
		}
		
		
	}


	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == submit) {
			Boolean isComplete = true;
				
			for(ButtonGroup current: radioGroup) {					
				if(current.getSelection() == null) {
					isComplete = false;
				}
			} 
				
			if(isComplete == true) {
				
				
				int count = 0;
				
				for(int i=0; i<questionAnswers.size(); i++) {
					
					String myAnswer = radioGroup.get(i).getSelection().getActionCommand();
					String dbAnswer = questionAnswers.get(i);
					
					
	
					if(myAnswer.equals(dbAnswer)) {
						
						System.out.println("Correct");
						count++;
	
					} else {
	
						System.out.println("Incorrect");
	
					}
					
				
					
				}
				
				float percent = (count * 100.0f) / questionAnswers.size();
	
				
				
	//			double result = (count/questionAnswers.size())*100;
				String finalPercent = String.format("%.2f", percent);
	
				mark.setText("You Score: " + count + "/" + questionAnswers.size());
				percentage.setText(finalPercent + "%");
				card.show(content, "scores");
				
				System.out.println("Lesson: " + dbLesson);
				System.out.println("ID:     " + LoginGUI.id);
				
				
				
				Connection conn = null;
				String getConn = "jdbc:mysql://localhost/programmingassistant";
	
				try {
						
					Class.forName("com.mysql.jdbc.Driver");	        
					conn = DriverManager.getConnection(getConn, "root", "");  
				    
				} catch (ClassNotFoundException | SQLException e2) { 
					e2.printStackTrace(); 
				}
				
				ResultSet results = null;
			    int rowcount = 0;
	
			    try {
			    	
				    String query = "SELECT * FROM marks where LessonID='" + dbLesson +"' AND LeanerID ='" + LoginGUI.id + "'";
				    Statement statement = (Statement) conn.createStatement(); 
				    results = statement.executeQuery(query);
				    
				    if (results.last()) {
					      rowcount = results.getRow();
					      results.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
					    }
					    
				    
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
			    
			    
			   
			    
			    System.out.println(rowcount);
			    
			    
			    LoginGUI.id = "1";
			    
			    if(rowcount == 0) {
			    	
			    	 try {
				    	 String query = "INSERT INTO marks (LessonID, LeanerID, Mark) VALUES ("+ dbLesson + "," + LoginGUI.id + "," + percent + ")";
						 Statement statement = (Statement) conn.createStatement(); 
						 statement.execute(query);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	
			    	
			    	
			    	
			    } else {
			    	
			    	 try {
				    	 String query = "UPDATE marks SET Mark='" + percent + "' where LessonID='" + dbLesson +"' AND LeanerID ='" + LoginGUI.id + "'";
						 Statement statement = (Statement) conn.createStatement(); 
						 statement.execute(query);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	
			    	 
			    	
			    	
			    }
			    
			    
			    
				
	//	    	
	//	    	/*-- -----------------------
	//			 	Get Lesson Questions
	//			--------------------------*/
	//			
	//		   
	//		    try {
	//		    	 String query = "INSERT INTO marks (LessonID, LeanerID, Mark) VALUES ('1','2','20.00')";
	//				 Statement statement = (Statement) conn.createStatement(); 
	//				 statement.execute(query);
	//			} catch (SQLException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			}
					
				
				
				
				
			} else {
				
				JLabel msg = new JLabel("Give all the questions a go.", JLabel.CENTER); 
				JOptionPane.showMessageDialog (null, msg, "", JOptionPane.PLAIN_MESSAGE);
				
			}
			
		}
		
		if(e.getSource() == retry) {
			
			frame.dispose();
			
			try {
				new TestGUI(dbLesson).run();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
			
		}
		
		
		if(e.getSource() == back) {
			
			frame.dispose();
			
			MainGUI.frame.setVisible(true);
			
		}

		
	}


	
	
	

}
