package loyaltyscheme.v1.sources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import loyaltyscheme.Reward;
import loyaltyscheme.beans.RewardBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("rewards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin
public class RewardsSource {

    @Context
    protected UriInfo uriInfo;

    @Inject
    RewardBean rewardBean;


    private Logger log = Logger.getLogger(RewardsSource.class.getName());

    @Operation(description = "Returns a list of rewards.", summary = "List of rewards", tags = "rewards", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of rewards",
                    content = @Content(
                            schema = @Schema(implementation = Reward.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "int"))}
            )})
    @GET
    public Response getRewards() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        Object[] resp = rewardBean.getRewards(query);
        return Response.status(Response.Status.OK).entity(resp[0]).header("X-Total-Count", resp[1]).build();
    }


    @Operation(description = "Retruns reward by id.", summary = "Returns reward by id", tags = {"rewards"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Reward by id",
                    content = @Content(
                            schema = @Schema(implementation = Reward.class))
            )})
    @Path("{id}")
    @GET
    public Response getReward(@PathParam("id") Integer id) {
        return Response.status(Response.Status.OK).entity(rewardBean.getRewardById(id)).build();
    }

    @Operation(description = "Updates a reward.", summary = "Updates reward", tags = {"rewards"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updates reward by id"
            )})
    @Path("{id}")
    @PUT
    public Response updateReward(@PathParam("id") Integer id, Reward reward) {
        rewardBean.updateReward(id, reward);
        return Response.status(Response.Status.OK).build();
    }

    @Operation(description = "Adds a reward.", summary = "Adds reward", tags = {"rewards"}, responses = {
            @ApiResponse(responseCode = "201",
                    description = "Adds reward"
            )})
    @POST
    public Response addReward(Reward reward) {
        rewardBean.addReward(reward);
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(description = "Removes a reward.", summary = "Removes reward", tags = {"rewards"}, responses = {
            @ApiResponse(responseCode = "204",
                    description = "Removes a reward"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Reward not found"
            )})
    @Path("{id}")
    @DELETE
    public Response removeReward(@PathParam("id") Integer id) {
        boolean removed = rewardBean.removeReward(id);
        if(removed) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
