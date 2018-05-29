import { Component, OnInit, ViewChild } from '@angular/core';
import {Task} from '../domain';
import {TaskService} from '../task.service';

import {DataSource} from '@angular/cdk/table';

import {MatPaginator, MatTableDataSource, MatTable, MatSort} from '@angular/material';


@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {

  @ViewChild(MatPaginator) 
  paginator: MatPaginator;

  @ViewChild(MatTable) 
  table: MatTable<Task>;

  @ViewChild(MatSort) sort: MatSort;

  tasks: Task[]; //only used to load the dataSource field

  displayedColumns = [ 'title','state', 
                      'creationDate', 'dueDate', 'assignedUser', 
                      'priority', 'parent', 'description', 'actions'];
                      
  dataSource: MatTableDataSource <Task>;


  constructor(private taskService : TaskService) {}

  ngOnInit() {
    this.getAllTasks("any");
  }

  getAllTasks(state:string):void {
    this.taskService.getAllTasks(state).subscribe((data) => {
      this.tasks = data;
      this.dataSource = new MatTableDataSource<Task>(this.tasks);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  createTask(title:string, description: string):void{
    if (title!="" && description!=""){
      let task = new Task(title, description);
      this.taskService.createTask(task)
        .subscribe(task => {
          //we refresh the displayed data right away
          const data = this.dataSource.data;
          data.push(task);
          this.dataSource.data = data;
        }
      );
    }

  }

  deleteTask(task :Task):void{
    this.taskService.deleteTask(task).subscribe();
    //we refresh the displayed data right away
    this.dataSource.data = this.dataSource.data.filter( t => t !== task);
  }



}
