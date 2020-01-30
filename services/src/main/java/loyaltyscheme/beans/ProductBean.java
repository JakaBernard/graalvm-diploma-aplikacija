package loyaltyscheme.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;


import loyaltyscheme.Product;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.List;
// import java.util.logging.Logger;

@ApplicationScoped
public class ProductBean {
    @PersistenceContext(unitName = "loyalty-scheme")
    private EntityManager em;

    // private Logger log = Logger.getLogger(ProductBean.class.getName());

    @PostConstruct
    private void init() {
        // log.info("Product initialised.");
    }

    public Object[] getProducts(QueryParameters queryParameters) {
        List<Product> products = JPAUtils.queryEntities(em, Product.class, queryParameters);
        Long count = JPAUtils.queryEntitiesCount(em, Product.class, queryParameters);
        Object[] resp = {products, count};
        return resp;
    }

    public Product getProductById(int id) {
        TypedQuery<Product> q =  em.createQuery("SELECT p FROM product p WHERE p.id = :id", Product.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Transactional
    public void addProduct(Product product) {
        if (product != null) {
            em.persist(product);
        }
    }

    @Transactional
    public void updateProduct(int productId, Product product) {
        Product p = em.find(Product.class, productId);
        product.setId(p.getId());
        em.merge(product);
    }

    @Transactional
    public boolean removeProduct(int productId) {
        Product product = em.find(Product.class, productId);
        if(product != null) {
            em.remove(product);
            return true;
        }
        return false;
    }

}
