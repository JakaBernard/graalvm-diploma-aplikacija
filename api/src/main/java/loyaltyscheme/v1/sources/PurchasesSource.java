package loyaltyscheme.v1.sources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import loyaltyscheme.Purchase;
import loyaltyscheme.beans.PurchaseBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
// import java.util.logging.Logger;

@Path("purchases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin
public class PurchasesSource {

    @Context
    protected UriInfo uriInfo;

    @Inject
    PurchaseBean purchaseBean;


    // private Logger log = Logger.getLogger(PurchasesSource.class.getName());

    @Operation(description = "Returns a list of purchases.", summary = "List of purchases", tags = "purchases", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of purchases",
                    content = @Content(
                            schema = @Schema(implementation = Purchase.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "int"))}
            )})
    @GET
    public Response getPurchases() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        Object[] resp = purchaseBean.getPurchases(query);
        return Response.status(Response.Status.OK).entity(resp[0]).header("X-Total-Count", resp[1]).build();
    }


    @Operation(description = "Retruns purchase by id.", summary = "Returns purchase by id", tags = {"purchases"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Purchase by id",
                    content = @Content(
                            schema = @Schema(implementation = Purchase.class))
            )})
    @Path("{id}")
    @GET
    public Response getPurchase(@PathParam("id") Integer id) {
        return Response.status(Response.Status.OK).entity(purchaseBean.getPurchaseById(id)).build();
    }

    @Operation(description = "Updates a purchase.", summary = "Updates purchase", tags = {"purchases"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updates purchase by id"
            )})
    @Path("{id}")
    @PUT
    public Response updatePurchase(@PathParam("id") Integer id, Purchase purchase) {
        purchaseBean.updatePurchase(id, purchase);
        return Response.status(Response.Status.OK).build();
    }

    @Operation(description = "Adds a purchase.", summary = "Adds purchase", tags = {"purchases"}, responses = {
            @ApiResponse(responseCode = "201",
                    description = "Adds purchase"
            )})
    @POST
    public Response addPurchase(Purchase purchase) {
        purchaseBean.addPurchase(purchase);
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(description = "Removes a purchase.", summary = "Removes purchase", tags = {"purchases"}, responses = {
            @ApiResponse(responseCode = "204",
                    description = "Removes a purchase"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Purchase not found"
            )})
    @Path("{id}")
    @DELETE
    public Response removePurchase(@PathParam("id") Integer id) {
        boolean removed = purchaseBean.removePurchase(id);
        if(removed) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("graph")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getPurchasesGraph() {
        return Response.ok(purchaseBean.getPurchasesGraph()).build();
    }

}
