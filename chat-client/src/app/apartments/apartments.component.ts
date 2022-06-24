import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { ApartmentSearch } from '../model/apartment-search';
import { ApartmentService } from '../service/apartment.service';

@Component({
  selector: 'app-apartments',
  templateUrl: './apartments.component.html',
  styleUrls: ['./apartments.component.css']
})
export class ApartmentsComponent implements OnInit {

  search: ApartmentSearch = new ApartmentSearch('', null, null, null, null);
  constructor(public apartmentService: ApartmentService) { }

  ngOnInit(): void {
    this.searchApartments();
  }
  
  searchApartments() {
    this.apartmentService.searchApartments(this.search);
  }

  sortData(sort: Sort) {
    const data = this.apartmentService.apartments.slice();
    if (!sort.active || sort.direction === '') {
      this.apartmentService.apartments = data;
      return;
    }

    this.apartmentService.apartments = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'title': return compare(a.title, b.title, isAsc);
        case 'metaInfo': return compare(a.metaInfo, b.metaInfo, isAsc);
        case 'location': return compare(a.location, b.location, isAsc);
        case 'price': return compare(a.price, b.price, isAsc);
        case 'surfaceArea': return compare(a.surfaceArea, b.surfaceArea, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
