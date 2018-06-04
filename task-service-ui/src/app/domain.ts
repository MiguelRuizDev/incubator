import * as Collections from 'typescript-collections';

export class Task {

    id:string;
    title: string;
    state:string;
    creationDate:string; 
    dueDate:string;
    assignedUser:string;
    priority:string;
    parent:string;
    description:string;

    data: Array<string>;

    _links: any;

    constructor(title: string, description: string){
        this.title = title;
        this.description = description;
    }
    
}

export class ResponseTasks {
    
    _embedded: Task[];
    _links: any;
    page: any;

    constructor (embedded: Task[], links: any, page: any){
        this._embedded = embedded;
        this._links = links;
        this.page = page;

    }
}

