package Model;

public class Seat {

	private int seatNumber;
	private boolean reserved = false;

	public Seat(int seatNumber) {
		setSeatNumber(seatNumber);
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved() {
		reserved = true;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
}
