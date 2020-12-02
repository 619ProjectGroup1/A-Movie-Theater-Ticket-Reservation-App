package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Controller for the GUI
 */
public class GUIController {

    private LoginForm loginForm;
    private RegistrationForm registrationForm;
    private Menu menu;
    private ReservationForm reservationForm;
    private PaymentForm paymentForm;
    private AnnualFeeForm annualFeeForm;
    private SeatSelectionForm seatSelectionForm;
    private CancellationForm cancellationForm;
    private MainController mainController;

    /**
     * Instantiates a new Gui controller.
     */
    public GUIController() {
        setLoginForm(new LoginForm());
        setRegistrationForm(new RegistrationForm());
        setMenu(new Menu());
        setReservationForm(new ReservationForm());
        setCancellationForm(new CancellationForm());
        setPaymentForm(new PaymentForm());
        setAnnualFeeForm(new AnnualFeeForm());
        setSeatSelectionForm(new SeatSelectionForm());

        //setting up all action listeners
        loginForm.signUpButton(new SignUpButton());
        loginForm.loginButton(new LoginButton());
        loginForm.continueAsGuestButton(new ContinueAsGuest());

        registrationForm.registerButton(new RegisterButton());

        menu.reservationButton(new MakeReservationButton());
        menu.cancelReservation(new CancelReservationButton());
        menu.logoutButton(new LogoutButton());

        reservationForm.returnToMenu(new ReturnToMenu());
        reservationForm.movieList(new MovieList());
        reservationForm.showTimeList(new ShowTimeList());
        reservationForm.seatSelection(new SeatSelection());

        cancellationForm.returnToMenu(new ReturnToMenu());
        cancellationForm.cancelTicket(new CancelTicket());

        seatSelectionForm.seatButtonListener(new SeatButton());
        seatSelectionForm.confirmSelection(new ConfirmSelection());
        seatSelectionForm.returnToReservation(new ReturnToReservation());

        annualFeeForm.submitAnnualFeePayment(new SubmitAnnualFeePayment());

        paymentForm.submitPayment(new SubmitPayment());
        paymentForm.returnToReservation(new ReturnToReservation());

        loginForm.setVisible(true);
    }

    //action listener for launching the registration form
    private class SignUpButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            loginForm.setVisible(false);
            registrationForm.setVisible(true);
        }
    }

    //action listener for logging in a user
    private class LoginButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            boolean login = mainController.login(loginForm.getEmail().getText(), new String((loginForm.getPassword().getPassword())));
            if (login) {
                loginForm.getErrorMsg().setText("Logging in!");
                loginForm.setVisible(false);
                menu.setVisible(true);
            } else {
                loginForm.getErrorMsg().setText("Incorrect email or password");
            }
        }
    }

    //action listener to continue as a guest in the system
    private class ContinueAsGuest implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            loginForm.setVisible(false);
            menu.setVisible(true);
        }
    }

    //action listener for registering a new registered user
    private class RegisterButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                mainController.getUserCtrl().createRegisteredUser(500,
                        registrationForm.getFirstName().getText(),
                        registrationForm.getLastName().getText(),
                        registrationForm.getEmail().getText(),
                        registrationForm.getPassword().getText(),
                        new CreditCard(registrationForm.getFirstName().getText()
                                + " " + registrationForm.getLastName().getText(),
                                registrationForm.getCcNum().getText(),
                                Integer.parseInt(registrationForm.getExpiry().getText()),
                                registrationForm.getCvc().getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "An error occurred, please try again");
                return;
            }
            JOptionPane.showMessageDialog(null, "Thank you! Account created! You are now required to pay the annual fee of $20");

            registrationForm.setVisible(false);
            annualFeeForm.getSubTotal().setText("$20.00");
            annualFeeForm.getEmail().setText(registrationForm.getEmail().getText());
            annualFeeForm.getCardName().setText(registrationForm.getFirstName().getText() + " " + registrationForm.getLastName().getText());
            annualFeeForm.getCcNum().setText(registrationForm.getCcNum().getText());
            annualFeeForm.getExpiry().setText(registrationForm.getExpiry().getText());
            annualFeeForm.getCvc().setText(registrationForm.getCvc().getText());
            annualFeeForm.setVisible(true);

//            CreditCard card = new CreditCard(registrationForm.getFirstName().getText() + " " + registrationForm.getLastName().getText(),
//                    registrationForm.getCcNum().getText(),
//                    Integer.parseInt(registrationForm.getCvc().getText()),
//                    registrationForm.getExpiry().getText());
//            mainController.getPaymentCtrl().createPayment(registrationForm.getFirstName().getText() + " " + registrationForm.getLastName().getText(),
//                    card, null);

            registrationForm.clearAllFields();
        }
    }

    private class SubmitAnnualFeePayment implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(null, "Thank you! Payment accepted, account now ready for use!");
            annualFeeForm.setVisible(false);
            annualFeeForm.clearAllTextFields();
            loginForm.setVisible(true);


            RegisteredUser user = mainController.getUserCtrl().getRegisteredUserList().get(mainController.getUserCtrl().getRegisteredUserList().size() - 1);
            mainController.getPaymentCtrl().createPayment(user.getfName() + " " + user.getlName(),
                    user.getCreditCard(), user);
        }
    }

    //action listener for making a reservation
    private class MakeReservationButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            menu.setVisible(false);

            DefaultListModel<String> model = new DefaultListModel<>();
            Theatre theatre = mainController.getTheatreCtrl().getTheatreCtrlSys().getTheatre();
            for (Movie movie : theatre.getMovieList()) {
                model.addElement(movie.getMovieName());
            }
            reservationForm.getMovies().setModel(model);
            reservationForm.setVisible(true);

        }
    }

    //action listener for cancelling a reservation
    //TODO implement the cancellation of a ticket
    private class CancelReservationButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            menu.setVisible(false);
            cancellationForm.setVisible(true);
        }
    }

    //action listener for logging out of the application
    private class LogoutButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            menu.setVisible(false);
            mainController.setLoggedInUser(null);
            loginForm.clearAllFields();
            loginForm.setVisible(true);
        }
    }

    //action listener for the movie list JList on the reservation page
    private class MovieList extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            JList selection = (JList) me.getSource();
            int index = selection.locationToIndex(me.getPoint());
            Movie selected = mainController.getTheatreCtrl().getTheatreCtrlSys().getMovies().get(index);
            //setting the selected movie in the theatre controller
            mainController.getTheatreCtrl().setSelectedMovie(selected);
            DefaultListModel<String> model = new DefaultListModel<>();
            for (ShowTime time : selected.getShowTimeList()) {
                model.addElement("Date: " + time.getDate() + ", Time: " + time.getTime());
            }
            reservationForm.getShowtimes().setModel(model);
        }
    }

    //action listener for the show time list JList on the reservation page
    private class ShowTimeList extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            JList selection = (JList) me.getSource();
            int index = selection.locationToIndex(me.getPoint());
            ShowTime selected = mainController.getTheatreCtrl().getSelectedMovie().getShowTimeList().get(index);

//            System.out.println(mainController.getTheatreCtrl().getSelectedMovie());
//            System.out.println(mainController.getTheatreCtrl().getSelectedMovie().getShowTimeList().get(index));

            //setting the selected showtime in the theatre controller
            mainController.getTheatreCtrl().setSelectedShowTime(selected);
        }
    }

    //action listener for the return to menu button on the reservation page
    private class ReturnToMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            reservationForm.setVisible(false);
            cancellationForm.setVisible(false);
            menu.setVisible(true);
        }
    }

    //action listener for continuing to the seat selection page from the reservation page
    private class SeatSelection implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            reservationForm.setVisible(false);
            seatSelectionForm.updateSeatMap(mainController.getTheatreCtrl().getSelectedShowTime().getSeats());
            seatSelectionForm.setVisible(true);
        }
    }

    //action listener for the radio buttons when you are selecting a seat, updates selection and price text fields
    private class SeatButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int index = 0;
            int numSelected = 0;
            StringBuilder selection = new StringBuilder();
            for (JRadioButton seat : seatSelectionForm.getButtons()) {
                if (seat.isSelected()) {
                    selection.append(index + 1).append(", ");
                    numSelected++;
                }
                index++;
            }
            seatSelectionForm.getSelection().setText(selection.toString());
            seatSelectionForm.getPrice().setText(String.valueOf(numSelected * 21.50));
        }
    }

    //action listener to confirm seat selection and open up the payment form, if the user is logged in it will remember their credit card
    private class ConfirmSelection implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            seatSelectionForm.setVisible(false);
            paymentForm.getSubTotal().setText(seatSelectionForm.getPrice().getText());
            paymentForm.getSeatsSelected().setText(seatSelectionForm.getSelection().getText());

            if (mainController.getLoggedInUser() != null) {
                paymentForm.getEmail().setText(mainController.getLoggedInUser().getEmail());
                paymentForm.getCardName().setText(mainController.getLoggedInUser().getCreditCard().getCardHolderName());
                paymentForm.getCcNum().setText(mainController.getLoggedInUser().getCreditCard().getCardNumber());
                paymentForm.getExpiry().setText(mainController.getLoggedInUser().getCreditCard().getExpiry());
                paymentForm.getCvc().setText(String.valueOf(mainController.getLoggedInUser().getCreditCard().getCVC()));
            }


            paymentForm.setVisible(true);
        }
    }

    //action listener for the payment form to return to the reservation page
    private class ReturnToReservation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            seatSelectionForm.setVisible(false);
            paymentForm.setVisible(false);
            reservationForm.setVisible(true);
        }
    }

    //action listener for submitting payment, reserves the selected seats from the seat selection page
    private class SubmitPayment implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] selection = seatSelectionForm.getSelection().getText().split(",");
            ArrayList<Seat> seats = new ArrayList<>();
            boolean paymentComplete = false;
            getSeatsSelected(seats, selection);
            if (paymentForm.getCreditCardRadioButton().isSelected()) {

                if (mainController.getLoggedInUser() != null) {
                    mainController.getPaymentCtrl().createPayment(mainController.getLoggedInUser().getCreditCard().getCardHolderName(),
                            mainController.getLoggedInUser().getCreditCard(), mainController.getLoggedInUser());

                    //creating a ticket for a registered user and adding it to the master ticket list
                    createTicketRegisteredUser(seats);
                } else {
                    CreditCard card = new CreditCard(paymentForm.getCardName().getText(),
                            paymentForm.getCcNum().getText(),
                            Integer.parseInt(String.valueOf(paymentForm.getExpiry().getText())),
                            paymentForm.getCvc().getText());
                    mainController.getPaymentCtrl().createPayment(paymentForm.getCardName().getText(), card, null);
                    //creating a ticket for a generic user and adding it to the masterTicketList
                    createTicketGenericUser(seats);
                }
                displayTicketPurchase();
                paymentComplete = true;
            } else if (paymentForm.getVoucherRadioButton().isSelected()) {
                int voucherID = Integer.parseInt(paymentForm.getVoucherID().getText());
                boolean voucherExists = mainController.getReserveCtrl().getTicketReserveSys().getCancellationList().searchVoucher(voucherID);
                if (voucherExists) {
                    if (mainController.getLoggedInUser() != null) {
                        createTicketRegisteredUser(seats);
                    } else {
                        createTicketGenericUser(seats);
                    }
                    displayTicketPurchase();
                    paymentComplete = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Voucher does not exist, please try again");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Please select one of the forms of " +
                        "payment and fill out the required information");
            }

            if (paymentComplete) {
                paymentForm.setVisible(false);
                paymentForm.clearTextFields();
                menu.setVisible(true);
            }

        }

        /**
         * Create ticket registered user.
         *
         * @param seats the seats
         */
        public void createTicketRegisteredUser(ArrayList<Seat> seats) {
            mainController.getReserveCtrl().reserve(
                    mainController.getLoggedInUser(),
                    mainController.getTheatreCtrl().getTheatreCtrlSys().getTheatre(),
                    mainController.getTheatreCtrl().getSelectedMovie(),
                    mainController.getTheatreCtrl().getSelectedShowTime(),
                    seats, Double.parseDouble(seatSelectionForm.getPrice().getText()));
        }

        /**
         * Create ticket generic user.
         *
         * @param seats the seats
         */
        public void createTicketGenericUser(ArrayList<Seat> seats) {
            mainController.getReserveCtrl().reserve(
                    mainController.getUserCtrl().createCasualUser(),
                    mainController.getTheatreCtrl().getTheatreCtrlSys().getTheatre(),
                    mainController.getTheatreCtrl().getSelectedMovie(),
                    mainController.getTheatreCtrl().getSelectedShowTime(),
                    seats, Double.parseDouble(seatSelectionForm.getPrice().getText()));
        }

        /**
         * Display ticket purchase.
         */
        public void displayTicketPurchase() {
            Ticket ticket = mainController.getReserveCtrl().getTicketReserveSys().getMasterTicketList().getMostRecentTicket();

            JOptionPane.showMessageDialog(null, "Thank you for your purchase!\n" +
                    "Tickets have been emailed to: " + paymentForm.getEmail().getText() + "\n" + ticket);
        }

        /**
         * Gets seats selected.
         *
         * @param seats     the seats
         * @param selection the selection
         */
        public void getSeatsSelected(ArrayList<Seat> seats, String[] selection) {
            for (int i = 0; i < selection.length - 1; i++) {
                int seatNum = Integer.parseInt(selection[i].stripLeading());
                //reserving the selected seats in that particular showtime
                mainController.getTheatreCtrl().getSelectedShowTime().getSeats().get(seatNum - 1).setReserved();

                seats.add(mainController.getTheatreCtrl().getSelectedShowTime().getSeats().get(seatNum - 1));
            }
        }
    }

    private class CancelTicket implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int ticketID = Integer.parseInt(cancellationForm.getTextField1().getText());
            Ticket ticketToCancel = mainController.getReserveCtrl().getTicketReserveSys().getMasterTicketList().searchTicket(ticketID);
            boolean successful = mainController.getReserveCtrl().cancelTicket(ticketID);

            if (successful && mainController.getLoggedInUser() == null) {
                Voucher voucher = new Voucher(ticketToCancel.getAmount() * 0.85);
                mainController.getReserveCtrl().addCancellation(new Cancellation(voucher));

                //cancelling the seats from that showtime
                mainController.getTheatreCtrl().getSelectedShowTime().cancelSeats(ticketToCancel.getSeat());

                JOptionPane.showMessageDialog(null, "Thank you for your cancellation, " +
                        "you will receive a voucher for future use:\n" + voucher);
            } else if (successful && mainController.getLoggedInUser() != null) {
                Refund refund = new Refund(ticketToCancel.getAmount());

                //cancelling the seats from that showtime
                mainController.getTheatreCtrl().getSelectedShowTime().cancelSeats(ticketToCancel.getSeat());
                JOptionPane.showMessageDialog(null, "Thank you for your cancellation, " +
                        "you will receive a full refund:\n" + refund);
            }

            cancellationForm.setVisible(false);
            cancellationForm.getTextField1().setText("");
            menu.setVisible(true);
        }


    }

    /**
     * Gets annual fee form.
     *
     * @return the annual fee form
     */
    public AnnualFeeForm getAnnualFeeForm() {
        return annualFeeForm;
    }

    /**
     * Sets annual fee form.
     *
     * @param annualFeeForm the annual fee form
     */
    public void setAnnualFeeForm(AnnualFeeForm annualFeeForm) {
        this.annualFeeForm = annualFeeForm;
    }

    /**
     * Gets reservation form.
     *
     * @return the reservation form
     */
    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    /**
     * Sets reservation form.
     *
     * @param reservationForm the reservation form
     */
    public void setReservationForm(ReservationForm reservationForm) {
        this.reservationForm = reservationForm;
    }

    /**
     * Gets main controller.
     *
     * @return the main controller
     */
    public MainController getMainController() {
        return mainController;
    }

    /**
     * Gets menu.
     *
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Sets menu.
     *
     * @param menu the menu
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    /**
     * Sets login form.
     *
     * @param loginForm the login form
     */
    public void setLoginForm(LoginForm loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * Gets login form.
     *
     * @return the login form
     */
    public LoginForm getLoginForm() {
        return loginForm;
    }

    /**
     * Sets registration form.
     *
     * @param registrationForm the registration form
     */
    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    /**
     * Gets registration form.
     *
     * @return the registration form
     */
    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    /**
     * Gets payment form.
     *
     * @return the payment form
     */
    public PaymentForm getPaymentForm() {
        return paymentForm;
    }

    /**
     * Sets payment form.
     *
     * @param paymentForm the payment form
     */
    public void setPaymentForm(PaymentForm paymentForm) {
        this.paymentForm = paymentForm;
    }

    /**
     * Gets seat selection form.
     *
     * @return the seat selection form
     */
    public SeatSelectionForm getSeatSelectionForm() {
        return seatSelectionForm;
    }

    /**
     * Sets seat selection form.
     *
     * @param seatSelectionForm the seat selection form
     */
    public void setSeatSelectionForm(SeatSelectionForm seatSelectionForm) {
        this.seatSelectionForm = seatSelectionForm;
    }

    /**
     * Gets cancellation form.
     *
     * @return the cancellation form
     */
    public CancellationForm getCancellationForm() {
        return cancellationForm;
    }

    /**
     * Sets cancellation form.
     *
     * @param cancellationForm the cancellation form
     */
    public void setCancellationForm(CancellationForm cancellationForm) {
        this.cancellationForm = cancellationForm;
    }

    /**
     * Sets main controller.
     *
     * @param mainController the main controller
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}

