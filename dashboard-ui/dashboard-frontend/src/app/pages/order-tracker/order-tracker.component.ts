import { Component, Input } from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';

export enum OrderState {
  CREATED = 'CREATED',
  OFFER_APPLIED = 'OFFER_APPLIED',
  PAYMENT_APPROVED = 'PAYMENT_APPROVED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  COMPLETED = 'COMPLETED',
}

@Component({
  selector: 'app-order-tracker',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    MatIcon,
    NgClass
  ],
  templateUrl: './order-tracker.component.html',
  styleUrl: './order-tracker.component.scss'
})
export class OrderTrackerComponent {

  @Input() currentOrderState: OrderState | null = OrderState.PAYMENT_APPROVED;

  isLoading: boolean = false;
  customerId: string = '';

  OrderState = OrderState;

  isStepComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex > stepStateIndex;
  }

  isStepActive(stepState: OrderState): boolean {
    return this.currentOrderState === stepState;
  }

  isLineComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex > stepStateIndex;
  }

}
