import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router, Routes} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  formLogin! :FormGroup ;
  constructor(private  fb:FormBuilder ,
              private authService : AuthService ,
              private router :Router) {  }

  ngOnInit(): void {
    this.formLogin=this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })
  }


  handleLogin() {
    if (this.formLogin.valid) {
      console.log(this.formLogin.value);
      let username = this.formLogin.value.username;
      let pwd = this.formLogin.value.password;
      this.authService.login(username,pwd).subscribe({
        next : data => {
          //console.log(data);
          this.authService. loadProfile(data);
          this.router.navigateByUrl("/admin")

        },
        error :err => {
          console.log(err.message);

        }
      })

    } else {
      console.log('Formulario inválido');
    }

  }
}
