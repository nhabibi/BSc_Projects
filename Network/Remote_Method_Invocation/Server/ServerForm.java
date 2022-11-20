import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class ServerForm {
 
	static Server server;
	
	public static void main(String[] args){
		
		
		JFrame frame = new JFrame("RPC Server");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout);
		JButton start = new JButton("Start Server");
		contentPane.add(start);
		ActionListener listener1 = new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				
				if (server == null){
					
				    server = new Server();
				    server.start();
				}    
			}
		};
		start.addActionListener(listener1);
		frame.setSize(50, 50);
		frame.setVisible(true);
    }
}
