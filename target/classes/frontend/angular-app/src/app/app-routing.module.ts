import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  {
    path:"",
    component: HomeComponent
  },
  {
    path:"home",
    component: HomeComponent
  }
];

@NgModule({
  declarations:[
    HomeComponent
  ],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule, HomeComponent]
})
export class AppRoutingModule { }
