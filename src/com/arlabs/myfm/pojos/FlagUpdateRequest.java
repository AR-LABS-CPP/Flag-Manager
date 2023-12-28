package com.arlabs.myfm.pojos;

/**
 *
 * @author AR-LABS
 */
public class FlagUpdateRequest {
    public String flagName;
    public String flagValue;
    
    public FlagUpdateRequest(String flagName, String flagValue) {
        this.flagName = flagName;
        this.flagValue = flagValue;
    }
}
