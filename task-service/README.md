# task-service
Pet project task manager system

The goal is to recreate a state machine around the concept of Tasks, in a way that the machine accepts only specific inputs/actions at a given time (at a given state), making its behaviour more predictable and easier to code.

The states (Active, Suspended, Completed -for now-) are able to be modified by several endpoints, and have certain restrictions:

An active task can either be suspended or completed.
A suspended task can only be activated.
A completed task cannot be modified.

Active state implies that the task's properties can be modified in any way (its title, content, due date and attachments are subject to modification)

Suspended state entails that the task is frozen, not a subject to modification, and out of the pending task to be completed.

Completed state also involves the task to be taken out of the list of pending tasks, unable to be modified in terms of properties and state.
