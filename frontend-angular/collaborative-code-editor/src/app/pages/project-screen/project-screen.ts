import { CommonModule } from '@angular/common';
import { Component,  } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-project-screen',
  imports: [ FormsModule,CommonModule],
  templateUrl: './project-screen.html',
  styleUrl: './project-screen.scss'
})
export class ProjectScreen {
code: string = `console.log('Hello Angular Editor!');`;

  editorOptions: {
    lineNumbers: boolean;
    roundedSelection: boolean;
    scrollBeyondLastLine: boolean;
    readOnly: boolean;
    minimap: { enabled: boolean };
  } = {
    lineNumbers: true,
    roundedSelection: true,
    scrollBeyondLastLine: false,
    readOnly: false,
    minimap: { enabled: true }
  };

  onCodeChange(newCode: string) {
    this.code = newCode;
    console.log('Updated Code:', this.code);
  }
}
