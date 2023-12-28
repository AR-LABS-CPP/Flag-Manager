package com.arlabs.myfm.interfaces;

import java.util.List;

/**
 *
 * @author AR-LABS
 */
public interface EntryAddedListener {
    void entryAdded(int webhook, List<Integer> flagIds);
}
