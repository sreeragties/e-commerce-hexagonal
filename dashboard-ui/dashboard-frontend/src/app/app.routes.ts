import { Routes } from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {OrderTrackerComponent} from './pages/order-tracker/order-tracker.component';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'tracker', component: OrderTrackerComponent },
  { path: '**', redirectTo: '/home' },
];
