package entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import main.ConnectedSocket;

@Getter  //방장이 바뀔일도 방이름이 바뀔일도 없으니 setter는 X
public class Room {
	private String roomName;
	private String owner;
	private List<ConnectedSocket> users;
	
	public Room(String roomName, String owner) {
		this.roomName = roomName;
		this.owner = owner;
		users = new ArrayList<>();
	}
	
	public List<String> getUsernameList() {
		List<String> usernameList = new ArrayList<>();
		for(ConnectedSocket connectedSocket : users) {
			usernameList.add(connectedSocket.getUsername());
		}
		return usernameList;
	}
}
