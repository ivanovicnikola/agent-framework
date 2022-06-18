import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { AgentService } from '../service/agent.service';

@Component({
  selector: 'app-agent-classes',
  templateUrl: './agent-classes.component.html',
  styleUrls: ['./agent-classes.component.css']
})
export class AgentClassesComponent implements OnInit {

  constructor(public agentService: AgentService) { }

  ngOnInit(): void {
    this.getAgentClasses();
  }

  getAgentClasses() {
    this.agentService.getClasses();
  }
  
  sortData(sort: Sort) {
    const data = this.agentService.agentClasses.slice();
    if (!sort.active || sort.direction === '') {
      this.agentService.agentClasses = data;
      return;
    }

    this.agentService.agentClasses = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name': return compare(a.name, b.name, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}