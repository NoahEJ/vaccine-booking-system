import java.util.ArrayList;

public class Patient extends Person {

    private String address;
    private String city;
    private Eligibility eligibility;

    Patient() {
        super();
        address = "200 Wilmington Avenue";
        city = "Toronto";
    }

    Patient (String firstName, String lastName, String dateOfBirth, String address, String city, ArrayList<String> allEligibilities, String numbers) throws Exception {
        super(firstName, lastName, dateOfBirth);
        setAddress(address);
        setCity(city);
        eligibility = new Eligibility(allEligibilities, numbers);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        String trimmed = address.trim();
            this.address = trimmed;
        }

    public String getCity() {
        return city;
    }

    public void setCity(String city) throws Exception{
        if (city.matches(".*\\d.*")) {
            throw new Exception();
        } else {
            String trimmed = city.trim();
            this.city = trimmed;
        }
    }
    public ArrayList<String> getEligibility(){
        return eligibility.getConditions();
    }

    public void setEligibility(ArrayList<String> allEligibilities, String numbers) {
        eligibility = new Eligibility(allEligibilities, numbers);
    }
}
