# CalendarProject
<h2>This is my calendar project for RecurseCenter...This is very much a work in progress.</h3>
<h3>What is this and why does it matter</h3>
<p>The idea is to programmatically represent events that recur outside of the rhythm of a five or seven day week. The inciting example is a school that schedules classes according to a custom day-cycle but whose classes still occur Monday-Friday. I have an example illustrated in the table below.</p>
<table>
    <tr><th></th><th>Monday</th><th>Tuesday</th><th>Wednesday</th><th>Thursday</th><th>Friday</th></tr>
    <tr>
        <th>Week 1</th>
        <td>A Day</td>
        <td>Holiday: No Class</td>
        <td>B Day</td>
        <td>C Day</td>
        <td>D Day</td>
    </tr>
    <tr>
        <th>Week 2</th>
        <td>E Day</td>
        <td>A Day</td>
        <td>B Day</td>
        <td>C Day</td>
        <td>D Day</td>
    </tr>
    <tr>
        <th>Week 3</th>
        <td>E Day</td>
        <td>A Day</td>
        <td>B Day</td>
        <td>C Day</td>
        <td>D Day</td>
    </tr>
    <tr>
        <th>Week 4</th>
        <td>E Day</td>
        <td>Holiday: No Class</td>
        <td>A Day</td>
        <td>B Day</td>
        <td>C Day</td>
    </tr>
</table>

<p>Such an institution might need decide to schedule classes on a custom length cycle in order to optimize teacher and class availability. That in and of itself is not very complicated. The iCalendar standard allows for events to recur every n-number of days. What makes this task difficult is the tendency for these institutions to pause and resume their schedules around vacations, holidays and school events in which classes are not held.</p>
<p>The main of appeal of these kinds of scheduling systems comes from their ability to pause for or map around breaks in instruction. </p>

<p>In the example below, Section A and Section B represent to sections of the same course under a traditional scheduling system. Typically in such a system, classes that meet on Tuesdays and Thursdays do so for longer than classes that meet every Monday, Wednesday and Friday so that Sections A and B meet for an equal amount of time in any given week.</p>
<p>In both calendars there are two Tuesday holidays within four-week span, keeping Section B from meeting. At the end of the month, Section B has 25% less instructional time as Section A.</p>
<p>The flexible calendar can easily adjust for the holiday by adding a couple of days to the semester so that there can be four complete cycles. The cycles themselves map around the holidays so that there are no course disruptions.</p>
<p>In a traditional calendar would need to add two Tuesday classes one day after another which wouldn't fall into the typical rhythm of the school year.</p>

<table>
    <tr><th></th><th>Monday</th><th>Tuesday</th><th>Wednesday</th><th>Thursday</th><th>Friday</th></tr>
    <tr>
        <th>Week 1</th>
        <td>Section A</td>
        <td>Holiday: No Class</td>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
    </tr>
    <tr>
        <th>Week 2</th>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
    </tr>
    <tr>
        <th>Week 3</th>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
    </tr>
    <tr>
        <th>Week 4</th>
        <td>Section A</td>
        <td>Holiday: No Class</td>
        <td>Section A</td>
        <td>Section B</td>
        <td>Section A</td>
    </tr>
</table>
<h3>How does it work</h3>
<p>The project consists of two main classes</p>
<ul>
<li>CCalendar<p>This class contains the psudocalendar. It takes events, day labels, as well as dates to exclude. I also contains the function that exports all that information into an iCalendar file.</p></li>
<li>CCEvent<p>This class represents events in the psudocalendar. It contains the main properties of an iCal VEvent minus the date. It can be turned into a VEvent if given a date.</p></li>
</ul>
<p>There is a third class, CustomCalendar.java. It's essentially a demo class. It creates an instance of CCalendar, populates it with events and exports a sample iCalendar file.</p>
