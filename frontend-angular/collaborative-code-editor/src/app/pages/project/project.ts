import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-project',
  standalone: true,
  templateUrl: './project.html',
  styleUrls: ['./project.scss']
})
export class Project  {
  
  constructor(private router:Router) {}
onItemClick(item:any){
  console.log("naving")
this.router.navigate(['', 0]);
}

}
