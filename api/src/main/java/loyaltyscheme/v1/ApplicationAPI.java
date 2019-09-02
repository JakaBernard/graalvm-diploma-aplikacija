package loyaltyscheme.v1;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.kumuluz.ee.cors.annotations.CrossOrigin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import loyaltyscheme.v1.sources.ClaimsSource;
import loyaltyscheme.v1.sources.ProductsSource;
import loyaltyscheme.v1.sources.PurchasesSource;
import loyaltyscheme.v1.sources.RewardsSource;

@OpenAPIDefinition(info=@Info(title="Loyalty scheme REST API", version = "v1", description = "API for a loyalty scheme."), servers = @Server(url = "http://localhost:8080"))
@ApplicationPath("v1")
@CrossOrigin
public class ApplicationAPI extends Application {

    @Override()
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(ProductsSource.class);
        resources.add(PurchasesSource.class);
        resources.add(RewardsSource.class);
        resources.add(ClaimsSource.class);

        return resources;
    }
}