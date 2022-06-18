import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AgentClassesComponent } from './agent-classes/agent-classes.component';
import { MessageAllComponent } from './message-all/message-all.component';
import { MessagesComponent } from './messages/messages.component';
import { PerformativesComponent } from './performatives/performatives.component';
import { RegisteredUsersComponent } from './registered-users/registered-users.component';
import { RegistrationComponent } from './registration/registration.component';
import { RunningAgentsComponent } from './running-agents/running-agents.component';
import { SendMessageComponent } from './send-message/send-message.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignedInUsersComponent } from './signed-in-users/signed-in-users.component';

const routes: Routes = [
  { path: 'register', component: RegistrationComponent},
  { path: 'sign-in', component: SignInComponent},
  { path: 'registered-users', component: RegisteredUsersComponent},
  { path: 'signed-in-users', component: SignedInUsersComponent},
  { path: 'send-message', component: SendMessageComponent},
  { path: 'message-all', component: MessageAllComponent},
  { path: 'messages', component: MessagesComponent},
  { path: 'running-agents', component: RunningAgentsComponent},
  { path: 'agent-classes', component: AgentClassesComponent},
  { path: 'performatives', component: PerformativesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
