import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AID } from '../model/AID';

const baseUrl = 'http://localhost:8080/Chat-war/api/agents/';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  runningAgents: AID[] = []

  constructor(private http: HttpClient) { }
  
  getRunning() {
    return this.http.get(baseUrl + 'running').subscribe();
  }
}
