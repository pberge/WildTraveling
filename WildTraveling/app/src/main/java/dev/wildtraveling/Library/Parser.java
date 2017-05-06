package dev.wildtraveling.Library;

import java.util.List;

/**
 * Created by pere on 5/3/17.
 */
public interface Parser {
    List<Route> parse() throws RouteException;
}
