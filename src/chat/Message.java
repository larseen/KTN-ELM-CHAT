package chat;

public class Message implements Comparable<Message> {
	private User author;
	private String message;
	private String timeStamp; //yyyymmddhhmmss (e.g. 20140313200320 = 13.03.2014 20h 03m 20s)
	private int id = -1;
	
	public Message(User author, String message) {
		this.author = author;
		this.message = message;
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
	
	public int getId() {
		return id;
	}

	@Override
	public int compareTo(Message m) {
		return timeStamp.compareTo(m.timeStamp);
	}
}
