package loyaltyscheme.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import loyaltyscheme.Claim;

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
public class ClaimBean {
    @PersistenceContext(unitName = "loyalty-scheme")
    private EntityManager em;

    private Value comparisonLambda;
    private Value graphLambda;

    
    // private Logger log = Logger.getLogger(ClaimBean.class.getName());

    @PostConstruct
    private void init() {
        Context context = Context.newBuilder().allowAllAccess(true).build();
        comparisonLambda = context.eval("js", "(points, spent) => (points || 0) - (spent || 0);");
        graphLambda = context.eval("R", "function (names, amount) {\n" +
        "names <- t(names);\n" +
        "amount <- t(amount);\n" +
        "require(lattice);\n" +
        "tmpFile <- tempfile()\n" +
        "svg(tmpFile);\n" +
        "print(barchart(names ~ amount, main = 'Reward claim distribution'));\n" +
        "dev.off();\n" +
        "lines <- list(svg=paste0(readLines(tmpFile), collapse = ''));\n" +
        "file.remove(tmpFile)\n" +
        "return(lines);\n" +
        "}");
        // log.info("Claim initialised.");
    }

    public Object[] getClaims(QueryParameters queryParameters) {
        List<Claim> claims = JPAUtils.queryEntities(em, Claim.class, queryParameters);
        Long count = JPAUtils.queryEntitiesCount(em, Claim.class, queryParameters);
        Object[] resp = {claims, count};
        return resp;
    }

    public Claim getClaimById(int id) {
        TypedQuery<Claim> q =  em.createQuery("SELECT c FROM claim c WHERE c.id = :id", Claim.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    public Long getClaimCostSum() {
        TypedQuery<Long> q = em.createQuery("SELECT SUM(c.amount * r.price) FROM claim c JOIN c.reward r", Long.class);
        return q.getSingleResult();
    }

    @Transactional
    public void addClaim(Claim claim) {
        if (claim != null) {
            em.persist(claim);
        }
    }

    @Transactional
    public void updateClaim(int claimId, Claim claim) {
        Claim p = em.find(Claim.class, claimId);
        claim.setId(p.getId());
        em.merge(claim);
    }

    @Transactional
    public boolean removeClaim(int claimId) {
        Claim claim = em.find(Claim.class, claimId);
        if(claim != null) {
            em.remove(claim);
            return true;
        }
        return false;

    }

    public Value getComparisonLambda() {
        return this.comparisonLambda;
    }

    public String getClaimsGraph() {
        TypedQuery<Object[]> q = em.createQuery("SELECT r.rewardName, SUM(c.amount) purchases FROM claim c JOIN c.reward r GROUP BY r.rewardName", Object[].class);
        List<Object[]> objs = q.getResultList();
        if (objs.size() == 0) {
            return "No claims found";
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
