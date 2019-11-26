import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Uid;

import java.util.Calendar;
import java.util.GregorianCalendar;

//import java.util.Date;

public class CCEvent {
    private long time;
    private long duration;
    private String eventName;
    private String summary;
    private Uid uid;
    private int sHr;
    private int sMin;
    private int sSec;
    private int eHr;
    private int eMin;
    private int eSec;

    CCEvent(Uid uid, int startHr, int startMin, int startSec, int endHr, int endMin, int endSec, String name){
        sHr = startHr;
        sMin = startMin;
        sSec = startSec;
        eHr = endHr;
        eMin = endMin;
        eSec = endSec;
        eventName = name;
        this.uid = uid;
    }
    CCEvent(Uid uid, int startHr, int startMin, int startSec, int endHr, int endMin, int endSec, String name, String summary){
        this(uid, startHr, startMin, startSec, endHr, endMin, endSec, name);
        this.summary = summary;
    }
    VEvent toVEvent(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY,sHr);
        cal.set(Calendar.MINUTE,sMin);
        cal.set(Calendar.SECOND,sSec);
        DateTime start = new DateTime(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY,eHr);
        cal.set(Calendar.MINUTE,eMin);
        cal.set(Calendar.SECOND,eSec);
        DateTime end = new DateTime(cal.getTime());
        //long end = start+duration;
        VEvent out = new VEvent(start,end,eventName);
        out.getProperties().add(uid);

        return out;
    }
    String getSummary(){
        return summary;
    }
    String getEventName(){
        return eventName;
    }
    Uid getUid(){
        return uid;
    }
}
