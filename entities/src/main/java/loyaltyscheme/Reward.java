package loyaltyscheme;

import javax.persistence.*;

@Entity(name = "reward")
/*@NamedQueries(value =
    {
        @NamedQuery(name = "Reward.getAll", query = "SELECT r FROM reward r"),
        @NamedQuery(name = "Reward.findById", query = "SELECT r FROM reward r WHERE r.id = :id"),
        @NamedQuery(name = "Reward.findByName", query = "SELECT r FROM reward r WHERE r.reward_name = :name"),
    })*/
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="reward_name")
    private String rewardName;
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
