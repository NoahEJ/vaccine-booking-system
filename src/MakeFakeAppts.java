import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class MakeFakeAppts {


    public void generateAppts() throws Exception {
        String[] firstNames = {"James", "John", "Robert", "Mary", "Jennifer", "Patricia", "Michael", "William", "David", "Sarah", "Jessica", "Linda", "Matthew", "Emily"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
        String[] streets = {"Yonge St.", "Bloor St.", "Queen St. W", "King St. W", "Spadina Ave.", "Bay St.", "College St.", "Dundas St. W"};


        ArrayList<String> allDates = new ArrayList<String>();
        ArrayList<String> allLocations = new ArrayList<String>();

        BufferedReader inputDates = new BufferedReader(new FileReader("Dates.txt"));
        String dateLine = inputDates.readLine();
        while (dateLine != null){
            allDates.add(dateLine);
            dateLine = inputDates.readLine();
        }
        inputDates.close();

        BufferedReader inputLocations = new BufferedReader(new FileReader("Locations.txt"));
        String locationLine = inputLocations.readLine();
        while (locationLine != null){
            allLocations.add(locationLine);
            locationLine = inputLocations.readLine();
        }
        inputLocations.close();

        Patient p;
        Appointments appointment;
        int numFirst;
        int numLast;
        int numAddress;
        int numStreet;
        int dayOfBirth;
        int monthOfBirth;
        String monthOfBirthString;
        String dayOfBirthString;
        int yearOfBirth;
        String numEligibility;
        String first;
        String last;
        String birthday;
        String address;
        String city = "Toronto";

        BufferedReader eligibilities = new BufferedReader(new FileReader("Eligibility.txt"));
        ArrayList<String> currentEligibility = new ArrayList<String>();
        String line = eligibilities.readLine();
        while (line != null) {
            currentEligibility.add(line);
            line = eligibilities.readLine();
        }
        eligibilities.close();

        int counter = 0;
        String lastTimeChosen = "";

    for (int i = 0; i < allDates.size(); i++){
        for (int j = 0; j < allLocations.size(); j++){
            for (int k = 0; k < 30; k++) {
                counter = 0;
                for (int l = 0; l < 2; l++) {
                    numFirst = (int) Math.floor(Math.random() * (14));
                    numLast = (int) Math.floor(Math.random() * (9));
                    numAddress = (int) Math.floor(Math.random() * (100)+1);
                    numStreet = (int) Math.floor(Math.random() * (8));
                    dayOfBirth = (int) Math.floor(Math.random() * (28)+1);
                    monthOfBirth = (int) Math.floor(Math.random() * (12)+1);
                    yearOfBirth = (int) Math.floor(Math.random() * (2009 - 1920 + 1) + 1920);
                    numEligibility = (int) Math.floor(Math.random() * (currentEligibility.size()) + 1) + "";
                    first = firstNames[numFirst];
                    last = lastNames[numLast];
                    if (monthOfBirth < 10){
                        monthOfBirthString = "0" + monthOfBirth;
                    }
                    else {
                        monthOfBirthString = monthOfBirth + "";
                    }
                    if (dayOfBirth < 10){
                        dayOfBirthString = "0" + dayOfBirth;
                    }
                    else {
                        dayOfBirthString = dayOfBirth + "";
                    }
                    birthday = "" + yearOfBirth + "/" + monthOfBirthString + "/" + dayOfBirthString;
                    address = numAddress + " " + streets[numStreet];
                    p = new Patient(first, last, birthday, address, city, currentEligibility, numEligibility);
                    appointment = new Appointments();
                    String[][] datesAvailable = appointment.locations(allDates.get(i));
                    String[] timesAvailable = appointment.times(allLocations.get(j));
                    String timeChosen = "";
                    if (counter == 0) {
                        ArrayList<String> notNullTimes = new ArrayList<String>();

                        for (int x = 0; x < timesAvailable.length; x++) {
                            if (timesAvailable[x] != null) {
                                notNullTimes.add(timesAvailable[x]);
                            }
                        }
                        int numTime = (int) Math.floor(Math.random() * (notNullTimes.size()));
                        timeChosen = notNullTimes.get(numTime);
                        lastTimeChosen = timeChosen;
                        counter++;
                    }
                    else {
                        timeChosen = lastTimeChosen;
                    }
                    appointment.setAppointment(timeChosen, p);
                }
            }
        }
    }

    }

}
