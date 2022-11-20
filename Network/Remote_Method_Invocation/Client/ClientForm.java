import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;

public class ClientForm {
	
	  JLabel askIP = new JLabel();
	  JTextField ip = new JTextField();
	  JLabel askFirst = new JLabel();
	  JTable first = new JTable(1,10);
	  JLabel askSecond = new JLabel();
	  JTable second = new JTable(1, 10);
	  JLabel showResult = new JLabel();
	  JButton getResult = new JButton();
	  JTable result = new JTable(1, 10);
	
	public void init() {
		
		  JFrame frame = new JFrame();
		  frame.setSize(new Dimension(400, 300));
		  frame.setTitle("RPC Client");
		  Container contentPane = frame.getContentPane();
		  BoxLayout layout = new BoxLayout(contentPane,1);
		  contentPane.setLayout(layout);
		  
		 	  
		  askIP.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
		  askIP.setText("Enter the IP address of server host");
		  ip.setText("127.0.0.1");
		  askFirst.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
		  askFirst.setText("Enter the values of the first array:");
		  first.setBackground(SystemColor.inactiveCaptionText);
		  first.setBorder(BorderFactory.createRaisedBevelBorder());
		  askSecond.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
		  askSecond.setText("Enter the values of the second array:");
		  second.setBackground(SystemColor.inactiveCaptionText);
		  second.setBorder(BorderFactory.createRaisedBevelBorder());
		  showResult.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
		  showResult.setText("Result:");
		  getResult.setText("Get Result Form Server");
		  getResult.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		      getResult();
		  }
		 });
		  
		 contentPane.add(askIP); 
		 contentPane.add(ip);
		 contentPane.add(askFirst);
		 contentPane.add(first);
		 contentPane.add(askSecond);
		 contentPane.add(second);
		 contentPane.add(getResult);
		 contentPane.add(showResult);
		 contentPane.add(result);
		  
		 frame.setVisible(true);
		  
	}
	
	void getResult(){
		
		String serverIP = ip.getText();
		int[] firstArr = new int[10];
		int[] secondArr = new int[10];
		int[] resultArr = new int[10];
		TableModel TMF = first.getModel();
		TableModel TMS = second.getModel();
		TableModel TMR = result.getModel();
		String value;
		
		for(int i=0; i<10; ++i){
			
			value = (String)TMF.getValueAt(0, i);
			firstArr[i] = Integer.parseInt(value);
		}		
        for(int i=0; i<10; ++i){
			
			value = (String)TMS.getValueAt(0, i);
			secondArr[i] = Integer.parseInt(value);
		}			
		Client client = new Client();
		resultArr = client.connect(serverIP, firstArr, secondArr);
		
        for(int i=0; i<10; ++i){
			
        	TMR.setValueAt(resultArr[i], 0, i);
		}	
			
	}
	
	public static void main(String[] args) {
		
		ClientForm clientForm = new ClientForm();
		clientForm.init();
	}

}
