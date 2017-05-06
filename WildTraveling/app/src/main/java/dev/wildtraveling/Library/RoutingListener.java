package dev.wildtraveling.Library;

import java.util.List;

/**
 * Created by pere on 5/3/17.
 */
public interface RoutingListener {
    void onRoutingFailure(RouteException e);

    void onRoutingStart();

    void onRoutingSuccess(List<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}
