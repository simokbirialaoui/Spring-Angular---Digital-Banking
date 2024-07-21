import {Component, OnInit} from '@angular/core';
import {Customer} from "../model/customer.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Account} from "../model/Account.model";
import {catchError, Observable, throwError} from "rxjs";

import {FormGroup} from "@angular/forms";


@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent implements OnInit{
  customerId! : string ;
  customer! : Customer;
  accounts !: Observable<Account>;
  searchFormGroup !: FormGroup;

  protected erroMassage: any;

    constructor( private route : ActivatedRoute, private router :Router ) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];

   /* this.accounts = this.AccountsService.getAccountCustomer(this.customer).pipe(
      catchError(err => {
        this.erroMassage = err.message;
        return throwError(() => err);
      })
    );*/
  }

}
