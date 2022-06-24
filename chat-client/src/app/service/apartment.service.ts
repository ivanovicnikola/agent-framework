import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Apartment } from '../model/apartment';
import { ApartmentSearch } from '../model/apartment-search';

const baseUrl = 'http://localhost:8080/Chat-war/api/apartments/';

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {

  apartments: Apartment[] = [];
  constructor(private http: HttpClient) { }

  searchApartments(search: ApartmentSearch) {
    this.apartments = [];
    return this.http.post(baseUrl, search).subscribe();
  }
}
