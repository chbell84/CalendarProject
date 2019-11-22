import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Uid;

public class CCEvent {
    private long time;
    private long duration;
    private String eventName;
    private String summary;
    CCEvent(long start, long duration, String name){
        //super(start, end, name);
        time = start;
        this.duration = duration;
        eventName = name;
    }
    CCEvent(long start, long duration, String name, String summary){
        this(start, duration, name);
        this.summary = summary;
    }
    VEvent toVEvent(DateTime date, Uid uid){
        long start = date.getTime()-date.getTime()%86400000+time;
        long end = start+duration;
        VEvent out = new VEvent(new DateTime(new java.util.Date(start)),new DateTime(new java.util.Date(start+duration)),eventName);
        out.getProperties().add(uid);
        return out;
    }
}
