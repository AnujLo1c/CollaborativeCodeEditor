import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-project',
  standalone: true,
  templateUrl: './project.html',
  styleUrls: ['./project.scss']
})
export class Project implements OnInit {
  constructor() {}

  ngOnInit() {
    this.name();   
  }
  name() {
    console.log(localStorage.getItem("auth_token"));
  }
}
