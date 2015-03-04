# CS2103 project page of Team w09-4j

## Developers
1. Han Liang Wee, Eric: Team Lead, Integration, Tool X Expert
1. Rodson Chue Le Sheng: System Architect, Scheduling & Tracking
1. Lin Chen-Hsin: Testing, Deliverables & Deadlines
1. Chen Tze Cheng: Documentation & Code Quality

## Project \<Ctrl\> + \<8\>

### Features

#### Basic Features
* Quickly access the software by pressing shortcut keys <ctrl> + <8>.
* Command Line Interface supports keyboard input with three modes:
  * Basic mode – Easy and straight forward commands
  * Twitter mode – Twitter like syntax for short yet understandable commands
  * Advanced mode - For power users who are familiar with UNIX format
* Add, delete or modify tasks using only the keyboard
* Adding a task with
  * name
  * description (optional)
  * priority number (optional)
* Tasks can be specified as
  * Floating Tasks: Tasks with no known time
  * Deadline Tasks: Tasks with a deadline time
  * Timed Tasks: Tasks with a start and end time
* Reminders can be set
* Time can be specified using keywords such as ‘tomorrow’, ‘today’
* User can view all the tasks as a list
* Alternatively, he/she can specify other options such as to sort by deadline for a better view
* User can see his/her action history by simply typing ‘history’ with the keyboard
* User can revert the last action with ‘undo’

#### Advanced Features
The most exciting feature that Ctrl-8 will have is the capability to Sync with Google Calendar, so that
you may share your calender with friends and sync across all of your devices.
* Sync with Google Calendar/Task
  * One way to Google Calendar/Task(upload)
  * Two way sync
  * One way from Google Calendar/Task(download)
* Google QuickAdd
  * For Timed Tasks(events), adding to Google Calendar
  * For floating tasks/deadlines, adding to Google Task

#### Currently in test
 * #51 @ericvader Terminal
 * #57 @rodsonchue Command
 * #71 @imchenchen1994 Parameter
 * #72 @chenhsin ParameterDatePayload
 * #73 @chenhisn PriorityParameter
