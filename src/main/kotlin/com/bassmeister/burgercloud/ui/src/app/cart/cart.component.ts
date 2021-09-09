import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { ApiService } from '../api/api.service';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';
import { CreditCardValidators } from 'angular-cc-library';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  burgers = this.cartService.getItems();
  createNew=false;

   ccSelectionForm:FormGroup;

  constructor(private cartService: CartService, private apiService:ApiService,
              private modalService: NgbModal, private fb:FormBuilder) { }

  ngOnInit(): void {
      this.ccSelectionForm=this.fb.group({
        ccSelection: new FormControl('UseExisting'),
        creditCard: new FormControl(''),
        expirationDate: new FormControl(''),
        cvc: new FormControl('')
      });

      this.ccSelectionForm.get('ccSelection').valueChanges.subscribe( value => {
        if(value === 'UseExisting'){
            this.ccSelectionForm.get('creditCard').clearValidators();
            this.ccSelectionForm.get('expirationDate').clearValidators();
            this.ccSelectionForm.get('cvc').clearValidators();
        }else if (value === 'CreateNew'){
            this.ccSelectionForm.get('creditCard').setValidators(CreditCardValidators.validateCCNumber);
            this.ccSelectionForm.get('expirationDate').setValidators(CreditCardValidators.validateExpDate);
            this.ccSelectionForm.get('cvc').setValidators([Validators.required, Validators.minLength(3), Validators.maxLength(4)]);
        }
        this.ccSelectionForm.get('creditCard').updateValueAndValidity();
        this.ccSelectionForm.get('expirationDate').updateValueAndValidity();
        this.ccSelectionForm.get('cvc').updateValueAndValidity();

      }
      );
  }


  checkOut(){
    var creditCard=this.ccSelectionForm.get('creditCard').value.replace(/ /g,'');
    if(!creditCard){
        //Currently hardcoded with fake cc Number, might be fixed in future version
        creditCard='378618187748325';
    }
    var ccExp=this.ccSelectionForm.get('expirationDate').value;
    if(!ccExp){
        ccExp='07/23';
    }
    var ccCvc=this.ccSelectionForm.get('cvc').value;
    if(!ccCvc){
        ccCvc='321';
    }
    this.apiService.sendOrders(this.burgers, creditCard,ccExp,ccCvc);
    this.cartService.clearCart();
    this.burgers=this.cartService.getItems();
  }

  open(content){
      this.modalService.open(content, {ariaLabelledBy: 'paymentInfo'}).result.then((result) => {
          if(`${result}`==='Submit'){
            this.checkOut();
          }
        },(reason) => {
          //Might be needed. Nothing to do here
        });
  }

}
