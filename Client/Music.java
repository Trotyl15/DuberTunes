package Client;

import java.awt.Image;

public class Music{
	String filePath;
	String name;
	Image cover;
	byte[] b;

	//constructor
	public Music(String filePath, String name, Image cover, byte[] b) {
		this.filePath = filePath;
		this.name = name;
		this.cover = cover;
		this.b = b;
	}
	public Music() {
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
