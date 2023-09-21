import java.io.BufferedReader;
import java.io.FileReader;

public class Person {
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    Person() {
        firstName = "Jane";
        lastName = "Doe";
        dateOfBirth = "2000/01/01";
    }

    Person(String first, String last, String date) throws Exception {
        setFirstName(first);
        setLastName(last);
        setDateOfBirth(date);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws Exception {
        if (firstName.matches(".*\\d.*")) {
            throw new Exception();
        } else {
           String trimmed = firstName.trim();
            this.firstName = trimmed;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws Exception {
        if (lastName.matches(".*\\d.*")) {
            throw new Exception();
        } else {
            String trimmed = lastName.trim();
            this.lastName = trimmed;
        }
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) throws Exception {
        char [] characters = new char [dateOfBirth.length()];
        for (int i = 0; i < dateOfBirth.length(); i++){
            characters[i] = dateOfBirth.charAt(i);
        }
        if (Character.isDigit(characters[0]) == true &&
                Character.isDigit(characters[1]) == true &&
                Character.isDigit(characters[2]) == true &&
                Character.isDigit(characters[3]) == true &&
                characters[4] == '/' &&
                Character.isDigit(characters[5]) == true &&
                Character.isDigit(characters[6]) == true &&
                characters[7] == '/' &&
                Character.isDigit(characters[8]) == true &&
                Character.isDigit(characters[9]) == true)
        this.dateOfBirth = dateOfBirth;
        else{
            throw new Exception();
        }
    }

    // example of polymorphism
    // can be used to see if someone is in the system at all
    public boolean findPerson(String first, String last) throws Exception {
        boolean verified = false;
        BufferedReader people = new BufferedReader(new FileReader("All People.txt"));
        String name = people.readLine();
        String fullName = first + " " + last;
        while (name != null && verified == false) {
            if (name.equals(fullName)){
                verified = true;
            }
            name = people.readLine();
        }
        setFirstName(first);
        setLastName(last);
        return verified;
    }
}
