package var;

public class Message {
	String to;
	String from;
	String text;
	String date;
	long sequence;

	String to(){
		return to;
	}

	String from(){
		return from;
	}

	String date(){
		return date;
	}

	String text(){
		return text;
	}

	long sequence(){
		return sequence;
	}

	public Message(String text, String to, String from, String date, int seq) {
		this.to= to;
		this.from = from;
		this.text = text;
		this.date = date;
		this.sequence = seq;
	}

	Message(){
		super();
	}
}
