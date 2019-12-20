import java.net.SocketException;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import io.javalin.Javalin;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;

public class CustomCalendar{
	public static void main(String[] args) {
		String [] labels = {"a","b","c","d","e"};
		ArrayList<DayOfWeek> cycleDays = new ArrayList<>(Arrays.asList(
				DayOfWeek.MONDAY,
				DayOfWeek.TUESDAY,
				//DayOfWeek.WEDNESDAY,
				DayOfWeek.THURSDAY,
				DayOfWeek.FRIDAY));
		ArrayList<LocalDate> excludeDays = new ArrayList<>(Arrays.asList(
				LocalDate.parse("2019-12-25"),
				LocalDate.parse("2019-12-25").plusDays(1),
				LocalDate.parse("2019-12-25").plusDays(2),
				LocalDate.parse("2019-12-25").plusDays(3),
				LocalDate.parse("2019-12-25").plusDays(4),
				LocalDate.parse("2019-12-25").plusDays(5),
				LocalDate.parse("2019-12-25").plusDays(6),
				LocalDate.parse("2019-12-25").plusDays(7)));
		//Javalin app = Javalin.create().start(7000);
		System.out.println("Building Calendar:\t"+java.time.LocalTime.now());
		// Create a TimeZone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("America/New_York");
		VTimeZone tz = timezone.getVTimeZone();
		java.util.Calendar startDate = GregorianCalendar.getInstance(timezone);

		CCalendar c = new CCalendar(startDate, 40, labels, cycleDays, excludeDays);
		System.out.println("Calendar Build done:\t"+java.time.LocalTime.now());
		System.out.println("Adding Event...\t"+java.time.LocalTime.now());
		//c.addEvent(labels[3],3,"Test Event","In this test event we shoot the shit",13,00,00,14,00,00);
		c.addEvent(new String[]{"a","e","c"},"Math 6a","In this test event we shoot the shit",13,00,00,14,00,00);
		c.addEvent(new String[]{"a","e","c"},"Math 6b","In this test event we shoot the shit",11,00,00,12,00,00);
		c.addEvent(new String[]{"a","e","c"},"Algebra 1c","In this test event we shoot the shit",9,00,00,10,00,00);
		c.addEvent(new String[]{"a","e","c"},"Math 7b","In this test event we shoot the shit",14,00,00,15,00,00);
		c.addEvent(new String[]{"b","d"},"Calculus AB","In this test event we shoot the shit",14,00,00,15,00,00);
		System.out.println("Added\t"+java.time.LocalTime.now());
		//app.get("/", ctx -> ctx.result("Hello World"));
		System.out.println(c.getEvents(labels[3],3));
		//c.removeEvent(labels[3],3, (CCEvent) c.getEvents(labels[3],3).values().toArray()[0]);
		System.out.println(c.getEvents(labels[3],3));
		c.setExcludeDays(new ArrayList<>(Arrays.asList(
				LocalDate.parse("2019-12-25"))));
		c.addExcludeDay(LocalDate.parse("2020-01-15"));
		c.export();
	}
	private static VEvent sampleEvent(){
		// Create a TimeZone
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("America/New_York");
		VTimeZone tz = timezone.getVTimeZone();

		// Start Date is on: April 1, 2008, 9:00 am
		java.util.Calendar startDate = GregorianCalendar.getInstance(timezone);
		startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
		startDate.set(java.util.Calendar.MINUTE, 0);
		startDate.set(java.util.Calendar.SECOND, 0);

		// End Date is on: April 1, 2008, 13:00
		java.util.Calendar endDate = GregorianCalendar.getInstance(timezone);
		endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
		endDate.set(java.util.Calendar.MINUTE, 0);
		endDate.set(java.util.Calendar.SECOND, 0);

		// Create the event
		String eventName = "Progress Meeting";
		DateTime start = new DateTime(startDate.getTime());
		DateTime end = new DateTime(endDate.getTime());
		VEvent meeting = new VEvent(start, end, eventName);

		// add timezone info..
		meeting.getProperties().add(tz.getTimeZoneId());

		// generate unique identifier..
		UidGenerator ug = null;
		try {
			ug = new UidGenerator("uidGen");
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);

		// add attendees..
		Attendee dev1 = new Attendee(URI.create("mailto:dev1@mycompany.com"));
		dev1.getParameters().add(Role.REQ_PARTICIPANT);
		dev1.getParameters().add(new Cn("Developer 1"));
		meeting.getProperties().add(dev1);

		Attendee dev2 = new Attendee(URI.create("mailto:dev2@mycompany.com"));
		dev2.getParameters().add(Role.OPT_PARTICIPANT);
		dev2.getParameters().add(new Cn("Developer 2"));
		meeting.getProperties().add(dev2);
		return meeting;
	}
}