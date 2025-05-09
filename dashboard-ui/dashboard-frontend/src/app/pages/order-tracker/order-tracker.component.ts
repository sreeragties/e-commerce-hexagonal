import {Component, Input} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {OrderTrackerService} from '../../service/order-tracker.service';

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
    NgClass,
  ],
  templateUrl: './order-tracker.component.html',
  styleUrl: './order-tracker.component.scss'
})
export class OrderTrackerComponent {

  constructor(private orderTrackerService: OrderTrackerService) {}

  @Input() currentOrderState: OrderState | null = null;

  itemId: string = '';
  customerId: string = '';

  isLoading: boolean = false;

  OrderState = OrderState;

  isStepComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex >= stepStateIndex;
  }

  isStepActive(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex === stepStateIndex - 1;
  }

  isLineComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex > stepStateIndex;
  }

  submitOrder(): any {
    this.orderTrackerService.createOrder(this.customerId, this.itemId).subscribe(
      {
        next: res => {
          console.log('Order created successfully:', res);
          if (typeof res.orderState === 'string') {
            this.currentOrderState = res.orderState;
          }
        },
        error: (error) => {
          console.error('Error creating order:', error);
        },
        complete: () => {
          console.log('Order creation observable completed.');
        }
      }
    );
  }

}
