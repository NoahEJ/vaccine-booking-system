import java.io.*;
import java.util.*;


public class Appointments {

    private String dateSelected;
    private String locationSelected;
    private String locationSelectedNoWhitespace;
    private String timeSelected;

    // This method returns the dates that are currently available
    public LinkedList<String> datesAvailable() throws IOException {
        BufferedReader inputDates = new BufferedReader(new FileReader("Dates.txt"));
        LinkedList<String> dates = new LinkedList<String>();
        String line = inputDates.readLine();
        int counter = 0;
        while (line != null){
            dates.add(line);
            line = inputDates.readLine();
            counter++;
        }
        dates.add("" + counter);
        return dates;
    }

// this method returns a list of locations where the user can book
    public String [] [] locations (String date) throws IOException {
        dateSelected = date;

       // counts number of locations
        BufferedReader numberLocations = new BufferedReader(new FileReader("Locations.txt"));
        String s = numberLocations.readLine();
        int counter = 0;
        while (s != null){
            counter++;
            s = numberLocations.readLine();
        }
        numberLocations.close();

        // Each location file is read and the number of times available is found
        BufferedReader inputLocations = new BufferedReader(new FileReader("Locations.txt"));
        String line = inputLocations.readLine();
        String [] [] apptDetails = new String [counter] [2];
        String fileName;
        int counter1 = 0;
        int counter2;
        while (line != null){
            counter1++;
            counter2 = 0;
            fileName = date + "/" + line.replaceAll(" ", "") + "/availabilities.txt";
            BufferedReader inputDates = new BufferedReader(new FileReader(fileName));
            String time = inputDates.readLine();
            while (time != null){
                counter2++;
                time = inputDates.readLine();
            }

            // locations and their respective number of available appointments are returned
            apptDetails[counter1-1][0] = line;
            apptDetails[counter1-1][1] = counter2 + "";
            inputDates.close();
            line = inputLocations.readLine();
        }
        return apptDetails;
    }

    // maximum of 96 available unique times (9:00-5:00), therefore size of availableTimes array
public String [] times(String location) throws IOException {
        locationSelected = location;
        locationSelectedNoWhitespace = location.replaceAll(" ", "");
        String locationFile = dateSelected + "/" + locationSelectedNoWhitespace + "/availabilities.txt";
        BufferedReader inputTimes = new BufferedReader(new FileReader(locationFile));
        String [] availableTimes = new String [96];
        String time = inputTimes.readLine();
        String nextTime = inputTimes.readLine();

       // ensures that duplicate times aren't loaded into the array
        boolean a;
        if (time != null) {
            availableTimes[0] = time;
        }
            for (int i = 1; i < availableTimes.length; i++){
                a = true;
                while (a == true && nextTime != null && time != null){
                if (nextTime.equals(time) == false){
                    availableTimes[i] = nextTime;
                    a = false;
                }
                time = nextTime;
                nextTime = inputTimes.readLine();
                }
        }
    return availableTimes;
}

    public String getDateSelected() {
        return dateSelected;
    }

    public String getLocationSelected() {
        return locationSelected;
    }



    public void setAppointment(String time, Patient p) throws IOException {
        timeSelected = time;

        // reads all previous times and removes the one now taken
        BufferedReader allTimes = new BufferedReader(new FileReader(dateSelected + "/" + locationSelectedNoWhitespace + "/availabilities.txt"));
        String line = allTimes.readLine();
        String [] allLines = new String [192];
        int counter = 0;
        while (line != null){
            allLines[counter] = line;
            line = allTimes.readLine();
            counter++;
        }
        int numberTimes = 0;
        PrintWriter removeTime = new PrintWriter(new FileWriter(dateSelected + "/" + locationSelectedNoWhitespace + "/availabilities.txt"));
        for (int i = 0; i < 192; i++){
            if (allLines[i] == null){
            }
            else if (allLines[i].equals(timeSelected) && numberTimes == 0){
                numberTimes = 1;
            }
            else if (allLines[i].equals(timeSelected) && numberTimes == 1){
                removeTime.println(allLines[i]);
                numberTimes = 2;
            }
            else{
                removeTime.println(allLines[i]);
            }
        }
        removeTime.close();


        PrintWriter appointmentWriter = new PrintWriter(new FileWriter(dateSelected + "/" + locationSelectedNoWhitespace + "/appointments.txt", true));

       // creates a number based on the appointment time to later be used for sequentially sorting appointments
        String appointmentNumber;
        String [] hourAndMinute = time.split(":");
        appointmentNumber = hourAndMinute[0];
        int minutes = Integer.parseInt(hourAndMinute[1]);
        minutes = minutes/5;
        if (minutes < 10){
            appointmentNumber += "0" + minutes;
        }
        else{
            appointmentNumber += minutes;
        }
        appointmentWriter.println(appointmentNumber);
        appointmentWriter.println(timeSelected);
        appointmentWriter.println(p.getFirstName());
        appointmentWriter.println(p.getLastName());
        appointmentWriter.println(p.getDateOfBirth());
        appointmentWriter.println(p.getAddress());
        appointmentWriter.println(p.getCity());
        appointmentWriter.println(p.getEligibility());
        if (numberTimes == 2){
            appointmentWriter.println("1");
        }
        else if (numberTimes == 1){
            appointmentWriter.println("2");
        }
        appointmentWriter.println();
        appointmentWriter.close();
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public void setLocationSelected(String locationSelected) {
        this.locationSelected = locationSelected;
    }

    public void setLocationSelectedNoWhitespace(String locationSelectedNoWhitespace) {
        this.locationSelectedNoWhitespace = locationSelectedNoWhitespace;
    }

    public void setTimeSelected(String timeSelected) {
        this.timeSelected = timeSelected;
    }
}
