package ca.team2706.vision.trackerboxreloaded;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Frame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtTeam;
	private JTextField textField;
	private JTextField txtIp;
	private JTextField textField_1;
	private JTextArea textArea;
	private JButton btnUseTeam;
	private JButton btnUseIp;
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
	public Frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtTeam = new JTextField();
		txtTeam.setEditable(false);
		txtTeam.setText("team#");
		txtTeam.setBounds(0, 0, 45, 20);
		contentPane.add(txtTeam);
		txtTeam.setColumns(10);
		
		textField = new JTextField();
		textField.setBounds(44, 0, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnUseTeam = new JButton("Use team #");
		btnUseTeam.setBounds(132, -1, 100, 23);
		btnUseTeam.addActionListener(this);
		contentPane.add(btnUseTeam);
		
		txtIp = new JTextField();
		txtIp.setEditable(false);
		txtIp.setText("ip:");
		txtIp.setBounds(242, 0, 23, 20);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(264, 0, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		btnUseIp = new JButton("Use IP");
		btnUseIp.setBounds(357, -1, 89, 23);
		btnUseIp.addActionListener(this);
		contentPane.add(btnUseIp);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 414, 219);
		scrollPane.setAutoscrolls(true);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		txtFilter = new JTextField();
		txtFilter.setEditable(false);
		txtFilter.setText("Filter:");
		txtFilter.setBounds(456, 11, 45, 20);
		contentPane.add(txtFilter);
		txtFilter.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(456, 34, 218, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		txtKey = new JTextField();
		txtKey.setEditable(false);
		txtKey.setText("Key:");
		txtKey.setBounds(456, 139, 32, 20);
		contentPane.add(txtKey);
		txtKey.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(456, 157, 218, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		txtData = new JTextField();
		txtData.setEditable(false);
		txtData.setText("Data:");
		txtData.setBounds(456, 188, 39, 20);
		contentPane.add(txtData);
		txtData.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(456, 207, 218, 20);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		btnPush = new JButton("Push");
		btnPush.setBounds(461, 238, 89, 23);
		btnPush.addActionListener(this);
		contentPane.add(btnPush);
		
		textField_5 = new JTextField();
		textField_5.setBounds(456, 115, 218, 20);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		txtTable = new JTextField();
		txtTable.setEditable(false);
		txtTable.setText("Table:");
		txtTable.setBounds(456, 95, 45, 20);
		contentPane.add(txtTable);
		txtTable.setColumns(10);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(456, 65, 89, 23);
		btnSave.addActionListener(this);
		contentPane.add(btnSave);
		
		btnLoad = new JButton("Load");
		btnLoad.addActionListener(this);
		btnLoad.setBounds(585, 65, 89, 23);
		contentPane.add(btnLoad);
		
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
		}else if(e.getSource() == btnUseTeam){
			new Thread(new Runnable(){
				public void run(){
					NetworkTablesClient.useTeam(Integer.valueOf(textField.getText().trim()));
				}
			}).start();
			btnUseIp.setEnabled(false);
			btnUseTeam.setEnabled(false);
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
		}
	}
	public void println(String text){
		textArea.append(text+System.lineSeparator());
	}
	public void clear(){
		textArea.setText("");
		for(int i = 0; i < 5; i++){
			System.out.println();
		}
	}
}
