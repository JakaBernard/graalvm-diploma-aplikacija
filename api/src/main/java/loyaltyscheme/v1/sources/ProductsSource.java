package loyaltyscheme.v1.sources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import loyaltyscheme.Product;
import loyaltyscheme.beans.ProductBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@CrossOrigin
public class ProductsSource {

    @Context
    protected UriInfo uriInfo;

    @Inject
    ProductBean productBean;

    private Logger log = Logger.getLogger(ProductsSource.class.getName());

    @Operation(description = "Returns a list of products.", summary = "List of products", tags = "products", responses = {
            @ApiResponse(responseCode = "200",
                    description = "List of products",
                    content = @Content(
                            schema = @Schema(implementation = Product.class)),
                    headers = {@Header(name = "X-Total-Count", schema = @Schema(type = "int"))}
            )})
    @GET
    public Response getProducts() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        Object[] resp = productBean.getProducts(query);
        return Response.status(Response.Status.OK).entity(resp[0]).header("X-Total-Count", resp[1]).build();
    }


    @Operation(description = "Retruns product by id.", summary = "Returns product by id", tags = {"products"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Product by id",
                    content = @Content(
                            schema = @Schema(implementation = Product.class))
            )})
    @Path("{id}")
    @GET
    public Response getProduct(@PathParam("id") Integer id) {
        return Response.status(Response.Status.OK).entity(productBean.getProductById(id)).build();
    }

    @Operation(description = "Updates a product.", summary = "Updates product", tags = {"products"}, responses = {
            @ApiResponse(responseCode = "200",
                    description = "Updates product by id"
            )})
    @Path("{id}")
    @PUT
    public Response updateProduct(@PathParam("id") Integer id, Product product) {
        productBean.updateProduct(id, product);
        return Response.status(Response.Status.OK).build();
    }

    @Operation(description = "Adds a product.", summary = "Adds product", tags = {"products"}, responses = {
            @ApiResponse(responseCode = "201",
                    description = "Adds product"
            )})
    @POST
    public Response addProduct(Product product) {
        productBean.addProduct(product);
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(description = "Removes a product.", summary = "Removes product", tags = {"products"}, responses = {
            @ApiResponse(responseCode = "204",
                    description = "Removes a product"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Product not found"
            )})
    @Path("{id}")
    @DELETE
    public Response removeProduct(@PathParam("id") Integer id) {
        boolean removed = productBean.removeProduct(id);
        if(removed) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
