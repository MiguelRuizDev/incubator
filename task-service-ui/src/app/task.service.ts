import { Injectable } from '@angular/core';
import {Task} from './domain';
import { Observable} from 'rxjs';

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { forEach } from 'typescript-collections/dist/lib/arrays';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private mainUrl = 'http://localhost:8080/tasks';

  constructor(private http: HttpClient) { }

  getAllTasks(state: string): Observable<Task[]>{
    if(state != "any"){
      return this.http.get<Task[]>(this.mainUrl + `?state=${state}`+ '?page=0&size=5');
    }else{
      return this.http.get<Task[]>(this.mainUrl);
    }
  }

  getTask(id: string): Observable<Task>{
    const url = this.mainUrl + `/${id}`;
    return this.http.get<Task>(url);
  }

  createTask(task : Task):Observable<Task>{
    return this.http.post<Task>(this.mainUrl, task, httpOptions);
  }

  updateTask(task: Task) :Observable<any>{
    return this.http.post(task._links.Update.href.replace("{id}",task.id), task, httpOptions);
  }

  deleteTask(task: Task): Observable<Task>{
    //TODO implement "task._links.delete" once server collection retrieves the same response structure than a single element
    return this.http.delete<Task>(this.mainUrl + `/${task.id}/delete`, httpOptions);
  }

  genericActionService(url: string, input: string):Observable<any> {
    
    if (input){
      //for now, the only method with params is the assignUser method
      //more scalability in the future
      url = url.replace('user={user}','');

      const params = new HttpParams()
      .set('user', input);

      return this.http.post(url, httpOptions, {params});

    }else {
      return this.http.post(url, httpOptions);
    }
    
  }

}
