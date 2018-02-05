package ca.team2706.vision.trackerboxreloaded;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTablesClient {
	public static Frame frame;
	public static String filter = "";
	private static boolean done = false;
	public static NetworkTableInstance instance;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		frame = new Frame();
		System.out.println("Would you like to enter a ip? Y/N");
		Scanner scan = new Scanner(System.in);
		String response = scan.nextLine();
		if (response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES")) {
			System.out.println("Enter a ip!");
			useIp(scan.nextLine());
		} else {
			System.out.println("Enter a team #");
			useTeam(Integer.valueOf(scan.nextLine()));
		}
	}

	public static void useIp(String ip) {
		if (done) {
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startClient(ip);

		while (true) {
			NetworkTable root = instance.getTable(filter);
			frame.clear();
			recursiveSearch(root, "");

			/*
			 * for(String key : vision.getKeys()){ NetworkTableEntry entry =
			 * vision.getEntry(key); if(entry.getValue().isString()){
			 * frame.println(key+": "+(entry.getValue().getString())); } }
			 * for(String subTable : vision.getSubTables()){ NetworkTable table
			 * = vision.getSubTable(subTable); for(String key :
			 * table.getKeys()){ if(table.getEntry(key).getValue().isString()){
			 * frame.println(key+": "+(table.getEntry(key).getValue().getString(
			 * ))); }else if(table.getEntry(key).getValue().isDouble()){
			 * frame.println(key+": "+(table.getEntry(key).getValue().getDouble(
			 * ))); } } }
			 */
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void useTeam(int team) {
		if (done) {
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startClientTeam(team);

		while (true) {
			NetworkTable root = instance.getTable(filter);
			frame.clear();
			recursiveSearch(root, "");
			/*
			 * for(String key : root.getKeys()){ NetworkTableEntry entry =
			 * root.getEntry(key); if(entry.getValue().isString()){
			 * frame.println(key+": "+(entry.getValue().getString())); } }
			 * for(String subTable : root.getSubTables()){ NetworkTable table =
			 * root.getSubTable(subTable); for(String key : table.getKeys()){
			 * if(table.getEntry(key).getValue().isString()){
			 * frame.println(key+": "+(table.getEntry(key).getValue().getString(
			 * ))); }else if(table.getEntry(key).getValue().isDouble()){
			 * frame.println(key+": "+(table.getEntry(key).getValue().getDouble(
			 * ))); } } }
			 */
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static void recursiveSearch(NetworkTable table, String currentPath) {
		for (String key : table.getKeys()) {
			NetworkTableEntry entry = table.getEntry(key);
			if (entry.getValue().isString()) {
				frame.println(currentPath + key + ": " + (entry.getValue().getString()));
				System.out.println(currentPath + key + ": " + (entry.getValue().getString()));
			} else if (entry.getValue().isDouble()) {
				frame.println(currentPath + key + ": " + (entry.getValue().getDouble()));
				System.out.println(currentPath + key + ": " + (entry.getValue().getDouble()));
			} else if (entry.getValue().isBoolean()) {
				frame.println(currentPath + key + ": " + (entry.getValue().getBoolean()));
				System.out.println(currentPath + key + ": " + (entry.getValue().getBoolean()));
			} else if (entry.getValue().isBooleanArray()) {
				String data = "";
				for (boolean b : entry.getValue().getBooleanArray()) {
					data += b + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data);
			} else if (entry.getValue().isDoubleArray()) {
				String data = "";
				for (double d : entry.getValue().getDoubleArray()) {
					data += d + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data);
			} else if (entry.getValue().isStringArray()) {
				String data = "";
				for (String s : entry.getValue().getStringArray()) {
					data += s + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data);
			}
		}
		for (String subTable : table.getSubTables()) {
			NetworkTable subtable = table.getSubTable(subTable);
			recursiveSearch(subtable, currentPath + subTable + "/");
		}
	}

	public static void push(String table, String key, String data) {
		NetworkTable networkTable = instance.getTable(table);
		if (isDouble(data)) {
			networkTable.getEntry(key).forceSetDouble(Double.valueOf(data));
		} else if (data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")) {
			networkTable.getEntry(key).forceSetBoolean(Boolean.valueOf(data));
		} else {
			networkTable.getEntry(key).forceSetString(data);
		}
	}

	@SuppressWarnings("unused")
	private static boolean isDouble(String string) {
		try {
			double d = Double.valueOf(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void save() {
		try {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileFilter(new FileNameExtensionFilter("TABLE files", ".table"));
			int returnValue = jfc.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File saveFile = jfc.getSelectedFile();
				saveFile.delete();
				try {
					saveFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				PrintWriter out = null;
				try {
					out = new PrintWriter(new FileWriter(saveFile, true));
				} catch (IOException e) {
					e.printStackTrace();
				}
				recursiveSave(instance.getTable(""), "", out);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		try {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileFilter(new FileNameExtensionFilter("TABLE files", ".table"));
			int returnValue = jfc.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File loadFile = jfc.getSelectedFile();
				Scanner in = null;
				try {
					in = new Scanner(loadFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				while (in.hasNextLine()) {
					try {
						String[] raw = in.nextLine().split(":");
						NetworkTable table = instance.getTable(raw[0]);
						if (raw[3].equalsIgnoreCase("STRING")) {
							table.getEntry(raw[1]).forceSetString(raw[2]);
						} else if (raw[3].equalsIgnoreCase("DOUBLE")) {
							table.getEntry(raw[1]).forceSetDouble(Double.valueOf(raw[2]));
						} else if (raw[3].equalsIgnoreCase("BOOLEAN")) {
							table.getEntry(raw[1]).forceSetBoolean(Boolean.valueOf(raw[2]));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void recursiveSave(NetworkTable table, String currentPath, PrintWriter out) {
		for (String key : table.getKeys()) {
			NetworkTableEntry entry = table.getEntry(key);
			if (entry.getValue().isString()) {
				out.println(currentPath.substring(0, currentPath.length() - 1) + ":" + key + ":"
						+ entry.getValue().getString() + ":STRING");
			} else if (entry.getValue().isDouble()) {
				out.println(currentPath.substring(0, currentPath.length() - 1) + ":" + key + ":"
						+ entry.getValue().getDouble() + ":DOUBLE");
			} else if (entry.getValue().isBoolean()) {
				out.println(currentPath.substring(0, currentPath.length() - 1) + ":" + key + ":"
						+ entry.getValue().getBoolean() + ":BOOLEAN");
			}
		}
		for (String subTable : table.getSubTables()) {
			NetworkTable subtable = table.getSubTable(subTable);
			recursiveSave(subtable, currentPath + subTable + "/", out);
		}
	}

	public static void pushArray(String table, String key, String data) {
		NetworkTable networkTable = instance.getTable(table);
		if(!data.contains(":")){
			if(isDouble(data)){
				networkTable.getEntry(key).forceSetDoubleArray(new double[] {Double.valueOf(data)});
			}else if(data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")){
				networkTable.getEntry(key).forceSetBooleanArray(new boolean[] {Boolean.valueOf(data)});
			}else{
				networkTable.getEntry(key).forceSetStringArray(new String[] {data});
			}
		}
		boolean first = true;
		boolean d = false;
		boolean b = false;
		boolean string = false;
		String[] raw = data.split(":");
		for (String s : raw) {
			s = s.trim();
			if (first) {
				if(isDouble(s)){
					d = true;
				}else if(data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false")){
					b = true;
				}else{
					string = true;
				}
				first = false;
				break;
			}
		}
		if(d){
			double[] da = new double[raw.length];
			for(int i = 0; i < raw.length;i++){
				da[i] = Double.valueOf(raw[i]);
			}
			networkTable.getEntry(key).forceSetDoubleArray(da);
		}else if(b){
			boolean[] ba = new boolean[raw.length];
			for(int i = 0; i < raw.length;i++){
				ba[i] = Boolean.valueOf(raw[i]);
			}
			networkTable.getEntry(key).forceSetBooleanArray(ba);
		}else if(string){
			String[] sa = new String[raw.length];
			for(int i = 0; i < raw.length;i++){
				sa[i] = raw[i];
			}
			networkTable.getEntry(key).forceSetStringArray(sa);
		}
	}
}
