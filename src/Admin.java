import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends Person {

    private String idNumber;
    private String password;

    Admin() {
        super();
        idNumber = "123";
        password = "ghjksh-yisjs-98sj2";
    }

    // This method verifies the admin's password
    // Example of polymorphism
    public boolean findPerson(String id, String password) throws IOException {

        BufferedReader admins = new BufferedReader(new FileReader("Admins.txt"));
        String adminInfo = admins.readLine();
        boolean b = false;
        verification:
        {
            while (adminInfo != null && b == false) {
                if (id.equals(adminInfo)) {
                    adminInfo = admins.readLine();
                    if (password.equals(adminInfo)) {
                        b = true;
                    } else {
                        break verification;
                    }
                } else {
                    adminInfo = admins.readLine();
                }
            }
        }
        return b;
    }

    // this method presents the admin with daily vaccine statistics.
    public void dailyStatistics() throws IOException {
        System.out.println();
        System.out.println("Today's numbers!");
        System.out.println();
        System.out.println("Number of appointments scheduled for today:");

        // currentCounter determines how many appointments are still available and subtracts that from 192
        // to determine how many have been taken. It adds this value to totalCounter.
        // currentCounter does this for every location and then a final total is returned.
        BufferedReader locations = new BufferedReader(new FileReader("Locations.txt"));
        BufferedReader appointmentsAvailable;
        String location = locations.readLine();
        String appointment;
        int totalCounter = 0;
        int currentCounter;
        while (location != null) {
            location = location.replaceAll(" ", "");
            appointmentsAvailable = new BufferedReader(new FileReader(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(LocalDate.now()) + "/" + location + "/availabilities.txt"));
            appointment = appointmentsAvailable.readLine();
            currentCounter = 0;
            while (appointment != null) {
                currentCounter++;
                appointment = appointmentsAvailable.readLine();
            }
            currentCounter = 192 - currentCounter;
            totalCounter += currentCounter;
            location = locations.readLine();
        }
        System.out.println(totalCounter);
        System.out.println();

        // gets the dose count from the Doses.txt file
        System.out.println("Number of doses given so far today:");
        BufferedReader doseCount = new BufferedReader(new FileReader("Doses.txt"));
        String number = doseCount.readLine();
        System.out.println(number);
    }

    // this method allows an admin to change the eligibility requirements currently listed
    public void changeEligibility() throws IOException {

        // Loads current items into an ArrayList
        Scanner sc = new Scanner(System.in);
        BufferedReader eligibilityItems = new BufferedReader(new FileReader("Eligibility.txt"));
        String item = eligibilityItems.readLine();
        ArrayList<String> everyItem = new ArrayList<String>();
        while (item != null) {
            everyItem.add(item);
            item = eligibilityItems.readLine();
        }

        // sees if user wants to add or remove eligibility group
        System.out.println("Would you like to add or remove an eligibility group? (Type \"add\" or \"remove\")");
        boolean validInput = false;
        while (validInput == false) {
            String addOrRemove = sc.nextLine();

            // adds item
            if (addOrRemove.equals("add") || addOrRemove.equals("\"add\"")) {
                validInput = true;
                System.out.println("Please enter the new eligibility.");
                String newEligibility = sc.nextLine();
                PrintWriter pw = new PrintWriter(new FileWriter("Eligibility.txt", true));
                pw.println(newEligibility);
                System.out.println("It has been added!");
                pw.close();

                // if user wants to remove, a numbered list appears with current eligibility groups
            } else if (addOrRemove.equals("remove") || addOrRemove.equals("\"remove\"")) {
                validInput = true;
                int counter = 0;
                for (String s : everyItem) {
                    counter++;
                    System.out.println(counter + ". " + s);
                }
                // takes number from user and reprints every item except the one they inputted
                System.out.println("Which number would you like to remove?");
                validInput = false;
                boolean validInput2 = false;
                String numberToRemove = "";
                int intNum;
                while (validInput == false) {
                    numberToRemove = sc.nextLine();
                        try {
                            intNum = Integer.parseInt(numberToRemove);
                            everyItem.remove(intNum - 1);
                            validInput = true;
                        } catch (Exception e) {
                            System.out.println("Uh oh! Please enter a valid number.");
                        }
                }
                PrintWriter pw = new PrintWriter(new FileWriter("Eligibility.txt"));
                for (String i : everyItem) {
                    pw.println(i);
                }
                pw.close();
                System.out.println("Done!");
            } else {
                System.out.println("Please enter a valid input.");
                System.out.println("Type \"add\" or \"remove\"");
            }
        }
    }
}