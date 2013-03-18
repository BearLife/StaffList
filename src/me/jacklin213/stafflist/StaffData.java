package me.jacklin213.stafflist;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffData {
	
	private File staffDataFile;
	private ArrayList<String> staffNames;
	public StaffData(File file){
		this.staffDataFile = file;
		this.staffNames = new ArrayList<String>();
		
		if (this.staffDataFile.exists() ==  false){
			try {
				this.staffDataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void load(){
		try {
			//Processes the FileData to the BufferedReader
			DataInputStream input = new DataInputStream(new FileInputStream(this.staffDataFile));
			//Processes the data into Eclipse
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			//Creates a String called line
			String line;
			
			//When the string line is equal to the lines read off the file and does not = 0, repeat method.
			while ((line = reader.readLine()) != null){
				//If the staffNames ArrayList doesnt contain the name
				if(this.staffNames.contains(line) == false){
					//This adds the name
					this.staffNames.add(line);
				}
			}
			
			reader.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		try {
			//Filewriter to write to file
			FileWriter writer = new FileWriter(this.staffDataFile);
			//BufferedWriter to process data to the FileWriter
			BufferedWriter out = new BufferedWriter(writer);
			
			//for loop to save all staff names to the file
			for (String staffNames : this.staffNames){
				out.write(staffNames);
				out.newLine();
			}
			
			//closes the BufferedWriter and the FileWriter to prevent data loss
			out.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Make a Method to check if The thing "contains" the String/Name
	public boolean contains(String staffNames){
		return this.staffNames.contains(staffNames);
	}
	
	public void add(String staffName){
		//Using the Contains method above, checks to see if the Name that is going to be added is already there
		if (this.contains(staffName) == false){
			this.staffNames.add(staffName);	
		}
	}
	
	public void remove(String staffName){
		this.staffNames.remove(staffName);
	}
	
	//Gets all the StaffNames (NOT USED ATM)
	public ArrayList<String> getStaffNamesArrayed(){
		return this.staffNames;
	}
	
	public String getStaffNames(){
		String allStaff = "";

		for (String s : this.staffNames){
			allStaff += s + ", ";
		}
		
		return allStaff;
	}
	
	public int staffCount() throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(this.staffDataFile));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public int numStaffOnline() throws IOException {
		DataInputStream input = new DataInputStream(new FileInputStream(this.staffDataFile));
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		int num = 0;
	    try {
	    	String name;
			while ((name = reader.readLine()) != null){
				if (playerOnlineCheck(name) == true){
					num += 1;
				}
			}
			 return num;
		} finally {
			reader.close();
			input.close();
		}
	   
	}
	
	public boolean playerOnlineCheck(String playerName){
		Player player = Bukkit.getServer().getPlayerExact(playerName);
		if (player != null){
			return true;
		} else {
			return false;
		}
	}
	
	public String staffOnline() throws IOException{
		DataInputStream input = new DataInputStream(new FileInputStream(this.staffDataFile));
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    	try {
    		String name;
    		String allStaff = "";
			while ((name = reader.readLine()) != null){
				if (playerOnlineCheck(name) == true) {
					allStaff += name + ", ";
				}
			}
    	return allStaff;	
    	} finally {
    		reader.close();
    		input.close();
    	}
	}
	
}
	

