import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	private ArrayList<DayOfWeek> weekdays = new ArrayList<DayOfWeek>(Arrays.asList(DayOfWeek.values()));
	private ArrayList<LocalDate> excludeDays = new ArrayList<LocalDate>();
	private net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
	private java.util.Calendar tempCal = new GregorianCalendar();

	CCalendar(String startDay, int numCycles, String[] labels){
		totalCycles = numCycles;
		cycles = new ArrayList<HashMap<String,List<VEvent>>>();
		LocalDate date = LocalDate.parse(startDay);
		dayLabels = labels;
		build(date);
	}
	CCalendar(String startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays){
		totalCycles = numCycles;
		cycles = new ArrayList<HashMap<String,List<VEvent>>>();
		LocalDate date = LocalDate.parse(startDay);
		dayLabels = labels;
		weekdays = cycleDays;
		build(date);
	}
	CCalendar(String startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays, ArrayList<LocalDate> excludeList){
		totalCycles = numCycles;
		cycles = new ArrayList<HashMap<String,List<VEvent>>>();
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
			HashMap<String, List<VEvent>> cycle = new HashMap<String, List<VEvent>>();
			for(String l : dayLabels) {
				cycle.put(l, new ArrayList<VEvent>());
				tempCal.clear();
				tempCal.set(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());
				Date d = new Date(tempCal.getTime());
				VEvent day = new VEvent(d,l);

				// Generate a UID for the event..
				try {
					UidGenerator ug = new UidGenerator("1");
					day.getProperties().add(ug.generateUid());
					cal.getComponents().add(day);
				}
				catch(java.net.SocketException e) {
					return;
				}

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}