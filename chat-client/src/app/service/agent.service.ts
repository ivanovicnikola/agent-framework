import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AgentType } from '../model/agent-type';
import { AID } from '../model/AID';

const baseUrl = 'http://localhost:8080/Chat-war/api/agents/';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  runningAgents: AID[] = []
  agentClasses: AgentType[] = []
  performatives: string[] = []

  constructor(private http: HttpClient) { }
  
  getRunning() {
    return this.http.get(baseUrl + 'running').subscribe();
  }

  getClasses() {
    return this.http.get(baseUrl + 'classes').subscribe();
  }

  getPerformatives() {
    return this.http.get(baseUrl + 'performatives').subscribe();
  }
}
