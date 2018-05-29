import { InMemoryDbService } from 'angular-in-memory-web-api';

export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const tasks = [
        { id: 1, title: 'Saluto', state: "ACTIVE", creationDate: "now", dueDate:"next week", assignedUser: "Marcello", priority:  "NORMAL", parent: "", description: "Buongiorno a tutti!!!" },
        { id: 2, title: 'Greeting', state: "ACTIVE", creationDate: "now", dueDate:"next week", assignedUser: "Ryan", priority:  "NORMAL", parent: "", description: "Good morning everyone!!!" },
        { id: 3, title: 'Cumprimento', state: "ACTIVE", creationDate: "now", dueDate:"next week", assignedUser: "Elias", priority:  "NORMAL", parent: "", description: "Bom dia a todos!!!" },
    ];
    return {tasks};
  }
}


    