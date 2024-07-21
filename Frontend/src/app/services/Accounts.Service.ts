import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";
import {Account} from "../model/Account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private  http :HttpClient) { }
  backendHost ="http://localhost:8082";

  public getAccount(accountId : string, page : number, size : number):Observable<Account>{
    return this.http.get<Account>(this.backendHost+"/accounts/"+accountId+"/pageOperations?page="+page+"&size="+size);
  }
  public getAccountCustomer(customer: Customer):Observable<Account>
  {
    return this.http.get<Account>(this.backendHost+"/getBankAccountByCustomer/"+customer);
  }
  public debit(accountId : string, amount : number, description:string){
    let data={accountId : accountId, amount : amount, description : description}
    return this.http.post(this.backendHost+"/accounts/debit",data);
  }
  public credit(accountId : string, amount : number, description:string){
    let data={accountId : accountId, amount : amount, description : description}
    return this.http.post(this.backendHost+"/accounts/credit",data);
  }
  public transfer(accountSource: string,accountDestination: string, amount : number, description:string){
    let data={accountSource, accountDestination, amount, description }
    return this.http.post(this.backendHost+"/accounts/transfer",data);
  }
}
