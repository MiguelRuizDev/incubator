import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import { Task } from "./domain";
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import { Observable} from 'rxjs';
import {TaskService} from './task.service';

import {catchError, finalize} from "rxjs/operators";
import {of} from "rxjs/internal/observable/of";

export class TaskDataSource implements DataSource<Task> {

    private tasksSubject = new BehaviorSubject<Task[]>([]);

    private loadingSubject = new BehaviorSubject<boolean>(false);
    public loading$ = this.loadingSubject.asObservable();
    

    constructor(private taskService: TaskService) {}

    connect(collectionViewer: CollectionViewer): Observable<Task[]> {
        return this.tasksSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.tasksSubject.complete();
        this.loadingSubject.complete();
    }

    loadTasks(  state = 'any',
                filter = '',
                sortDirection = 'asc',
                pageIndex = 0, 
                pageSize = 3) {

        this.loadingSubject.next(true);

        this.taskService.getAllTaskP(   state,
                                        filter, 
                                        sortDirection,
                                        pageIndex, 
                                        pageSize)
                                        .pipe(
            catchError(() => of([])),
            finalize(() => this.loadingSubject.next(false))
        )
        .subscribe(tasks => this.tasksSubject.next(tasks));
    }    
}