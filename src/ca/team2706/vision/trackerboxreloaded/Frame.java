package ca.team2706.vision.trackerboxreloaded;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ScrollPaneConstants;

public class Frame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtTeam;
	private JTextField textField;
	private JTextField txtIp;
	private JTextField textField_1;
	private JTextArea textArea;
	public JButton btnUseTeam;
	public JButton btnUseIp;
	private JTextField txtFilter;
	private JTextField textField_2;
	private JTextField txtKey;
	private JTextField textField_3;
	private JTextField txtData;
	private JTextField textField_4;
	private JButton btnPush;
	private JTextField textField_5;
	private JTextField txtTable;
	private JButton btnSave;
	private JButton btnLoad;
	private JButton btnPushArray;
	public JButton btnStartServer;
	private JScrollPane scrollPane;
	private JTextField txtIp_1;
	private JTextField textField_6;
	private JTextField txtPort;
	private JScrollPane scrollPane_1;
	private JTextField txtText;
	private JTextField textField_7;
	private JScrollPane scrollPane_2;
	public JButton btnSend;
	public JButton btnConnect;
	public JButton btnStartServer_1;
	private JTextArea textArea_1;
	
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 684, 261);
		JPanel networkTables = new JPanel();
		networkTables.setLayout(null);
		JPanel socket = new JPanel();
		socket.setLayout(null);
		tabbedPane.addTab("Network Tables", networkTables);
		tabbedPane.addTab("Sockets", socket);
		
		txtIp_1 = new JTextField();
		txtIp_1.setText("Ip:");
		txtIp_1.setEditable(false);
		txtIp_1.setBounds(0, 0, 24, 20);
		socket.add(txtIp_1);
		txtIp_1.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setBounds(23, 0, 239, 20);
		socket.add(textField_6);
		textField_6.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setText("Port:");
		txtPort.setBounds(272, 0, 36, 20);
		socket.add(txtPort);
		txtPort.setColumns(10);
		
		JTextField formattedTextField = new JTextField();
		formattedTextField.setBounds(306, 0, 83, 20);
		socket.add(formattedTextField);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip = textField_6.getText();
				String port = formattedTextField.getText();
				if(isInt(port)){
					btnConnect.setEnabled(false);
					btnStartServer_1.setEnabled(false);
					btnSend.setEnabled(true);
					new Thread(new Runnable(){

						@Override
						public void run() {
							NetworkTablesClient.connect(ip,Integer.valueOf(port));
						}
						
					}).start();
					
				}
			}
		});
		btnConnect.setBounds(405, -1, 89, 23);
		socket.add(btnConnect);
		
		btnStartServer_1 = new JButton("Start Server");
		btnStartServer_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String port = formattedTextField.getText();
				if(isInt(port)){
					btnConnect.setEnabled(false);
					btnStartServer_1.setEnabled(false);
					btnSend.setEnabled(true);
					new Thread(new Runnable(){

						@Override
						public void run() {
							NetworkTablesClient.startServer(Integer.valueOf(port));
						}
						
					}).start();
					
				}
			}
		});
		btnStartServer_1.setBounds(504, -1, 108, 23);
		socket.add(btnStartServer_1);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 52, 379, 170);
		socket.add(scrollPane_1);
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		scrollPane_1.setViewportView(textArea_1);
		
		txtText = new JTextField();
		txtText.setEditable(false);
		txtText.setText("Text:");
		txtText.setBounds(405, 68, 41, 20);
		socket.add(txtText);
		txtText.setColumns(10);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_2.setBounds(444, 68, 225, 20);
		socket.add(scrollPane_2);
		
		textField_7 = new JTextField();
		scrollPane_2.setViewportView(textField_7);
		textField_7.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(NetworkTablesClient.out != null){
					NetworkTablesClient.out.println(textField_7.getText());
					textField_7.setText("");
				}
			}
		});
		btnSend.setBounds(492, 106, 89, 23);
		socket.add(btnSend);
		txtTeam = new JTextField();
		txtTeam.setEditable(false);
		txtTeam.setText("team#");
		txtTeam.setBounds(0, 0, 45, 20);
		networkTables.add(txtTeam);
		txtTeam.setColumns(10);
		
		textField = new JTextField();
		textField.setBounds(44, 0, 86, 20);
		networkTables.add(textField);
		textField.setColumns(10);
		
		btnUseTeam = new JButton("Use team #");
		btnUseTeam.setBounds(132, -1, 100, 23);
		btnUseTeam.addActionListener(this);
		networkTables.add(btnUseTeam);
		
		txtIp = new JTextField();
		txtIp.setEditable(false);
		txtIp.setText("ip:");
		txtIp.setBounds(242, 0, 23, 20);
		networkTables.add(txtIp);
		txtIp.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(264, 0, 86, 20);
		networkTables.add(textField_1);
		textField_1.setColumns(10);
		
		btnUseIp = new JButton("Use IP");
		btnUseIp.setBounds(357, -1, 89, 23);
		btnUseIp.addActionListener(this);
		networkTables.add(btnUseIp);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 414, 219);
		scrollPane.setAutoscrolls(true);
		networkTables.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		txtFilter = new JTextField();
		txtFilter.setEditable(false);
		txtFilter.setText("Filter:");
		txtFilter.setBounds(424, 34, 45, 20);
		networkTables.add(txtFilter);
		txtFilter.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(466, 34, 208, 20);
		networkTables.add(textField_2);
		textField_2.setColumns(10);
		
		txtKey = new JTextField();
		txtKey.setEditable(false);
		txtKey.setText("Key:");
		txtKey.setBounds(456, 139, 32, 20);
		networkTables.add(txtKey);
		txtKey.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(456, 157, 218, 20);
		networkTables.add(textField_3);
		textField_3.setColumns(10);
		
		txtData = new JTextField();
		txtData.setEditable(false);
		txtData.setText("Data:");
		txtData.setBounds(456, 188, 39, 20);
		networkTables.add(txtData);
		txtData.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(456, 207, 218, 20);
		networkTables.add(textField_4);
		textField_4.setColumns(10);
		
		btnPush = new JButton("Push");
		btnPush.setBounds(456, 238, 89, 23);
		btnPush.addActionListener(this);
		networkTables.add(btnPush);
		
		textField_5 = new JTextField();
		textField_5.setBounds(456, 115, 218, 20);
		networkTables.add(textField_5);
		textField_5.setColumns(10);
		
		txtTable = new JTextField();
		txtTable.setEditable(false);
		txtTable.setText("Table:");
		txtTable.setBounds(456, 95, 45, 20);
		networkTables.add(txtTable);
		txtTable.setColumns(10);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(456, 65, 89, 23);
		btnSave.addActionListener(this);
		networkTables.add(btnSave);
		
		btnLoad = new JButton("Load");
		btnLoad.addActionListener(this);
		btnLoad.setBounds(585, 65, 89, 23);
		networkTables.add(btnLoad);
		
		btnPushArray = new JButton("Push Array");
		btnPushArray.setBounds(563, 238, 111, 23);
		btnPushArray.addActionListener(this);
		networkTables.add(btnPushArray);
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(this);
		btnStartServer.setBounds(456, -1, 111, 23);
		networkTables.add(btnStartServer);
		
		contentPane.add(tabbedPane);
		
		setVisible(true);
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					NetworkTablesClient.filter = textField_2.getText();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnUseIp){
			new Thread(new Runnable(){
				public void run(){
					NetworkTablesClient.useIp(textField_1.getText());
				}
			}).start();
			btnUseIp.setEnabled(false);
			btnUseTeam.setEnabled(false);
			btnStartServer.setEnabled(false);
		}else if(e.getSource() == btnUseTeam){
			new Thread(new Runnable(){
				public void run(){
					NetworkTablesClient.useTeam(Integer.valueOf(textField.getText().trim()));
				}
			}).start();
			btnUseIp.setEnabled(false);
			btnUseTeam.setEnabled(false);
			btnStartServer.setEnabled(false);
		}else if(e.getSource() == btnPush){
			if(!btnUseIp.isEnabled() && !btnUseTeam.isEnabled()){
				NetworkTablesClient.push(textField_5.getText(),textField_3.getText(),textField_4.getText());
			}
		}else if(e.getSource() == btnSave){
			if(!btnUseIp.isEnabled() && !btnUseTeam.isEnabled()){
				NetworkTablesClient.save();
			}
		}else if(e.getSource() == btnLoad){
			if(!btnUseIp.isEnabled() && !btnUseTeam.isEnabled()){
				NetworkTablesClient.load();
			}
		}else if(e.getSource() == btnPushArray){
			if(!btnUseIp.isEnabled() && !btnUseTeam.isEnabled()){
				NetworkTablesClient.pushArray(textField_5.getText(),textField_3.getText(),textField_4.getText());
			}
		}else if(e.getSource() == btnStartServer){
			btnUseIp.setEnabled(false);
			btnUseTeam.setEnabled(false);
			btnStartServer.setEnabled(false);
			new Thread(new Runnable(){
				public void run(){
					NetworkTablesClient.startServer();
				}
			}).start();
		}
	}
	public void println(String text, String key){
		boolean found = false;
		int scrollH = scrollPane.getHorizontalScrollBar().getValue();
		int scrollV = scrollPane.getVerticalScrollBar().getValue();
		for(String s : textArea.getText().split(System.lineSeparator())){
			if(s.length() >= key.length() && s.substring(0,key.length()).equals(key)){
				String string = "";
				for(String s1 : textArea.getText().split(System.lineSeparator())){
					if(!s1.equals(s)){
						string += s1+System.lineSeparator();
					}else{
						string += text+System.lineSeparator();
					}
				}
				textArea.setText(string);
				scrollPane.getHorizontalScrollBar().setValue(scrollH);
				scrollPane.getVerticalScrollBar().setValue(scrollV);
				found = true;
				break;
			}
		}
		if(!found){
			textArea.append(text+System.lineSeparator());
		}
	}
	public void sPrintln(String text){
		textArea_1.append(text+System.lineSeparator());
	}
	@SuppressWarnings("unused")
	private boolean isInt(String port) {
		try{
			int i = Integer.valueOf(port);
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
