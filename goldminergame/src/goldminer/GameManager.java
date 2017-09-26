package goldminer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import goldminer.items.GameInfo;

public class GameManager {
	
	private final static String savePath=System.getenv("APPDATA")+"/goldeminer/goldeminer.sav";

	public static void saveGame(GameInfo _gameInfo) {
		GameManager.createFolder();
		FileOutputStream fileOut=null;
		ObjectOutputStream out=null;
		try {
			fileOut=new FileOutputStream(new File(GameManager.savePath));
			out=new ObjectOutputStream(fileOut);
			out.writeObject(_gameInfo);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if(out!=null) out.close();
				if(fileOut!=null) fileOut.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static GameInfo loadGame() {
		FileInputStream fileIn=null;
		ObjectInputStream in=null;
		GameInfo gameInfo=null;
		try {
			fileIn=new FileInputStream(new File(GameManager.savePath));
			in=new ObjectInputStream(fileIn);
			gameInfo=(GameInfo) in.readObject();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} finally {
			try {
				if(in!=null) in.close();
				if(fileIn!=null) fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gameInfo;
	}
	
	private static void createFolder() {
		File file=new File(GameManager.savePath);
		if(!file.exists()) {
			File parentDir=new File(file.getParent());
			if(!parentDir.exists()) {
				parentDir.mkdir();
			}
		}
	}
	
	public static boolean checkSave() {
		return new File(GameManager.savePath).exists();
	}
	
	public static void removeSave() {
		 new File(GameManager.savePath).delete();
	}
}
