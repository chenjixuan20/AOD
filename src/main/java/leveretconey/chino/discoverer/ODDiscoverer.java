package leveretconey.chino.discoverer;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODTree;

public abstract class ODDiscoverer {
    public abstract ODTree discover(DataFrame data, ODTree reference);
    public final ODTree discover(DataFrame data){
        return discover(data,null);
    }
}
