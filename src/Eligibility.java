import java.util.ArrayList;

public class Eligibility {
    ArrayList<String> specificConditions = new ArrayList<String>();

    // takes an ArrayList of the current eligibilities available, as well as the numbers selected by the user
    // uses the number - 1 to get the index of the ArrayList and adds it to another ArrayList
    // which holds the user's specific reason(s) for eligibility.
    Eligibility(ArrayList<String> conditions, String numbers) {
        String[] numsSeperate = numbers.split(",");
        for (String s : numsSeperate) {
            String noSpaces = s.trim();
            int num = Integer.parseInt(noSpaces);
            specificConditions.add(conditions.get(num-1));
        }
    }

        public ArrayList<String> getConditions(){
            return specificConditions;
        }
    }
