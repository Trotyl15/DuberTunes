package Client;

import java.util.ArrayList;

public class Album{
	ArrayList<Music> albumByDate;
	ArrayList<Music>albumByName;
	String name;
	Album(String name){
		this.name=name;
		albumByName=new ArrayList<>();
		albumByDate=new ArrayList<>();
	}

	//initializing the two types of sorting ways
	public void addSong(Music music) {
		albumByDate.add(music);
		String musicName=music.getName();
		int i=0;
		if(albumByName.size()==0) {
			albumByName.add(music);
		}else {
			for(Music m:albumByName) {
				if(m.getName().compareTo(musicName)<0||i==albumByName.size()-1) {
					albumByName.add(i,music);
					break;
				}
				i++;
			}
		}
	}





	@Override
	public String toString() {
		return name;
	}

	public ArrayList<Music> getAlbumByDate() {
		return albumByDate;
	}

	public ArrayList<Music> getAlbumByName() {
		return albumByName;
	}

}
