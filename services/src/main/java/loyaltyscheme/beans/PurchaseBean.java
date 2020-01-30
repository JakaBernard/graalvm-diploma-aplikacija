package loyaltyscheme.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import loyaltyscheme.Purchase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
// import java.util.logging.Logger;

@ApplicationScoped
public class PurchaseBean {
    @PersistenceContext(unitName = "loyalty-scheme")
    private EntityManager em;

    Value graphLambda;

    // private Logger log = Logger.getLogger(PurchaseBean.class.getName());

    @PostConstruct
    private void init() {
        Context context = Context.newBuilder().allowAllAccess(true).build();
        graphLambda = context.eval("R", "function (names, amount) {\n" +
        "names <- t(names);\n" +
        "amount <- t(amount);\n" +
        "require(lattice);\n" +
        "tmpFile <- tempfile()\n" +
        "svg(tmpFile);\n" +
        "print(barchart(names ~ amount, main = 'Product purchase distribution'));\n" +
        "dev.off();\n" +
        "lines <- list(svg=paste0(readLines(tmpFile), collapse = ''));\n" +
        "file.remove(tmpFile)\n" +
        "return(lines);\n" +
        "}");
        // log.info("Purchase initialised.");
    }

    public Object[] getPurchases(QueryParameters queryParameters) {
        List<Purchase> purchases = JPAUtils.queryEntities(em, Purchase.class, queryParameters);
        Long count = JPAUtils.queryEntitiesCount(em, Purchase.class, queryParameters);
        Object[] resp = {purchases, count};
        return resp;
    }

    public Purchase getPurchaseById(int id) {
        TypedQuery<Purchase> q =  em.createQuery("SELECT p FROM purchase p WHERE p.id = :id", Purchase.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    public Long getPurchaseCostSum() {
        TypedQuery<Long> q = em.createQuery("SELECT SUM(pu.amount * pr.price) FROM purchase pu JOIN pu.product pr", Long.class);
        return q.getSingleResult();
    }

    @Transactional
    public void addPurchase(Purchase purchase) {
        if (purchase != null) {
            em.persist(purchase);
        }
    }

    @Transactional
    public void updatePurchase(int purchaseId, Purchase purchase) {
        Purchase p = em.find(Purchase.class, purchaseId);
        purchase.setId(p.getId());
        em.merge(purchase);
    }

    @Transactional
    public boolean removePurchase(int purchaseId) {
        Purchase purchase = em.find(Purchase.class, purchaseId);
        if(purchase != null) {
            em.remove(purchase);
            return true;
        }
        return false;

    }


    public String getPurchasesGraph() {
        TypedQuery<Object[]> q = em.createQuery("SELECT pr.productName, SUM(pu.amount) purchases FROM purchase pu JOIN pu.product pr GROUP BY pr.productName", Object[].class);
        List<Object[]> objs = q.getResultList();
        if (objs.size() == 0) {
            return "No purchases found";
        }
        List<Object> names = new ArrayList<Object>();
        List<Object> amounts = new ArrayList<Object>();
        for(Object[] obj : objs) {
            names.add(obj[0]);
            amounts.add(obj[1]);
        }
        ProxyArray proxyNames = ProxyArray.fromList(names);
        ProxyArray proxyAmounts = ProxyArray.fromList(amounts);
        Value result = this.graphLambda.execute(proxyNames, proxyAmounts);
        String output = new String(result.getMember("svg").toString().substring(4));
        output = output.substring(1, output.length() - 1);
        return output;
    }
}
