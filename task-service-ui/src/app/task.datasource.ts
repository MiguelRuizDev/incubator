import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import { Task, ResponseTasks } from "./domain";
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import { Observable} from 'rxjs';
import {TaskService} from './task.service';

import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs/internal/observable/of";

export class TaskDataSource implements DataSource<Task> {

    private taskSubject = new BehaviorSubject<Task[]>([]);

    public numberOfItems: number; 

    constructor(private taskService: TaskService) {}

    connect(collectionViewer: CollectionViewer): Observable<Task[]> {
        return this.taskSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.taskSubject.complete();
    }

    loadTasks(  state='any',
                page = 0, 
                size = 5) {

        this.taskService.getAllTaskCustomDataSource(   
                                        state,
                                        page, 
                                        size)
                                        .pipe(
            catchError(() => of([])),
        )
        .subscribe(response => {
            if(response['_embedded'] != undefined){
                this.taskSubject.next(response['_embedded'].tasks);
            }else{
                this.taskSubject.next([]);
            }
            this.numberOfItems = response['page'].totalElements;
        } );
    }    
}