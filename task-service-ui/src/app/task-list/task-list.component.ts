import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import {Task} from '../domain';
import {TaskService} from '../task.service';
import {MatPaginator, MatTableDataSource, MatTable, MatSort} from '@angular/material';

import {TaskDataSource} from "../task.datasource";
import {map, tap, filter} from "rxjs/operators";


@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) 
  paginator: MatPaginator;

  @ViewChild(MatTable) 
  table: MatTable<Task>;

  @ViewChild(MatSort) 
  sort: MatSort;

  displayedColumns = [ 'title','state', 
                      'creationDate', 'dueDate', 'assignedUser', 
                      'priority', 'parent', 'description', 'actions'];
                      

  dataSource: TaskDataSource;

  items: number = 0;

  constructor(private taskService : TaskService) {}

  ngOnInit() {

    this.dataSource = new TaskDataSource(this.taskService);
    this.dataSource.loadTasks();
    this.loadNumberOfItems();
  }

ngAfterViewInit() {
    this.paginator.page
        .pipe(
            tap(() => this.loadLessonsPage())
        )
        .subscribe();
}

loadLessonsPage(): void {
    this.dataSource.loadTasks(
        'any',
        this.paginator.pageIndex,
        this.paginator.pageSize);
}

loadNumberOfItems(): any{
  this.taskService.getAllTaskCustomDataSource('any', 0, 3)
  .subscribe( res => {
     this.items = res['page'].totalElements;
   });
}

  createTask(title:string, description: string):void{
    if (title!="" && description!=""){
      let task = new Task(title, description);
      this.taskService.createTask(task)
        .subscribe(task => {
          //we refresh the displayed data right away
          this.loadLessonsPage(); 
          this.loadNumberOfItems();
        }
      );
    }
  }

  deleteTask(task :Task):void{
    this.taskService.deleteTask(task).subscribe(()=> {
      this.loadLessonsPage(); 
      this.loadNumberOfItems();
     } 
    );
  }



}
