package chat;

public class Message implements Comparable<Message> {
	private User author;
	private String message;
	private String timeStamp; //yyyymmddhhmmss (e.g. 20140313200320 = 13.03.2014 20h 03m 20s)
	private int ID;
	
	public Message(User author, String message, int ID) {
		this.author = author;
		this.message = message;
		this.ID = ID;
	}
	
	public String getMessage() {
		return message;
	}
	
	public User getAuthor() {
		return author;
	}
	
	public String getTime() {
		return timeStamp;
	}
	
	public int getID() {
		return ID;
	}

	@Override
	public int compareTo(Message m) {
		return timeStamp.compareTo(m.timeStamp);
	}
}
