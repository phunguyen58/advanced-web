package com.ptudw.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_price_sale")
    private Double productPriceSale;

    @NotNull
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_short_description")
    private String productShortDescription;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_point_rating")
    private Double productPointRating;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Categories categories;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return this.productPrice;
    }

    public Product productPrice(Double productPrice) {
        this.setProductPrice(productPrice);
        return this;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getProductPriceSale() {
        return this.productPriceSale;
    }

    public Product productPriceSale(Double productPriceSale) {
        this.setProductPriceSale(productPriceSale);
        return this;
    }

    public void setProductPriceSale(Double productPriceSale) {
        this.productPriceSale = productPriceSale;
    }

    public String getProductDescription() {
        return this.productDescription;
    }

    public Product productDescription(String productDescription) {
        this.setProductDescription(productDescription);
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductShortDescription() {
        return this.productShortDescription;
    }

    public Product productShortDescription(String productShortDescription) {
        this.setProductShortDescription(productShortDescription);
        return this;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public Integer getProductQuantity() {
        return this.productQuantity;
    }

    public Product productQuantity(Integer productQuantity) {
        this.setProductQuantity(productQuantity);
        return this;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Product productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getProductPointRating() {
        return this.productPointRating;
    }

    public Product productPointRating(Double productPointRating) {
        this.setProductPointRating(productPointRating);
        return this;
    }

    public void setProductPointRating(Double productPointRating) {
        this.productPointRating = productPointRating;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Product createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedTime() {
        return this.createdTime;
    }

    public Product createdTime(ZonedDateTime createdTime) {
        this.setCreatedTime(createdTime);
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Categories getCategories() {
        return this.categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Product categories(Categories categories) {
        this.setCategories(categories);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productPrice=" + getProductPrice() +
            ", productPriceSale=" + getProductPriceSale() +
            ", productDescription='" + getProductDescription() + "'" +
            ", productShortDescription='" + getProductShortDescription() + "'" +
            ", productQuantity=" + getProductQuantity() +
            ", productCode='" + getProductCode() + "'" +
            ", productPointRating=" + getProductPointRating() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            "}";
    }
}
