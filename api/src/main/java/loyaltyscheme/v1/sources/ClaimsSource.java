package loyaltyscheme.v1.sources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import loyaltyscheme.Claim;
import loyaltyscheme.beans.ClaimBean;
import loyaltyscheme.beans.PurchaseBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
// import java.util.logging.Logger;

@Path("claims")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin
public class ClaimsSource {

    @Context
    protected UriInfo uriInfo;

    @Inject
    ClaimBean claimBean;

    @Inject
    PurchaseBean purchaseBean;


    // private Logger log = Logger.getLogger(ClaimsSource.class.getName());

    @Operation(description = "Returns a list of claims.", summary = "List of claims", tags = "claims", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of claims",
                    content = @Content(
                            schema = @Schema(implementation = Claim.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "int"))}
            )})
    @GET
    public Response getClaims() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        Object[] resp = claimBean.getClaims(query);
        return Response.status(Response.Status.OK).entity(resp[0]).header("X-Total-Count", resp[1]).build();
    }


    @Operation(description = "Retruns claim by id.", summary = "Returns claim by id", tags = {"claims"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Claim by id",
                    content = @Content(
                            schema = @Schema(implementation = Claim.class))
            )})
    @Path("{id}")
    @GET
    public Response getClaim(@PathParam("id") Integer id) {
        return Response.status(Response.Status.OK).entity(claimBean.getClaimById(id)).build();
    }

    @Operation(description = "Updates a claim.", summary = "Updates claim", tags = {"claims"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updates claim by id"
            )})
    @Path("{id}")
    @PUT
    public Response updateClaim(@PathParam("id") Integer id, Claim claim) {
        claimBean.updateClaim(id, claim);
        return Response.status(Response.Status.OK).build();
    }

    @Operation(description = "Adds a claim.", summary = "Adds claim", tags = {"claims"}, responses = {
            @ApiResponse(responseCode = "201",
                    description = "Adds claim"
            )})
    @POST
    public Response addClaim(Claim claim) {
        Long points = purchaseBean.getPurchaseCostSum();
        Long spent = claimBean.getClaimCostSum();
        Long diff = claimBean.getComparisonLambda().execute(points, spent).asLong();
        if (claim.getReward().getPrice() * claim.getAmount() <= diff) {
            claimBean.addClaim(claim);
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Operation(description = "Removes a claim.", summary = "Removes claim", tags = {"claims"}, responses = {
            @ApiResponse(responseCode = "204",
                    description = "Removes a claim"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Claim not found"
            )})
    @Path("{id}")
    @DELETE
    public Response removeClaim(@PathParam("id") Integer id) {
        boolean removed = claimBean.removeClaim(id);
        if(removed) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("graph")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getClaimsGraph() {
        return Response.ok(claimBean.getClaimsGraph()).build();
    }

}
