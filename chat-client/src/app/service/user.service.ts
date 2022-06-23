import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AgentType } from '../model/agent-type';
import { AID } from '../model/AID';
import { Apartment } from '../model/apartment';
import { Host } from '../model/host';
import { Message } from '../model/message';
import { User } from '../model/user';
import { AgentService } from './agent.service';
import { ApartmentService } from './apartment.service';
import { MessageService } from './message.service';

const baseUrl = 'http://localhost:8080/Chat-war/api/users/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  isSignedIn = false;
  loggedUsers: User[] = [];
  registeredUsers: User[] = [];
  user: User = new User('', '');

  constructor(private http: HttpClient, private toastr: ToastrService, private router: Router, private messageService: MessageService, private agentService: AgentService, private apartmentService: ApartmentService) { }

  signIn(user: User) {
    return this.http.post(baseUrl + 'login', user).subscribe({
      next: (user) => {
        this.user = user as User;
        this.isSignedIn = true;
        initSocket(this, this.router, this.toastr, this.messageService, this.agentService, this.apartmentService)
        this.router.navigate(['send-message']);
      },
      error: () => (this.toastr.error("Invalid username/password"))
    });
  }
  
  register(user:  User) {
    return this.http.post(baseUrl + 'register', user).subscribe({
      next: () => (this.toastr.success("Successfully registered")),
      error: () => (this.toastr.error("Username taken"))
    });
  }

  signOut() {
    return this.http.delete(baseUrl + 'loggedIn/' + this.user.username).subscribe({
      next: () => {
        this.isSignedIn = false;
        this.user = new User('', '');
      }
    });
  }

  getLoggedUsers() {
    return this.http.get(baseUrl + 'loggedIn').subscribe();
  }

  getRegisteredUsers() {
    return this.http.get(baseUrl + 'registered').subscribe();
  }
}

function initSocket(userService: UserService, router: Router, toastr: ToastrService, messageService: MessageService, agentService: AgentService, apartmentService: ApartmentService) {
  let connection: WebSocket|null = new WebSocket("ws://localhost:8080/Chat-war/ws/" + userService.user.username);
  connection.onopen = function() {
    console.log("Socket is open");
  }

  connection.onclose = function () {
    userService.signOut();
    connection = null;
  }

  connection.onmessage = function (msg) {
    const data = msg.data.split("!");
    if(data[0] === "LOGGEDIN") {
        let users:User[] = [];
        data[1].split("|").forEach((user: string) => {
            if (user) {
                let userData = user.split(",");
                users.push(new User(userData[0], userData[1]));
            }
        });
        userService.loggedUsers = users;
    }
    else if(data[0] === "REGISTERED") {
      let users:User[] = [];
      data[1].split("|").forEach((user: string) => {
          if (user) {
              let userData = user.split(",");
              users.push(new User(userData[0], userData[1]));
          }
      });
      userService.registeredUsers = users;
    }
    else if(data[0] === "MESSAGES") {
      let messages:Message[] = [];
      data[1].split("|").forEach((message: string) => {
          if (message) {
              let messageData = message.split(",");
              messages.push(new Message(null, new User(messageData[0], ""), new Date(parseInt(messageData[1])), messageData[2], messageData[3]));
          }
      });
      messageService.messages = messages;
    }
    else if(data[0] === "MESSAGE") {
      let messageData = data[1].split(",");
      messageService.messages.push(new Message(null, new User(messageData[0], ""), new Date(parseInt(messageData[1])), messageData[2], messageData[3]));
      toastr.info("New message from " + messageData[0]);
    }
    else if(data[0] === "RUNNING") {
      let runningAgents: AID[] = [];
      data[1].split("|").forEach((agent: string) => {
        if (agent) {
            let agentData = agent.split(",");
            runningAgents.push(new AID(agentData[0], new AgentType(agentData[3], new Host(agentData[1], agentData[2]))));
        }
      });
      agentService.runningAgents = runningAgents;
    }
    else if(data[0] === "CLASSES") {
      let agentClasses: AgentType[] = [];
      data[1].split("|").forEach((agentType: string) => {
        if (agentType) {
          let typeData = agentType.split(",");
          agentClasses.push(new AgentType(typeData[0], new Host(typeData[1], typeData[2])));
        }
      });
      agentService.agentClasses = agentClasses;
    }
    else if(data[0] === "PERFORMATIVES") {
      let performatives: string[] = [];
      data[1].split("|").forEach((performative: string) => {
        if (performative) {
          performatives.push(performative);
        }
      });
      agentService.performatives = performatives;
    }
    else if(data[0] === "APARTMENTS") {
      data[1].split("|").forEach((apartment: string) => {
        if (apartment) {
          let apartmentData = apartment.split(";");
          apartmentService.apartments.push(new Apartment(apartmentData[0], apartmentData[1], apartmentData[2], apartmentData[3], apartmentData[4]));
        }
      });
    }
    else {
      toastr.info(data[1]);
    }
  }
}
