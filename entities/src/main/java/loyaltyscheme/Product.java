package loyaltyscheme;

import javax.persistence.*;

@Entity(name = "product")
/*@NamedQueries(value =
    {
        @NamedQuery(name = "Product.getAll", query = "SELECT p FROM product p"),
        @NamedQuery(name = "Product.findById", query = "SELECT p FROM product p WHERE p.id = :id"),
        @NamedQuery(name = "Product.findByName", query = "SELECT p FROM product p WHERE p.product_name = :name"),
    })*/
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="product_name")
    private String productName;
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
