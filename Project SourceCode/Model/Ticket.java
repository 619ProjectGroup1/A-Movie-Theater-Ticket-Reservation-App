package Model;

import java.util.ArrayList;

public class Ticket {

	private int ticketId;
	private Theatre theatre;
	private Movie movie;
	private ShowTime showTime;
	private ArrayList<Seat> seats;
	private User user;
	private double amount;

	
	public Ticket(int ticketId, User user, Theatre theatre, Movie movie, ShowTime showTime, ArrayList<Seat> seat, double amount) {
		setTicketId(ticketId);
		setUser(user);
		setTheatre(theatre);
		setMovie(movie);
		setShowTime(showTime);
		setSeat(seat);
		setAmount(amount);
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public void setShowTime(ShowTime showTime) {
		this.showTime = showTime;
	}

	public void setSeat(ArrayList<Seat> seat) {
		this.seats = seat;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getTicketId() {
		return ticketId;
	}
	
	public Theatre getTheatre() {
		return theatre;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public ShowTime getShowTime() {
		return showTime;
	}
	
	public ArrayList<Seat> getSeat() {
		return seats;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "TicketID: " + getTicketId() + "\nMovie: " + getMovie() + "\nShowtime: "
				+ getShowTime() + "\nSeats: " + getSeat() + "\nAmount: $" + getAmount();
	}
}
