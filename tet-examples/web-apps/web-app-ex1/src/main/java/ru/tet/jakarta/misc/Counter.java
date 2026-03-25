package ru.tet.jakarta.misc;

@SuppressWarnings("serial")
public class Counter implements java.io.Serializable {
	int counter = 0;
	String last;

	public int getCount() {
		counter++;
		return counter;
	}

	public void setLast(String uri) {
		last = uri;
	}

	public String getLast() {
		return last;
	}
}
