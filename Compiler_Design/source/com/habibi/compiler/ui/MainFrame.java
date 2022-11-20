/**
 * MainFrame.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.habibi.compiler.ui;


import java.io.*;
import javax.swing.*;

import com.habibi.compiler.CodeGenerator;
import com.habibi.compiler.OneAddressCommand;
import com.habibi.compiler.Semantic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MainFrame extends JFrame implements DocumentListener
{
	private JMenuBar mainMenu;
	
	private JMenu fileMenu;
	
	private JMenuItem newMI;
	private JMenuItem openMI;
	private JMenuItem saveMI;
	private JMenuItem saveAsMI;
	private JMenuItem exitMI;
	private JMenuItem runMI;
//	private JMenuItem traceMI;
//	private JMenuItem stopMI;
	private JMenuItem aboutMI;
	
	private JToolBar toolBar;
	
	private JToolBarButton newBtn;
	private JToolBarButton openBtn;
	private JToolBarButton saveBtn;
	private JToolBarButton saveAsBtn;
	private JToolBarButton runBtn;
//	private JToolBarButton traceBtn;
//	private JToolBarButton stopBtn;
	
	private Action newAction;
	private Action openAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action runAction;
//	private Action traceAction;
//	private Action stopAction;
	private Action aboutAction;
	private Action exitAction;
	
	private Icon newIcon;
	private Icon openIcon;
	private Icon saveIcon;
	private Icon saveAsIcon;
	private Icon runIcon;
	
	private Icon aboutIcon;
	private Icon exitIcon;
//	private Icon stopIcon;
	
	private JEditorPane editor;
	private JTextArea   output;
	
	private boolean isDirty;
	private File outFile;
	private String subTitle;
	
	private ExampleFileFilter filter;
	private JFileChooser fileChooser;
	
	private JTextComponentWriter out;
	private JPanel pane1;
	private JPanel pane2;
	
	private static final String TITLE = "Simple_Lang IDE";
	
	public MainFrame() {
		super();
		setTitle("Untitled", false);
		setSize(640, 480);
		Dimension dim = getToolkit().getScreenSize();
		setLocation((int)(dim.getWidth()-getWidth())/2,(int)(dim.getHeight()- getHeight())/2);
		setupIcons();
		setupActions();
		setupMenus();
		setupComponents();
		setupToolbar();
		setupFileChooser();
		addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						System.exit(0);
					}
				});
		isDirty = false;
		outFile = null;
	}
	
	
	
	private void setupFileChooser() {
		filter = new ExampleFileFilter("sl", "Simple_Lang Files");
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
	}
	
	public void setTitle(String subTitle, boolean dirty) {
		this.subTitle = subTitle;
		super.setTitle(TITLE + " - " + subTitle + (dirty? " *" : ""));
	}
	
	private void setupIcons() {
		newIcon = new ImageIcon("icons/New24.gif");
		openIcon = new ImageIcon("icons/Open24.gif");
		saveIcon = new ImageIcon("icons/Save24.gif");
		saveAsIcon = new ImageIcon("icons/SaveAs24.gif");
		runIcon = new ImageIcon("icons/Play24.gif");
		
		aboutIcon = new ImageIcon("icons/About24.gif");
		exitIcon = new ImageIcon("icons/Exit24.gif");
//		stopIcon = new ImageIcon("icons/Stop24.gif");
	}
	
	private void setupActions() {
		newAction = new AbstractAction("New...", newIcon) { {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('n'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doNew();
			}
		};
		
		openAction = new AbstractAction("Open...", openIcon) {  {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('o'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doOpen();
			}
		};
		
		saveAction = new AbstractAction("Save", saveIcon) { {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('s'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doSave();
			}
		};
		
		saveAsAction = new AbstractAction("Save As...", saveAsIcon) {   {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('a'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doSaveAs();
			}
		};
		
		runAction = new AbstractAction("Run", runIcon) {    {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('r'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doRun();
			}
		};
		
		
		
//		stopAction = new AbstractAction("Stop", stopIcon) {   {
//				putValue(AbstractAction.MNEMONIC_KEY, new Integer('t'));
//				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F7, InputEvent.CTRL_MASK));
//			}
//			
//			public void actionPerformed(ActionEvent ae) {
//				doStop();
//			}
//		};
//		
		
		aboutAction = new AbstractAction("About...", aboutIcon) {   {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('a'));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doAbout();
			}
		};
		
		exitAction = new AbstractAction("Exit...", exitIcon) {  {
				putValue(AbstractAction.MNEMONIC_KEY, new Integer('x'));
				putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			}
			
			public void actionPerformed(ActionEvent ae) {
				doExit();
			}
		};
		
		
	}
	
	private void setupMenus() {
		mainMenu = new JMenuBar();
		setJMenuBar(mainMenu);
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		mainMenu.add(fileMenu);
		
		newMI = new JMenuItem(newAction);
		fileMenu.add(newMI);
		openMI = new JMenuItem(openAction);
		fileMenu.add(openMI);
		saveMI = new JMenuItem(saveAction);
		fileMenu.add(saveMI);
		saveAsMI = new JMenuItem(saveAsAction);
		fileMenu.add(saveAsMI);
		
		fileMenu.addSeparator();
		
		runMI = new JMenuItem(runAction);
		fileMenu.add(runMI);
//		traceMI = new JMenuItem(traceAction);
//		fileMenu.add(traceMI);
//		stopMI = new JMenuItem(stopAction);
//		fileMenu.add(stopMI);
		
		fileMenu.addSeparator();
		
		aboutMI = new JMenuItem(aboutAction);
		fileMenu.add(aboutMI);
		
		fileMenu.addSeparator();
		
		exitMI = new JMenuItem(exitAction);
		fileMenu.add(exitMI);
	}
	
	private void setupComponents() {
		JSplitPane spinner=new JSplitPane();
		
		editor = new JEditorPane();
		JScrollPane edPane = new JScrollPane(editor);
		editor.setFont(new Font("Monospaced", Font.PLAIN, 13));
		//getContentPane().add(edPane, BorderLayout.CENTER);
		editor.getDocument().addDocumentListener(this);
		editor.setCaretColor(Color.red);
		output = new JTextArea(5, 0);
		output.setEditable(false);
		JScrollPane outPane = new JScrollPane(output);
		output.setFont(new Font("Monospaced", Font.PLAIN, 13));
		
		JPanel outputPanel = new JPanel(new BorderLayout());
		JLabel outputLabel = new JLabel(" Output: ");
		outputPanel.add(outputLabel, BorderLayout.NORTH);
		outputPanel.add(outPane, BorderLayout.SOUTH);
		pane1 = new JPanel();
		pane1.setLayout(new BorderLayout());
		pane1.add(outPane, BorderLayout.CENTER);
		pane1.add(outputPanel,BorderLayout.NORTH);
		spinner.setBottomComponent(pane1);
		spinner.setTopComponent(edPane);
		spinner.setOrientation(JSplitPane.VERTICAL_SPLIT);
		spinner.setDividerSize(15);
		spinner.setOneTouchExpandable(true);
		spinner.setResizeWeight(1);
		getContentPane().add(spinner);
	}
	
	private void setupToolbar() {
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		newBtn = new JToolBarButton(newAction);
		openBtn = new JToolBarButton(openAction);
		saveBtn = new JToolBarButton(saveAction);
		saveAsBtn = new JToolBarButton(saveAsAction);
		runBtn = new JToolBarButton(runAction);
//		traceBtn = new JToolBarButton(traceAction);
//		stopBtn = new JToolBarButton(stopAction);
		
		toolBar.add(newBtn);
		toolBar.add(openBtn);
		toolBar.add(saveBtn);
		toolBar.add(saveAsBtn);
		toolBar.addSeparator();
		toolBar.add(runBtn);
//		toolBar.add(traceBtn);
//		toolBar.add(stopBtn);
	}
	
	private boolean isDirty() {
		return isDirty;
	}
	
	private void setDirty(boolean dirty) {
		isDirty = dirty;
		if (dirty == true) {
			setTitle(subTitle, true);
		} else {
			setTitle(subTitle, false);
		}
	}
	
	private void setFile(File file) {
		this.outFile = file;
	}
	
	private File getFile() {
		return outFile;
	}
	
	private String getFileName() {
		return outFile.getName();
	}
	
	private void newDocument() {
		setTitle("Untitled", false);
		editor.setText("");
		setDirty(false);
		setFile(null);
	}
	
	private int askForSave() {
		String message = "The current program is not saved.\nSave it now?";
		int choice = JOptionPane.showConfirmDialog(this, message);
		return choice;
	}
	
	private File showSaveDialog() {
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(null);
		
		int choice = fileChooser.showSaveDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	private void save(File file) throws IOException {
		/*String s=file.getAbsolutePath()+".bc";
		 System.out.println(s);
		 File f=new File(s);*/
		
		FileWriter fw = new FileWriter(file);
		//FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(editor.getText());
		bw.close();
		fw.close();
		outFile = file;
	}
	
	private void doNew() {
		if (isDirty()) {
			int choice = askForSave();
			if (choice == JOptionPane.YES_OPTION) {
				if (outFile == null) {
					File file = showSaveDialog();
					if (file != null) {
						try {
							save(file);
							newDocument();
						} catch (IOException ioe) {
							String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage();
							JOptionPane.showMessageDialog(this, message);
						}
					} else {
						// do nothing
					}
				} else {
					try {
						save(outFile);
						newDocument();
					} catch (IOException ioe) {
						String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage();
						JOptionPane.showMessageDialog(this, message);
					}
				}
			} else if (choice == JOptionPane.NO_OPTION) {
				newDocument();
			} else {
				// do nothing
			}
		} else {
			newDocument();
		}
	}
	
	private File showOpenDialog() {
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(null);
		int choice = fileChooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	private void open(File file) throws IOException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[4096];
		int readCount = 0;
		while ((readCount = br.read(buffer)) != -1) {
			sb.append(buffer, 0, readCount);
		}
		
		br.close();
		fr.close();
		
		editor.setText(sb.toString());
		setTitle(file.getName(), false);
		setDirty(false);
		setFile(file);
	}
	
	private void openProcess() {
		File file = showOpenDialog();
		if (file != null) {
			try {
				open(file);
			} catch (IOException ioe) {
				String message = "An I/O problem occured while trying to open " + file.getName() +":\n" + ioe.getMessage();
				JOptionPane.showMessageDialog(this, message);
			}
		}
	}
	
	private void doOpen() {
		if (isDirty()) {
			int choice = askForSave();
			if (choice == JOptionPane.YES_OPTION) {
				File file = showSaveDialog();
				if (file != null) {
					try {
						save(file);
						openProcess();
					} catch (IOException ioe) {
						String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage() + "\nOpen aborted.";
						JOptionPane.showMessageDialog(this, message);
					}
				}
			} else if (choice == JOptionPane.NO_OPTION) {
				openProcess();
			}
		} else {
			openProcess();
		}
	}
	
	private void doSave() {
		if (outFile != null) {
			try {
				if (outFile.getName().indexOf('.')!=-1)
					
					
					save(outFile);
				setTitle(outFile.getName(), false);
				setDirty(false);
			} catch (IOException ioe) {
				String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage();
				JOptionPane.showMessageDialog(this, message);
			}
		} else {
			File file = showSaveDialog();
			if (file != null) {
				try {
					save(file);
					setTitle(file.getName(), false);
					setDirty(false);
				} catch (IOException ioe) {
					String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage();
					JOptionPane.showMessageDialog(this, message);
				}
			}
		}
	}
	
	private File showSaveAsDialog() {
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(null);
		int choice = fileChooser.showDialog(this, "Save As");
		if (choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	private void doSaveAs() {
		File file = showSaveAsDialog();
		if (file != null) {
			try {
				save(file);
			} catch (IOException ioe) {
				String message = "An I/O problem occured while trying to save the current program:.\n" + ioe.getMessage();
				JOptionPane.showMessageDialog(this, message);
			}
		}
	}
	
	private void doRun() {
		output.setText("");
		output.setForeground(Color.blue);
		try
		{
				Semantic symbolTable = new Semantic();
				CodeGenerator code=new CodeGenerator(symbolTable);
				ArrayList commands=code.generateCode(editor.getText());
								
				output.append("Parse compeleted. The Programe has no errors\n");
				output.append("The virtual machine source code : \n");
				Iterator iter = commands.iterator();
				System.out.println("---");
				while (iter.hasNext()){
					OneAddressCommand element = (OneAddressCommand) iter.next();
					output.append(element.toString()+"\n");
				}
			//}
			
		}
		catch (Exception e) { //IOE changed to E
			e.printStackTrace();
		}
		
	}
	
	private void doExit() {
		int x=JOptionPane.showConfirmDialog(this,"Are you sure","Confirm",JOptionPane.YES_NO_OPTION);
		if (x!=JOptionPane.YES_OPTION)
			return;
		
		if (isDirty())
			doSaveAs();
		System.exit(0);
		
		
	}
	
	private void doAbout() {
		JOptionPane.showMessageDialog(this,"This is the front-end part of the \nCompiler Project for: \nSimple_Lang language\nWritten By :\nNarges Habibi\n","About Dialog",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void changedUpdate(DocumentEvent e) {
	}
	
	public void insertUpdate(DocumentEvent e) {
		setDirty(true);
	}
	
	public void removeUpdate(DocumentEvent e) {
		setDirty(true);
	}
	
}

