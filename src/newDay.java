
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.io.*;
import java.util.ArrayList;


public class newDay {

    private LocalDate now = LocalDate.now();
    private String todayFormatted = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(now);


    // This method determines the days that files exist for but have already passed
    public void oldDirectories() throws IOException {

       // Reads the dates for which files exist
        BufferedReader inputDates = new BufferedReader(new FileReader("Dates.txt"));
        String oldDate = inputDates.readLine();
        if (oldDate != null) {
            File oldFile;
            ArrayList<String> oldDates = new ArrayList<String>();
            ArrayList<String> remainingDates = new ArrayList<String>();

            // Adds old dates to an arraylist
            while (oldDate.equals(todayFormatted) == false && oldDate != null) {
                oldDates.add(oldDate);
                oldDate = inputDates.readLine();
            }

            // Adds dates that have not yet passed to a different arraylist
            while (oldDate != null) {
                remainingDates.add(oldDate);
                oldDate = inputDates.readLine();
            }
            inputDates.close();

            // For all old dates, their files are deleted
            for (String i : oldDates) {
                if (i != null) {
                    oldFile = new File(i);
                    deleteOldDirectory(oldFile);
                }
            }

            // Remaining dates are reprinted to the Dates.txt file
            PrintWriter outputDates = new PrintWriter(new FileWriter("Dates.txt"));
            if (remainingDates.size() > 0) {
                for (String s : remainingDates) {
                    outputDates.println(s);
                }
            }
            outputDates.close();
        }
    }

    // This method deletes any old appointments
    public boolean deleteOldDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteOldDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    // This method ensures appointments are created for up to a week from when it is opened
    public void generateNewDays() throws IOException {

        // Old dates have already been removed from Dates.txt
        // And the program always generates a week's worth of dates
        // So by determining how many dates are in the Dates.txt file, one knows how many / which dates
        // need to be generated now
        LocalDate now = LocalDate.now();
        int counter = 0;
        LocalDate extraDays;
        String newDateFormatted;
        BufferedReader dates = new BufferedReader(new FileReader("Dates.txt"));
        String dateFromFile = dates.readLine();
        PrintWriter output;
        while (dateFromFile != null) {
            counter++;
            dateFromFile = dates.readLine();
        }

        // This loop creates the right amount of directories to have a week's worth of appointments
        // at all times
        for (int i = counter; i < 7; i++){
            extraDays = now.plusDays(i);
            newDateFormatted = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(extraDays);
            output = new PrintWriter(new FileWriter("Dates.txt", true));
            output.println(newDateFormatted);
            output.close();

            File file = new File(newDateFormatted);
            boolean bool = file.mkdir();
            BufferedReader location = new BufferedReader(new FileReader("Locations.txt"));
            String line2 = location.readLine();
            String fileName;
            String noWhiteSpace;
            PrintWriter output2;
            while (line2 != null) {
                noWhiteSpace = line2.replaceAll(" ", "");
                fileName = newDateFormatted + "/" + noWhiteSpace;
                file = new File(fileName);
                bool = file.mkdir();
                output2 = new PrintWriter(new FileWriter(fileName + "/availabilities.txt"));
                String hour;
                String minute;
                for (int j = 9; j < 17; j++) {
                    hour = j + "";
                    for (int k = 00; k < 60; k += 5) {
                        if (k < 10) {
                            minute = "0" + k;
                        } else {
                            minute = k + "";
                        }
                        output2.println(hour + ":" + minute);
                        output2.println(hour + ":" + minute);
                    }
                }
                output2.close();
                output2 = new PrintWriter(new FileWriter(fileName + "/appointments.txt"));
                output2.close();
                line2 = location.readLine();
            }
            location.close();
        }
        }
    // This resets the dose counter in Doses.txt
        public void doseReset() throws IOException {
            BufferedReader location = new BufferedReader(new FileReader("Doses.txt"));
            String line = location.readLine();
            line = location.readLine();
            if (!line.equals(todayFormatted)) {
                PrintWriter doseCount = new PrintWriter(new FileWriter("Doses.txt"));
                doseCount.println("0");
                doseCount.println(todayFormatted);
                doseCount.close();
            }
        }
    }