import dayjs from 'dayjs';
import { ICategories } from 'app/shared/model/categories.model';

export interface IProduct {
  id?: number;
  productName?: string;
  productPrice?: number;
  productPriceSale?: number | null;
  productDescription?: string;
  productShortDescription?: string | null;
  productQuantity?: number | null;
  productCode?: string | null;
  productPointRating?: number | null;
  createdBy?: string | null;
  createdTime?: string | null;
  categories?: ICategories | null;
}

export const defaultValue: Readonly<IProduct> = {};
