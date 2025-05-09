import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderTrackerService {

  constructor(private http: HttpClient) { }

  createOrder(customerId: string, itemId: string): Observable<any> {
    const requestBody = {
      customerId: customerId,
      itemId: itemId,
    };
    return this.http.post('/api/orders/create', requestBody);
  }
}
