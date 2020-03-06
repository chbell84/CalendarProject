import java.util.Scanner;

public class CalendarCLI {
    public static void main(String[] args){
        final String BOILERPLATE = "**********************************************\n" +
                "* Welcome to the Custom CalendarCLI\n" +
                "* Use this to create calendars and schedule\n" +
                "* events following an n-length cycle of days.\n" +
                "* Let's start by building out your calendar    ";
        Scanner s = new Scanner(System.in);
        System.out.println(BOILERPLATE);
        System.out.print("Please enter a start date (DD-MM-YYYY):");
        String date = s.next();
        String[] dates = date.split("-");

    }
}
