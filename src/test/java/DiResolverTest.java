import org.example.*;
import org.testng.annotations.Test;

public class DiResolverTest {

    @Test
    void test() {
        DependingOnWeatherService threeDep = new DiResolver()
                .withEntities(NoDependency.class, ThreeDependencies.class, DependingOnWeatherService.class)
                .withSuppliers(OneDependencySupplier.class)
                .withSuppliers(YaWeatherProvider.class)
                .get(DependingOnWeatherService.class);
        threeDep.tellWeather();
    }
}
