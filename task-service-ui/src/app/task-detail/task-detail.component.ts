import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {Task} from '../domain';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { TaskService }  from '../task.service';
import { toString } from 'typescript-collections/dist/lib/arrays';
import { forEach } from '@angular/router/src/utils/collection';

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent implements OnInit {

  @Input() 
  task: Task;



  constructor(
    private taskService: TaskService,
    private location: Location,
    private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.getTask();
  }

  getTask(): void{
    const id = this.route.snapshot.paramMap.get('id');
    this.taskService.getTask(id)
      .subscribe(
        data => this.task = data
      );
  }

  save(): void {
    this.taskService.updateTask(this.task).subscribe(
      () => this.getTask(),
    );
  }

  goBack(): void {
    this.location.back();
  }

  addItemData(item: string ): void {
    this.task.data.push(item);
    this.save();
  }

  deleteItemData(itemToDelete: string){
    this.task.data = this.task.data.filter(item => item !== itemToDelete);
    this.save();
  }




  filterLinks: Array<string> = ['self', 'Update', 'Delete']

  getKeys(filters :Array<string>): string[]{

    let keys = Object.keys(this.task._links);
    for( let filter of filters){
      keys = keys.filter(
        key => key !== filter,
      );
    }
    return keys;
  }

  genericAction(url: string, input: string){
    this.taskService.genericActionService(url, input).subscribe(
      () => this.getTask(),
    );
  }

  checkIfLinkRelAssignIsPresent(key: string): boolean{
    return key == "Assign";
  }

  hasParameter(url : string): boolean {
    return url.includes('?');
  }


  

  





}
