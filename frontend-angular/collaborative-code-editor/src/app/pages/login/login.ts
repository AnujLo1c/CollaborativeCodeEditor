import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../service/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private auth: Auth) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    console.log(this.loginForm.value);
    if (this.loginForm.valid) {
      const username = this.loginForm.get('username')?.value;
      const password = this.loginForm.get('password')?.value;

      this.auth.login(username, password).subscribe({
        next: (response) => {
          console.log('Login successful:', response);
          localStorage.setItem('auth_token', response);
          window.location.href = '/project';
        },
        error: (error) => {
          console.error('Login error:', error);
          alert("Login failed: " + (error.error || error.message || "Unknown error"));
        }
      });
    } else {
      alert("Please enter a valid username and password.");
    }
  }
}
