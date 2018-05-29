import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskListComponent } from './task-list/task-list.component';
import { TaskDetailComponent } from './task-detail/task-detail.component';


const routes: Routes = [

  { path: 'tasks/:id', component: TaskDetailComponent },
  //{ path: 'tasks/:id', component: TaskDetailComponent },
  { path: '', redirectTo: '/tasks', pathMatch: 'full' },
  { path: 'tasks', component: TaskListComponent }
  //{ path: 'tasks', component: TaskListComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}


