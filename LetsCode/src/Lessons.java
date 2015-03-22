import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.mysql.jdbc.Statement;

import net.miginfocom.swing.MigLayout;

class Lessons extends JPanel {

    JButton buttonTwo;
    CardLayout card;
    JPanel content;
    JScrollPane scroll;
    static String currentLesson;

	public Lessons(final CardLayout card, JPanel content, String lang) throws SQLException {
        this.card = card;
        this.content = content;

		MainGUI.backBtn.setEnabled(true);

        Border  border  = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xdddddd));
		String constraints = "width 26%, height 350px, gap 0.5% 0.5% 2%, top";
		Font descrFont= new Font("Segoe UI", Font.PLAIN, 16);
		Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
		
		scroll = new JScrollPane(this);
		scroll.setHorizontalScrollBarPolicy(scroll.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);
		this.setLayout(new MigLayout("insets 0% 9%, wrap 3"));
		
        ResultSet results = null;
        Connection conn = null;
    	try {
			
			Class.forName("com.mysql.jdbc.Driver");	        
			conn = DriverManager.getConnection("jdbc:mysql://localhost/programmingassistant", "root", "");
	    	String query = "SELECT * FROM lessons WHERE langID ='" + lang + "'";
	    	Statement statement = (Statement) conn.createStatement(); 
	    	results = statement.executeQuery(query);

		} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
		
    	
    	ArrayList<JPanel> panels = new ArrayList<>();	
		ArrayList<JButton> buttons = new ArrayList<>();	
		
	
    	int current = 0;
    	
    	while(results.next()) {
    		
    		panels.add(new JPanel());
    		JPanel panel = panels.get(current);
    		JLabel title = new JLabel(results.getString("lessonName"));
    		JLabel description = new JLabel();
    		
    		buttons.add(new JButton("SELECT"));
    		buttons.get(current);
    		
    		panel.setBorder(border);
    		panel.setLayout(new MigLayout());
    		
    		String id = results.getString("lessonID");
    		String name = results.getString("lessonName");
    		
    		try {
    			Image image = ImageIO.read(new URL("http://placehold.it/135x135"));
    			Image  resized  = image.getScaledInstance(135, 135,Image.SCALE_SMOOTH);
    			ImageIcon logoImage = new ImageIcon(resized);
    			JLabel logo = new JLabel(logoImage);
    			panel.add(logo, "w 100%, wrap, gap 0 0 40 40, top");
  		
    		} catch (IOException k) { k.printStackTrace(); }

    		description.setText("<html><center>"+ results.getString("lessonDescrip")+ "</center></html>");
    		title.setFont(titleFont);
    		title.setHorizontalAlignment(SwingConstants.CENTER);
    		title.setHorizontalTextPosition(SwingConstants.CENTER);
    		description.setFont(descrFont);
    		
    		buttons.get(current).setBorderPainted(false);
    		buttons.get(current).setCursor(new Cursor(Cursor.HAND_CURSOR));		
    		buttons.get(current).setBackground(new Color(0xcfd0d0));
    		buttons.get(current).setForeground(new Color(0x828282));
    		panel.setBackground(Color.WHITE);
    		
    		panel.add(title, "w 100%, gap 0 0 0 5, wrap");
    		panel.add(description, "w 100%,  h 500px, gap 15 15, wrap, push");
    		panel.add(buttons.get(current), "w 100%, h 45px, gap 40px 40px 20px 50px");
    		this.add(panel, constraints);
    		

    		buttons.get(current).addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					
					
					
					
					card.show(content, name);
	
//				   try {
//					new Lesson(card, content, "t") ;
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			    	

			    	
				}    			
    			
    		});   		
    		
    		
    		
    		current++;
    	}
		
    	this.setBackground(new Color(0xf8f8f8));
		scroll.setBackground(new Color(0,0,0,0));
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		
		
	
		
//		Component[] components = content.getComponents();
//		
//		if(components.length > 1) content.remove(1);
//		System.out.println(components.length);

		scroll.setName("lesson");
		content.add(scroll, "lesson");
		
		
		
		
		
		
		
		
		card.show(content, "lesson");
		

			
			
		//	System.out.println("test");
		
		/*-- -----------------------
		 Get Lesson Material
	--------------------------*/
	
	String query = "SELECT * FROM lessons WHERE langID = '" + lang + "'";
	Statement statement = (Statement) conn.createStatement(); 
	ResultSet dbLesson = statement.executeQuery(query);
	    
	while(dbLesson.next()) {		
		String lessonName = dbLesson.getString("lessonName");
		System.out.println(lessonName);
		new Lesson(card, content, lang, dbLesson);
		MainGUI.nextBtn.setEnabled(false);

	
	}
		
	//	new Lesson(card, content, lang);
//			
			
//		card.show(content, "Data Types");

		
      

    }
}