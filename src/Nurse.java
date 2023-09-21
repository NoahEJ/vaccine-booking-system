import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;

public class Nurse extends Person {
    private String hospital;
    private int nurseNumber;
    private String[][] specificNurseSorted = new String[96][9];


    Nurse() {
        super();
        hospital = "North York General";
        nurseNumber = 1;
    }

    Nurse(String first, String last, String date, String hospital, int number) throws Exception {
        super(first, last, date);
        this.hospital = hospital;
        this.nurseNumber = number;
    }

// Example of polymorphism
    // sees if the user's name and birthday belong to a nurse in the system
    public boolean findPerson(String fullName, String DOB) throws Exception {
        String[] firstLastName = fullName.split(" ");
        BufferedReader nurses = new BufferedReader(new FileReader("Nurses.txt"));
        String nurseInfo = nurses.readLine();
        boolean b = false;
        while (nurseInfo != null && b == false) {
            if (firstLastName[0].equals(nurseInfo.toLowerCase())) {
                this.setFirstName(nurseInfo);
                nurseInfo = nurses.readLine();
                if (firstLastName[1].equals(nurseInfo.toLowerCase())) {
                    this.setLastName(nurseInfo);
                    nurseInfo = nurses.readLine();
                    if (DOB.equals(nurseInfo)) {
                        b = true;
                    }
                }
            }
            nurseInfo = nurses.readLine();
        }
        if (b == true) {
            hospital = nurseInfo;
            setDateOfBirth(DOB);
            nurseNumber = Integer.parseInt(nurses.readLine());
        }
        return b;
    }

    // This sorts the nurse's appointments for the day so that they are displayed in order of time
    public boolean createDay() throws IOException {
        boolean peopleToVaccinate;
        LocalDate now = LocalDate.now();
        String todayFormatted = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(now);
        BufferedReader apptInfo = new BufferedReader(new FileReader(todayFormatted + "/" + hospital.replaceAll(" ", "") + "/appointments.txt"));
        String[][] allInfo = new String[192][9];
        String[][] sortedInfo = new String[192][9];


        // loads all appointment details into an array
        // index [][0] for each appointment is the number generated based on time
        String line = apptInfo.readLine();
        for (int i = 0; i < 192; i++) {
            for (int j = 0; j < 9; j++) {
                if (line != null) {
                    allInfo[i][j] = line;
                }
                line = apptInfo.readLine();
            }
            line = apptInfo.readLine();
        }
        if (allInfo[0][0] != null) {
            peopleToVaccinate = true;
            int smallestNumber = Integer.parseInt(allInfo[0][0]);
            int lastSmallestNumber = 0;
            int newNumber;
            int position = 0;

            //these loops sort the items and put them into a new MDA
            outerloop:
            {
                for (int l = 0; l < 192; l++) {
                    kLoop:
                    {
                        for (int k = 0; k < 192; k++) {
                            if (allInfo[k][0] == null) {
                                break kLoop;
                            } else {
                                newNumber = Integer.parseInt(allInfo[k][0]);
                                if (newNumber < smallestNumber && newNumber >= lastSmallestNumber) {
                                    smallestNumber = newNumber;
                                    position = k;
                                }
                            }
                        }
                    }
                    if (smallestNumber == 10000) {
                        break outerloop;
                    }
                    for (int m = 0; m < 9; m++) {
                        sortedInfo[l][m] = allInfo[position][m];
                    }
                    lastSmallestNumber = smallestNumber;
                    smallestNumber = 10000;
                    allInfo[position][0] = "0";
                }
            }

            int counter = 0;
            // sees if the appointments belong to this nurse based on her number
            // or the other nurse who works at the same location
            String nurseNumberString = nurseNumber + "";
            pLoop:
            {
                for (int p = 0; p < 192; p++) {
                    if (sortedInfo[p][0] == null) {
                        break pLoop;
                    } else if (sortedInfo[p][8].equals(nurseNumberString)) {
                        for (int q = 0; q < 9; q++) {
                            specificNurseSorted[counter][q] = sortedInfo[p][q];
                        }
                        counter++;
                    }
                }
            }
        }
        else {
            peopleToVaccinate = false;
        }
        return peopleToVaccinate;
    }

    // generates the nurse's schedule and opens it
    public void getSchedule() throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter("schedule.html"));
        pw.println("<h1>" + getFirstName() + " " + getLastName() + "'s Schedule</h1>");
        pw.println("<h3>" + DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(LocalDate.now()) + "</h3>");
        pw.println("<TABLE BORDER><TR><TH>Time<TH>Patient<TH>Date of Birth<TH>Address</TR>");
        tableCreation:
        {
            for (int x = 0; x < 96; x++) {
                if (specificNurseSorted[x][0] == null) {
                    break tableCreation;
                }
                pw.println("<TR ALIGN=CENTER><TD>" + specificNurseSorted[x][1] + "<TD>" + specificNurseSorted[x][2] + " " + specificNurseSorted[x][3] + "<TD>" + specificNurseSorted[x][4] + "<TD>" + specificNurseSorted[x][5]);
            }
        }
        pw.println("</TABLE>");
        pw.close();

        File file = new File("schedule.html");
        if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
        {
            System.out.println("not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file.exists())         //checks file exists or not
            desktop.open(file);
    }

    // goes through the motions of verifying a person's identity and "giving them" a vaccine
    public void startImmunizing() throws IOException {
        System.out.println("Let's start vaccinating!");
        Scanner sc = new Scanner(System.in);
        int numberOfPeopleVaccinated = 0;
        boolean ready = true;
        for (int y = 0; y < 96; y++) {
            if (specificNurseSorted[y][0] != null && ready == true) {
                ready = false;
                System.out.println("Here is patient #" + (y + 1) + " of the day!");
                System.out.println();
                System.out.println("Name: " + specificNurseSorted[y][2] + " " + specificNurseSorted[y][3]);
                System.out.println("Time: " + specificNurseSorted[y][1]);
                System.out.println("Please ensure that the patient meets the following eligibility requirements:");

                String[] eligibilities = specificNurseSorted[y][7].split(",");
                for (int c = 0; c < eligibilities.length; c++) {
                    eligibilities[c] = eligibilities[c].trim().replaceAll("\\[", "").replaceAll("\\]", "");
                    System.out.println("- " + eligibilities[c]);
                }
                System.out.println();
                System.out.println("Please type \"confirm\" if they do.");
                boolean validInput = false;
                boolean validInput2 = false;
                boolean validInput3 = false;
                while (validInput == false) {
                    String confirmation = sc.nextLine().toLowerCase();
                    if (confirmation.equals("confirm") || confirmation.equals("\"confirm\"")) {
                        validInput = true;
                        System.out.println("Please verify that this is the identity of the patient:");
                        System.out.println("Date of birth: " + specificNurseSorted[y][4]);
                        System.out.println("Address: " + specificNurseSorted[y][5]);
                        System.out.println("City: " + specificNurseSorted[y][6]);
                        System.out.println();
                        System.out.println("Please type \"confirm\" if this is the patient.");
                        while (validInput2 == false) {
                            String secondConfirmation = sc.nextLine().toLowerCase();
                            if (secondConfirmation.equals("confirm") || secondConfirmation.equals("\"confirm\"")) {
                                validInput2 = true;
                                System.out.println("Great! Please administer the dose.");
                                System.out.println("Please type \"done\" once the dose has been given and you are ready for the next patient.");
                                while (validInput3 == false) {
                                    String thirdConfirmation = sc.nextLine().toLowerCase();
                                    if (thirdConfirmation.equals("done") || thirdConfirmation.equals("\"done\"")) {
                                        validInput3 = true;
                                        numberOfPeopleVaccinated++;
                                        ready = true;
                                        addToDoseCount(1);
                                        System.out.println();
                                    }
                                    else {
                                        System.out.println("Whoops! Please try again.");
                                    }
                                }
                            }
                            else {
                                System.out.println("Whoops! Please try again");
                            }
                        }
                    }
                    else {
                        System.out.println("Whoops! Please try again.");
                    }
                }
            }
        }

        System.out.println("That's it!");
        System.out.println("You vaccinated " + numberOfPeopleVaccinated + " people today.");
        System.out.println("We can't wait to see you tomorrow!");
    }

    // Adds a number of doses to the total dose count of the day
public void addToDoseCount(int numberOfPeopleVaccinated) throws IOException {
    BufferedReader pastDoses = new BufferedReader(new FileReader("Doses.txt"));
    String line = pastDoses.readLine();
    int lineAsInt = Integer.parseInt(line);
    int newDoseCount = lineAsInt + numberOfPeopleVaccinated;
    PrintWriter doseCount = new PrintWriter(new FileWriter("Doses.txt"));
    doseCount.println(newDoseCount);
    doseCount.close();
}
        }