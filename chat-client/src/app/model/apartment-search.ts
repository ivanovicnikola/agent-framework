export class ApartmentSearch {
    constructor (
        public location: string,
        public minPrice: number|null,
        public maxPrice: number|null,
        public minArea: number|null,
        public maxArea: number|null
    ) {}
}