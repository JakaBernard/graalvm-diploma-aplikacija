package loyaltyscheme.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import loyaltyscheme.Reward;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
// import java.util.logging.Logger;

@ApplicationScoped
public class RewardBean {
    @PersistenceContext(unitName = "loyalty-scheme")
    private EntityManager em;

    // private Logger log = Logger.getLogger(RewardBean.class.getName());

    @PostConstruct
    private void init() {
        // log.info("Reward initialised.");
    }

    public Object[] getRewards(QueryParameters queryParameters) {
        List<Reward> rewards = JPAUtils.queryEntities(em, Reward.class, queryParameters);
        Long count = JPAUtils.queryEntitiesCount(em, Reward.class, queryParameters);
        Object[] resp = {rewards, count};
        return resp;
    }

    public Reward getRewardById(int id) {
        TypedQuery<Reward> q =  em.createQuery("SELECT r FROM reward r WHERE r.id = :id", Reward.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    @Transactional
    public void addReward(Reward reward) {
        if (reward != null) {
            em.persist(reward);
        }
    }

    @Transactional
    public void updateReward(int rewardId, Reward reward) {
        Reward p = em.find(Reward.class, rewardId);
        reward.setId(p.getId());
        em.merge(reward);
    }

    @Transactional
    public boolean removeReward(int rewardId) {
        Reward reward = em.find(Reward.class, rewardId);
        if(reward != null) {
            em.remove(reward);
            return true;
        }
        return false;

    }
}
