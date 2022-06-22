import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Apartment } from '../model/apartment';

const baseUrl = 'http://localhost:8080/Chat-war/api/apartments/';

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {

  apartments: Apartment[] = [];
  constructor(private http: HttpClient) { }

  getApartments() {
    this.apartments = [];
    return this.http.get(baseUrl).subscribe();
  }
}
