import {Component} from '@angular/core';


// @ts-ignore
@Component({
  selector: 'app-root',
  template: `



    <app-home></app-home>
    Witaj w aplikacji

    <!---routes injected here------>



    <!---footer------>

        <p>Aplikacja firmowa @All Rights Reserved, by Tomasz Moch </p>


  `,

  styles:[]
    })
    export class AppComponent {
    title = 'Home Page';
    }
