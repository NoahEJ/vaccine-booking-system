/*
Program Name: Vaccine Management System
Author: Noah Juravsky
Purpose: To allow Ontarians to book COVID-19 vaccination appointments and for nurses to attend to those appointments.
Date began: June 7, 2021
Date modified: June 22, 2021
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;


public class Interface {
    public static void main(String[] args) throws Exception {

        // generates new days and deletes old dates if necessary
        System.out.println("Generating appointments...");
        Scanner sc = new Scanner(System.in);
        newDay today = new newDay();
        today.oldDirectories();
        today.generateNewDays();
        today.doseReset();


        // UNCOMMENT LINE 21 TO GENERATE RANDOM APPOINTMENTS
        MakeFakeAppts x = new MakeFakeAppts();
        x.generateAppts();

        // If you want to generate a new set of appointments after you've already generated a first set,
        // Clear the Dates.txt file first


        // variables to check if first input is valid
        boolean personType = true;
        boolean containsNumbers;
        boolean containsLetters;
        String person = "";
        System.out.println("Hey there!");
        System.out.println("Welcome to Ontario's COVID-19 Vaccine Booking System!");
        String personInput = "";

        // loop makes sure input is valid and if it is, determines if the user is a Patient, Nurse, or Admin
        while (personType == true) {
            personType = false;
            containsNumbers = false;
            containsLetters = false;
            System.out.println("If you are a patient, please type the letter \"P\".");
            System.out.println("If you are a nurse, please enter your first and last name.");

            personInput = sc.nextLine().toLowerCase();
            char[] inputCharacters = new char[personInput.length()];
            for (int i = 0; i < personInput.length(); i++) {
                inputCharacters[i] = personInput.charAt(i);
                if (Character.isDigit(inputCharacters[i])) {
                    containsNumbers = true;
                }
                if (!Character.isDigit(inputCharacters[i])) {
                    containsLetters = true;
                }
            }

            if (personInput.contains(" ") && !containsNumbers) {
                person = "Nurse";
            } else if (personInput.contains("p") && containsNumbers == false && personInput.length() == 1) {
                person = "Patient";
            } else if (containsNumbers == true && containsLetters == false) {
                person = "Admin";
            } else {
                System.out.println("That doesn't seem right... please try again");
                personType = true;
            }
        }
        System.out.println();


        // if the user is a patient
        if (person.contains("Patient")) {
            System.out.println("Thanks for helping Ontario in its fight to eradicate COVID-19!");
            Thread.sleep(500);

            // loads current eligiibility from Eligibility.txt into an ArrayList
            BufferedReader input = new BufferedReader(new FileReader("Eligibility.txt"));
            ArrayList<String> currentEligibility = new ArrayList<String>();
            String line = input.readLine();
            int counter = 1;
            boolean success = false;
            Patient human = new Patient();
            while (line != null) {
                currentEligibility.add(line);
                line = input.readLine();
            }

            // displays eligibility in ordered list
            while (success == false) {
                System.out.println("Currently, we are only serving individuals with the following eligibility:");
                for (int i = 0; i < currentEligibility.size(); i++) {
                    System.out.println(counter + ". " + currentEligibility.get(i));
                    Thread.sleep(500);
                    counter++;
                }
                counter = 1;
                System.out.println();

                // The following while loops take data from the user, ensure they're in the correct format
                // Then saves the data to the instance called human
                System.out.println("Enter the number that applies to you. If there is more than one, separate the numbers with a comma.");
                String numbers = sc.nextLine();
                try {
                    human.setEligibility(currentEligibility, numbers);
                    success = true;
                } catch (Exception e) {
                    System.out.println("Whoops! Please try again");
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("Thanks! Now, let's collect some personal information.");

            success = false;
            while (success == false) {
                System.out.println("Please enter your first name:");
                String firstName = sc.nextLine();
                try {
                    human.setFirstName(firstName);
                    success = true;
                } catch (Exception e) {
                    System.out.println("Uh oh! Please enter your real first name");
                }
            }
            success = false;
            while (success == false) {
                System.out.println("Please enter your last name:");
                String lastName = sc.nextLine();
                try {
                    human.setLastName(lastName);
                    success = true;
                } catch (Exception e) {
                    System.out.println("Uh oh! Please enter your real last name");
                }
            }
            success = false;
            while (success == false) {
                System.out.println("Please enter your date of birth (yyyy/mm/dd):");
                String dateOfBirth = sc.nextLine();
                try {
                    human.setDateOfBirth(dateOfBirth);
                    success = true;
                } catch (Exception e) {
                    System.out.println("Uh oh! Please enter your date of birth using the correct format.");
                }
            }
            System.out.println("Please enter your street address:");
            String address = sc.nextLine();
            human.setAddress(address);
            success = false;
            while (success == false) {
                System.out.println("Please enter the city in which you live:");
                String city = sc.nextLine();
                try {
                    human.setCity(city);
                    success = true;
                } catch (Exception e) {
                    System.out.println("Uh oh! Please enter your real city.");
                }
            }

            //  creates instance of appointment and finds the dates that one can book
            Appointments appointment = new Appointments();
            LinkedList<String> dates = appointment.datesAvailable();
            int numberOfDates = Integer.parseInt(dates.getLast());
            System.out.println("Amazing! Now, on what date are you looking to book your appointment?");
            System.out.println("We have the following " + numberOfDates + " dates available:");
            for (int i = 0; i < numberOfDates; i++) {
                System.out.println(dates.get(i));
            }

            // asks user for date they would like to book their appointment
            boolean verifyInfo = false;
            String dateInput = "";
            while (verifyInfo == false) {
                System.out.println("Please type the date on which you would like to book your appointment:");
                dateInput = sc.nextLine();
                for (int i = 0; i < numberOfDates; i++) {
                    if (dateInput.equals(dates.get(i))) {
                        verifyInfo = true;
                    }
                }
                if (verifyInfo == false) {
                    System.out.println("That doesn't look right. Please try again. Capitalization matters.");
                }
            }

            // shows locations and the numbers of appointments that they have available at each one
            System.out.println();
            System.out.println("AVAILABLE APPOINTMENTS:");
            String[][] datesAvailable = appointment.locations(dateInput);
            for (int i = 0; i < datesAvailable.length; i++) {
                System.out.println();
                System.out.println(datesAvailable[i][0] + ":");
                System.out.println(datesAvailable[i][1] + " appointments available");

            }

            // verifies that the location inputted matches one of the locations available
            String locationInput = "";
            verifyInfo = false;
            System.out.println("Please type the location at which you would like to view times.");
            while (verifyInfo == false) {
                locationInput = sc.nextLine();
                for (int i = 0; i < datesAvailable.length; i++) {
                    if (locationInput.equals(datesAvailable[i][0])) {
                        verifyInfo = true;
                    }
                }
                if (verifyInfo == false) {
                    System.out.println("Hmmm that doesn't seem right. Please retype the location name as you see it on screen.");
                }
                System.out.println();
            }

            // shows all times available at that location
            String[] timesAvailable = appointment.times(locationInput);
            System.out.println();
            System.out.println("We have appointments available at the following times:");

            // stops displaying times if null
            for (int i = 0; i < timesAvailable.length; i++) {
                if (timesAvailable[i] == null) {
                    System.out.println();
                    System.out.println();
                    break;
                }
                // formatting
                System.out.printf("%-10s", timesAvailable[i]);
                if ((i + 1) % 4 == 0) {
                    System.out.println();
                }
            }
            // verifies that the user's time input is valid
            String time = "";
            verifyInfo = false;
            while (verifyInfo == false) {
                System.out.println("At what time would you like to book your appointment?");
                time = sc.nextLine();
                for (int i = 0; i < timesAvailable.length; i++) {
                    if (time.equals(timesAvailable[i])) {
                        verifyInfo = true;
                    }
                }
                if (verifyInfo == false) {
                    System.out.println("Please enter one of the times you see above.");
                }
            }

            // asks user to verify all of their information
            System.out.println();
            System.out.println("Great! Please verify all of the following information:");
            System.out.println();
            System.out.println();
            System.out.println("Appointment details:");
            System.out.println(appointment.getLocationSelected());
            System.out.println(appointment.getDateSelected());
            System.out.println(time);
            System.out.println();
            System.out.println();
            System.out.println("Reason(s) for eligibility:");
            ArrayList<String> eligibility = human.getEligibility();
            for (int i = 0; i < eligibility.size(); i++) {
                System.out.println("- " + eligibility.get(i));
            }
            System.out.println();
            System.out.println("Personal details:");
            System.out.println("Full name: " + human.getFirstName() + " " + human.getLastName());
            System.out.println("Date of Birth: " + human.getDateOfBirth());
            System.out.println("Address: " + human.getAddress());
            System.out.println("City: " + human.getCity());
            System.out.println();

            // ensures that the user types book or "book" and then sets the appointment
            verifyInfo = false;
            while (verifyInfo == false) {
                System.out.println("Please type \"book\" if this information is correct and you would like to confirm your appointment.");
                String apptBooking = sc.nextLine();
                apptBooking = apptBooking.toLowerCase().trim();
                if (apptBooking.equals("book") || apptBooking.equals("\"book\"")) {
                    verifyInfo = true;
                    appointment.setAppointment(time, human);
                }
            }
            System.out.println("Your appointment has been booked! We'll see you soon.");
        }


        // If the user is a nurse:
        else if (person.contains("Nurse")) {
            boolean verifyInfo = false;
            Nurse nurse = new Nurse();
            String nurseDOB = "";
            while (verifyInfo == false) {
                System.out.println("To verify your identity, please enter your date of birth (yyyy/mm/dd).");
                nurseDOB = sc.nextLine();

                // ensures that the DOB submitted is correctly formatted
                try {
                    nurse.setDateOfBirth(nurseDOB);
                    verifyInfo = true;
                } catch (Exception exception) {
                    System.out.println("Hmm, that's not a valid birthday! Please try again.");
                }
            }
            // uses name and DOB to see if the nurse exists
            boolean b = nurse.findPerson(personInput, nurseDOB);

            // if he/she exists:
            if (b == true) {
                System.out.println("Looks good!");

                // sees if the nurse has people to vaccinate
                boolean people = nurse.createDay();
                if (people == true) {
                    verifyInfo = false;
                    String nurseAction = "";

                    // makes sure input from user is valid
                    while (verifyInfo == false) {
                        System.out.println("Enter the number 1 to view your schedule for today.");
                        System.out.println("Enter the number 2 to start vaccinating!");
                        nurseAction = sc.nextLine();
                        if (nurseAction.equals("1") || nurseAction.equals("2")) {
                            verifyInfo = true;
                        } else {
                            System.out.println("That doesn't seem right.");
                        }
                    }
                    // either gets schedule or allows nurse to "start immunizing"
                    int inputNurse = Integer.parseInt(nurseAction);
                    if (inputNurse == 1) {
                        nurse.getSchedule();
                    } else {
                        nurse.startImmunizing();
                    }
                } else {
                    System.out.println("Wow, no people to vaccinate today!");
                }
            } else {
                System.out.println("Uh oh... we couldn't find you in the system. Please try again.");
            }
        } else {
            Admin admin = new Admin();
            boolean verifyInfo = false;
            System.out.println("Please enter your password.");
            String password = sc.nextLine();

            // verifies if ID # and password match
            boolean isAdmin = admin.findPerson(personInput, password);
            if (isAdmin == true) {
                String adminAction = "";

                // verifies that user input is valid
                while (verifyInfo == false) {
                    System.out.println("Enter the number 1 to see daily statistics.");
                    System.out.println("Enter the number 2 to change eligibility requirements.");
                    adminAction = sc.nextLine();
                    if (adminAction.equals("1") || adminAction.equals("2")) {
                        verifyInfo = true;
                    } else {
                        System.out.println("That doesn't seem right.");
                    }
                }

                // allows admin to either see daily statistics or change eligibility
                int inputAdmin = Integer.parseInt(adminAction);
                if (inputAdmin == 1) {
                    admin.dailyStatistics();
                } else {
                    admin.changeEligibility();
                }
            } else {
                System.out.println("Sorry, the ID and password are incorrect");
            }
        }
    }
}