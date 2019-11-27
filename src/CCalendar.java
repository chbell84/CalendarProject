import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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
	private HashMap<String, List<CCEvent>>[] cycles;
	private String[] dayLabels;
	private ArrayList<DayOfWeek> weekdays;
	private ArrayList<LocalDate> excludeDays;
	private net.fortuna.ical4j.model.Calendar cal;
	private java.util.Calendar tempCal;
	private UidGenerator ug;
	private LocalDate date;

	CCalendar(Calendar startDay, int numCycles, String[] labels){
		this(startDay,numCycles,labels,new ArrayList<>(Arrays.asList(DayOfWeek.values())),new ArrayList<>());
	}
	CCalendar(Calendar startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays){
		this(startDay,numCycles,labels,cycleDays,new ArrayList<>());
	}
	CCalendar(Calendar startDay, int numCycles, String[] labels, ArrayList<DayOfWeek> cycleDays, ArrayList<LocalDate> excludeList){
		try {
			ug = new UidGenerator("1");
		} catch (SocketException e) {
			e.printStackTrace();
		}
		totalCycles = numCycles;
		cycles = new HashMap[numCycles];
		date = LocalDate.of(startDay.get(Calendar.YEAR),startDay.get(Calendar.MONTH),startDay.get(Calendar.DAY_OF_MONTH));
		dayLabels = labels;
		weekdays = cycleDays;
		excludeDays=excludeList;
		tempCal = startDay;
		for(int i=0;i<numCycles;i++){
			cycles[i] = new HashMap<String, List<CCEvent>>();
			for(String s:labels){
				cycles[i].put(s, new ArrayList<CCEvent>());
			}
		}
		//build(date);
	}
	private void build() {
		cal = new net.fortuna.ical4j.model.Calendar();
		cal.getProperties().add(new ProdId("-//Charles Bell//trying something//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		//tempCal = new GregorianCalendar();
		LocalDate futureDate = date;
		for(int i=0;i<totalCycles;i++) {
			for(String l : dayLabels) {
				//avoid weekends and holidays
				while(!weekdays.contains(futureDate.getDayOfWeek())||excludeDays.contains(futureDate)) futureDate=futureDate.plusDays(1);
				tempCal.clear();
				tempCal.set(futureDate.getYear(),futureDate.getMonthValue()-1,futureDate.getDayOfMonth());
				Date d = new Date(tempCal.getTime());
				VEvent day = new VEvent(d,l);

				// Generate a UID for the event..
				day.getProperties().add(ug.generateUid());
				cal.getComponents().add(day);
				for(CCEvent e:cycles[i].get(l)) cal.getComponents().add(e.toVEvent((Calendar) tempCal.clone()));

				//increment the day
				futureDate=futureDate.plusDays(1);
			}
		}
	}
	public String toString(){
		build();
		return cal.toString();
	}
	public int length() {
		return totalCycles;
	}
	public void addEvent(VEvent event){
		cal.getComponents().add(event);
	}
	public void addEvent(String CDay, int cycle, CCEvent e){
		cycles[cycle].get(CDay).add(e);
	}
	public void addEvent(String CDay, int cycle, String name, String summary, int hr, int min, int sec, int endHr, int endMin, int endSec){
		CCEvent e = new CCEvent(ug.generateUid(), hr, min, sec, endHr, endMin, endSec, name, summary);
		cycles[cycle].get(CDay).add(e);
	}

	public void export() {
		build();
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