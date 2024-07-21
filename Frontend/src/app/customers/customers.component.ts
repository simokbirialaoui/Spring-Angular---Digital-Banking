<<<<<<< HEAD
import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
=======
import {Component, OnInit} from '@angular/core';
>>>>>>> 59b08ce64785e39626b26aa8679ad71733474ab4
import {CustomerService} from "../services/customer.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
<<<<<<< HEAD
export class CustomersComponent implements OnInit {
  customers! : Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup : FormGroup | undefined;
  constructor(private customerService : CustomerService, private fb : FormBuilder, private router : Router) { }

=======
export class CustomersComponent implements OnInit{
  customers ?:Observable<Array<Customer>>;
  erroMassage :String|undefined;
  searchFormGroup !: FormGroup;


  constructor(private  customerService :CustomerService ,private fb :FormBuilder ,private router :Router) {
  }
>>>>>>> 59b08ce64785e39626b26aa8679ad71733474ab4
  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });
<<<<<<< HEAD
    this.handleSearchCustomers();
=======
    this.customers = this.customerService.getCustomers().pipe(
      catchError(err => {
        this.erroMassage = err.message;
        return throwError(() => err);
      })
    );
>>>>>>> 59b08ce64785e39626b26aa8679ad71733474ab4
  }
  handleSearchCustomers() {
    let kw=this.searchFormGroup?.value.keyword;
    this.customers=this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage=err.message;
        return throwError(err);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    let conf = confirm("Are you sure?");
    if(!conf) return;
    this.customerService.deleteCustomer(c.id).subscribe({
<<<<<<< HEAD
      next : (resp) => {
        this.customers=this.customers.pipe(
          map(data=>{
            let index=data.indexOf(c);
            data.slice(index,1)
            return data;
          })
        );
      },
      error : err => {
        console.log(err);
      }
    })
  }
=======
    next : (resp) => {
      this.customers=this.customers?.pipe(
        map(data =>{

          let index=data.indexOf(c);
          data.slice(index,1);
          return data;
        })
      )
      },
     error : err => {
      console.log(err);
      }
    })
    console.log(c.nom);
    return null ;
  }

>>>>>>> 59b08ce64785e39626b26aa8679ad71733474ab4

  handleCustomerAccounts(customer: Customer) {

    this.router.navigateByUrl("/customer-accounts/"+customer.id,{state :customer});
  }
<<<<<<< HEAD
=======




>>>>>>> 59b08ce64785e39626b26aa8679ad71733474ab4
}










