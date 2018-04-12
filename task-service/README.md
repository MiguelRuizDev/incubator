#Task-service

###Task Manager System

The goal is to recreate a state machine around the concept of Tasks, in a way that the machine accepts only specific inputs/actions at a given time (at a given state), making its behaviour more predictable and easier to code.

The states (Active, Assigned, Suspended and Completed) are able to be modified by several endpoints, and have certain restrictions:

* An __active__ task is waiting to be assigned.
* An __assigned__ task can either be suspended or completed.
* A __suspended__ task can only be activated.
* A __completed__ task cannot be modified.

Active and assigned state implies that the task's properties can be modified in any way (its title, content, due date and attachments are subject to modification)
Suspended state entails that the task is frozen, not a subject to modification, and out of the pending task to be completed.
Completed state also involves the task to be taken out of the list of pending tasks, unable to be modified in terms of properties and state.
