import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

/**
 * @author Charles Henry Bell
 *
 */
public class CCalendar {
	private int totalCycles;
	private ArrayList<HashMap<String, List<VEvent>>> cycles;
	private String[] dayLabels;
	private ArrayList<DayOfWeek> weekdays;
	private ArrayList<LocalDate> excludeDays;
	private net.fortuna.ical4j.model.Calendar cal;
	private java.util.Calendar tempCal;
	private UidGenerator ug;

	CCalendar(String startDay, int numCycles, String[] labels){
		this(startDay,numCycles,labels,new ArrayList<>(Arrays.asList(DayOfWeek.values())),new ArrayList<>());
	}
	CCalendar(String startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays){
		this(startDay,numCycles,labels,cycleDays,new ArrayList<>());
	}
	CCalendar(String startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays, ArrayList<LocalDate> excludeList){
		try {
			ug = new UidGenerator("1");
		} catch (SocketException e) {
			e.printStackTrace();
		}
		cal = new net.fortuna.ical4j.model.Calendar();
		tempCal = new GregorianCalendar();
		totalCycles = numCycles;
		cycles = new ArrayList<>();
		LocalDate date = LocalDate.parse(startDay);
		dayLabels = labels;
		weekdays = cycleDays;
		excludeDays=excludeList;
		build(date);
	}
	private void build(LocalDate date) {
		cal.getProperties().add(new ProdId("-//Charles Bell//trying something//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		for(int i=0;i<totalCycles;i++) {
			HashMap<String, List<VEvent>> cycle = new HashMap<>();
			for(String l : dayLabels) {
				cycle.put(l, new ArrayList<>());
				tempCal.clear();
				tempCal.set(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());
				Date d = new Date(tempCal.getTime());
				VEvent day = new VEvent(d,l);

				// Generate a UID for the event..
				day.getProperties().add(ug.generateUid());
				cal.getComponents().add(day);
				int n=1;
				while(!weekdays.contains(date.plusDays(n).getDayOfWeek())||excludeDays.contains(date.plusDays(n))) n++;
				date=date.plusDays(n);
			}
			cycles.add(cycle);
		}
	}
	public String toString(){
		return cal.toString();
	}
	public int length() {
		return totalCycles;
	}
	public void addEvent(VEvent event){
		cal.getComponents().add(event);
	}
	public void export() {
		try {
			FileOutputStream fout = new FileOutputStream("MyCalendar.ics");

			CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(cal, fout);
		}
		catch(FileNotFoundException e) {
			System.out.print("File Not Found");
		} catch (IOException | ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}