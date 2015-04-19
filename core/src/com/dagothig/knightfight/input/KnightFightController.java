package com.dagothig.knightfight.input;

import java.util.ArrayList;
import java.util.List;

public abstract class KnightFightController {
    protected List<KnightFightControllerListener> listeners = new ArrayList<>();

    public void addListener(KnightFightControllerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(KnightFightControllerListener listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners = new ArrayList<>();
    }
}
