package ca.team2706.vision.trackerboxreloaded;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
	public static ArrayList<SubProcess> processes = new ArrayList<SubProcess>();

	public static void main(String[] args) {
		frame = new Frame();
	}

	public static void useIp(String ip) {
		if (done) {
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startClient(ip);
		loadAddons();
		while (true) {
			NetworkTable root = instance.getTable(filter);
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
		loadAddons();
		while (true) {
			NetworkTable root = instance.getTable(filter);
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
				frame.println(currentPath + key + ": " + (entry.getValue().getString()),currentPath+key);
				//System.out.println(currentPath + key + ": " + (entry.getValue().getString()));
			} else if (entry.getValue().isDouble()) {
				frame.println(currentPath + key + ": " + (entry.getValue().getDouble()),currentPath+key);
				//System.out.println(currentPath + key + ": " + (entry.getValue().getDouble()));
			} else if (entry.getValue().isBoolean()) {
				frame.println(currentPath + key + ": " + (entry.getValue().getBoolean()),currentPath+key);
				//System.out.println(currentPath + key + ": " + (entry.getValue().getBoolean()));
			} else if (entry.getValue().isBooleanArray()) {
				String data = "";
				for (boolean b : entry.getValue().getBooleanArray()) {
					data += b + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data,currentPath+key);
			} else if (entry.getValue().isDoubleArray()) {
				String data = "";
				for (double d : entry.getValue().getDoubleArray()) {
					data += d + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data,currentPath+key);
			} else if (entry.getValue().isStringArray()) {
				String data = "";
				for (String s : entry.getValue().getStringArray()) {
					data += s + " : ";
				}
				data = data.substring(0, data.length() - 3);
				frame.println(currentPath + key + ": " + data,currentPath+key);
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

	@SuppressWarnings({ "deprecation", "resource" })
	public static void loadAddons() {
		File dir = new File("addons/");
		if (dir.exists() && dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				if (f.getName().endsWith(".jar")) {
					File newFile = new File(f.getParentFile().getPath() + "/temp/");
					if(newFile.exists() || !newFile.isDirectory()){
						newFile.delete();
						newFile.mkdir();
					}
					try {
						extractJar(f.getPath(), newFile.getPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						File programData = new File(newFile.getPath() + "/manifest.txt");
						if (!programData.exists()) {
							continue;
						}
						Scanner in = new Scanner(programData);

						URL url = newFile.toURL();
						URL[] urls = new URL[] { url };
						ClassLoader cl = new URLClassLoader(urls);
						Class<?> cls = cl.loadClass(in.nextLine());
						in.close();
						SubProcess process = (SubProcess) cls.newInstance();
						processes.add(process);
						process.start();
						newFile.delete();
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
	}

	public static void extractJar(String loc, String dest) throws IOException{
		JarFile jar = new JarFile(new File(loc));
		Enumeration<?> enumeration1 = jar.entries();
		while(enumeration1.hasMoreElements()){
			JarEntry file = (JarEntry) enumeration1.nextElement();
			File f = new File(dest + java.io.File.separator + file.getName());
			if(file.isDirectory()){
				f.mkdir();
				continue;
			}
		}
		Enumeration<?> enumeration = jar.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry file = (JarEntry) enumeration.nextElement();
			File f = new File(dest + java.io.File.separator + file.getName());
			if (file.isDirectory()) { // if its a directory, create it
				f.mkdir();
				continue;
			}
			InputStream is = jar.getInputStream(file); // get the input stream
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			f.delete();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			while (is.available() > 0) {  // write contents of 'is' to 'fos'
				fos.write(is.read());
			}
			fos.close();
			is.close();
		}
		jar.close();
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
	public static void startServer(){
		if (done) {
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startServer();
		loadAddons();
		while (true) {
			NetworkTable root = instance.getTable(filter);
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
	public static void connect(String ip, int port){
		try{
			Socket s = new Socket(ip,port);
			out = new PrintWriter(s.getOutputStream(),true);
			Scanner in = new Scanner(s.getInputStream());
			while(!s.isClosed()){
				frame.sPrintln(in.nextLine());
			}
			in.close();
			s.close();
		}catch (Exception e) {
			frame.btnStartServer_1.setEnabled(true);
			frame.btnConnect.setEnabled(true);
			e.printStackTrace();
			out = null;
		}
	}
	public static PrintWriter out = null;
	public static void startServer(int port){
		try{
			ServerSocket ss = new ServerSocket(port);
			while(!ss.isClosed()){
				Socket s = null;
				try{
					s = ss.accept();
					frame.sPrintln("Connection from: "+s.getInetAddress().toString());
					Scanner in = new Scanner(s.getInputStream());
					out = new PrintWriter(s.getOutputStream(),true);
					while(!s.isClosed()){
						frame.sPrintln(in.nextLine());
					}
					in.close();
				}catch(Exception e){
					frame.sPrintln(s.getInetAddress().toString()+" Disconnected!");
					out = null;
				}
			}
			ss.close();
		}catch (Exception e) {
			frame.btnStartServer_1.setEnabled(true);
			frame.btnConnect.setEnabled(true);
			e.printStackTrace();
			out = null;
		}
	}
}