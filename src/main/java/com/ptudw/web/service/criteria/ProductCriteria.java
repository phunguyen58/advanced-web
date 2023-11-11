package com.ptudw.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.Product} entity. This class is used
 * in {@link com.ptudw.web.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productName;

    private DoubleFilter productPrice;

    private DoubleFilter productPriceSale;

    private StringFilter productDescription;

    private StringFilter productShortDescription;

    private IntegerFilter productQuantity;

    private StringFilter productCode;

    private DoubleFilter productPointRating;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdTime;

    private LongFilter categoriesId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.productPrice = other.productPrice == null ? null : other.productPrice.copy();
        this.productPriceSale = other.productPriceSale == null ? null : other.productPriceSale.copy();
        this.productDescription = other.productDescription == null ? null : other.productDescription.copy();
        this.productShortDescription = other.productShortDescription == null ? null : other.productShortDescription.copy();
        this.productQuantity = other.productQuantity == null ? null : other.productQuantity.copy();
        this.productCode = other.productCode == null ? null : other.productCode.copy();
        this.productPointRating = other.productPointRating == null ? null : other.productPointRating.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdTime = other.createdTime == null ? null : other.createdTime.copy();
        this.categoriesId = other.categoriesId == null ? null : other.categoriesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProductName() {
        return productName;
    }

    public StringFilter productName() {
        if (productName == null) {
            productName = new StringFilter();
        }
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public DoubleFilter getProductPrice() {
        return productPrice;
    }

    public DoubleFilter productPrice() {
        if (productPrice == null) {
            productPrice = new DoubleFilter();
        }
        return productPrice;
    }

    public void setProductPrice(DoubleFilter productPrice) {
        this.productPrice = productPrice;
    }

    public DoubleFilter getProductPriceSale() {
        return productPriceSale;
    }

    public DoubleFilter productPriceSale() {
        if (productPriceSale == null) {
            productPriceSale = new DoubleFilter();
        }
        return productPriceSale;
    }

    public void setProductPriceSale(DoubleFilter productPriceSale) {
        this.productPriceSale = productPriceSale;
    }

    public StringFilter getProductDescription() {
        return productDescription;
    }

    public StringFilter productDescription() {
        if (productDescription == null) {
            productDescription = new StringFilter();
        }
        return productDescription;
    }

    public void setProductDescription(StringFilter productDescription) {
        this.productDescription = productDescription;
    }

    public StringFilter getProductShortDescription() {
        return productShortDescription;
    }

    public StringFilter productShortDescription() {
        if (productShortDescription == null) {
            productShortDescription = new StringFilter();
        }
        return productShortDescription;
    }

    public void setProductShortDescription(StringFilter productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public IntegerFilter getProductQuantity() {
        return productQuantity;
    }

    public IntegerFilter productQuantity() {
        if (productQuantity == null) {
            productQuantity = new IntegerFilter();
        }
        return productQuantity;
    }

    public void setProductQuantity(IntegerFilter productQuantity) {
        this.productQuantity = productQuantity;
    }

    public StringFilter getProductCode() {
        return productCode;
    }

    public StringFilter productCode() {
        if (productCode == null) {
            productCode = new StringFilter();
        }
        return productCode;
    }

    public void setProductCode(StringFilter productCode) {
        this.productCode = productCode;
    }

    public DoubleFilter getProductPointRating() {
        return productPointRating;
    }

    public DoubleFilter productPointRating() {
        if (productPointRating == null) {
            productPointRating = new DoubleFilter();
        }
        return productPointRating;
    }

    public void setProductPointRating(DoubleFilter productPointRating) {
        this.productPointRating = productPointRating;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTimeFilter getCreatedTime() {
        return createdTime;
    }

    public ZonedDateTimeFilter createdTime() {
        if (createdTime == null) {
            createdTime = new ZonedDateTimeFilter();
        }
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTimeFilter createdTime) {
        this.createdTime = createdTime;
    }

    public LongFilter getCategoriesId() {
        return categoriesId;
    }

    public LongFilter categoriesId() {
        if (categoriesId == null) {
            categoriesId = new LongFilter();
        }
        return categoriesId;
    }

    public void setCategoriesId(LongFilter categoriesId) {
        this.categoriesId = categoriesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(productPrice, that.productPrice) &&
            Objects.equals(productPriceSale, that.productPriceSale) &&
            Objects.equals(productDescription, that.productDescription) &&
            Objects.equals(productShortDescription, that.productShortDescription) &&
            Objects.equals(productQuantity, that.productQuantity) &&
            Objects.equals(productCode, that.productCode) &&
            Objects.equals(productPointRating, that.productPointRating) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdTime, that.createdTime) &&
            Objects.equals(categoriesId, that.categoriesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            productName,
            productPrice,
            productPriceSale,
            productDescription,
            productShortDescription,
            productQuantity,
            productCode,
            productPointRating,
            createdBy,
            createdTime,
            categoriesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (productName != null ? "productName=" + productName + ", " : "") +
            (productPrice != null ? "productPrice=" + productPrice + ", " : "") +
            (productPriceSale != null ? "productPriceSale=" + productPriceSale + ", " : "") +
            (productDescription != null ? "productDescription=" + productDescription + ", " : "") +
            (productShortDescription != null ? "productShortDescription=" + productShortDescription + ", " : "") +
            (productQuantity != null ? "productQuantity=" + productQuantity + ", " : "") +
            (productCode != null ? "productCode=" + productCode + ", " : "") +
            (productPointRating != null ? "productPointRating=" + productPointRating + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
            (categoriesId != null ? "categoriesId=" + categoriesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
