import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  isLoading: boolean = false;
  customerId: string = '';

  handleSubmit(): void {
    if (!this.customerId) {
      console.log("Customer ID is empty");
      return;
    }

    this.isLoading = true;
    console.log('Submitting Customer ID:', this.customerId);

    setTimeout(() => {
      this.isLoading = false;
      console.log('Processing complete for:', this.customerId);
    }, 3000);
  }

  ngOnInit(): void {
    console.log('Initial isLoading state in ngOnInit:', this.isLoading);
  }

}
