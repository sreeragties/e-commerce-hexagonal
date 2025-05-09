import { Component } from '@angular/core';
import {NgIf} from "@angular/common";
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-order-tracker',
  standalone: true,
    imports: [
        NgIf,
        FormsModule,
    ],
  templateUrl: './order-tracker.component.html',
  styleUrl: './order-tracker.component.scss'
})
export class OrderTrackerComponent {

  isLoading: boolean = false;
  customerId: string = '';

}
