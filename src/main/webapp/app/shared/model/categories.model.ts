import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';

export interface ICategories {
  id?: number;
  categoryName?: string;
  categoryDescription?: string | null;
  categoryUrl?: string | null;
  createdBy?: string | null;
  createdTime?: string | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<ICategories> = {};
