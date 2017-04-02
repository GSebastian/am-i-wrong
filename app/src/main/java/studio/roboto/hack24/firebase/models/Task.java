package studio.roboto.hack24.firebase.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sebastian on 27/03/2017.
 */

public abstract class Task {

    abstract String getTaskType();

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("_state", getTaskType());

        return map;
    }

}
