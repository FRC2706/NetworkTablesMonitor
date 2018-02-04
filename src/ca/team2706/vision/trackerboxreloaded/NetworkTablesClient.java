package ca.team2706.vision.trackerboxreloaded;

import java.util.Scanner;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTablesClient {
	public static Frame frame;
	public static String filter = "";
	private static boolean done = false;
	public static NetworkTableInstance instance;
	@SuppressWarnings("resource")
	public static void main(String[] args){
		frame = new Frame();
		System.out.println("Would you like to enter a ip? Y/N");
		Scanner scan = new Scanner(System.in);
		String response = scan.nextLine();
		if(response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES")){
			System.out.println("Enter a ip!");
			useIp(scan.nextLine());
		}else{
			System.out.println("Enter a team #");
			useTeam(Integer.valueOf(scan.nextLine()));
		}
	}

	public static void useIp(String ip) {
		if(done){
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startClient(ip);
		
		while(true){
			NetworkTable root = instance.getTable(filter);
			frame.clear();
			recursiveSearch(root,"");
			
			/*for(String key : vision.getKeys()){
				NetworkTableEntry entry = vision.getEntry(key);
				if(entry.getValue().isString()){
					frame.println(key+": "+(entry.getValue().getString()));
				}
			}
			for(String subTable : vision.getSubTables()){
				NetworkTable table = vision.getSubTable(subTable);
				for(String key : table.getKeys()){
					if(table.getEntry(key).getValue().isString()){
						frame.println(key+": "+(table.getEntry(key).getValue().getString()));
					}else if(table.getEntry(key).getValue().isDouble()){
						frame.println(key+": "+(table.getEntry(key).getValue().getDouble()));
					}
				}
			}*/
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void useTeam(int team) {
		if(done){
			return;
		}
		done = true;
		instance = NetworkTableInstance.getDefault();
		instance.setUpdateRate(0.02);
		instance.startClientTeam(team);
		
		while(true){
			NetworkTable root = instance.getTable(filter);
			frame.clear();
			recursiveSearch(root,"");
			/*for(String key : root.getKeys()){
				NetworkTableEntry entry = root.getEntry(key);
				if(entry.getValue().isString()){
					frame.println(key+": "+(entry.getValue().getString()));
				}
			}
			for(String subTable : root.getSubTables()){
				NetworkTable table = root.getSubTable(subTable);
				for(String key : table.getKeys()){
					if(table.getEntry(key).getValue().isString()){
						frame.println(key+": "+(table.getEntry(key).getValue().getString()));
					}else if(table.getEntry(key).getValue().isDouble()){
						frame.println(key+": "+(table.getEntry(key).getValue().getDouble()));
					}
				}
			}*/
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	private static void recursiveSearch(NetworkTable table, String currentPath){
		for(String key : table.getKeys()){
			NetworkTableEntry entry = table.getEntry(key);
			if(entry.getValue().isString()){
				frame.println(currentPath+key+": "+(entry.getValue().getString()));
				System.out.println(currentPath+key+": "+(entry.getValue().getString()));
			}else if(entry.getValue().isDouble()){
				frame.println(currentPath+key+": "+(entry.getValue().getDouble()));
				System.out.println(currentPath+key+": "+(entry.getValue().getDouble()));
			}else if(entry.getValue().isBoolean()){
				frame.println(currentPath+key+": "+(entry.getValue().getBoolean()));
				System.out.println(currentPath+key+": "+(entry.getValue().getBoolean()));
			}
		}
		for(String subTable : table.getSubTables()){
			NetworkTable subtable = table.getSubTable(subTable);
			recursiveSearch(subtable, currentPath+subTable+"/");
		}
	}

	public static void push(String table,String key, String data) {
		NetworkTable networkTable = instance.getTable(table);
		if(isDouble(data)){
			networkTable.getEntry(key).setDouble(Double.valueOf(data));
		}else if(isBoolean(data)){
			networkTable.getEntry(key).setBoolean(Boolean.valueOf(data));
		}else{
			networkTable.getEntry(key).setString(data);
		}
	}
	@SuppressWarnings("unused")
	private static boolean isDouble(String string){
		try{
			double d = Double.valueOf(string);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	@SuppressWarnings("unused")
	private static boolean isBoolean(String string){
		try{
			boolean b = Boolean.valueOf(string);
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
