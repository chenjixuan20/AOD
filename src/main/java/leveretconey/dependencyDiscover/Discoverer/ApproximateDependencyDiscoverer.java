package leveretconey.dependencyDiscover.Discoverer;

import java.util.Collection;

import leveretconey.ReturnData;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.AbstractDependency;

public interface ApproximateDependencyDiscoverer<E extends AbstractDependency> {
   Collection<E> discover(DataFrame data, double errorRateThreshold);

   ReturnData newDiscover(DataFrame data, double errorRateThreshold);
}
